package de.chess.fx.app.provider;

public class NicknameProvider implements INicknameProvider {
  @Override
  public String getDefaultRandomNickname() {
      return "Default-Player-" + Math.round((Math.random() * 100000));
  }
}
