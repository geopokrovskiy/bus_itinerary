package com.geopokrovskiy.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryLog {
    public static void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        log.info("Memory Usage: Total={} MB, Free={} MB, Used={} MB",
                totalMemory / (1024 * 1024), freeMemory / (1024 * 1024), usedMemory / (1024 * 1024));
    }
}
