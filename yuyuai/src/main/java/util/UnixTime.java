package util;

public class UnixTime {
    public static String get() {
        long l = System.currentTimeMillis() / 1000L;
        return new Long(l).toString();
    }
}
