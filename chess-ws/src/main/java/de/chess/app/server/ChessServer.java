package de.chess.app.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class ChessServer implements IChessServer {
  private static final Logger LOGGER = Logger.getLogger(ChessServer.class.getName());

  private final int port;
  private final boolean isBlockingMode;
  private final ServerProperties serverProperties;

  public ChessServer(ServerProperties serverProperties) {
    this.serverProperties = serverProperties;
    this.port = serverProperties.getServerPort();
    this.isBlockingMode = serverProperties.isBlockingMode();
  }

  @Override
  public void start() throws IOException {
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

    serverSocketChannel.socket().bind(new InetSocketAddress(port));
    serverSocketChannel.configureBlocking(isBlockingMode);

    LOGGER.info("Chess Backend Started...");
    LOGGER.info("Port: " + serverProperties.getServerPort());
    LOGGER.info("Blocking Mode: " + serverProperties.isBlockingMode());

    while (true) {
      SocketChannel socketChannel = serverSocketChannel.accept();

      if (socketChannel != null) {
        // do something with socketChannel...
      }
    }
  }
}
