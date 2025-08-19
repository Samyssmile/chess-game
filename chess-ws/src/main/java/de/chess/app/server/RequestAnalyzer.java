package de.chess.app.server;

import de.chess.app.game.GameManager;
import de.chess.app.game.GameThread;
import de.chess.dto.ChessGame;
import de.chess.dto.GameStatus;
import de.chess.dto.RequestType;
import de.chess.dto.request.JoinGameRequest;
import de.chess.dto.request.MoveRequest;
import de.chess.dto.request.OpenGameRequest;
import de.chess.dto.request.Request;
import de.chess.dto.request.SelfIntroducingRequest;
import de.chess.dto.response.*;
import de.chess.game.IGameManager;
import de.chess.io.server.ClientThread;
import de.chess.io.server.IRequestAnalyzer;
import de.chess.io.server.PlayerConnection;
import de.chess.io.server.PlayerManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.chess.dto.RequestType.*;

public class RequestAnalyzer implements IRequestAnalyzer {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final boolean ACCEPTED = true;
    private static final boolean DECLINED = false;
    private final IGameManager gameManager = GameManager.instance();

    @Override
    public Response analyze(Request request, ClientThread clientThread) {
        LOGGER.log(Level.INFO, "Incoming Request");

        Response response = null;
        if (request.getRequestType() == NEW_GAME) {
            LOGGER.log(Level.INFO, NEW_GAME + "Request Reeceived.");
            OpenGameRequest openGameRequest = (OpenGameRequest) request;
            response = onNewGameRequestReceived(openGameRequest);
        } else if (request.getRequestType() == JOIN) {
            LOGGER.log(Level.INFO, JOIN + "Request Reeceived.");
            JoinGameRequest joinGameRequest = (JoinGameRequest) request;
            response = onJoinRequestReceived(joinGameRequest);
        } else if (request.getRequestType() == REQUEST_GAME_LIST) {
            List<ChessGame> gameList = GameManager.instance().getActiveGameList();
            response = new ReceiveGameListResponse(gameList);
        } else if (request.getRequestType() == SELF_INTRODUCING) {
            SelfIntroducingRequest selfIntroducingRequest = (SelfIntroducingRequest) request;
            LOGGER.log(
                    Level.INFO,
                    SELF_INTRODUCING + " Request Reeceived." + selfIntroducingRequest.getPlayerUUID());
            response = onSelfIntroduceRequestReceived(selfIntroducingRequest, clientThread);
        } else if (request.getRequestType() == MOVE) {
            LOGGER.log(Level.INFO, MOVE + " Request Received.");
            MoveRequest moveRequest = (MoveRequest) request;
            response = onMoveRequestReceived(moveRequest, clientThread);
        }

        return response;
    }

    private Response onSelfIntroduceRequestReceived(
            SelfIntroducingRequest selfIntroducingRequest, ClientThread clientThread) {
        PlayerManager.getInstance()
                .addPlayerConnection(
                        new PlayerConnection(selfIntroducingRequest.getPlayerUUID(), clientThread));
        return null;
    }

    private Response onJoinRequestReceived(JoinGameRequest joinGameRequest) {
        Response response;
        Optional<ChessGame> chessGameOpt =
                gameManager.requestToJoinGame(joinGameRequest.getGameUUID(), joinGameRequest.getPlayer());

        if (chessGameOpt.isPresent()) {
            UUID hostPlayerUUID = chessGameOpt.get().getHostPlayer().getUuid();
            Optional<PlayerConnection> hostPlayerConnection =
                    PlayerManager.getInstance().getConnectionByPlayerUUID(hostPlayerUUID);
            if (hostPlayerConnection.isPresent()) {
                hostPlayerConnection
                        .get()
                        .getClientThread()
                        .sendResponse(
                                new PlayerJoinedIntoMyHostedGameResponse(
                                        chessGameOpt.get().getUuid(),
                                        RequestType.PLAYER_JOINED,
                                        joinGameRequest.getPlayer()));
                response = new JoinGameResponse(chessGameOpt.get(), JOIN, ACCEPTED);
                Thread t0 = new GameThread(chessGameOpt.get());
                t0.start();

            } else {
                response = new JoinGameResponse(HOST_NOT_EXISTS, DECLINED);
            }

        } else {
            response = new JoinGameResponse(JOIN, DECLINED);
        }
        return response;
    }

