/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

import java.util.List;
import java.util.Random;

public class MathUtils {
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min + 1) + min;
        return randomNum;
    }

    public static double round(double number, double amount) {
        return (double)Math.round(number * amount) / amount;
    }

    public static double getAverageDouble(List<Double> list) {
        if (list == null) {
            return -1.0;
        }
        if (list.size() == 0) {
            return -1.0;
        }
        double avg = 0.0;
        for (double val : list) {
            avg += val;
        }
        return MathUtils.round(avg /= (double)list.size(), 100.0);
    }

    public static int getAverageInt(List<Integer> list) {
        if (list == null) {
            return -1;
        }
        if (list.size() == 0) {
            return -1;
        }
        int avg = 0;
        for (int val : list) {
            avg += val;
        }
        return avg /= list.size();
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}

