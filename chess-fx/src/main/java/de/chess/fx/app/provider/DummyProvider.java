package de.chess.fx.app.provider;

import de.chess.dto.ChessGame;
import de.chess.fx.app.model.GameRowData;
import de.chess.model.ChessColor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class DummyProvider implements IGameListProvider {

    private SubmissionPublisher<List<ChessGame>> publisher;
    private List<Flow.Subscriber> subscribers = new ArrayList<>();

    private List<ChessGame> gameList = new ArrayList<>();

    public DummyProvider() {
        publisher = new SubmissionPublisher<>();
    }

    @Override
    public List<GameRowData> receiveGameList() {
        List<GameRowData> gameList = new ArrayList<>();
        GameRowData dummyGame1 = new GameRowData(UUID.randomUUID(), "Samy", "Samuels Chess Massaka", ChessColor.BLACK, "10:00");
        GameRowData dummyGame2 = new GameRowData(UUID.randomUUID(), "Dummy_User12", "Noobs Only", ChessColor.WHITE);
        GameRowData dummyGame3 = new GameRowData(UUID.randomUUID(), "Alfred", "Wessex Attack", ChessColor.RANDOM, "20:00");
        gameList.add(dummyGame1);
        gameList.add(dummyGame2);
        gameList.add(dummyGame3);
        return gameList;
    }

    @Override
    public void setGameList(List<ChessGame> gameList) {
        this.gameList.clear();
        this.gameList.addAll(gameList);

        publisher.submit(gameList);
    }

    @Override
    public void subscribe(Flow.Subscriber<List<ChessGame>> subscriber) {
        subscribers.add(subscriber);
    }

}
