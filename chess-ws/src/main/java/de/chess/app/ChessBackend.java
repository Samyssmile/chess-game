package de.chess.app;

import de.chess.app.server.ChessServer;
import de.chess.app.server.ServerProperties;

import java.io.IOException;
import java.util.logging.Logger;

/** Chess Server */
public class ChessBackend implements IChessBackend {
  private static final Logger LOGGER = Logger.getLogger(ChessBackend.class.getName());
  private static ChessBackend chessBackend;

  private ServerProperties serverProperties;

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    chessBackend = new ChessBackend();
    chessBackend.preStart();
    chessBackend.start();
    chessBackend.postStart();
  }

  @Override
  public void preStart() throws IOException {
    LOGGER.info("************* - Starting Chess Backend - ************");
    serverProperties = new ServerProperties();
    LOGGER.info("************* - " + serverProperties.getServerName() + "- ************");
  }

  @Override
  public void start() throws IOException, ClassNotFoundException {
    Thread gameServerThread = new ChessServer(serverProperties);
    gameServerThread.start();
  }

  @Override
  public void postStart() {
    LOGGER.info("Running post start operations..");
  }

  @Override
  public void onExit() {
    LOGGER.info("Exiting...");
  }
}
