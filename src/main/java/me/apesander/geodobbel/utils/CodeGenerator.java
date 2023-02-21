package me.apesander.geodobbel.utils;

import me.apesander.geodobbel.constants.Numbers;
import me.apesander.geodobbel.data.ActiveGames;

import java.util.Random;

public class CodeGenerator {
    public static short generate() {
        Random random = new Random();

        while (true) {
            short code = (short) (random.nextInt(Numbers.MAX_CODE_VALUE-Numbers.MIN_CODE_VALUE) + Numbers.MIN_CODE_VALUE);
            if (ActiveGames.getByCode(code) == null) return code;
        }
    }
}
