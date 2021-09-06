package de.chess.fx.app.ui.views.bottom;

import de.chess.fx.app.ui.command.ICommando;
import de.chess.fx.app.ui.command.menu.MuteMusicCommand;

public class BottomAudioPanelViewModel {
    public ICommando muteAudioCommand() {
        return  new MuteMusicCommand();
    }

}
