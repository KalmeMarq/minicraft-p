package me.kalmemarq.minicraft.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import me.kalmemarq.minicraft.Minicraft;
import me.kalmemarq.minicraft.gfx.Renderer;

public class Window {
    private final JFrame frame;

    private final BufferedImage image;

    private final Canvas canvas;
    private final BufferStrategy bufferStrategy;
    private GraphicsEnvironment graphicsEnvironment;
    private GraphicsDevice graphicsDevice;

    private int prevWidth;
    private int prevHeight;

    private double x;
    private double y;

    private int width;
    private int height;
    private boolean fullscreen;

    private boolean shouldClose;
    private boolean vsync;
    private int maxFrameLimit = 60; 

    public Window(String title, int width, int height, String icon32, String icon64) {
        this.width = width;
        this.height = height;
        this.prevWidth = width;
        this.prevHeight = height;

        this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = this.graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = this.graphicsDevice.getDefaultConfiguration();

        this.image = new BufferedImage(Renderer.WIDTH, Renderer.HEIGHT, BufferedImage.TYPE_INT_RGB);

        this.frame = new JFrame(title);
        this.frame.getContentPane().setPreferredSize(new Dimension(width, height));
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.setResizable(true);
        
        {
            List<BufferedImage> icons = new ArrayList<>();

            try {
                BufferedImage icon = ImageIO.read(Objects.requireNonNull(Window.class.getResourceAsStream("/" + icon32)));
                icons.add(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedImage icon = ImageIO.read(Objects.requireNonNull(Window.class.getResourceAsStream("/" + icon64)));
                icons.add(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.frame.setIconImages(icons);
        }

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                shouldClose = true;
            }
        });

        this.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                
                if (Window.this.frame.getState() == Frame.NORMAL) {
                    try {
                        double x = Window.this.frame.getLocationOnScreen().getX();
                        double y = Window.this.frame.getLocationOnScreen().getY();

                        if (x > 0 && y > 0) {
                            Window.this.x = x;
                            Window.this.y = y;
                        }
                    } catch(Exception ignored) {
                    }
                }
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                if (Window.this.frame.getState() != Frame.NORMAL && Window.this.frame.getState() != Frame.ICONIFIED) {
                    Window.this.prevWidth = Window.this.width;
                    Window.this.prevHeight = Window.this.height;
                }

                Window.this.width = Window.this.canvas.getWidth();
                Window.this.height = Window.this.canvas.getHeight();
            }
        });

        this.canvas = new Canvas(graphicsConfiguration);
        this.canvas.setPreferredSize(new Dimension(width, height));
        this.canvas.setSize(width, height);
        this.canvas.setIgnoreRepaint(true);
        this.frame.add(this.canvas);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);

        // this.frame.setDropTarget(new DropTarget() {
        //     @Override
        //     public synchronized void drop(DropTargetDropEvent evt) {
        //         super.drop(evt);

        //         try {
        //             evt.acceptDrop(DnDConstants.ACTION_COPY);

        //             Object droppedFiles = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

        //             if (droppedFiles instanceof List && ((List<?>)droppedFiles).size() >= 1) {
        //                 for (Object file : (List<?>)droppedFiles) {
        //                     if (file instanceof File) {
        //                         System.out.println(((File)file).getName());
        //                     }
        //                 }
        //             }

        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //         // System.out.println(dtde.getDropAction());
        //     }
        // });

        

        // this.frame.setTransferHandler(new TransferHandler() {
        //     private DataFlavor fileFlavor = DataFlavor.javaFileListFlavor;

        //     @Override
        //     public int getSourceActions(JComponent c) {
        //         return COPY_OR_MOVE;
        //     }
            
        //     @Override
        //     public boolean importData(JComponent comp, Transferable t) {
        //         System.out.println("bs");
        //         if (!canImport(t.getTransferDataFlavors())) {
        //             return false;
        //         }
                
        //         try {
        //             System.out.println("nop");
        //             if (hasFileFlavor(t.getTransferDataFlavors())) {
        //             System.out.println("yes");

        //                 Object droppedFiles = t.getTransferData(DataFlavor.javaFileListFlavor);

        //                 if (droppedFiles instanceof List && ((List<?>)droppedFiles).size() >= 1) {
        //                     for (Object file : (List<?>)droppedFiles) {
        //                         if (file instanceof File) {
        //                             System.out.println(((File)file).getName());
        //                         }
        //                     }
        //                 }
        //             }
        //             return true;
        //         } catch(Exception e) {
        //             e.printStackTrace();
        //         }

        //         return false;
        //     }

        //     public boolean canImport(DataFlavor[] flavors) {
        //         if (hasFileFlavor(flavors)) {
        //             return true;
        //         }

        //         return false;
        //     }

        //     private boolean hasFileFlavor(DataFlavor[] flavors) {
        //         for (int i = 0; i < flavors.length; i++) {
        //           if (fileFlavor.equals(flavors[i])) {
        //             return true;
        //           }
        //         }
        //         return false;
        //     }
        // });

        this.frame.setDropTarget(new DropTarget() {
            @SuppressWarnings("unchecked")
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    Object obj = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    if (obj instanceof List) {
                        Minicraft.getInstance().onFileDrop((List<File>)obj);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.canvas.setFocusable(false);

        this.frame.requestFocus();

        this.frame.setVisible(true);
        this.canvas.setVisible(true);

        Renderer.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();

        this.canvas.createBufferStrategy(2);
        this.bufferStrategy = this.canvas.getBufferStrategy();
    
        try {
            this.x = this.frame.getLocationOnScreen().getX();
            this.y = this.frame.getLocationOnScreen().getY();
        } catch(Exception ignored) {}
    }

    public void close() {
        this.frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
    }

    public boolean shouldClose() {
        return this.shouldClose;
    }

    public void setVSync(boolean vsync) {
        this.vsync = vsync;
    }

    public boolean isVSync() {
        return this.vsync;
    }

    public void setMaxFrameLimit(int frameLimit) {
        this.maxFrameLimit = frameLimit;
    }

    public int getMaxFrameLimit() {
      return this.maxFrameLimit;
    }

    public void toggleFullscreen() {
        this.graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = this.graphicsEnvironment.getDefaultScreenDevice();

        this.frame.dispose();

        if (this.fullscreen) {
            this.fullscreen = false;

            this.graphicsDevice.setFullScreenWindow(null);
            this.frame.setUndecorated(false);
            this.frame.setResizable(true);

            this.frame.setSize(this.prevWidth, this.prevHeight);
            this.frame.setLocation((int)this.x, (int)this.y);

            this.frame.setVisible(true);
        } else {
            this.fullscreen = true;

            this.frame.setUndecorated(true);
            this.frame.setResizable(false);
            this.frame.setVisible(true);
            this.graphicsDevice.setFullScreenWindow(this.frame);
        }

        this.frame.requestFocus();
    }

    public void renderFrame() {
        Graphics graphics = this.bufferStrategy.getDrawGraphics();

        // #region From Jdah Microcraft code
        double rendererAspect = (double) Renderer.WIDTH / (double) Renderer.HEIGHT;
        double windowAspect = (double) this.width / (double) this.height;

        double scaleFactor = windowAspect > rendererAspect
            ? (double) this.height / (double) Renderer.HEIGHT
            : (double) this.width / (double) Renderer.WIDTH;

        int rw = (int) (Renderer.WIDTH * scaleFactor);
        int rh = (int) (Renderer.HEIGHT * scaleFactor);

        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0, 0, this.width, this.height);
        graphics.drawImage(this.image, (this.width - rw) / 2, (this.height - rh) / 2, rw, rh, null);

        graphics.dispose();

        if (!this.bufferStrategy.contentsLost()) {
            this.bufferStrategy.show();
        }

        // #endregion
    }

    public JFrame getWindowFrame() {
        return this.frame;
    }

    public boolean hasFocus() {
        return this.frame.isActive();
    }
}
