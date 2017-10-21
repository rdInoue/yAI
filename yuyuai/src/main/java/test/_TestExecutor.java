package test;

import http.JavaNetHttpClient;
import http.QueryString;

import java.util.TreeMap;

import util.Cnst;

public class _TestExecutor {
    public static void main(String[] args) {


        // クエリストリング構築
        // TreeMapでキーの昇順に並べる
        // https://support.e-map.ne.jp/manuals/v3/oauth_sig
        // http://java-reference.com/java_collection_treemap.html
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("keyword", "TCP/IP");
        map.put("output", "json");
        map.put("callback", "root");

        // GET
        String urlg = Cnst.WebServiceURL + Cnst.QueryStringStarter + QueryString.get(map);
        System.out.println(urlg);
        JavaNetHttpClient.executeGet(urlg);


        //        // POST
        //        String urlp = Const.WebServiceURL;
        //
        //        ArrayList<String> pList = new ArrayList<>();
        //        pList.add(QueryString.get(map));
        //
        //        JavaNetHttpClient.executePost(urlp, pList);
    }

}
