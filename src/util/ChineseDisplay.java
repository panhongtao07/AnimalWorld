package util;

public class ChineseDisplay {
    enum Align {
        Right, Left, Center
    };

    public static int getWordCount(String s) {
        return s.replaceAll("[\\x00-\\xff]", "").length();
    }

    public static int getAlphabatCount(String s) {
        return s.replaceAll("[^\\x00-\\xff]", "").length();
    }

    public static int getPrintLength(String s) {
        return s.replaceAll("[^\\x00-\\xff]", "**").length();
    }

    private static int getAdjustSize(String s, int size) {
        return Integer.max(size - getWordCount(s), s.length());
    }

    public static String adjust(String s, int size, Align align) {
        switch (align) {
            case Right:
                s = String.format("%" + getAdjustSize(s, size) + "s", s);
                break;
            case Left:
                s = String.format("%-" + getAdjustSize(s, size) + "s", s);
                break;
            case Center:
                s = Display.center(s, getAdjustSize(s, size));
                break;
        }
        return s;
    }

    public static String adjust(String s, int size) {
        return adjust(s, size, Align.Center);
    }

    public static <T> String adjust(T obj, int size, Align align) {
        return adjust(obj.toString(), size, align);
    }

    public static <T> String adjust(T obj, int size) {
        return adjust(obj, size, Align.Center);
    }
}
