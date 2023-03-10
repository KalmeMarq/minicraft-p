package me.kalmemarq.minicraft.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;

import me.kalmemarq.minicraft.client.gfx.Font;
import me.kalmemarq.minicraft.client.gfx.MinicraftImage;
import me.kalmemarq.minicraft.client.gfx.Renderer;
import me.kalmemarq.minicraft.client.gui.Menu;
import me.kalmemarq.minicraft.client.gui.TitleMenu;
import me.kalmemarq.minicraft.client.util.Keyboard;
import me.kalmemarq.minicraft.client.util.Window;
import me.kalmemarq.minicraft.util.Identifier;
import me.kalmemarq.minicraft.util.TextManager;
import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.minicraft.util.Util.Version;
import me.kalmemarq.minicraft.util.language.Language;
import me.kalmemarq.minicraft.util.resource.ReloadableResourceManager;
import me.kalmemarq.minicraft.util.resource.Resource;
import me.kalmemarq.minicraft.util.resource.ResourceManager;
import me.kalmemarq.minicraft.util.resource.ResourcePackManager;
import me.kalmemarq.minicraft.util.resource.loader.ResourceLoader;
import me.kalmemarq.minicraft.util.resource.loader.ResourceReloader;
import me.kalmemarq.minicraft.util.resource.loader.WithPreparationResourceReloader;
import me.kalmemarq.minicraft.util.resource.pack.VanillaResourcePack;
import me.kalmemarq.minicraft.util.resource.provider.DefaultResourcePackProvider;
import me.kalmemarq.minicraft.util.sound.SoundManager;
import me.kalmemarq.minicraft.world.World;

public class Minicraft {
    public static final Version VERSION = new Version("1.0.0");
    public static final Logger LOGGER = Util.Logging.getLogger();

    public static boolean debug;
    
    public static final int TPS = 60;
    private static Minicraft INSTANCE;
    
    private final Window window;
    private boolean running = true;
    
    public final Keyboard keyboardHandler;
    
    public final SoundManager soundManager;
    public final TextManager textManager;
    
    public Font font;
    
    @Nullable
    public Menu menu;
    
    @Nullable
    public World world;

    public final Options options;
    
    public final ReloadableResourceManager resourceManager;
    private final ResourcePackManager resourcePackManager;
    public final VanillaResourcePack defautResourcePack;
    @Nullable
    private ResourceLoader resourceLoader;

    public int currentFPS;
    public int currentTicks;
    
    public Minicraft(ClientRunArgs runArgs) {
        INSTANCE = this;
        debug = runArgs.debug();
        
        if (!runArgs.gameDir().exists()) {
            if (!runArgs.gameDir().mkdir()) {
                throw new RuntimeException("Unable to create game directory");
            }
        }

        this.resourcePackManager = new ResourcePackManager();
        this.defautResourcePack = new VanillaResourcePack();
        
        this.options = new Options(runArgs.gameDir());
        this.options.load();

        this.window = new Window("Minicraft P", runArgs.width(), runArgs.height());
        
        this.window.setIcon(
            this.defautResourcePack.open(new Identifier("icons/icon32.png")),
            this.defautResourcePack.open(new Identifier("icons/icon64.png"))
        );
        
        this.keyboardHandler = new Keyboard(this);
        this.window.getWindowFrame().addKeyListener(this.keyboardHandler.getListener());
        
        this.font = new Font();
        this.textManager = new TextManager();
        this.soundManager = new SoundManager();
        this.setMenu(new TitleMenu());
        
        this.resourcePackManager.addProvider(new DefaultResourcePackProvider(this.defautResourcePack));
        this.resourceManager = new ReloadableResourceManager();
        this.resourceManager.addReloader(this.soundManager);
        this.resourceManager.addReloader(Font.reloader);
        this.resourceManager.addReloader(Language.reloader);
        this.resourceManager.addReloader(this.textManager);
        this.resourceManager.addReloader(preloadTextureReloader);
    }
    
