package de.chess.app;

import de.chess.app.server.ServerProperties;
import de.chess.logging.WebTextLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

/** Chess Server */
public class ChessBackend implements IChessBackend {
  private static final Logger LOGGER = Logger.getGlobal();
  private static ChessBackend chessBackend;

  private ServerProperties serverProperties;

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    try {
      WebTextLogger.setup();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Problems with creating the log files");
    }
    chessBackend = new ChessBackend();
    chessBackend.preStart();
    chessBackend.start();
    chessBackend.postStart();
  }

  @Override
  public void preStart() throws IOException {
    serverProperties = new ServerProperties();
    InetAddress ip = InetAddress.getLocalHost();
    String hostname = ip.getHostName();

    LOGGER.info("************* - Starting Chess Backend - ************");
    LOGGER.info("************* - " + serverProperties.getServerName() + "- ************");
    LOGGER.info("************* - " + ip + "- ************");
    LOGGER.info("************* - " + hostname + "- ************");
  }

  @Override
  public void start() throws IOException, ClassNotFoundException {
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
