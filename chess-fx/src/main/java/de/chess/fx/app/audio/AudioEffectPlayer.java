package de.chess.fx.app.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class AudioEffectPlayer {

    private static final String AUDIO_EFFECT_FOLDER = "audio" + File.separator + "effects";
    private static final int START_POSITION = 0;
    private boolean isNotFinished = true;

    public void playSound(AudioEffectType type) {
        try {

            String filePath = getSoundFilePath(type);

            final Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            URL audioFileResource =
                    classLoader.getResource(AUDIO_EFFECT_FOLDER + File.separator + filePath);
            URI audioFileURI;

            Objects.requireNonNull(audioFileResource);
            audioFileURI = audioFileResource.toURI();
            addStopListener(clip);

            clip.open(AudioSystem.getAudioInputStream(new File(audioFileURI)));

            while (isNotFinished) {
                clip.setFramePosition(START_POSITION);
                clip.start();
                clip.drain();
                if (!clip.isActive() && !clip.isRunning()) {
                    isNotFinished = false;
                }
            }


        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void addStopListener(Clip clip) {
        clip.addLineListener(
                event -> {
                    if (event.getType() == LineEvent.Type.STOP) clip.close();
                });
    }

    private String getSoundFilePath(AudioEffectType type) {
        return switch (type) {
            case MOVE -> "move.wav";
            case WIN -> "win.wav";
            case LOSE -> "lose.wav";
            case REMIS -> "remis.wav";
            case NOT_ALLOWED -> "not_allowed.wav";
            case MESSAGE -> "message.wav";
            case PLAYER_JOINED -> "player_joined.wav";
            case TEST -> "test.wav";
        };
    }

    public float getVolume(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(Clip clip, float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
