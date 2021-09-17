package de.chess.fx.app.ui.command.menu;

import de.chess.fx.app.audio.MusicPlayer;
import de.chess.fx.app.ui.command.ICommando;

import java.net.URISyntaxException;

public class MuteMusicCommand implements ICommando {

    @Override
    public void execute() {
        try {
            MusicPlayer.getInstance().muteUnmuteMusic();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
