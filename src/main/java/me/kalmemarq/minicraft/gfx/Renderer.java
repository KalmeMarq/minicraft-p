package me.kalmemarq.minicraft.gfx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.jetbrains.annotations.Nullable;

import me.kalmemarq.minicraft.util.MathHelper;

public class Renderer {
    public static final int WIDTH = 320;
    public static final int HEIGHT = 194;

    private static Renderer INSTANCE;

    public static int[] zbuffer = new int[WIDTH * HEIGHT];
    public static int[] pixels = new int[WIDTH * HEIGHT];

    public static final Map<String, MinicraftImage> images = new HashMap<>();

    public static final Stack<Camera> cameraStack = new Stack<>();
    public static Camera camera = new Camera();

    public static void loadImages() {
        images.put("title.png", new MinicraftImage("/title.png"));
        images.put("hud.png", new MinicraftImage("/hud.png"));
        images.put("font.png", new MinicraftImage("/font.png"));
        images.put("tiles.png", new MinicraftImage("/tiles.png"));
    }

    public static void reset() {
        Renderer.clear();
        Renderer.cameraStack.clear();
        Renderer.camera = new Camera();
    }

    public static void pushCamera() {
        Renderer.cameraStack.add(Renderer.camera);
        Renderer.camera = new Camera();
    }

    public static void popCamera() {
        Renderer.camera = Renderer.cameraStack.pop();
    }

    public static MinicraftImage getImage(String name) {
        return images.get(name);
    }
    
    public static void fill(int color) {
        for (int x = 0; x < Renderer.WIDTH; x++) {
            for (int y = 0; y < Renderer.HEIGHT; y++) {
               pixels[y * Renderer.WIDTH + x] = color;
            }
        }
    }

    public static void fillRect(int x, int y, int width, int height, int color) {
        int xt = x - Renderer.camera.getTx();
        int yt = y - Renderer.camera.getTy();

        for (int xx = xt; xx < xt + width; xx++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;

            for (int yy = yt; yy < yt + height; yy++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
               
                pixels[yy * Renderer.WIDTH + xx] = color;
            }
        }
    }

    public static void render(String image, int x, int y) {
        render(getImage(image), x, y);
    }

    public static void render(MinicraftImage image, int x, int y) {
        int xt = x - Renderer.camera.getTx();
        int yt = y - Renderer.camera.getTy();

        for (int xx = xt, xs = 0; xx < xt + image.getWidth(); xx++, xs++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;
            if (scissor && (xx < scissorBox[0] || xx >= scissorBox[2])) continue;
            
            for (int yy = yt, ys = 0; yy < yt + image.getHeight(); yy++, ys++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
                if (scissor && (yy < scissorBox[1] || yy >= scissorBox[3])) continue;

                int color = image.getPixels()[ys * image.getWidth() + xs];

                if (blend) {
                    int idx = yy * Renderer.WIDTH + xx;
                    pixels[idx] = blendEquation.calc(color, pixels[idx], srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor);
                } else {
                    // Discard color if alpha == 0
                    if ((color >> 24 & 0xFF) != 0) { 
                        pixels[yy * Renderer.WIDTH + xx] = color;
                    }
                }
            }
        }
    }

    public static void render(String image, int x, int y, int u, int v, int us, int vs) {
        render(getImage(image), x, y, u, v, us, vs);
    }

    public static void render(String image, int x, int y, int u, int v, int us, int vs, int color) {
        render(getImage(image), x, y, u, v, us, vs, color);
    }

    public static void render(MinicraftImage image, int x, int y, int u, int v, int us, int vs, int color) {
        int xt = x - Renderer.camera.getTx();
        int yt = y - Renderer.camera.getTy();

        for (int xx = xt, xs = u; xx < xt + us; xx++, xs++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;

            for (int yy = yt, ys = v; yy < yt + vs; yy++, ys++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
            
                int c = image.getPixels()[ys * image.getWidth() + xs];

                if ((c >> 24 & 0xFF) != 0) {
                    pixels[yy * Renderer.WIDTH + xx] = tint(c, color);
                }
            }
        }
    }

    private static int tint(int color1, int color2) {
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 >> 0 & 0xFF;

        if (r1 == 0xFF && g1 == 0xFF && b1 == 0xFF) {
            return color2;
        }

        return color1;
    }

