package me.kalmemarq.minicraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import me.kalmemarq.minicraft.gfx.Font;
import me.kalmemarq.minicraft.gfx.MinicraftImage;
import me.kalmemarq.minicraft.gfx.Renderer;
import me.kalmemarq.minicraft.gui.Menu;
import me.kalmemarq.minicraft.gui.TitleMenu;
import me.kalmemarq.minicraft.main.RunArgs;
import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.JsonUtil;
import me.kalmemarq.minicraft.util.Keyboard;
import me.kalmemarq.minicraft.util.SplashManager;
import me.kalmemarq.minicraft.util.Window;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.resource.ReloadableResourceManager;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.ResourcePackManager;
import me.kalmemarq.minicraft.util.resource.loader.ResourceLoader;
import me.kalmemarq.minicraft.util.resource.loader.SyncResourceReloader;
import me.kalmemarq.minicraft.util.resource.loader.WithPreparationResourceReloader;
import me.kalmemarq.minicraft.util.resource.pack.VanillaResourcePack;
import me.kalmemarq.minicraft.util.resource.provider.DefaultResourcePackProvider;
import me.kalmemarq.minicraft.util.sound.SoundManager;
import me.kalmemarq.minicraft.world.World;

public class Minicraft {
    public static final int TPS = 30;
    private static Minicraft INSTANCE;

    private final Window window;
    private boolean running = true;

    public final Keyboard keyboardHandler;

    public final SoundManager soundManager;

    public Font font;

    @Nullable
    public Menu menu;

    @Nullable
    public World world;

    private final ReloadableResourceManager resourceManager;
    private final ResourcePackManager resourcePackManager;
    public final VanillaResourcePack defautResourcePack;
    @Nullable
    private ResourceLoader resourceLoader;
    public static ExecutorService WORKER = Executors.newSingleThreadExecutor();

