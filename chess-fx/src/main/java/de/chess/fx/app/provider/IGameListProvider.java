package de.chess.fx.app.provider;

import de.chess.fx.app.model.Game;

import java.util.List;

public interface IGameListProvider {

    public List<Game> receiveGameList();
}
