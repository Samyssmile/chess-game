package de.chess.app.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClientReceiver implements IGameClientReceiver {
    private static final Logger LOGGER = Logger.getLogger(GameClientReceiver.class.getName());

    private final List<ServerGameClient> clientList = Collections.synchronizedList(new ArrayList<>());
    private Flow.Subscription subscription;

    @Override
    public void onIncomingClient(ServerGameClient newIncomingClient) {
        clientList.add(newIncomingClient);
        LOGGER.log(Level.INFO, "New Game Client was registered, total client online: {0}", getNumberOfConnectedClients());
    }

    @Override
    public int getNumberOfConnectedClients() {
        return clientList.size();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        LOGGER.log(Level.INFO, "New Subscription received...");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(ServerGameClient newIncomingClient) {
        LOGGER.log(Level.INFO, "New Client received...");

        onIncomingClient(newIncomingClient);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.log(Level.SEVERE, "Something went wrong: {0} ", throwable.getMessage());
    }

    @Override
    public void onComplete() {
        LOGGER.log(Level.INFO, "Complete...");
    }
}
