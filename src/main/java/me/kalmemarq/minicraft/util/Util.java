package me.kalmemarq.minicraft.util;

import java.io.IOException;
import java.io.Reader;
import java.lang.StackWalker.Option;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public final class Util {
    private Util() {}

    public static ExecutorService WORKER = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Worker-Main").build());
    
    public static void shutdownWorkers() {
        WORKER.shutdown();
        boolean terminated;
        try {
            terminated = WORKER.awaitTermination(2L, TimeUnit.SECONDS);
        } catch(Exception e) {
            e.printStackTrace();
            terminated = false;
        }

        if (!terminated) {
            WORKER.shutdownNow();
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
