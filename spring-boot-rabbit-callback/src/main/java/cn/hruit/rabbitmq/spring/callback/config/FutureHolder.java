package cn.hruit.rabbitmq.spring.callback.config;

import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HONGRRY
 * @description
 * @date 2022/10/01 22:52
 **/
public final class FutureHolder {
    private final static ConcurrentHashMap<String, SettableListenableFuture<String>> FUTURE_MAP = new ConcurrentHashMap<>();

    public static void set(String id, SettableListenableFuture<String> future) {
        FUTURE_MAP.put(id, future);
    }

    public static SettableListenableFuture<String> get(String id) {
        return FUTURE_MAP.get(id);
    }

    public static void remove(String correlationId) {
        FUTURE_MAP.remove(correlationId);
    }
}
