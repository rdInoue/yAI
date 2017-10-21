package zenrinAPIs;

import java.util.TreeMap;

import util.Cnst;

public class ZnrnQueryString {

    //https://support.e-map.ne.jp/manuals/v3/oauth_sig
    public static String getAOuthQS (TreeMap<String,String> m) {

        StringBuffer sb = new StringBuffer();

        for (String key: m.keySet()) {
            sb.append(key + Cnst.QueryStringEquals);
            sb.append(m.get(key));
            sb.append(CnstZnrn.Amp);
        }

        return m.size()>0  ? sb.toString().substring(0, sb.length()-1) : null;
    }
}
