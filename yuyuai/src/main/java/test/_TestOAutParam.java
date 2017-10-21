package test;

import java.util.HashMap;

import zenrinAPIs.OAuthParam;

public class _TestOAutParam {

    public static void main(String[] args) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("keyword", "TCP/IP");
        map.put("output", "json");
        map.put("callback", "root");

        OAuthParam.set(map);

        //        System.out.println(OAuthSignature.get(map));
    }


}
