package de.chess.fx.app.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.Objects;


public class MusicPlayer {
    private static final String MUSIC_FILE_NAME = "audio/music/got.bm";
    private final double VOLUME=.05;
    private static MediaPlayer player;
    private static Media media;

    public static MusicPlayer instance = null;

    public static MusicPlayer getInstance() throws URISyntaxException {
        if (Objects.isNull(instance)) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    private MusicPlayer() throws URISyntaxException {
        media = new Media(Thread.currentThread().getContextClassLoader().getResource(MUSIC_FILE_NAME).toURI().toASCIIString());
        player = new MediaPlayer(media);
        player.setVolume(VOLUME);
    }

    public void playBackgroundMusic() {
        player.play();
    }

    public void stopBackgroundMusic(){
        player.setMute(true);
    }

    public void muteUnmuteMusic(){
        if (player.isMute()){
            player.setMute(false);
        }else{
            player.setMute(true);
        }
    }


}
