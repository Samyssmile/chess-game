package de.chess.fx.app.handler;

public interface IEventHandler {

    public void registerForEvent(IChannel channel, EventType eventType);

    public void removeObserver(IChannel channel);

    public void fireGameEvent(EventType eventType, EventData eventData);


}
