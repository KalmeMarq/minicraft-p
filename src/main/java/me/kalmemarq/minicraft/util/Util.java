package me.kalmemarq.minicraft.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;

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
            return LoggerFactory.getLogger(STACK_WALKER.getCallerClass());
         }
    
        static {
            STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
         }
    }
}
