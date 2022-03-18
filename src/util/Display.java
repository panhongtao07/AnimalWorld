package util;

import java.text.*;

public class Display {
    public static int precision = 2;

    private static NumberFormat getNumberFormat(int precision) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(precision);
        return numberFormat;
    }

    private static NumberFormat getNumberFormat() {
        return getNumberFormat(precision);
    }

    public static String formatNumber(double number, int precision) {
        return getNumberFormat(precision).format(number);
    }

    public static String formatNumber(double number) {
        return getNumberFormat().format(number);
    }

    public static String formatNumber(long number, int precision) {
        return getNumberFormat(precision).format(number);
    }

    public static String formatNumber(long number) {
        return getNumberFormat().format(number);
    }

    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    public static <T> String center(T obj, int size) {
        return center(obj, size, ' ');
    }
    
    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < (size - s.length() + 1) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

    public static <T> String center(T obj, int size, char pad) {
        return center(obj.toString(), size, pad);
    }
}