    public static void render(MinicraftImage image, int x, int y, int u, int v, int us, int vs) {
        int xt = x - Renderer.camera.getTx();
        int yt = y - Renderer.camera.getTy();

        for (int xx = xt, xs = u; xx < xt + us; xx++, xs++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;

            for (int yy = yt, ys = v; yy < yt + vs; yy++, ys++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
            
                int color = image.getPixels()[ys * image.getWidth() + xs];

                if ((color >> 24 & 0xFF) != 0) {
                    pixels[yy * Renderer.WIDTH + xx] = color;
                }
            }
        }
    }

    public static void renderPanel(int x, int y, int width, int height) {
        int xt = x - Renderer.camera.getTx();
        int yt = y - Renderer.camera.getTy();

        MinicraftImage image = getImage("hud.png");

        for (int i = 0; i < width / 8 - 2; i++) {
            render(image, xt + 8 + i * 8, yt, 8, 24, 8, 8);
            render(image, xt + 8 + i * 8, yt + height - 8, 8, 40, 8, 8);
        }

        for (int i = 0; i < height / 8 - 2; i++) {
            render(image, xt, yt + 8 + i * 8, 0, 32, 8, 8);
            render(image, xt + width - 8, yt + 8 + i * 8, 16, 32, 8, 8);
        }

        for (int i = 0; i < width / 8 - 2; i++) {
            for (int j = 0; j < height / 8 - 2; j++) {
                render(image, xt + 8 + i * 8, yt + 8 + j * 8, 8, 32, 8, 8);
            }
        }

        render(image, xt, yt, 0, 24, 8, 8);
        render(image, xt + width - 8, yt, 16, 24, 8, 8);
        render(image, xt, yt + height - 8, 0, 40, 8, 8);
        render(image, xt + width - 8, yt + height - 8, 16, 40, 8, 8);
    }

    private static void resetG() {
        scissor = false;
        blend = false;
        scissorBox[0] = 0;
        scissorBox[1] = 0;
        scissorBox[2] = WIDTH;
        scissorBox[3] = HEIGHT;
        // blendColor[0] = 1.0f;
        // blendColor[1] = 1.0f;
        // blendColor[2] = 1.0f;
        // blendColor[3] = 1.0f;
        blendEquation = BlendEquation.FUNC_ADD;
        srcRGBFactor = BlendFactor.SRC_ALPHA;
        dstRGBFactor = BlendFactor.ONE_MINUS_SRC_ALPHA;
        srcAlphaFactor = BlendFactor.SRC_ALPHA;
        dstAlphaFactor = BlendFactor.ONE_MINUS_SRC_ALPHA;
    }

    public static void clear() {
        Arrays.fill(Renderer.zbuffer, 0);
        Arrays.fill(Renderer.pixels, 255 << 24 | 0 << 16 | 0 << 8 | 0);
        // resetG();
    }

    public static Renderer getInstance() {
        return INSTANCE;
    }

    // Test
    // Trying to recreate opengl stuff
    // These operations will be obviously slower than the opengl would.
    // So, if used, using frustum and other optimizations are a must.
    // Features: Blend, Scissor, DepthTest

    private static boolean blend = false;
    private static BlendEquation blendEquation = BlendEquation.FUNC_ADD;
    private static BlendFactor srcRGBFactor = BlendFactor.SRC_ALPHA;
    private static BlendFactor dstRGBFactor = BlendFactor.ONE_MINUS_SRC_ALPHA;
    private static BlendFactor srcAlphaFactor = BlendFactor.SRC_ALPHA;
    private static BlendFactor dstAlphaFactor = BlendFactor.ONE_MINUS_SRC_ALPHA;
    // private static float[] blendColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

    public static void enableBlend() {
        blend = true;
    }

    public static void disableBlend() {
        blend = false;
    }

    public static void defaultBlendFunc() {
        blendFuncSeparate(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA, BlendFactor.ONE, BlendFactor.ZERO);
    }

    // public static void blendColor(float r, float g, float b, float a) {
    //     blendColor[0] = r;
    //     blendColor[1] = g;
    //     blendColor[2] = b;
    //     blendColor[3] = a;
    // }

    public static void blendEquation(BlendEquation equation) {
        blendEquation = equation;
    }

    public static void blendFunc(BlendFactor src, BlendFactor dst) {
        srcRGBFactor = src;
        dstRGBFactor = dst;
        srcAlphaFactor = src;
        dstAlphaFactor = dst;
    }

    public static void blendFuncSeparate(BlendFactor srcRGB, BlendFactor dstRGB, BlendFactor srcAlpha, BlendFactor dstAlpha) {
        srcRGBFactor = srcRGB;
        dstRGBFactor = dstRGB;
        srcAlphaFactor = srcAlpha;
        dstAlphaFactor = dstAlpha;
    }

