package de.chess.app.server;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Flow;

public interface IGameClientReceiver extends Flow.Subscriber<SocketChannel>  {

    public void onIncomingClient(SocketChannel socketChannel);

    public int getNumberOfConnectedClients();


}
