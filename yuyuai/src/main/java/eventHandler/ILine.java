package eventHandler;

public interface ILine {

    // LINE情報
    static final String CHANNEL_SECRET ="5bfa632e9d19d3d174285dc20d1b2370";
      // ◆AIチームのシークレットキー→// "dc9644985b796f2d4dc9fdc65badfc59";
    static final String CHANNEL_ACCESS_TOKEN = "0Vna+EKfc+CVLFmNqxjo978+nK4WyBUuOu7Jc+gRFilI6DKY9gzxM+U9RqZfdeBNN0xo43MTeqi0o53KdK92gFyEBsgJ7XKevajLNz79Ks8u5TkZOiY7A/+vlyH0bN4sTC7sYTXBrCE8dL4F+AUiEgdB04t89/1O/w1cDnyilFU=";
      // ◆AIチームのアカウントのアクセストークン→// "hQv9u+29YK3sR7fKg+rX0hCfAMt0q9c1aONGzyy4A55F8qt5IDwqs8CjcPgMS4RmI8rTW1TMD3j4kc3SQznpa7vUSRaITa89D1hd8MCQU+A3FitHaudx/OWM4sCmJlGZPFphBKCcBB3oIODNar00tAdB04t89/1O/w1cDnyilFU=";

    // ステータス
    static final String DEFAULT            = "DEFAULT";
    static final String JYH_BTN_START      = "JYH_BTN_START";
    static final String JYH_BTN_CHOICE     = "JYH_BTN_CHOICE";
    static final String JYH_WAITE_LOCATION = "JYH_WAITE_LOCATION";
    static final String JYH_BTN_CNFADR     = "JYH_BTN_CNFADR";
    static final String JYH_WAITE_PICT     = "JYH_WAITE_PICT";
    static final String JYH_WAITE_ADRINPUT = "JYH_WAITE_ADRINPUT";
    static final String JYH_WAITE_ADD      = "JYH_WAITE_ADD";
    static final String JYH_BTN_LASTCNF    = "JYH_BTN_LASTCNF";

    // メッセージ
    static final String MSG001 = "住所変更でよろしいですか？";
    static final String MSG002 = "ご用件がわかりませんでした。もう一度お問い合わせいただくか、カスタマーサービスへお問い合わせください。";
    static final String MSG003 = "住所変更の方法を選択してください。";
    static final String MSG004 = "ご利用ありがとうございました。";
    static final String MSG005 = "新住所の位置情報を送信してください。";
    static final String MSG006 = "新住所情報記載の画像を送信してください。";
    static final String MSG007 = "新住所を入力してください。";
    static final String MSG008 = "新住所は「{0}」でよろしいですか？";
    static final String MSG009 = "住所情報が取得できませんでしたので手続きを中止します。もう一度やり直してください。";
    static final String MSG010 = "追記分を入力してください。";
    static final String MSG011 = "お手続きを中止します。もう一度やり直してください。";
    static final String MSG012 = "お手続きを完了しました。ご利用ありがとうございました。";
    static final String MSG101 ="ご利用ありがとうございます。ご用件は何でしょうか？";

    // アクション（ボタン、選択肢）
    static final String ACT_DSP_YES = "はい";
    static final String ACT_VAL_YES = "yes";

    static final String ACT_DSP_NO = "いいえ";
    static final String ACT_VAL_NO = "no";

    static final String ACT_DSP_LOC = "位置情報を利用する";
    static final String ACT_VAL_LOC = "location";

    static final String ACT_DSP_PICT = "住所記載の画像を送信";
    static final String ACT_VAL_PICT = "picture";

    static final String ACT_DSP_INP = "直接入力する";
    static final String ACT_VAL_INP = "input";

    static final String ACT_DSP_ADD = "追記する";
    static final String ACT_VAL_ADD = "add";

    static final String ACT_DSP_STP = "手続を中止する";
    static final String ACT_VAL_STP = "stop";

}