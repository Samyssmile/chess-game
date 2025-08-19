package de.chess.fx.app.handler;

public enum EventType {
  GAME_GRANTED,
  GAME_DECLINED,
  OPEN_NEW_GAME,
  /** A new Player joined your hosted game. */
  PLAYER_JOINED,
  MOVE_DONE,
  REMIS,
  WON,
  LOSE,
  /** This Player joined a hosted game another player created. */
  JOINED_GAME,
  /** Game has started and is now running. */
  GAME_STARTED;
}
