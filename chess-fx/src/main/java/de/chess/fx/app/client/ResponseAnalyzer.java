package de.chess.fx.app.client;

import de.chess.dto.ChessGame;
import de.chess.dto.response.*;
import de.chess.fx.app.handler.EventData;
import de.chess.fx.app.handler.EventHandler;
import de.chess.fx.app.handler.EventType;
import de.chess.fx.app.provider.GameListProvider;
import de.chess.io.client.IResponseAnalyzer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseAnalyzer implements IResponseAnalyzer {
  private static final Logger LOGGER = Logger.getGlobal();

  @Override
  public void analyze(Response response) {

    if (response != null) {
      switch (response.getRequestType()) {
        case REQUEST_GAME_LIST:
          onGameListResponse(response);
          break;
        case JOIN:
          onJoinGameResponse(response);
          break;
        case MOVE:
          break;
        case MESSAGE:
          break;
        case SURRENDER:
          break;
        case NEW_GAME:
          onNewGameResponse(response);
          break;
        case REMIS:
          break;
        case PLAYER_JOINED:
          onPlayerJoinedMyGame(response);
          break;
      }
    }
  }

  private void onPlayerJoinedMyGame(Response response) {
    LOGGER.log(Level.INFO, "Player Joined Game Response");
    PlayerJoinedIntoMyHostedGameResponse playerJoinedIntoMyHostedGameResponse =
        (PlayerJoinedIntoMyHostedGameResponse) response;
    EventHandler.getInstance()
        .fireGameEvent(
            EventType.PLAYER_JOINED,
            new EventData(playerJoinedIntoMyHostedGameResponse.getPlayer()));
  }

  private void onJoinGameResponse(Response response) {
    LOGGER.log(Level.INFO, "Join Game Response");
    JoinGameResponse joinGameResponse = (JoinGameResponse) response;
    EventHandler.getInstance()
        .fireGameEvent(EventType.JOINED_GAME, new EventData(joinGameResponse.getGameDTO()));
  }

  private void onNewGameResponse(Response response) {
    LOGGER.log(Level.INFO, "New Game Response");
    OpenGameResponse openGameResponse = (OpenGameResponse) response;
    if (openGameResponse.isGranted()) {
      EventHandler.getInstance()
          .fireGameEvent(EventType.OPEN_NEW_GAME, new EventData(openGameResponse.getGameDTO()));
    }
  }

  private void onGameListResponse(Response response) {
    LOGGER.log(Level.INFO, "Game List Response");
    ReceiveGameListResponse receiveGameListResponse = (ReceiveGameListResponse) response;
    List<ChessGame> gameList = receiveGameListResponse.getGameList();
    GameListProvider.getInstance().setGameList(gameList);
  }

  @Override
  public Response analyze(String jsonResponse) {
    return null;
  }
}
