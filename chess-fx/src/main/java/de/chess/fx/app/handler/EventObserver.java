package de.chess.fx.app.handler;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class EventObserver {

    private static AtomicInteger id = new AtomicInteger(0);
    private IChannel channel;
    private EventType eventType;

    public EventObserver(IChannel channel, EventType eventType) {
        this.channel = channel;
        this.eventType = eventType;
        id.addAndGet(1);
    }

    public static AtomicInteger getId() {
        return id;
    }

    public IChannel getChannel() {
        return channel;
    }

    public void setChannel(IChannel channel) {
        this.channel = channel;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventObserver that = (EventObserver) o;
        return Objects.equals(channel, that.channel) &&
                eventType == that.eventType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, eventType);
    }
}
