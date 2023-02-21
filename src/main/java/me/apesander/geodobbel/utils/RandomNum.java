package me.apesander.geodobbel.utils;

import java.util.Random;

public class RandomNum {
    private static Random random = new Random();

    public static short genShort(short min, short max) {
        return (short) (random.nextInt(max-min+1) + min);
    }
}
