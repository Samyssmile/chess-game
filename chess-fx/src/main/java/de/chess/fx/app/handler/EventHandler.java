package de.chess.fx.app.handler;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    private List<EventObserver> observerList = new ArrayList<>();

    private static EventHandler instance = null;

    public static EventHandler getInstance() {
        if (instance == null) {
            instance = new EventHandler();
        }
        return instance;
    }

    private EventHandler() {
    }

    public void registerForEvent(IChannel channel, EventType eventType) {
        EventObserver eventObserver = new EventObserver(channel, eventType);
        observerList.add(eventObserver);
    }

    public void removeObserver(IChannel channel) {
        observerList.removeIf(e -> channel.equals(e));
    }

    public void fireEvent(EventType eventType) {
        observerList.stream().filter(e -> e.getEventType().equals(eventType)).findFirst().ifPresent(e -> e.getChannel().update(eventType));
    }
}
