package util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class CryptoHmacSHA1 {

    private static final String HmacSHA1 = "HmacSHA1";

    // http://d.hatena.ne.jp/billest/20090730/1248910076
    // org.apache.commons.codec.binary.Hex が必要
    public static String getDigest(String text, String key) throws Exception {

        SecretKeySpec sk = new SecretKeySpec(key.getBytes(), HmacSHA1);

        Mac mac = Mac.getInstance(HmacSHA1);
        mac.init(sk);
        byte[] result = mac.doFinal(text.getBytes());


        //        return new String(result, CnstZnrn.ENC_UTF8);


        return new String(Hex.encodeHex(result));


        //        StringBuilder sb = new StringBuilder(2 * result.length);
        //        for(byte b: result) {
        //            sb.append(String.format("%02x", b&0xff) );
        //        }
        //        System.out.println( sb );
        //
        //        return sb.toString();
    }
}
