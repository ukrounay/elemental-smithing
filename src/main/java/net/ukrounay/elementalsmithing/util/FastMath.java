package net.ukrounay.elementalsmithing.util;

public class FastMath {
    public static float pow(float a, int b) {
        float result = 1;
        for (int i = 0; i < b; i++) result *= a;
        return result;
    }

    public static float sq(float x) {
        return x * x;
    }

    public static float cubexp(float v) {
        return 1 - 4 * sq(v - 0.5f);
    }

    public static float expgrow(float x, int n) {
        return 1 - pow(1 - x, n * 2);
    }

//    public static float roundgrow(float x) {
//        x = clamp(x);
//        return (float) Math.sqrt((2 - x) * x);
//    }
//
//    public static float clamp(float value, float min, float max) {
//        return value < min ? min : value > max ? max : value;
//    }
//    public static float clamp(float value) {
//        return value < 0 ? 0 : value > 1 ? 1 : value;
//    }
}
