package me.kalmemarq.minicraft.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import me.kalmemarq.minicraft.main.Main;

public class Sound {
    public static final Sound BOSS_DEATH = new Sound("/sounds/bossdeath.wav");
    public static final Sound CONFIRM = new Sound("/sounds/confirm.wav");
    public static final Sound CRAFT = new Sound("/sounds/craft.wav");
    public static final Sound DEATH = new Sound("/sounds/death.wav");
    public static final Sound EXPLODE = new Sound("/sounds/explode.wav");
    public static final Sound FUSE = new Sound("/sounds/fuse.wav");
    public static final Sound MONSTER_HURT = new Sound("/sounds/monsterhurt.wav");
    public static final Sound PICKUP = new Sound("/sounds/pickup.wav");
    public static final Sound PLAYER_HURT = new Sound("/sounds/playerhurt.wav");
    public static final Sound SELECT = new Sound("/sounds/select.wav");

    private final Set<Clip> clips = Collections.synchronizedSet(new HashSet<>());

    private final AudioFormat format;
    private final byte[] bytes;

    public static void load() {}

    public Sound(String path) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(Objects.requireNonNull(Main.class.getResourceAsStream(path))));

            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();

            for (int i = 0; i < 4; i++) {
                this.createNewClip();
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new Error(e);
        }
    }

    private Clip createNewClip() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(this.format, this.bytes, 0, this.bytes.length);
            clips.add(clip);
            return clip;
        } catch (LineUnavailableException e) {
            throw new Error(e);
        }
    }

    public void play() {
        new Thread(() -> {
            Clip clip = clips.stream()
                .filter(c ->
                    c.getFramePosition() == 0 ||
                        c.getFramePosition() == c.getFrameLength())
                .findFirst()
                .orElseGet(this::createNewClip);

            clip.setFramePosition(0);
            clip.start();
        }).start();
    }
}








