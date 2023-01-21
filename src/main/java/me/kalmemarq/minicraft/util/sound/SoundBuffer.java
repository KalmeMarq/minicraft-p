package me.kalmemarq.minicraft.util.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import me.kalmemarq.minicraft.util.Identifier;

public class SoundBuffer {
    private AudioFormat format;
    private byte[] bytes;
    protected boolean failedToLoad;

    public SoundBuffer(Identifier id, InputStream inputStream) {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream))) {
            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();
        } catch (UnsupportedAudioFileException | IOException e) {
            SoundManager.LOGGER.error("Failed to load sound buffer for ", id, e);
            this.failedToLoad = true;
        }
    }

    public AudioFormat getFormat() {
      return format;
    }

    public byte[] getBytes() {
      return bytes;
    }
}
