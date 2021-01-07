package de.chess.fx.app.handler;

import de.chess.fx.app.ui.views.gameboard.GameBoardView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventHandler {

    private ConcurrentLinkedQueue<EventObserver> observerList = new ConcurrentLinkedQueue<>();

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

        for(EventObserver eventObserver : observerList ){
            if (eventObserver.getEventType().equals(eventType)){
                eventObserver.getChannel().update(eventType);
            }
        }
    }

    public void fireGameEvent(EventType eventType, EventData eventData){
        for(EventObserver eventObserver : observerList ){
            if (eventObserver.getEventType().equals(eventType)){
                eventObserver.getChannel().update(eventType, eventData);
            }
        }
    }


}
