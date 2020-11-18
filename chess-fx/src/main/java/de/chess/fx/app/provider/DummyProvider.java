package de.chess.fx.app.provider;

import de.chess.dto.GameDTO;
import de.chess.fx.app.model.GameRowData;
import de.chess.model.ChessColor;

import java.util.ArrayList;
import java.util.List;

public class DummyProvider implements IGameListProvider {


    @Override
    public List<GameRowData> receiveGameList() {
        List<GameRowData> gameList = new ArrayList<>();
        GameRowData dummyGame1 = new GameRowData("Samy", "Samuels Chess Massaka", ChessColor.BLACK, "10:00");
        GameRowData dummyGame2 = new GameRowData("Dummy_User12", "Noobs Only", ChessColor.WHITE);
        GameRowData dummyGame3 = new GameRowData("Alfred", "Wessex Attack", ChessColor.RANDOM, "20:00");
        gameList.add(dummyGame1);
        gameList.add(dummyGame2);
        gameList.add(dummyGame3);
        return gameList;
    }

    @Override
    public void setGameList(List<GameDTO> gameList) {

    }
}
