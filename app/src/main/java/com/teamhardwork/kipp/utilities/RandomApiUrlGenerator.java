package com.teamhardwork.kipp.utilities;

/**
 * Created by rcarino on 10/26/14.
 */
public class RandomApiUrlGenerator {
    private static int cacheBusterId = 0;

    public static String getUrl() {
        return "http://lorempixel.com/200/200/animals?cachebuster="
                + cacheBusterId++;
    }
}
