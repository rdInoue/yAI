package util;


public class GenerateUtil {

    public static String getRandom17() {
        long l = (long) (Math.random() * 100000000000000000L);
        return new Long(l).toString();
    }
}
