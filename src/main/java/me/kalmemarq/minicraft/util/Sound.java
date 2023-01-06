package me.kalmemarq.minicraft.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.common.collect.Maps;
import me.kalmemarq.minicraft.main.Main;
import me.kalmemarq.minicraft.util.syncloader.ResourceLoader;

public class Sound {
    private static final Map<Identifier, Sound> SOUNDS = Maps.newHashMap();

    private static final List<Identifier> SOUND_IDS = new ArrayList<>();

    public static final Identifier BOSS_DEATH = new Identifier("sounds/bossdeath.wav");
    public static final Identifier SELECT = new Identifier("sounds/select.wav");
    public static final Identifier CONFIRM = new Identifier("sounds/confirm.wav");
    public static final Identifier CRAFT = new Identifier("sounds/craft.wav");
    public static final Identifier DEATH = new Identifier("sounds/death.wav");
    public static final Identifier EXPLODE = new Identifier("sounds/explode.wav");
    public static final Identifier FUSE = new Identifier("sounds/fuse.wav");
    public static final Identifier MONSTER_HURT = new Identifier("sounds/monsterhurt.wav");
    public static final Identifier PICKUP = new Identifier("sounds/pickup.wav");
    public static final Identifier PLAYER_HURT = new Identifier("sounds/playerhurt.wav");

    static {
        SOUND_IDS.add(BOSS_DEATH);
        SOUND_IDS.add(CONFIRM);
        SOUND_IDS.add(CRAFT);
        SOUND_IDS.add(DEATH);
        SOUND_IDS.add(EXPLODE);
        SOUND_IDS.add(FUSE);
        SOUND_IDS.add(MONSTER_HURT);
        SOUND_IDS.add(PICKUP);
        SOUND_IDS.add(PLAYER_HURT);
        SOUND_IDS.add(SELECT);
    }

    private final Set<Clip> clips = Collections.synchronizedSet(new HashSet<>());

    private final AudioFormat format;
    private final byte[] bytes;

    private static final ResourceLoader reloader = new ResourceLoader() {
        @Override
        public void reload() {
            SOUNDS.forEach((identifier, sound) -> sound.stop());
            SOUNDS.clear();

            for (Identifier sound : SOUND_IDS) {
                SOUNDS.put(sound, new Sound(sound.getPath()));
            }
        }
    };

    public static ResourceLoader getReloader() {
        return reloader;
    }

    public Sound(String path) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(Objects.requireNonNull(Main.class.getResourceAsStream("/" + path))));

            this.format = stream.getFormat();
            this.bytes = stream.readAllBytes();

            for (int i = 0; i < 4; i++) {
                this.createNewClip();
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new Error(e);
        }
    }

    public static void play(Identifier soundId) {
        Sound sound = SOUNDS.get(soundId);

        if (sound != null) {
            sound.play();
        }
    }

    public static void stop(Identifier soundId) {
        Sound sound = SOUNDS.get(soundId);

        if (sound != null) {
            sound.stop();
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

    private void play() {
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

    private void stop() {
        for (Clip clip : clips) {
            clip.stop();
        }
    }
}








