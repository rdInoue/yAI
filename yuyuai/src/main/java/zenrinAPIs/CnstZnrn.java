package zenrinAPIs;

public interface  CnstZnrn {
    // ZenrinいつでもNAVI

    // ZDC発行秘密鍵
    public static final String ZDC_SECRET_KEY = "asdfghjkl";
    // クライアントID
    public static final String ZDC_CLIENT_ID = "test";
    // ZDC API URL
    public static final String API_URI = "http://test.core.its-mo.com/";

    // &（接続用文字）
    public static final String Amp = "&";

    // OAuth用パラメータ
    public static final String IF_CLIENTID = "if_clientid";
    public static final String IF_AUTH_TYPE = "if_auth_type";
    public static final String OAuth_CONSUMER_KEY = "oauth_consumer_key";
    public static final String OAuth_SIGNATURE_METHOD = "oauth_signature_method";
    public static final String OAuth_Signature = "oauth_signature";
    public static final String OAuth_TIMESTAMP = "oauth_timestamp";
    public static final String OAuth_NONCE = "oauth_nonce";
    public static final String OAuth_VERSION = "oauth_version";

    // 固定パラメータ値
    public static final String VAL_IF_AUTH_TYPE = "oauth";
    public static final String VAL_OAuth_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String VAL_OAuth_VERSION = "1.0";

    // 緯度経度での住所検索 :　API location
    // 緯度経度
    public static final String API_LOCATION_LATLON = "latlon";
    // 測地系
    public static final String API_LOCATION_DATUM = "datum";
    // 緯度経度形式
    public static final String API_LOCATION_LLUNIT = "llunit";

}
