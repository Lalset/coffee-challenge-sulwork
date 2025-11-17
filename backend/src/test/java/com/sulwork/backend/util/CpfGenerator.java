package com.sulwork.backend.util;

import java.util.Random;

public class CpfGenerator {

    public static String gerarCpfValido() {
        Random random = new Random();

        int[] n = new int[9];
        for (int i = 0; i < 9; i++) {
            n[i] = random.nextInt(10);
        }

        int d1 = 0;
        for (int i = 0; i < 9; i++) d1 += n[i] * (10 - i);
        d1 = 11 - (d1 % 11);
        d1 = d1 >= 10 ? 0 : d1;

        int d2 = 0;
        for (int i = 0; i < 9; i++) d2 += n[i] * (11 - i);
        d2 += d1 * 2;
        d2 = 11 - (d2 % 11);
        d2 = d2 >= 10 ? 0 : d2;

        return String.format("%d%d%d%d%d%d%d%d%d%d%d",
                n[0], n[1], n[2], n[3], n[4], n[5], n[6], n[7], n[8], d1, d2);
    }
}
