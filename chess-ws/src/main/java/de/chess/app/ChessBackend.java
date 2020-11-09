package de.chess.app;

import java.util.logging.Logger;

/** Chess Server */
public class ChessBackend implements IChessBackend {
  private static final Logger LOGGER = Logger.getLogger(ChessBackend.class.getName());
  private static ChessBackend chessBackend;

  public static void main(String[] args) {
    chessBackend = new ChessBackend();
    chessBackend.preStart();
    chessBackend.start();
    chessBackend.postStart();
  }

  @Override
  public void preStart() {
    LOGGER.info("************* - Starting Chess Backend - ************");
  }

  @Override
  public void start() {
    LOGGER.info("Chess Backend Started...");
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
