package me.kalmemarq.minicraft;

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
import me.kalmemarq.minicraft.util.syncloader.SyncResourceReloader;
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

    private boolean requestReload = false;
    private boolean reloading = false;
    @Nullable
    private SyncResourceReloader syncResourceReloader;
    @Nullable
    private Thread reloadThread;

    public Minicraft(RunArgs runArgs) {
        INSTANCE = this;
        this.window = new Window("Minicraft P", runArgs.width(), runArgs.height(), "icon32.png", "icon64.png");
        this.keyboardHandler = new Keyboard(this);
        this.window.getWindowFrame().addKeyListener(this.keyboardHandler.getListener());

        this.font = new Font();
        this.setMenu(new TitleMenu());

        requestSyncReload();
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

            while (unprocessed >= 1) {
                ticks++;
                this.tick();
                unprocessed--;
            }

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
        if (this.reloadThread != null) {
        }

        this.window.close();
    }

    public void render() {
        Renderer.clear();

        if (this.reloading || requestReload) {
            Renderer.fill(0);
            Renderer.render("title.png", Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 - 16);

            if (syncResourceReloader != null) {
                Renderer.fillRect(Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 + 16, 120, 8, 0xFF_FF_FF_FF);
                Renderer.fillRect(Renderer.WIDTH / 2 - 60 + 1, Renderer.HEIGHT / 2 + 16 + 1, 120 - 2, 8 - 2, 0xFF_00_00_00);
                Renderer.fillRect(Renderer.WIDTH / 2 - 60 + 2, Renderer.HEIGHT / 2 + 16 + 2, (int)(120 * syncResourceReloader.getProgress()) - 4, 8 - 4, 0xFF_FF_FF_FF);
            }
        } else {
            if (this.world != null) {
                this.world.render();
            }

            if (this.menu != null) {
                this.menu.render();
            }
        }

        if (!this.window.hasFocus()) {
            Renderer.renderPanel(Renderer.WIDTH / 2 - 64, Renderer.HEIGHT / 2 - 16 - 4, 128, 40);
        }

        this.window.renderFrame();

        if (requestReload && !this.reloading) {
            requestReload = false;
            this.syncReloadAssets();
        }
    }

    public void update() {
    }

    public void tick() {
        if (!this.reloading && this.menu != null) {
            this.menu.tick();
        } else if (this.world != null) {
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

    public void requestSyncReload() {
        requestReload = true;
    }

    public void syncReloadAssets() {
        this.reloading = true;

        // Well... not that syncronous :/
        syncResourceReloader = new SyncResourceReloader(() -> {
            this.reloading = false;
            this.syncResourceReloader = null;
            this.requestReload = false;
            this.reloadThread = null;
        }, Sound.getReloader(), () -> {
            Renderer.images.put("font.png", new MinicraftImage("/font.png"));
            Renderer.images.put("hud.png", new MinicraftImage("/hud.png"));
            Renderer.images.put("tiles.png", new MinicraftImage("/tiles.png"));
            Renderer.images.put("title.png", new MinicraftImage("/title.png"));
        });

        reloadThread = new Thread(() -> {
            try {
                if (syncResourceReloader != null) {
                    syncResourceReloader.startReload();
                }
            } catch (Exception e) {
                System.out.println("well... fuck");
            }
        });

        reloadThread.setDaemon(true);

        reloadThread.start();
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
