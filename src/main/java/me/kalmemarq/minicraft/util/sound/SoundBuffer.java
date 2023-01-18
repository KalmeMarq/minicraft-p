package me.kalmemarq.minicraft.util.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import me.kalmemarq.minicraft.main.Main;

public class SoundBuffer {
    private AudioFormat format;
    private byte[] bytes;

    public SoundBuffer(String path) {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(
            new BufferedInputStream(Objects.requireNonNull(Main.class.getResourceAsStream("/" + path))))) {
            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public AudioFormat getFormat() {
      return format;
    }

    public byte[] getBytes() {
      return bytes;
    }
}
