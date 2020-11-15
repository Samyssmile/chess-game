package de.chess.app.server;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Flow;

public interface IGameClientReceiver extends Flow.Subscriber<ServerGameClient>  {

    public void onIncomingClient(ServerGameClient gameClient);

    public int getNumberOfConnectedClients();


}
