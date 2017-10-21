package http;

import java.util.Map;

public class QueryString {

    public static String get(Map<String,String> m) {

        StringBuffer sb = new StringBuffer();

        for (String key: m.keySet()) {
            sb.append(key + "=");
            sb.append(m.get(key));
            sb.append("&");
        }

        return m.size()>0  ? sb.toString().substring(0, sb.length()-1) : null;
    }
}
