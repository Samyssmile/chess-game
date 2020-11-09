package de.chess.app;

public interface IChessBackend {

    public void preStart();
    public void start();
    public void postStart();
    public void onExit();
}
