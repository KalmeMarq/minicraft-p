package me.kalmemarq.minicraft.util.syncloader;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

// A Sync reload cuz I'm too stupid to use completablefuture
public class SyncResourceReloader {
    private float progress;
    private List<ResourceLoader> loaders;
    private OnFinish onFinish;

    public SyncResourceReloader(OnFinish onFinish, ResourceLoader... loaders) {
        this.onFinish = onFinish;
        this.loaders = Arrays.asList(loaders);
    }

    public void startReload() throws InterruptedException {
        int finished = 0;
        int total = loaders.size();

        for (ResourceLoader loader : loaders) {
            loader.reload();

            ++finished;

            progress = finished / (float)total;
        }

        // it reloads too fast. Suffering from success
        Thread.sleep(1000);

        onFinish.finish();
    }

    public float getProgress() {
        return progress;
    }

    @FunctionalInterface
    public interface OnFinish {
        void finish();
    }
}
