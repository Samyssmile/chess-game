package de.chess.fx.app.handler;

import javafx.event.Event;

import java.util.concurrent.ConcurrentLinkedQueue;

public class EventHandler<W extends Event> implements IEventHandler{

    private final ConcurrentLinkedQueue<EventObserver> observerList = new ConcurrentLinkedQueue<>();

    private static EventHandler<Event> instance = null;

    public static EventHandler<Event> getInstance() {
        if (instance == null) {
            instance = new EventHandler<Event>();
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
        observerList.removeIf(observer -> observer.equals(channel));
    }

    public void fireGameEvent(EventType eventType, EventData eventData){
        for(EventObserver eventObserver : observerList ){
            if (eventObserver.getEventType().equals(eventType)){
                eventObserver.getChannel().update(eventType, eventData);
            }
        }
    }


}
