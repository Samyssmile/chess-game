package de.chess.fx.app.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;


public class MusicPlayer {
    private static final String MUSIC_FILE_NAME = "audio/music/got.mp3";
    private final double VOLUME=.03;
    private static  MediaPlayer player;
    private static Media media;

    public MusicPlayer() throws URISyntaxException {
        media = new Media(Thread.currentThread().getContextClassLoader().getResource(MUSIC_FILE_NAME).toURI().toASCIIString());
        player = new MediaPlayer(media);
        player.setVolume(VOLUME);
    }

    public void playBackgroundMusic() {
        player.play();
    }


}
