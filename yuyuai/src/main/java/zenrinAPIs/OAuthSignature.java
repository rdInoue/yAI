package zenrinAPIs;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import util.Cnst;
import util.Base64Util;
import util.CryptoHmacSHA1;

public class OAuthSignature {

    // ダイジェスト生成に使用する秘密鍵
    private static final String DigestSecretKey = CnstZnrn.ZDC_SECRET_KEY + CnstZnrn.Amp;

    public static String get(Map<String,String> m) throws Exception {

        //String sortedQS = Cnst.QueryStringStarter + ZnrnQueryString.getAOuthQS(new TreeMap<String, String>(m));


        // key,valueをURLencしてつめかえ
        TreeMap<String, String> nm  = new TreeMap<String, String>();
        for (String key: m.keySet()) {
            nm.put(URLEncoder.encode(key, Cnst.ENC_UTF8), URLEncoder.encode(m.get(key), Cnst.ENC_UTF8));
        }

        String sortedQS = ZnrnQueryString.getAOuthQS(nm);
        System.out.println("sortedQS=" + sortedQS);

        // エンコード
        String encodedHTTPMethod = URLEncoder.encode(Cnst.HttpMethodGET, Cnst.ENC_UTF8);
        String encodedReqURI = URLEncoder.encode(CnstZnrn.API_URI, Cnst.ENC_UTF8);
        String encodedQS = URLEncoder.encode(sortedQS, Cnst.ENC_UTF8);

        // &で接続
        String sigunatureBaseString = encodedHTTPMethod + CnstZnrn.Amp + encodedReqURI + CnstZnrn.Amp + encodedQS;
        System.out.println("signatureBaseString = " + sigunatureBaseString);

        String digest = CryptoHmacSHA1.getDigest(sigunatureBaseString, DigestSecretKey);
        System.out.println("digest=" + digest);

        String encodedDigest = Base64Util.encode(digest);
        System.out.println("encodedDigest = " + encodedDigest);

        return encodedDigest;
    }
}