    private Response onNewGameRequestReceived(OpenGameRequest openGameRequest) {
        ChessGame game = gameManager.requestGame(openGameRequest.getGameDTO());

        return new OpenGameResponse(
                game.getUuid(), openGameRequest.getPlayerUUID(), NEW_GAME, game != null, game);
    }

    private Response onMoveRequestReceived(MoveRequest moveRequest, ClientThread clientThread) {
        try {
            UUID gameUUID = moveRequest.getGameUUID();
            UUID playerUUID = moveRequest.getPlayerUUID();
            String move = moveRequest.getMove();

            LOGGER.log(Level.INFO, "Processing move: " + move + " for player: " + playerUUID + " in game: " + gameUUID);

            // Validate that the game exists
            Optional<ChessGame> gameOptional = gameManager.getGameByUUIID(gameUUID);
            if (!gameOptional.isPresent()) {
                return new MoveResponse(gameUUID, MOVE, false, move, "Game not found");
            }

            ChessGame game = gameOptional.get();

            // Validate that the game is running
            if (game.getGameStatus() != GameStatus.RUNNING) {
                return new MoveResponse(gameUUID, MOVE, false, move, "Game is not in running state");
            }

            // Find the player making the move
            de.chess.dto.Player movingPlayer = null;
            if (game.getHostPlayer() != null && game.getHostPlayer().getUuid().equals(playerUUID)) {
                movingPlayer = game.getHostPlayer();
            } else if (game.getClientPlayer() != null && game.getClientPlayer().getUuid().equals(playerUUID)) {
                movingPlayer = game.getClientPlayer();
            }

            if (movingPlayer == null) {
                return new MoveResponse(gameUUID, MOVE, false, move, "Player not found in game");
            }

            // Check if it's the player's turn
            if (!game.isPlayerTurn(movingPlayer)) {
                return new MoveResponse(gameUUID, MOVE, false, move, "Not your turn");
            }

            // Execute the move using GameManager
            boolean moveSuccessful = gameManager.move(gameUUID, move);

            if (moveSuccessful) {
                // Notify the other player about the move
                notifyOtherPlayerAboutMove(game, movingPlayer, move);
                
                return new MoveResponse(gameUUID, MOVE, true, move, game);
            } else {
                return new MoveResponse(gameUUID, MOVE, false, move, "Invalid move");
            }

        } catch (Exception e) {
            LOGGER.severe("Error processing move request: " + e.getMessage());
            return new MoveResponse(moveRequest.getGameUUID(), MOVE, false, moveRequest.getMove(), "Server error");
        }
    }

    private void notifyOtherPlayerAboutMove(ChessGame game, de.chess.dto.Player movingPlayer, String move) {
        try {
            // Determine the other player
            de.chess.dto.Player otherPlayer;
            if (game.getHostPlayer().equals(movingPlayer)) {
                otherPlayer = game.getClientPlayer();
            } else {
                otherPlayer = game.getHostPlayer();
            }

            if (otherPlayer != null) {
                // Get the other player's connection
                Optional<PlayerConnection> otherPlayerConnection = 
                    PlayerManager.getInstance().getConnectionByPlayerUUID(otherPlayer.getUuid());
                
                if (otherPlayerConnection.isPresent()) {
                    // Send move notification to the other player
                    MoveResponse moveNotification = new MoveResponse(
                        game.getUuid(), 
                        MOVE, 
                        true, 
                        move, 
                        game
                    );
                    
                    otherPlayerConnection.get().getClientThread().sendResponse(moveNotification);
                    LOGGER.log(Level.INFO, "Move notification sent to player: " + otherPlayer.getUuid());
                } else {
                    LOGGER.warning("Could not find connection for other player: " + otherPlayer.getUuid());
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error notifying other player about move: " + e.getMessage());
        }
    }
}