    public static class BlendEquation {
        public static final BlendEquation FUNC_ADD = new BlendEquation((colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor) -> {
            float sa = (colorSrc >> 24 & 0xFF) / 255.0f;
            float sr = (colorSrc >> 16 & 0xFF) / 255.0f;
            float sg = (colorSrc >> 8 & 0xFF) / 255.0f;
            float sb = (colorSrc & 0xFF) / 255.0f;

            float da = (colorDst >> 24 & 0xFF) / 255.0f;
            float dr = (colorDst >> 16 & 0xFF) / 255.0f;
            float dg = (colorDst >> 8 & 0xFF) / 255.0f;
            float db = (colorDst & 0xFF) / 255.0f;

            sr *= Math.abs(srcRGBFactor.get(sr, dr, sa, da));
            sg *= Math.abs(srcRGBFactor.get(sg, dg, sa, da));
            sb *= Math.abs(srcRGBFactor.get(sb, db, sa, da));
            sa *= Math.abs(srcAlphaFactor.get(sb, db, sa, da));

            dr *= Math.abs(dstRGBFactor.get(sr, dr, sa, da));
            dg *= Math.abs(dstRGBFactor.get(sg, dg, sa, da));
            db *= Math.abs(dstRGBFactor.get(sb, db, sa, da));
            da *= Math.abs(dstAlphaFactor.get(sb, db, sa, da));

            float cr = MathHelper.clamp(sr + dr, 0f, 1f);
            float cg = MathHelper.clamp(sg + dg, 0f, 1f);
            float cb = MathHelper.clamp(sb + db, 0f, 1f);
            float ca = MathHelper.clamp(sa + da, 0f, 1f);

            return (int)(ca * 255) << 24 | (int)(cr * 255) << 16 | (int)(cg * 255) << 8 | (int)(cb * 255);
        });

        public static final BlendEquation FUNC_SUBTRACT = new BlendEquation((colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor) -> {
            float sa = (colorSrc >> 24 & 0xFF) / 255.0f;
            float sr = (colorSrc >> 16 & 0xFF) / 255.0f;
            float sg = (colorSrc >> 8 & 0xFF) / 255.0f;
            float sb = (colorSrc & 0xFF) / 255.0f;

            float da = (colorDst >> 24 & 0xFF) / 255.0f;
            float dr = (colorDst >> 16 & 0xFF) / 255.0f;
            float dg = (colorDst >> 8 & 0xFF) / 255.0f;
            float db = (colorDst & 0xFF) / 255.0f;

            sr *= Math.abs(srcRGBFactor.get(sr, dr, sa, da));
            sg *= Math.abs(srcRGBFactor.get(sg, dg, sa, da));
            sb *= Math.abs(srcRGBFactor.get(sb, db, sa, da));
            sa *= Math.abs(srcAlphaFactor.get(sb, db, sa, da));

            dr *= Math.abs(dstRGBFactor.get(sr, dr, sa, da));
            dg *= Math.abs(dstRGBFactor.get(sg, dg, sa, da));
            db *= Math.abs(dstRGBFactor.get(sb, db, sa, da));
            da *= Math.abs(dstAlphaFactor.get(sb, db, sa, da));

            float cr = MathHelper.clamp(sr - dr, 0f, 1f);
            float cg = MathHelper.clamp(sg - dg, 0f, 1f);
            float cb = MathHelper.clamp(sb - db, 0f, 1f);
            float ca = MathHelper.clamp(sa - da, 0f, 1f);

            return (int)(ca * 255) << 24 | (int)(cr * 255) << 16 | (int)(cg * 255) << 8 | (int)(cb * 255);
        });

        public static final BlendEquation FUNC_REVERSE_SUBTRACT = new BlendEquation((colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor) -> {
            float sa = (colorSrc >> 24 & 0xFF) / 255.0f;
            float sr = (colorSrc >> 16 & 0xFF) / 255.0f;
            float sg = (colorSrc >> 8 & 0xFF) / 255.0f;
            float sb = (colorSrc & 0xFF) / 255.0f;

            float da = (colorDst >> 24 & 0xFF) / 255.0f;
            float dr = (colorDst >> 16 & 0xFF) / 255.0f;
            float dg = (colorDst >> 8 & 0xFF) / 255.0f;
            float db = (colorDst & 0xFF) / 255.0f;
            
            sr *= Math.abs(srcRGBFactor.get(sr, dr, sa, da));
            sg *= Math.abs(srcRGBFactor.get(sg, dg, sa, da));
            sb *= Math.abs(srcRGBFactor.get(sb, db, sa, da));
            sa *= Math.abs(srcAlphaFactor.get(sb, db, sa, da));

            dr *= Math.abs(dstRGBFactor.get(sr, dr, sa, da));
            dg *= Math.abs(dstRGBFactor.get(sg, dg, sa, da));
            db *= Math.abs(dstRGBFactor.get(sb, db, sa, da));
            da *= Math.abs(dstAlphaFactor.get(sb, db, sa, da));

            float cr = MathHelper.clamp(dr - sr, 0f, 1f);
            float cg = MathHelper.clamp(dg - sg, 0f, 1f);
            float cb = MathHelper.clamp(db - sb, 0f, 1f);
            float ca = MathHelper.clamp(da - sa, 0f, 1f);

            return (int)(ca * 255) << 24 | (int)(cr * 255) << 16 | (int)(cg * 255) << 8 | (int)(cb * 255);
        });

