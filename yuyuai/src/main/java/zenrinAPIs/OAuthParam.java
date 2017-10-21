package zenrinAPIs;

import java.util.Map;
import java.util.TreeMap;

import util.GenerateUtil;
import util.UnixTime;

public class OAuthParam {

    public static Map<String, String> set(Map<String, String> m) throws Exception {
        m.put(CnstZnrn.IF_CLIENTID, CnstZnrn.ZDC_CLIENT_ID);
        m.put(CnstZnrn.IF_AUTH_TYPE, CnstZnrn.VAL_IF_AUTH_TYPE);
        m.put(CnstZnrn.OAuth_CONSUMER_KEY, CnstZnrn.ZDC_CLIENT_ID);
        m.put(CnstZnrn.OAuth_SIGNATURE_METHOD, CnstZnrn.VAL_OAuth_SIGNATURE_METHOD);
        m.put(CnstZnrn.OAuth_TIMESTAMP, UnixTime.get());
        m.put(CnstZnrn.OAuth_NONCE, GenerateUtil.getRandom17());
        m.put(CnstZnrn.OAuth_VERSION, CnstZnrn.VAL_OAuth_VERSION);

        m.put(CnstZnrn.OAuth_Signature, OAuthSignature.get(new TreeMap<String, String>(m)));

        return m;
    }

}
