package me.kalmemarq.minicraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.gfx.MinicraftImage;
import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.gui.Menu;
import me.kalmemarq.minicraft.gui.TitleMenu;
import me.kalmemarq.minicraft.main.RunArgs;
import me.kalmemarq.minicraft.util.Keyboard;
import me.kalmemarq.minicraft.util.Sound;
import me.kalmemarq.minicraft.util.Window;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.loader.ResourceLoader;
import me.kalmemarq.minicraft.util.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.loader.SyncResourceReloader;
import me.kalmemarq.minicraft.world.World;

public class Minicraft {
    public static final int TPS = 20;
    private static Minicraft INSTANCE;

    private final Window window;
    private boolean running = true;

    public final Keyboard keyboardHandler;

    public Font font;

    @Nullable
    public Menu menu;

    @Nullable
    public World world;

    private boolean reloading = false;
    @Nullable
    private ResourceLoader resourceReloader = null;
    private static ExecutorService worker = Executors.newSingleThreadExecutor();

    public Minicraft(RunArgs runArgs) {
        INSTANCE = this;
        this.window = new Window("Minicraft P", runArgs.width(), runArgs.height(), "icon32.png", "icon64.png");
        this.keyboardHandler = new Keyboard(this);
        this.window.getWindowFrame().addKeyListener(this.keyboardHandler.getListener());

        this.font = new Font();
        this.setMenu(new TitleMenu());

        reloadResources();
    }

    public void run() {
        Renderer.loadImages();

        long lastT = System.currentTimeMillis();
        long lastR = System.nanoTime();
        long lastTick = System.nanoTime();

        int currentFPS = 0;
        int ticks = 0;
        float tickDelta = 0.0f;
        float deltaTime = 0.0f;
        
        int unprocessed = 0;
        int frameCounter = 0;
        long lastMS = System.currentTimeMillis();

        while (this.running) {
            long now = System.nanoTime();

            long nowMS = System.currentTimeMillis();
            deltaTime = (float)(nowMS - lastMS);
            lastMS = nowMS;

            // System.out.println(deltaTime);


            double nsPerTick = 1E9D / TPS;
			unprocessed += (now - lastTick) / nsPerTick; 
			lastTick = now;

            this.tick();
            // while (unprocessed >= 1) {
            //     ticks++;
            //     unprocessed--;
            // }

            this.window.setMaxFrameLimit(300);

            int maxFPS = this.window.getMaxFrameLimit();
            double delta = (now - lastR) / 1.0E9;
            if (maxFPS > 250 || delta >  (double)(0.9 /maxFPS)) {
                this.update();
                this.render();
                ++frameCounter;
                lastR = System.nanoTime();
            }

            if (this.window.shouldClose()) {
                this.queueQuit();
            }

            if (System.currentTimeMillis() - lastT > 1000) {
                lastT += 1000L;
                currentFPS = frameCounter;
                
                System.out.printf("%d FPS %d TPS\n", currentFPS, ticks);
                
                frameCounter = 0;
                ticks = 0;
            }
        }
    }

    public void close() {
        worker.shutdown();
        boolean succ;
        try {
            succ = worker.awaitTermination(3L, TimeUnit.SECONDS);
        } catch(Exception e) {
            e.printStackTrace();
            succ = false;
        }

        if (!succ) {
            worker.shutdownNow();
        }

        this.window.close();
    }

    public void render() {
        Renderer.clear();

        if (this.reloading || this.resourceReloader != null) {
            Renderer.fill(0);
            Renderer.render("title.png", Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 - 16);

            if (resourceReloader != null) {
                Renderer.fillRect(Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 + 16, 120, 8, 0xFF_FF_FF_FF);
                Renderer.fillRect(Renderer.WIDTH / 2 - 60 + 1, Renderer.HEIGHT / 2 + 16 + 1, 120 - 2, 8 - 2, 0xFF_00_00_00);
                Renderer.fillRect(Renderer.WIDTH / 2 - 60 + 2, Renderer.HEIGHT / 2 + 16 + 2, (int)(120 * resourceReloader.getProgress()) - 4, 8 - 4, 0xFF_FF_FF_FF);
            }
        } else {
            if (this.world != null) {
                this.world.render();

                Renderer.enableBlend();
                Renderer.defaultBlendFunc();

                Renderer.renderColoredQuad(1, Renderer.HEIGHT - 12, Renderer.WIDTH - 2, 10, 0x99_000000);

                Renderer.disableBlend();
            }

            if (this.menu != null) {
                this.menu.render();
            }
        }

        // if (!this.reloading && !this.window.hasFocus()) {
        //     Renderer.renderPanel(Renderer.WIDTH / 2 - 68, Renderer.HEIGHT / 2 - 13, 136, 24);
        //     this.font.renderCentered("Click to Focus!", Renderer.WIDTH / 2 + 1, Renderer.HEIGHT / 2 - 4, (System.currentTimeMillis() / 300) % 2 == 0 ? 0x8F8F8F : 0x9F9F9F);
        // }

        this.window.renderFrame();
    }

    public void update() {
    }

    public void tick() {
        if (this.menu != null) {
            this.menu.tick();
        }
        
        if (this.world != null) {
            this.world.tick();
        }
    }

    public void setMenu(@Nullable Menu menu) {
        this.menu = menu;

        if (this.menu == null && this.world == null) {
            this.menu = new TitleMenu();
        }

        if (this.menu != null) {
            this.menu.init(this);
        }
    }

    public void reloadResources() {
        this.reloading = true;

        List<ResourceReloader> list = new ArrayList<>();
        list.add(Sound.getReloader());
        list.add(Language.reloader);
        list.add(new SyncResourceReloader() {
            @Override
            protected void reload() {
                Renderer.images.put("font.png", new MinicraftImage("/font.png"));
                Renderer.images.put("hud.png", new MinicraftImage("/hud.png"));
                Renderer.images.put("title.png", new MinicraftImage("/title.png"));
                Renderer.images.put("tiles.png", new MinicraftImage("/tiles.png"));
            }
        });
        list.add(TitleMenu.splashReloader);
        resourceReloader = new ResourceLoader(worker, list, () -> {
            this.reloading = false;
            this.resourceReloader = null;
            System.out.println("Finished reloading resources");
        });
    }

    public void queueQuit() {
        this.running = false;
    }

    public Window getWindow() {
      return this.window;
    }

    public static Minicraft getInstance() {
        return INSTANCE;
    }
}
