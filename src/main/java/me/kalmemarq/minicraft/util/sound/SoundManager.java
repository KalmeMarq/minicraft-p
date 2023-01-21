package me.kalmemarq.minicraft.util.sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.minicraft.util.resource.Resource;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;

public class SoundManager extends SyncResourceReloader implements AutoCloseable {
    protected static final Logger LOGGER = Util.Logging.getLogger();
    private static final Identifier SOUND_DEFINITIONS = new Identifier("sounds/sounds.json");

    private static final int MAX_CLIP_POOL_SIZE = 4;

    private final Map<Identifier, SoundBuffer> buffers = Maps.newHashMap();
    private final Map<Identifier, Integer> clipCounts = Maps.newHashMap();
    private final Map<Identifier, Clip> sounds = new ConcurrentHashMap<>();

    public void play(Identifier sound) {
        this.play(sound, 1.0f);
    }

    private boolean supportsVolumeControl(Clip clip) {
        return clip.isControlSupported(FloatControl.Type.VOLUME);
    }

    public void play(Identifier sound, float volume) {
        if (volume < 0) volume = 0;
        if (volume > 1) volume = 1;

        if (this.clipCounts.containsKey(sound) && this.clipCounts.get(sound) >= MAX_CLIP_POOL_SIZE) {
            for (Clip clip : sounds.values()) {
                if (clip.getFramePosition() == 0 || clip.getFramePosition() == clip.getFrameLength()) {
                    clip.setFramePosition(0);
                   
                    if (supportsVolumeControl(clip)) {
                        FloatControl volumeCtrl = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
                        volumeCtrl.setValue(volume);
                    }
                    
                    clip.start();

                    break;
                }
            }

        } else {
            try {
                SoundBuffer buffer = buffers.get(sound);

                if (buffer == null) {
                    LOGGER.warn("No sound buffer found for {}", sound);
                    return;
                }

                Clip clip = AudioSystem.getClip();
                clip.open(buffer.getFormat(), buffer.getBytes(), 0, buffer.getBytes().length);
                
                if (supportsVolumeControl(clip)) {
                    FloatControl volumeCtrl = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
                    volumeCtrl.setValue(volume);
                }

                sounds.put(sound, clip);
                clipCounts.put(sound, clipCounts.getOrDefault(sound, 0) + 1);

                clip.start();
            } catch (LineUnavailableException e) {
                throw new Error(e);
            }
        }
    }

    @Nullable
    private Clip createClip(Identifier sound) {
        try {

            SoundBuffer buffer = buffers.get(sound);

            if (buffer == null) {
                LOGGER.warn("No sound buffer found for {}", sound);
                return null;
            }

            Clip clip = AudioSystem.getClip();
            clip.open(buffer.getFormat(), buffer.getBytes(), 0, buffer.getBytes().length);

            return clip;
        } catch (LineUnavailableException e) {
            return null;
        }
    }
    
    public void stopAll() {
        for (Map.Entry<Identifier, Clip> entry : sounds.entrySet()) {
            Clip clip = entry.getValue();
            clip.stop();
            clip.flush();
            clip.close();
            clipCounts.clear();
            sounds.remove(entry.getKey());
        }
    }
    
    @Override
    protected void reload(ResourceManager manager) {
        stopAll();
        buffers.clear();

        Map<Identifier, Identifier> map = new HashMap<>();

        for (Resource res : manager.getResources(SOUND_DEFINITIONS)) {
            try (BufferedReader reader = res.getAsReader()) {
                JsonNode obj = Util.Json.parse(reader);

                for (Iterator<Entry<String, JsonNode>> iter = obj.fields(); iter.hasNext();) {
                    Entry<String, JsonNode> entry = iter.next();

                    Identifier soundId = new Identifier(entry.getKey());
                    JsonNode soundPath = entry.getValue().get("sound");
                    
                    if (soundPath != null && soundPath.isTextual()) {
                        try {
                            map.put(soundId, new Identifier(soundPath.asText()));
                        } catch (RuntimeException e) {
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Failed to load sounds.json in resource pack: {}", res.getResourcePackName(), e);
            }
        }

        for (Entry<Identifier, Identifier> entry : map.entrySet()) {
            Resource res = manager.getResource(entry.getValue());

            if (res != null) {
                try (InputStream stream = res.getAsInputStream()) {
                    SoundBuffer buffer = new SoundBuffer(entry.getKey(), stream);

                    if (!buffer.failedToLoad) {
                        buffers.put(entry.getKey(), buffer);
                    }
                } catch (IOException e) {}
            }
        }

        Set<Identifier> preload = new HashSet<>();
        preload.add(SoundEvents.CONFIRM);
        preload.add(SoundEvents.SELECT);

        for (Identifier sound : preload) {
            sounds.put(sound, createClip(sound));
            clipCounts.put(sound, 1);
        }
    }

    @Override
    public void close() {
        stopAll();
    }
}
