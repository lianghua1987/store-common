package com.hua.store.common.utils;

import java.util.Random;

public class IdUtil {

    public static String genImageName() {
        return System.currentTimeMillis() + String.format("%03d", new Random().nextInt(999));
    }

    public static long genItemId() {
        return new Long(System.currentTimeMillis() + String.format("%02d", new Random().nextInt(99)));
    }
}
