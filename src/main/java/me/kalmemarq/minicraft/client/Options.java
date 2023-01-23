package me.kalmemarq.minicraft.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import me.kalmemarq.minicraft.util.Util;
import me.kalmemarq.minicraft.util.math.MathHelper;

public class Options {
    public final BooleanOption showDebugFPS = new BooleanOption("showDebugFps", false);
    public final IntRangeOption fps = new IntRangeOption("fps", 60, 5, 260).byAmount(5);
    public final BooleanOption sound = new BooleanOption("sound", true);

    private final File file;

    public Options(File gameDir) {
        this.file = new File(gameDir, "options.json");
    }

    public void load() {
        if (!file.exists()) {
            this.save();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            ObjectNode obj = (ObjectNode) Util.Json.parse(reader);

            this.visit(new Visitor() {
                @Override
                public void accept(BooleanOption option) {
                    String key = option.getKey();
                    if (obj.has(key) && obj.get(key).isBoolean()) {
                        option.setValue(obj.get(key).asBoolean());
                    }
                }
    
                @Override
                public void accept(IntRangeOption option) {
                    String key = option.getKey();
                    if (obj.has(key) && obj.get(key).isInt()) {
                        option.setValue(obj.get(key).asInt());
                    }
                }
            });

        } catch (Exception e) {
            Minicraft.LOGGER.error("Failed to load game options");
        }
     }

    public void save() {
        ObjectNode obj = Util.Json.getDefaultMapper().createObjectNode();

        this.visit(new Visitor() {
            @Override
            public void accept(BooleanOption option) {
                obj.put(option.getKey(), option.value());
            }

            @Override
            public void accept(IntRangeOption option) {
                obj.put(option.getKey(), option.value());
            }
        });

        try (FileOutputStream outputStream = new FileOutputStream(file); OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            writer.write(Util.Json.stringify(obj));
        } catch (Exception e) {
            Minicraft.LOGGER.error("Failed to save game options");
        }
    }

    private void visit(Visitor visitor) {
        visitor.accept(this.fps);
        visitor.accept(this.sound);
    }

    interface Visitor {
        void accept(BooleanOption option);
        void accept(IntRangeOption option);
    }

    public static abstract class Option<T> {
        private final String key;

        public Option(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        abstract public void setValue(T value);
        abstract public T value();
        abstract public void cycle();
    }

    public static class BooleanOption extends Option<Boolean> {
        private boolean value;

        public BooleanOption(String key, boolean defaultValue) {
            super(key);
            this.value = defaultValue;
        }

        @Override
        public void setValue(Boolean value) {
            this.value = value;
        }

        @Override
        public Boolean value() {
            return this.value;
        }

        @Override
        public void cycle() {
            this.value = !this.value;
        }

        public void opposite() {
            cycle();
        }
    }

    public static class IntRangeOption extends Option<Integer> {
        private int min;
        private int max;
        private int value;
        private int amount;

        public IntRangeOption(String key, int defaultValue, int min, int max) {
            super(key);
            this.max = max;
            this.min = min;
            this.value = defaultValue;
        }

        public IntRangeOption byAmount(int value) {
            this.amount = value;
            return this;
        }

        @Override
        public void setValue(Integer value) {
            this.value = MathHelper.clamp(value, min, max);
        }

        @Override
        public Integer value() {
            return this.value;
        }

        @Override
        public void cycle() {
            this.value = this.value + this.amount;
            if (this.value > max) {
                this.value = min;
            }
        }
    }
}
