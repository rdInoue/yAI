package util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    // https://weblabo.oscasierra.net/java-apache-commons-codec-base64-1/
    public static String encode(String str) {

        // 単純なBase64エンコード
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
        return new String(encodedBytes);

        //        return (Base64.encodeBase64String(str.getBytes()));


        //         // URLに含めても問題が発生しないようにするBase64エンコード
        //        encodedBytes = Base64.encodeBase64URLSafe(str.getBytes());
        //         System.out.println(new String(encodedBytes));
    }
}
