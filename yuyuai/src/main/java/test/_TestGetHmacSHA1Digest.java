package test;

import util.Base64Util;
import util.CryptoHmacSHA1;
import zenrinAPIs.CnstZnrn;

public class _TestGetHmacSHA1Digest {
    public static void main(String[] args) throws Exception {

        // &で接続
        String sigunatureBaseString = "GET&http%3A%2F%2Ftest.core.its-mo.com%2F&callback%3Droot%26if_auth_type%3Doauth%26if_clientid%3Dtest%26keyword%3DTCP%252FIP%26oauth_consumer_key%3Dtest%26oauth_nonce%3D92360574550900384%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1507180942%26oauth_version%3D1.0%26output%3Djson";
        System.out.println("signatureBaseString = " + sigunatureBaseString);

        String digest = CryptoHmacSHA1.getDigest(sigunatureBaseString, CnstZnrn.ZDC_SECRET_KEY + CnstZnrn.Amp);
        System.out.println("digest=" + digest);

        String encodedDigest = Base64Util.encode(digest);
        System.out.println("encodedDigest = " + encodedDigest);
    }
}
