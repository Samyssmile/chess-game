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
                // CRITICAL: Get the FRESH game state after the move from GameManager
                // The original 'game' variable might be stale!
                Optional<ChessGame> updatedGameOptional = gameManager.getGameByUUIID(gameUUID);
                if (!updatedGameOptional.isPresent()) {
                    LOGGER.severe("Game disappeared after successful move - this should not happen!");
                    return new MoveResponse(gameUUID, MOVE, false, move, "Game state error");
                }
                
                ChessGame updatedGame = updatedGameOptional.get();
                
                // CRITICAL DEBUG: Verify the updated board state
                LOGGER.info("=== CRITICAL DEBUG AFTER MOVE " + move + " ===");
                LOGGER.info("Original game object ID: " + System.identityHashCode(game));
                LOGGER.info("Updated game object ID: " + System.identityHashCode(updatedGame));
                LOGGER.info("Are they the same object? " + (game == updatedGame));
                
                // Check if the board was actually updated
                de.chess.model.Piece originalE2 = game.getGameBoard() != null ? game.getGameBoard().getPiece("e2") : null;
                de.chess.model.Piece originalE4 = game.getGameBoard() != null ? game.getGameBoard().getPiece("e4") : null;
                de.chess.model.Piece updatedE2 = updatedGame.getGameBoard() != null ? updatedGame.getGameBoard().getPiece("e2") : null;
                de.chess.model.Piece updatedE4 = updatedGame.getGameBoard() != null ? updatedGame.getGameBoard().getPiece("e4") : null;
                
                LOGGER.info("Original game e2: " + (originalE2 != null ? originalE2.toString() : "NULL"));
                LOGGER.info("Original game e4: " + (originalE4 != null ? originalE4.toString() : "NULL"));
                LOGGER.info("Updated game e2: " + (updatedE2 != null ? updatedE2.toString() : "NULL"));
                LOGGER.info("Updated game e4: " + (updatedE4 != null ? updatedE4.toString() : "NULL"));
                
                // DEBUG: Log board state to verify move was applied
                LOGGER.info("Updated game board after move " + move + ":");
                logBoardState(updatedGame.getGameBoard());
                
                // Notify both players about the move with updated game state
                notifyBothPlayersAboutMove(updatedGame, movingPlayer, move);
                
                return new MoveResponse(gameUUID, MOVE, true, move, updatedGame);
            } else {
                return new MoveResponse(gameUUID, MOVE, false, move, "Invalid move");
            }

        } catch (Exception e) {
            LOGGER.severe("Error processing move request: " + e.getMessage());
            return new MoveResponse(moveRequest.getGameUUID(), MOVE, false, moveRequest.getMove(), "Server error");
        }
    }

    private void notifyBothPlayersAboutMove(ChessGame game, de.chess.dto.Player movingPlayer, String move) {
        try {
            // Create move notification with updated game state
            MoveResponse moveNotification = new MoveResponse(
                game.getUuid(), 
                MOVE, 
                true, 
                move, 
                game
            );
            
            // Notify host player
            if (game.getHostPlayer() != null) {
                Optional<PlayerConnection> hostConnection = 
                    PlayerManager.getInstance().getConnectionByPlayerUUID(game.getHostPlayer().getUuid());
                    
                if (hostConnection.isPresent()) {
                    hostConnection.get().getClientThread().sendResponse(moveNotification);
                    LOGGER.log(Level.INFO, "Move notification sent to host player: " + game.getHostPlayer().getUuid());
                } else {
                    LOGGER.warning("Could not find connection for host player: " + game.getHostPlayer().getUuid());
                }
            }
            
            // Notify client player
            if (game.getClientPlayer() != null) {
                Optional<PlayerConnection> clientConnection = 
                    PlayerManager.getInstance().getConnectionByPlayerUUID(game.getClientPlayer().getUuid());
                    
                if (clientConnection.isPresent()) {
                    clientConnection.get().getClientThread().sendResponse(moveNotification);
                    LOGGER.log(Level.INFO, "Move notification sent to client player: " + game.getClientPlayer().getUuid());
                } else {
                    LOGGER.warning("Could not find connection for client player: " + game.getClientPlayer().getUuid());
                }
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error notifying players about move: " + e.getMessage());
        }
    }
    
    /**
     * Debug method to log board state
     */
    private void logBoardState(de.chess.model.GameBoard gameBoard) {
        if (gameBoard == null) {
            LOGGER.warning("GameBoard is null!");
            return;
        }
        
        StringBuilder boardStr = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            boardStr.append("Row ").append(8 - row).append(": ");
            for (int col = 0; col < 8; col++) {
                de.chess.model.Piece piece = gameBoard.getPiece(((char)('a' + col)) + String.valueOf(8 - row));
                if (piece != null) {
                    boardStr.append(piece.getPieceType().toString().charAt(0))
                           .append(piece.getColor().toString().charAt(0))
                           .append(" ");
                } else {
                    boardStr.append("-- ");
                }
            }
            boardStr.append("\n");
        }
        LOGGER.info("Board state:\n" + boardStr.toString());
    }
}
