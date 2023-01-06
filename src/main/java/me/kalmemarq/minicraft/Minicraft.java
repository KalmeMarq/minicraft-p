package me.kalmemarq.minicraft;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.gui.Menu;
import me.kalmemarq.minicraft.gui.TitleMenu;
import me.kalmemarq.minicraft.main.RunArgs;
import me.kalmemarq.minicraft.util.KeyboardHandler;
import me.kalmemarq.minicraft.util.Sound;
import me.kalmemarq.minicraft.util.Window;
import me.kalmemarq.minicraft.world.World;

public class Minicraft {
    public static final int TPS = 20;
    private static Minicraft INSTANCE;

    private final Window window;
    private boolean running = true;

    public final KeyboardHandler keyboardHandler;

    public Font font;

    @Nullable
    public Menu menu;

    @Nullable
    public World world;

    public Minicraft(RunArgs runArgs) {
        INSTANCE = this;
        this.window = new Window("Minicraft P", runArgs.width(), runArgs.height(), "icon32.png", "icon64.png");
        this.keyboardHandler = new KeyboardHandler(this);
        this.window.getWindowFrame().addKeyListener(this.keyboardHandler.getListener());

        this.font = new Font();
        this.setMenu(new TitleMenu());
    }

    public void run() {
        Renderer.loadImages();
        Sound.load();

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
                
                System.out.println(String.format("%d FPS %d TPS", currentFPS, ticks));
                
                frameCounter = 0;
                ticks = 0;
            }
        }
    }

    public void close() {
        this.window.close();
    }

    public void render() {
        Renderer.clear();

        if (this.world != null) {
            this.world.render();
        }

        if (this.menu != null) {
            this.menu.render();
        }

        if (!this.window.hasFocus()) {
            Renderer.renderPanel(Renderer.WIDTH / 2 - 64, Renderer.HEIGHT / 2 - 16 - 4, 128, 40);
        }

        this.window.renderFrame();
    }

    public void update() {
    }

    public void tick() {
        if (this.menu != null) {
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