        private final Equation equation;

        private BlendEquation(Equation equation) {
            this.equation = equation;
        }

        public int calc(int colorSrc, int colorDst, BlendFactor srcRGBFactor, BlendFactor dstRGBFactor, BlendFactor srcAlphaFactor, BlendFactor dstAlphaFactor) {
            return this.equation.apply(colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor);
        }

        interface Equation {
            int apply(int colorSrc, int colorDst, BlendFactor srcRGBFactor, BlendFactor dstRGBFactor, BlendFactor srcAlphaFactor, BlendFactor dstAlphaFactor);
        } 
    }

    public static class BlendFactor {
        public static final BlendFactor ZERO = new BlendFactor((sc, dc, sa, da) -> 0.0f);
        public static final BlendFactor ONE = new BlendFactor((sc, dc, sa, da) -> 1.0f);

        public static final BlendFactor SRC_COLOR = new BlendFactor((sc, dc, sa, da) -> sc);
        public static final BlendFactor DST_COLOR = new BlendFactor((sc, dc, sa, da) -> dc);
        
        public static final BlendFactor ONE_MINUS_SRC_COLOR = new BlendFactor((sc, dc, sa, da) -> 1.0f - sc);
        public static final BlendFactor ONE_MINUS_DST_COLOR = new BlendFactor((sc, dc, sa, da) -> 1.0f - dc);

        public static final BlendFactor SRC_ALPHA = new BlendFactor((sc, dc, sa, da) -> sa);
        public static final BlendFactor DST_ALPHA = new BlendFactor((sc, dc, sa, da) -> da);

        public static final BlendFactor ONE_MINUS_SRC_ALPHA = new BlendFactor((sc, dc, sa, da) -> 1.0f - sa);
        public static final BlendFactor ONE_MINUS_DST_ALPHA = new BlendFactor((sc, dc, sa, da) -> 1.0f - da);

        private final GetFactor getFactor;

        private BlendFactor(GetFactor getFactor) {
            this.getFactor = getFactor;
        }

        public float get(float sc, float dc, float sa, float da) {
            return this.getFactor.get(sc, dc, sa, da);
        }
      
        @FunctionalInterface
        interface GetFactor {
            float get(float sc, float dc, float sa, float da);
        }
    }

    public static int blendRGBAColor(int color1, int color2) {
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int a1 = color1 >> 24 & 0xFF;

        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        int a2 = color2 >> 24 & 0xFF;

        // finish this shit
        return 0;
    }

    // Use a global texture. I set the texture so I don't have to pass it to every render method

    private static boolean texture = false;
    @Nullable
    private static MinicraftImage gtexture = null;

    public static void enableTexture() {
        texture = true;
    }

    public static void disableTexture() {
        texture = false;
    }

    public static void setGTexture(@Nullable MinicraftImage texture) {
        gtexture = texture;
    }

    private static boolean scissor = false;
    private static int[] scissorBox = new int[] {0, 0, WIDTH, HEIGHT};

    public static void enableScissor() {
        scissor = true;
    }

    public static void disableScissor() {
        scissor = false;
    }

    public static void scissor(int x, int y, int width, int height) {
        scissorBox[0] = x;
        scissorBox[1] = y;
        scissorBox[2] = width;
        scissorBox[3] = height;
    }

    /*  If the z value in the zbuffer is smaller than the z requested by the render method than the
        src color will be from the texture or color parameters and the dst color will be the current color at
        the pixel. If the z value is bigger than it's reverse logic.
    */

    private static boolean depthTest = false;

    public static void enableDepthTest() {
        depthTest = true;
    }

    public static void disableDepthTest() {
        depthTest = false;
    }

