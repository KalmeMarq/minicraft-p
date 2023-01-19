package me.kalmemarq.minicraft.util.sound;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;

import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;

public class SoundManager extends SyncResourceReloader implements AutoCloseable {
    private static final int MAX_CLIP_POOL_SIZE = 4;

    private final Map<String, SoundBuffer> buffers = Maps.newHashMap();
    private final Map<String, Integer> clipCounts = Maps.newHashMap();
    private final Map<String, Clip> sounds = new ConcurrentHashMap<>();

    public void play(String sound) {
        this.play(sound, 1.0f);
    }

    private boolean supportsVolumeControl(Clip clip) {
        return clip.isControlSupported(FloatControl.Type.VOLUME);
    }

    public void play(String sound, float volume) {
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
                    System.out.println("No sound buffer found for " + sound);
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
    private Clip createClip(String sound) {
        try {

            SoundBuffer buffer = buffers.get(sound);

            if (buffer == null) {
                System.out.println("No sound buffer found for " + sound);
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
        for (Map.Entry<String, Clip> entry : sounds.entrySet()) {
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

        for (String sound : SoundEvents.REGISTRY.values()) {
            buffers.put(sound, new SoundBuffer(sound));
        }

        Set<String> preload = new HashSet<>();
        preload.add(SoundEvents.CONFIRM);
        preload.add(SoundEvents.SELECT);

        for (String sound : preload) {
            sounds.put(sound, createClip(sound));
            clipCounts.put(sound, 1);
        }
    }

    @Override
    public void close() {
        stopAll();
    }
}