    public void run() {
        Renderer.loadTitleTexture();
        this.reloadResources();
        
        long lastT = System.currentTimeMillis();
        long lastR = System.nanoTime();
        long lastTick = System.nanoTime();

        int frameCounter = 0;
        int tickCounter = 0;
        double unprocessed = 0;
        double NS_PER_TICK = 1E9D / TPS;
        
        this.window.setMaxFrameLimit(this.options.fps.value());
        
        while (this.running) {
            long now = System.nanoTime();
            int maxFPS = this.window.getMaxFrameLimit();
            double delta = (now - lastR) / 1.0E9;

            unprocessed += (now - lastTick) / NS_PER_TICK;
            lastTick = now;
            
            while (unprocessed >= 1) {
                tickCounter++;
                this.tick();
                unprocessed--;
            }
            
            if (maxFPS > 250 || delta > (0.9d / maxFPS)) {
                this.update();
                this.render();
                ++frameCounter;
                lastR = System.nanoTime();
            }
            
            if (this.window.shouldClose()) {
                this.queueQuit();
            }
            
            if (System.currentTimeMillis() - lastT >= 1000L) {
                lastT += 1000L;
                currentFPS = frameCounter;
                currentTicks = tickCounter;
                
                if (debug && !this.options.showDebugFPS.value()) System.out.printf("%d FPS %d TPS\n", currentFPS, currentTicks);
                
                frameCounter = 0;
                tickCounter = 0;
            }
        }
    }
    
    public void stop() {
        try {
            this.soundManager.close();
            this.resourceManager.close();
            
            Util.shutdownWorkers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.window.close();
        }
    }
    
    public void render() {
        Renderer.clear();
        
        if (this.resourceLoader != null) {
            this.renderLoadingOverlay();
        } else {
            if (this.world != null) {
                this.world.render();

                if (this.options.showDebugFPS.value() && this.world.playerEntity != null) {
                    this.font.render(this.world.getCurrentDepth().getName(), 1, 10);
                    this.font.render(String.format("Pos: %.4f/%.4f", this.world.playerEntity.getX(), this.world.playerEntity.getY()), 1, 19);
                    this.font.render(String.format("Health: ", this.world.playerEntity.getHealth()), 1, 28);
                    this.font.render(String.format("Stamina: ", this.world.playerEntity.getStamina()), 1, 37);    
                }
                
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

            if (this.options.showDebugFPS.value()) this.font.render(String.format("%d FPS", this.currentFPS), 1, 1);
        }
        
        this.window.renderFrame();
    }
    
    private static final Identifier TITLE_TEXTURE = new Identifier("textures/title.png");
    
    private void renderLoadingOverlay() {
        Renderer.fill(0);
        Renderer.renderTexturedQuad(TITLE_TEXTURE, Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 - 16, 120, 16);
        
        Renderer.renderColoredQuad(Renderer.WIDTH / 2 - 60, Renderer.HEIGHT / 2 + 16, 120, 8, 0xFF_FF_FF_FF);
        Renderer.renderColoredQuad(Renderer.WIDTH / 2 - 60 + 1, Renderer.HEIGHT / 2 + 16 + 1, 120 - 2, 8 - 2, 0xFF_00_00_00);
        Renderer.renderColoredQuad(Renderer.WIDTH / 2 - 60 + 2, Renderer.HEIGHT / 2 + 16 + 2, (int)(120 * resourceLoader.getProgress()) - 4, 8 - 4, 0xFF_FF_FF_FF);
        
        if (this.resourceLoader.isCompleted()) {
            this.resourceLoader = null;
        }
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
            this.resourceLoader = this.resourceManager.reload();
        } else {
            this.resourcePackManager.findPacks();
            this.resourceLoader = this.resourceManager.reload(this.resourcePackManager.createResourcePacks());
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
    
    private static final ResourceReloader preloadTextureReloader = new WithPreparationResourceReloader<Set<Identifier>>() {
        private static final Identifier PRELOAD_TEXTURES = new Identifier("textures/preload_textures.json");
        
        @Override
        protected Set<Identifier> prepare(ResourceManager manager) {
            Set<Identifier> textures = new HashSet<>();
            
            for (Resource res : manager.getResources(PRELOAD_TEXTURES)) {
                try (BufferedReader reader = res.getAsReader()) {
                    JsonNode arr = Util.Json.parse(reader);
                    
                    if (arr.isArray()) {
                        for (JsonNode el : arr) {
                            textures.add(new Identifier(el.asText()));
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to load preload_textures.json in resource pack: ", res.getResourcePackName(), e);
                }
            };
            
            return textures;
        }
        
        @Override
        protected void apply(Set<Identifier> result, ResourceManager manager) {
            Renderer.textures.clear();
            
            result.forEach(texture -> {
                Resource res = manager.getResource(texture);
                
                if (res != null) {
                    try(InputStream stream = res.getAsInputStream()) {
                        MinicraftImage img = new MinicraftImage(stream);
                        Renderer.textures.put(texture, img);
                        String path = texture.getPath();
                        Renderer.images.put(path.substring(path.lastIndexOf('/') + 1), img);
                    } catch (IOException e) {
                        LOGGER.error("Failed to preload texture {}", texture, e);
                    }
                }
            });
        }
    };
}
