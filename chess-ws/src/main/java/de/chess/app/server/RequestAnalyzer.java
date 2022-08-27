package de.chess.app.server;

import de.chess.app.game.GameManager;
import de.chess.app.game.GameThread;
import de.chess.dto.ChessGame;
import de.chess.dto.GameStatus;
import de.chess.dto.RequestType;
import de.chess.dto.request.JoinGameRequest;
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
        game.setGameStatus(GameStatus.WATING);

        return new OpenGameResponse(
                game.getUuid(), openGameRequest.getPlayerUUID(), NEW_GAME, game != null, game);
    }
}