    public static void testDepth(int pixelIndex, int[] colors, int z) {
        if (zbuffer[pixelIndex] > z) {
            int temp = colors[0];
            colors[0] = colors[1];
            colors[1] = temp;
        } else {
            zbuffer[pixelIndex] = z;
        }
    }

    public static void renderColoredQuad(int x, int y, int width, int height, int color) {
        renderColoredQuad(x, y, 0, width, height, color);
    }

    public static void renderColoredQuad(int x, int y, int z, int width, int height, int color) {
        for (int xx = x; xx < x + width; xx++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;
            if (scissor && (xx < scissorBox[0] || xx >= scissorBox[2])) continue;   
        
            for (int yy = y; yy < y + height; yy++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
                if (scissor && (yy < scissorBox[1] || yy >= scissorBox[3])) continue;
                
                int pIdx = yy * Renderer.WIDTH + xx;

                int[] colors = { color, pixels[pIdx] };

                if (depthTest) {
                    testDepth(pIdx, colors, z);
                }

                if (blend) pixels[pIdx] = blendEquation.calc(colors[0], colors[1], srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor);
                else {
                    if ((colors[0] >> 24 & 0xFF) != 0) { // Discard color if alpha == 0 
                        pixels[pIdx] = colors[0];
                    }
                }
            }
        }
    }

    public static void renderTexturedQuad(int x, int y, int width, int height) {
        renderTexturedQuad(x, y,  width, height, 0, 0);
    }

    public static void renderTexturedQuad(int x, int y, int width, int height, int u, int v) {
        renderTexturedQuad(x, y,  width, height, u, v, width, height);
    }

    public static void renderTexturedQuad(int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight) {
        renderTexturedQuad(x, y, 0, width, height, u, v, regionWidth, regionHeight);
    }

    public static void renderTexturedQuad(int x, int y, int z, int width, int height, int u, int v, int regionWidth, int regionHeight) {
        if (!texture || gtexture == null) return;

        for (int xx = x, xs = u; xx < x + width; xx++, xs++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;
            if (scissor && (xx < scissorBox[0] || xx >= scissorBox[2])) continue;   
        
            for (int yy = y, ys = 0; yy < y + height; yy++, ys++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
                if (scissor && (yy < scissorBox[1] || yy >= scissorBox[3])) continue;
                
                int pIdx = yy * Renderer.WIDTH + xx;
            
                int[] colors = { gtexture.getPixels()[ys * gtexture.getWidth() + xs] };

                if (depthTest) {
                    testDepth(pIdx, colors, z);
                }

                if (blend) pixels[pIdx] = blendEquation.calc(colors[0], colors[1], srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor);
                else {
                    if ((colors[0] >> 24 & 0xFF) != 0) { // Discard color if alpha == 0 
                        pixels[pIdx] = colors[0];
                    }
                }
            }
        }
    }

    public static void renderTexturedQuad(MinicraftImage image, int x, int y, int width, int height) {
        renderTexturedQuad(image, x, y, width, height, 0, 0);
    }

    public static void renderTexturedQuad(MinicraftImage image, int x, int y, int width, int height, int u, int v) {
        renderTexturedQuad(image, x, y, width, height, u, v, width, height);
    }

    public static void renderTexturedQuad(MinicraftImage image, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight) {
        renderTexturedQuad(image, x, y, 0, width, height, u, v, regionWidth, regionHeight);
    }

    public static void renderTexturedQuad(MinicraftImage image, int x, int y, int z, int width, int height, int u, int v, int regionWidth, int regionHeight) {
        if (image == null) return;

        for (int xx = x, xs = u; xx < x + width; xx++, xs++) {
            if (xx < 0 || xx >= Renderer.WIDTH) continue;
            if (scissor && (xx < scissorBox[0] || xx >= scissorBox[2])) continue;   
        
            for (int yy = y, ys = 0; yy < y + height; yy++, ys++) {
                if (yy < 0 || yy >= Renderer.HEIGHT) continue;
                if (scissor && (yy < scissorBox[1] || yy >= scissorBox[3])) continue;
                
                int pIdx = yy * Renderer.WIDTH + xx;
            
                int[] colors = { image.getPixels()[ys * image.getWidth() + xs] };

                if (depthTest) {
                    testDepth(pIdx, colors, z);
                }

                if (blend) pixels[pIdx] = blendEquation.calc(colors[0], colors[1], srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor);
                else {
                    if ((colors[0] >> 24 & 0xFF) != 0) { // Discard color if alpha == 0 
                        pixels[pIdx] = colors[0];
                    }
                }
            }
        }
    }
}
