package com.cosmin.webserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ThreadUtils {
    private final static Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    public static void randomSleep(int bound) {
        Random random = new Random();
        sleep(random.nextInt(bound));
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