    public Minicraft(RunArgs runArgs) {
        INSTANCE = this;
        this.resourcePackManager = new ResourcePackManager();
        this.defautResourcePack = new VanillaResourcePack();

        this.window = new Window("Minicraft P", runArgs.width(), runArgs.height());
        
        this.window.setIcon(
            this.defautResourcePack.open(new Identifier("icons/icon32.png")),
            this.defautResourcePack.open(new Identifier("icons/icon64.png"))
        );

        this.keyboardHandler = new Keyboard(this);
        this.window.getWindowFrame().addKeyListener(this.keyboardHandler.getListener());

        this.font = new Font();
        this.soundManager = new SoundManager();
        this.setMenu(new TitleMenu());

        this.resourcePackManager.addProvider(new DefaultResourcePackProvider(this.defautResourcePack));
        this.resourceManager = new ReloadableResourceManager();
        this.resourceManager.addReloader(this.soundManager);
        this.resourceManager.addReloader(Font.reloader);
        this.resourceManager.addReloader(Language.reloader);
        this.resourceManager.addReloader(SplashManager.getReloader());
        this.resourceManager.addReloader(new SyncResourceReloader() {
            @Override
            protected void reload(ResourceManager manager) {
                Renderer.images.put("font.png", new MinicraftImage("/font.png"));
                Renderer.images.put("default_font.png", new MinicraftImage("/default_font.png"));
                Renderer.images.put("hud.png", new MinicraftImage("/hud.png"));
                Renderer.images.put("title.png", new MinicraftImage("/title.png"));
                Renderer.images.put("tiles.png", new MinicraftImage("/tiles.png"));
            }
        });
        this.resourceManager.addReloader(new WithPreparationResourceReloader<Set<Identifier>>() {
            private static final Identifier PRELOAD_TEXTURES = new Identifier("textures/preload_textures.json");
       
            @Override
            protected Set<Identifier> prepare(ResourceManager manager) {
                Set<Identifier> textures = new HashSet<>();
                
                manager.getResources(PRELOAD_TEXTURES).forEach(res -> {
                    System.out.println("sup");

                    try (BufferedReader reader = res.getAsReader()) {
                        JsonArray arr = JsonUtil.deserialize(reader, JsonArray.class, true);

                        for (JsonElement el : arr) {
                            textures.add(new Identifier(el.getAsString()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                return textures;
            }

            @Override
            protected void apply(Set<Identifier> result, ResourceManager manager) {
                Renderer.textures.clear();
                
                result.forEach(texture -> {
                    System.out.println(texture);
                //     Resource res = manager.getResource(texture);

                //     if (res != null) {
                //         try(InputStream stream = res.getAsInputStream()) {
                //             BufferedImage image = ImageIO.read(stream);
                //             Renderer.textures.put(texture, new MinicraftImage(image));
                //         } catch (IOException e) {
                //             e.printStackTrace();
                //         }
                //     }
                });
            }
        });

        this.reloadResources();
    }

    public void run() {
        Renderer.loadImages();

        long lastT = System.currentTimeMillis();
        long lastR = System.nanoTime();
        long lastTick = System.nanoTime();

        int currentFPS = 0;
        int ticks = 0;
        int frameCounter = 0;
        int tickCounter = 0;
        double unprocessed = 0;
        double NS_PER_TICK = 1E9D / TPS;

        this.window.setMaxFrameLimit(300);

        while (this.running) {
            long now = System.nanoTime();

            unprocessed += (now - lastTick) / NS_PER_TICK;
			lastTick = now;

            while (unprocessed >= 1) {
				tickCounter++;
				this.tick();
				unprocessed--;
			}


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
                ticks = tickCounter;

                System.out.printf("%d FPS %d TPS\n", currentFPS, ticks);
                
                frameCounter = 0;
                tickCounter = 0;
            }
        }
    }

    public void close() {
        WORKER.shutdown();
        boolean succ;
        try {
            succ = WORKER.awaitTermination(3L, TimeUnit.SECONDS);
        } catch(Exception e) {
            e.printStackTrace();
            succ = false;
        }

        if (!succ) {
            WORKER.shutdownNow();
        }

        this.soundManager.close();

        this.window.close();
    }

    public void render() {
        Renderer.clear();

        if (this.resourceLoader != null) {
            Renderer.fill(0);
            Renderer.render("title.png", Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 - 16);

            Renderer.fillRect(Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 + 16, 120, 8, 0xFF_FF_FF_FF);
            Renderer.fillRect(Renderer.WIDTH / 2 - 60 + 1, Renderer.HEIGHT / 2 + 16 + 1, 120 - 2, 8 - 2, 0xFF_00_00_00);
            Renderer.fillRect(Renderer.WIDTH / 2 - 60 + 2, Renderer.HEIGHT / 2 + 16 + 2, (int)(120 * resourceLoader.getProgress()) - 4, 8 - 4, 0xFF_FF_FF_FF);

            if (this.resourceLoader.isCompleted()) {
                this.resourceLoader = null;
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
        
            if (!this.window.hasFocus()) {
                Renderer.renderPanel(Renderer.WIDTH / 2 - 68, Renderer.HEIGHT / 2 - 13, 136, 24);
                this.font.renderCentered("Click to Focus!", Renderer.WIDTH / 2 + 1, Renderer.HEIGHT / 2 - 4, (System.currentTimeMillis() / 300) % 2 == 0 ? 0x8F8F8F : 0x9F9F9F);
            }
        }

        this.window.renderFrame();
    }

    public void update() {
        if (this.resourceLoader == null && this.menu != null) {
            this.menu.update();
        } 
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
        reloadResources(false);
    }

    public void reloadResources(boolean reloadCurrent) {
        if (reloadCurrent) {
            this.resourceLoader = this.resourceManager.reload(() -> {
                System.out.println("Finished reloading resources");
            });
        } else {
            this.resourceLoader = this.resourceManager.reload(this.resourcePackManager.createResourcePacks(), () -> {
                System.out.println("Finished reloading resources");
            });
        }
    }

    public void onFileDrop(List<File> files) {}

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
