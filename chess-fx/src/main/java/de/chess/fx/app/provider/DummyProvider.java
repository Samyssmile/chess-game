package de.chess.fx.app.provider;

import de.chess.fx.app.model.Game;

import java.util.ArrayList;
import java.util.List;

public class DummyProvider implements IGameListProvider {

    @Override
    public List<Game> receiveGameList() {
        List<Game> gameList = new ArrayList<>();
        Game dummyGame1 = new Game("Samy", "Samuels Chess Massaka", "Black", "10:00");
        Game dummyGame2 = new Game("Dummy_User12", "Noobs Only", "White");
        Game dummyGame3 = new Game("Alfred", "Wessex Attack", "Random", "20:00");
        gameList.add(dummyGame1);
        gameList.add(dummyGame2);
        gameList.add(dummyGame3);
        return gameList;
    }
}
