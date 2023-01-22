package me.kalmemarq.minicraft.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.kalmemarq.minicraft.Minicraft;

public final class Util {
    private Util() {}

    private static final List<ExecutorService> WORKERS = new ArrayList<>();
    public static ExecutorService WORKER = Util.createWorker("Worker-Main");
    
    private static ExecutorService createWorker(String name) {
        ExecutorService executor = new ForkJoinPool(Math.min(0x7fff, Runtime.getRuntime().availableProcessors()), pool -> {
            ForkJoinWorkerThread worker = new ForkJoinWorkerThread(pool) {
            };
            worker.setName(name);
            return worker;
        }, (thread, t) -> {
            Minicraft.LOGGER.error("Exception in thread {}", thread.getName(), t);            
        }, true);

        WORKERS.add(executor);
        return executor;
    }

    public static void shutdownWorkers() {
        for (ExecutorService worker : WORKERS) {
            worker.shutdown();
            boolean terminated;
            try {
                terminated = worker.awaitTermination(2L, TimeUnit.SECONDS);
            } catch(Exception e) {
                e.printStackTrace();
                terminated = false;
            }
    
            if (!terminated) {
                worker.shutdownNow();
            }
        }
    }

    public static final class Json {
        private Json() {}

        private static final ObjectMapper objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        
        public static JsonNode parse(Reader reader) throws IOException {
            return objectMapper.readTree(reader);
        }

        public static boolean hasString(JsonNode node, String key) {
            return node.isObject() && node.has(key) && node.get(key).isTextual();
        }

        public static boolean hasNumber(JsonNode node, String key) {
            return node.isObject() && node.has(key) && node.get(key).isNumber();
        }

        public static boolean hasBoolean(JsonNode node, String key) {
            return node.isObject() && node.has(key) && node.get(key).isBoolean();
        }

        public static boolean hasArray(JsonNode node, String key) {
            return node.isObject() && node.has(key) && node.get(key).isArray();
        }

        public static boolean getBoolean(JsonNode node, String key, boolean defaultValue) {
            if (node.isObject()) {
                if (node.has(key) && node.get(key).isBoolean()) {
                    return node.get(key).asBoolean();
                }
            }

            return defaultValue;
        }
    }

    public static final class Logging {
        private Logging() {}

        private static final StackWalker STACK_WALKER;

        public static Logger getLogger() {
            return LoggerFactory.getLogger(STACK_WALKER.getCallerClass().getCanonicalName());
        }

        public static void setupCustomOutputs() {
            System.setErr(new LoggerPrinterStream("SYSERR", System.err, true));
            System.setOut(new LoggerPrinterStream("SYSOUT", System.out, false));
        }

        static {
            STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
        }

        static class LoggerPrinterStream extends PrintStream {
            private final Logger LOGGER;
            private final boolean err;

            LoggerPrinterStream(String name, OutputStream out, boolean err) {
                super(out);
                LOGGER = LoggerFactory.getLogger(name);
                this.err = err;
            }

            @Override
            public void println(@Nullable String x) {
                log(x);
            }

            @Override
            public void println(Object obj) {
                this.log(String.valueOf(obj));
            }

            @Override
            public void println(boolean x) {
                this.log(String.valueOf(x));
            }

            @Override
            public void println(long x) {
                this.log(String.valueOf(x));
            }

            @Override
            public void println(float x) {
                this.log(String.valueOf(x));
            }

            @Override
            public void println(char x) {
                this.log(String.valueOf(x));
            }

            @Override
            public void println(int x) {
                this.log(String.valueOf(x));
            }

            @Override
            public void println(double x) {
                this.log(String.valueOf(x));
            }

            @Override
            public void println(char[] x) {
                this.log(String.valueOf(x));
            }

            private void log(@Nullable String obj) {
                if (err) LOGGER.error("{}", obj);
                else LOGGER.info("{}", obj);
            }
        }
    }

    @SuppressWarnings("unused")
    public static class Version implements Comparable<Version> {
        private static final String _a = "X.x.x";
        private static final String _b = "X.x.x-dev";
        private static final String _c = "X.x.x-pre";
        
        final boolean isDev;
        final boolean isPre;
        int major;
        int minor;
        int patch;

        public Version(int major, int minor, int patch) {
            this(major, minor, patch, false, false);
        }

        public Version(int major, int minor, int patch, boolean isDev, boolean isPre) {
            this.isDev = isDev;
            this.isPre = isPre;

            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }

        public Version(String version) {
            String ver = version;
            if (version.endsWith("-dev")) {
                this.isDev = true;
                ver = ver.substring(0, version.lastIndexOf("-dev"));
            } else {
                this.isDev = false;
            }
            
            if (version.endsWith("-pre")) {
                this.isPre = true;
                ver = ver.substring(0, version.lastIndexOf("-pre"));
            } else {
                this.isPre = false;
            }

            String[] s = ver.split("\\.");
            
            try {
                this.major = s.length > 0 ? Integer.parseInt(s[0]) : 0;
                this.minor = s.length > 1 ? Integer.parseInt(s[1]) : 0;
                this.patch = s.length > 2 ? Integer.parseInt(s[2]) : 0;
            } catch (NumberFormatException e) {
                this.major = 0;
                this.minor = 0;
                this.patch = 0;
            }
        }

        @Override
        public int compareTo(Version obj) {
            if (this.major != obj.major) return Integer.compare(this.major, major);
            if (this.minor != obj.minor) return Integer.compare(this.minor, minor);
            if (this.patch != obj.patch) return Integer.compare(this.patch, patch);
            
            if (this.isDev && obj.isPre) {
                return -1;
            }
            
            if (this.isPre && obj.isDev) {
                return 1;
            }

            return 0;
        }

        @Override
        public String toString() {
            return String.format("%d.%d.%d%s", major, minor, patch, isDev ? "-dev" : isPre ? "-pre" : "");
        }
    }
}
