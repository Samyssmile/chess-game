package de.chess.fx.app.provider;

import de.chess.fx.app.model.GameRowData;

import java.util.List;

public interface IGameListProvider {

    public List<GameRowData> receiveGameList();
}
