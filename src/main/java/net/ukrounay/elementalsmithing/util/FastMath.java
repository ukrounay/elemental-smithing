package net.ukrounay.elementalsmithing.util;

public class FastMath {
    public static float pow(float a, int b) {
        float result = a;
        for (int i = 1; i <= b; i++) result *= a;
        return result;
    }

    public static float sq(float x) {
        return x * x;
    }

    public static float cubexp(float v) {
        return 1 - 4 * sq(v - 0.5f);
    }

    public static float expgrow(float x, int n) {
        return 1 - pow(x - 1, n);
    }
}
