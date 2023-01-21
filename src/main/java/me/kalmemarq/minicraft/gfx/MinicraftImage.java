package me.kalmemarq.minicraft.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import me.kalmemarq.minicraft.main.Main;

public class MinicraftImage {
    private final int width;
    private final int height;

    private final int[] pixels;

    public MinicraftImage(InputStream stream) {
        BufferedImage image = null;
    
        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.pixels = new int[this.width * this.height];

            int[] imagePixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        
            for (int i = 0; i < this.width * this.height; i++) {
                int r = imagePixels[i] >> 16 & 0xFF;
                int g = imagePixels[i] >> 8 & 0xFF;
                int b = imagePixels[i] & 0xFF;
                int a = imagePixels[i] >> 24 & 0xFF;
                this.pixels[i] = a << 24 | r << 16 | g << 8 | b;
            }
        } else {
            this.width = 1;
            this.height = 1;
            this.pixels = new int[this.width * this.height];
        }
    }

    public MinicraftImage(String path) {
        BufferedImage image;

        try (InputStream stream = Main.class.getResourceAsStream(path)) {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            throw new Error(e);
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[this.width * this.height];

        int[] imagePixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0; i < this.width * this.height; i++) {
            int r = imagePixels[i] >> 16 & 0xFF;
            int g = imagePixels[i] >> 8 & 0xFF;
            int b = imagePixels[i] & 0xFF;

            int a = imagePixels[i] >> 24 & 0xFF;

            // if ((imagePixels[i] >> 24 & 0xFF) == 0) {
            //     a = 0;
            // }

            this.pixels[i] = a << 24 | r << 16 | g << 8 | b;
        }

        image.flush();
    }

    public MinicraftImage(BufferedImage image) {
        this(image, image.getWidth(), image.getHeight());
    }

    public MinicraftImage(BufferedImage image, int width, int height) {
        this.width = width;
        this.height = height;

        this.pixels = new int[this.width * this.height];

        int[] imagePixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0; i < this.width * this.height; i++) {
            int r = imagePixels[i] >> 16 & 0xFF;
            int g = imagePixels[i] >> 8 & 0xFF;
            int b = imagePixels[i] & 0xFF;

            int a = 1;

            if ((imagePixels[i] >> 24 & 0xFF) == 0) {
                a = 0;
            }

            this.pixels[i] = a << 24 | r << 16 | g << 8 | b;
        }

        image.flush();
    }

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    public int[] getPixels() {
      return pixels;
    }
}
