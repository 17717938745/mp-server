package com.lead.fund.base.server.mp.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * LockHelper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-17 09:03
 */
@Component
@Slf4j
public class LockHelper {

    private static final Map<String, ReentrantLock> LOCK_MAP = new HashMap<>(8);

    public void lock(String channel) {
        log.info("Start lock, channel: {}", channel);
        LOCK_MAP.computeIfAbsent(channel, k -> new ReentrantLock()).lock();
    }

    public void unlock(String channel) {
        LOCK_MAP.computeIfAbsent(channel, k -> new ReentrantLock()).unlock();
        log.info("Success release lock, channel: {}", channel);
    }
}
