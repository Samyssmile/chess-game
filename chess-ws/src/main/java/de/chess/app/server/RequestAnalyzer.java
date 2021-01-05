package de.chess.app.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.chess.app.manager.GameManager;
import de.chess.dto.request.JoinGameRequest;
import de.chess.dto.request.SelfIntroducingRequest;
import de.chess.dto.response.JoinGameResponse;
import de.chess.game.IGameManager;
import de.chess.dto.ChessGame;
import de.chess.dto.RequestType;
import de.chess.dto.request.OpenGameRequest;
import de.chess.dto.response.OpenGameResponse;
import de.chess.dto.response.ReceiveGameListResponse;
import de.chess.dto.response.Response;
import de.chess.io.server.IRequestAnalyzer;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.chess.dto.RequestType.*;
import static de.chess.dto.RequestType.REQUEST_GAME_LIST;

public class RequestAnalyzer implements IRequestAnalyzer {
  private static final Logger LOGGER = Logger.getGlobal();
  private static final boolean ACCEPTED = true;
  private static final boolean DECLINED = false;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final IGameManager gameManager = GameManager.instance();

  @Override
  public Response analyze(String jsonRequest) {
    LOGGER.log(Level.INFO, "Incoming Request: {0}", jsonRequest);
    JsonObject jsonObject = gson.fromJson(jsonRequest, JsonObject.class);
    String requestType = jsonObject.get("requestType").getAsString();

    Response response = null;
    if (requestType.equals(RequestType.NEW_GAME.name())) {
      LOGGER.log(Level.INFO, NEW_GAME + "Request Reeceived.");
      OpenGameRequest openGameRequest = gson.fromJson(jsonRequest, OpenGameRequest.class);
      ChessGame game = gameManager.requestGame(openGameRequest.getGameDTO());
      response =
          new OpenGameResponse(
              game.getUuid(), openGameRequest.getPlayerUUID(), NEW_GAME, game != null);
    } else if (requestType.equals(JOIN.name())) {
      LOGGER.log(Level.INFO, JOIN + "Request Reeceived.");
      JoinGameRequest joinGameRequest = gson.fromJson(jsonRequest, JoinGameRequest.class);
      Optional<ChessGame> chessGameOpt =
          gameManager.requestToJoinGame(joinGameRequest.getGameUUID(), joinGameRequest.getPlayer());

      if (chessGameOpt.isPresent()) {
        response = new JoinGameResponse(chessGameOpt.get(), JOIN, ACCEPTED);
      } else {
        response = new JoinGameResponse(JOIN, DECLINED);
      }

    } else if (requestType.equals(REQUEST_GAME_LIST.name())) {
      List<ChessGame> gameList = GameManager.instance().getActiveGameList();
      response = new ReceiveGameListResponse(gameList);
    } else if (requestType.equals(SELF_INTRODUCING.name())) {
      LOGGER.log(Level.INFO, SELF_INTRODUCING + " Request Reeceived.");
      SelfIntroducingRequest selfIntroducingRequest =
          gson.fromJson(jsonRequest, SelfIntroducingRequest.class);


    }

    return response;
  }
}
