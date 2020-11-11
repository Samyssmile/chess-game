package de.chess.fx.app.provider;

import de.chess.fx.app.model.Game;

import java.util.ArrayList;
import java.util.List;

public class DummyProvider implements IGameListProvider {

    @Override
    public List<Game> receiveGameList() {
        List<Game> gameList = new ArrayList<>();
        Game dummyGame = new Game("Samy", "Samuels Chess Massaka", "Black");
        gameList.add(dummyGame);
        return gameList;
    }
}
