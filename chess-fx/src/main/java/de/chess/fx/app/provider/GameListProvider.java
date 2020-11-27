package de.chess.fx.app.provider;

import de.chess.dto.ChessGame;
import de.chess.fx.app.model.GameRowData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameListProvider implements IGameListProvider {
    private static final Logger LOGGER = Logger.getGlobal();
    private SubmissionPublisher<List<ChessGame>> publisher;

    private List<GameRowData> gameRowList = new ArrayList<>();

    private static IGameListProvider instance = null;

    private GameListProvider() {
        publisher = new SubmissionPublisher<>();
    }

    public static IGameListProvider getInstance() {
        if (instance == null) {
            instance = new GameListProvider();
        }
        return instance;
    }

    @Override
    public List<GameRowData> receiveGameList() {
        return gameRowList;
    }

    @Override
    public void setGameList(List<ChessGame> gameList) {
        LOGGER.log(Level.INFO, "Update Game List - Site: {0}", gameList.size());
        gameRowList.clear();
        for (ChessGame gameDTO : gameList) {
            GameRowData gameRowData = new GameRowData(gameDTO.getUuid(), gameDTO.getHostPlayerName(), gameDTO.getGameName(), gameDTO.getHostColor(), gameDTO.getTimeLimit());
            gameRowList.add(gameRowData);
        }
        System.out.println("Publish did submit");
        publisher.submit(gameList);
        List<Flow.Subscriber<? super List<ChessGame>>> subs = publisher.getSubscribers();
    }

    @Override
    public void subscribe(Flow.Subscriber<List<ChessGame>> subscriber) {
        System.out.println("Subscription done !!!");
        publisher.subscribe(subscriber);

    }
}
