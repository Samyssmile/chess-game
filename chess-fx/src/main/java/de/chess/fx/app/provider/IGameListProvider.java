package de.chess.fx.app.provider;

import de.chess.dto.ChessGame;
import de.chess.fx.app.model.GameRowData;

import java.util.List;
import java.util.concurrent.Flow;

public interface IGameListProvider  {

    public List<GameRowData> receiveGameList();
    public void setGameList(List<ChessGame> gameList);
    public void subscribe(Flow.Subscriber<List<ChessGame>> subscriber);
}
