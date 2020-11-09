package de.chess.app;

import java.io.IOException;

public interface IChessBackend {

  public void preStart() throws IOException;

  public void start() throws IOException;

  public void postStart();

  public void onExit();
}
