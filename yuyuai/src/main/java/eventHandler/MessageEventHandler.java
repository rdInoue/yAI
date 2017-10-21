package eventHandler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import sessionCtrl.SessionBean;
import sessionCtrl.SessionData;
import util.MessageUtil;

import com.google.gson.Gson;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;

public class MessageEventHandler {

	private static final Logger LOGGER = Logger.getLogger(MessageEventHandler.class.getName());

	@SuppressWarnings("unchecked")
	protected ReplyMessage handleMessageEvent(MessageEvent<?> messageEvent) throws IOException, InterruptedException, ExecutionException {

		final MessageContent messageContent = messageEvent.getMessage();
		if (messageContent == null) {
			return null;
		}

		LOGGER.debug("message content=" + messageContent);

		if (messageContent instanceof TextMessageContent) {
			final MessageEvent<TextMessageContent> event = (MessageEvent<TextMessageContent>) messageEvent;
			return handleTextMessageEvent(event);
		}
		else if (messageContent instanceof ImageMessageContent) {
			final MessageEvent<ImageMessageContent> event = (MessageEvent<ImageMessageContent>) messageEvent;
			return handleImageMessageEvent(event);
		}
		else if (messageContent instanceof LocationMessageContent) {
			final MessageEvent<LocationMessageContent> event = (MessageEvent<LocationMessageContent>) messageEvent;
			return handleLocationMessageEvent(event);
		}
		else if (messageContent instanceof AudioMessageContent) {
			final MessageEvent<AudioMessageContent> event = (MessageEvent<AudioMessageContent>) messageEvent;
			return handleAudioMessageEvent(event);
		}
		else if (messageContent instanceof VideoMessageContent) {
			final MessageEvent<VideoMessageContent> event = (MessageEvent<VideoMessageContent>) messageEvent;
			return handleVideoMessageEvent(event);
		}
		else if (messageContent instanceof StickerMessageContent) {
			final MessageEvent<StickerMessageContent> event = (MessageEvent<StickerMessageContent>) messageEvent;
			return handleStickerMessageEvent(event);
		} else {
			return null;
		}
	}

	/**
	 * Called when a TextMessage is received
	 *
	 * @param event
	 * @return
	 * @throws IOException
	 */
	protected ReplyMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);

    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.DEFAULT)) {

			// NLC実行結果
			// TODO
			String nlcResult = INlc.JYUHEN;

			// 住所変更の場合
			if (nlcResult.equals(INlc.JYUHEN)) {

				// セッション更新
				bean.setStatus(ILine.JYH_BTN_START);
		        SessionData.setSessionBean(userId, bean);

		        // テンプレート取得
		        String text = MessageUtil.getMessage(ILine.MSG001);
		        ConfirmTemplate confirmTemplate = LineUtil.getComfirmTemplate_Yes_No(ILine.MSG001);

		        // リプライ
		        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(text, confirmTemplate));
			}
			// 意図が読めない場合
			else {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG002);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
			}
    	} else if (status.equals(ILine.JYH_WAITE_ADRINPUT)) {

    		// メッセージ取得
    		String message = event.getMessage().getText();

			// セッション更新
    		bean.setAddress(message);
			bean.setStatus(ILine.JYH_BTN_LASTCNF);
	        SessionData.setSessionBean(userId, bean);

	        // テンプレート取得
	        String text = MessageUtil.getMessage(ILine.MSG008, message);
	        ConfirmTemplate confirmTemplate = LineUtil.getComfirmTemplate_Yes_No(text);

	        // リプライ
	        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(text, confirmTemplate));
		}
		else if (status.equals(ILine.JYH_WAITE_ADD)) {

    		// メッセージ取得
			String address = bean.getAddress() + event.getMessage().getText();

			// セッション更新
			bean.setStatus(ILine.JYH_BTN_LASTCNF);
			bean.setAddress(address);
	        SessionData.setSessionBean(userId, bean);

	        // テンプレート準備
	        String text = MessageUtil.getMessage(ILine.MSG008,  address);
	        Action actionYes = new PostbackAction(ILine.ACT_DSP_YES, ILine.ACT_VAL_YES);
	        Action actionNo = new PostbackAction(ILine.ACT_DSP_NO, ILine.ACT_VAL_NO);
	        List<Action> actions = Arrays.asList(actionYes, actionNo);

	        // テンプレート作成
	        ConfirmTemplate confirmTemplate = new ConfirmTemplate(text, actions);

	        // リプライ
	        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(text, confirmTemplate));
		}
		// 上記以外
		else {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
		}
	}

	/**
	 * Called when a ImageMessage is received
	 *
	 * @param event
	 * @return
	 * @throws IOException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	protected ReplyMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException, InterruptedException, ExecutionException {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);

    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.DEFAULT)) {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    	else if (status.equals(ILine.JYH_WAITE_PICT)) {

    		// TODO WatsonVR,OCR
    		String result = "住所判明";
    		//String result = "住所不明";
    		String address = "東京都新宿区～";

    		// ▼以下、井上追加 Watson VR▼
    		LOGGER.debug("★WatsonVR_START");
    		System.out.println( "VisualRecognition classify start" );

    		/*
    		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
    		// APIキーをセット
    		service.setApiKey(IWatson.API_KEY);

    		System.out.println("Classify an image");

    		ClassifyImagesOptions op = new ClassifyImagesOptions();
    		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
    		    .images(new File("src/test/resources/visual_recognition/car.png"))
    		    .build();
    		VisualClassification result = service.classify(options).execute();
    		System.out.println(result);
    		*/

    		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
    		// APIキーをセット
    		service.setApiKey(IWatson.API_KEY);
    		/* 以下、パブリックなクラスとして判定することは成功
    		//service.setUsernameAndPassword("yuuta_inoue@rdslcs.com", "tr*******");
    		System.out.println("★Classify an image★");
    		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
    		    .images(new File("./fruitbowl.jpg"))
    		    .build();
    		VisualClassification result2 = service.classify(options).execute();
    		System.out.println(result2);
    		LOGGER.debug("★★" + result2);
			*/

    		// 詳細情報
	        // classifierIds用
	        List<String> list = Arrays.asList(IWatson.CLASSIFIER_ID);

	        // スコアを取得
	        ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
	        		.classifierIds(list)
	        		.threshold(0.0)
	        		.images(new File("./testIn_5.jpg"))
	        	    .build();
	        VisualClassification result2 = service.classify(options).execute();
	        System.out.println(result2);
    		LOGGER.debug("★★" + result2);

    		String json = result2.toString();
    		Gson gson = new Gson();
    		JsonImage jsonImage = gson.fromJson(result2.toString(), JsonImage.class);
    		//JsonImage jsonImage = JSON.decode(json, JsonImage.class);
    		// ◆課題
    		// 　①LINEで送信された画像を判定対象とする
    		// 　②受け取ったJSONからclass名およびscoreを取得する
    		// 　③適切にクラス化する
    		// 　④class名およびscoreの値に応じて処理を分岐させる
    		System.out.println("★getImages_processed:" + jsonImage.getImages_processed());
    		System.out.println("★getImages:" + jsonImage.getImages());

	        System.out.println( "VisualRecognition getClassifier end" );

	        /* 以下、NG
            // スコアを取得
    		InputStream imagesStream = new FileInputStream("./fruitbowl.jpg");
    		ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
    		  .imagesFile(imagesStream)
    		  .imagesFilename("fruitbowl.jpg")
    		  .parameters("{\"classifier_ids\": [\"Tegaki_1998646497\","
    		    + "\"owners\": [\"IBM\", \"me\"]}")
    		  .build();
    		ClassifiedImages result3 = service.classify(classifyOptions).execute();
    		System.out.println(result3);
    		LOGGER.debug("★★" + result3);
    		*/
    		LOGGER.debug("★WatsonVR_END");
    		// ▲以上、井上追加 Watson VR▲


    		if (result.equals("住所判明")) {
				// セッション更新
				bean.setStatus(ILine.JYH_BTN_CNFADR);
				bean.setAddress(address);
		        SessionData.setSessionBean(userId, bean);

		        // テンプレート取得
		        String text = MessageUtil.getMessage(ILine.MSG008, address);
		        ConfirmTemplate confirmTemplate = LineUtil.getComfirmTemplate_Yes_No(text);

		        // リプライ
		        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(text, confirmTemplate));
    		}
    		else {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG009);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));

    		}
    	}
    	else {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
	}

	/**
	 * Called when a LocationMessage is received
	 *
	 * @param event
	 * @return
	 * @throws IOException
	 */
	protected ReplyMessage handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);

    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.DEFAULT)) {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    	else if (status.equals(ILine.JYH_WAITE_LOCATION)) {

    		// TODO ゼンリンAPI
    		String result = "住所判明";
    		String znrnAddress = event.getMessage().getAddress();
//    		String znrnAddress =  "東京都新宿区～";
    		//String result = "住所不明";

    		if (result.equals("住所判明")) {
				// セッション更新
				bean.setStatus(ILine.JYH_BTN_CNFADR);
				bean.setAddress(znrnAddress);
		        SessionData.setSessionBean(userId, bean);

				// テンプレート利用準備
				// 画像URL
		        // TODO 画像差し替え
		        String thumbnailImageUrl = "https://riversun.github.io/img/riversun_256.png";
		        // タイトル
		        String title = "住所変更";
		        // 本文
		        String text = MessageUtil.getMessage(ILine.MSG008, znrnAddress);
		        // 選択肢
		        Action actionYes = new PostbackAction(ILine.ACT_DSP_YES, ILine.ACT_VAL_YES);
		        Action actionAdd = new PostbackAction(ILine.ACT_DSP_ADD, ILine.ACT_VAL_ADD);
		        Action actionNo = new PostbackAction(ILine.ACT_DSP_NO, ILine.ACT_VAL_NO);
		        List<Action> actions = Arrays.asList(actionYes, actionAdd, actionNo);

		        // テンプレートに設定
		        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(thumbnailImageUrl, title, text, actions);

		        // リプライ
		        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(title, buttonsTemplate));

    		}
    		else {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG009);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));

    		}
    	}
    	else {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
	}

	/**
	 * Called when a StickerMessage is received
	 *
	 * @param event
	 * @return
	 */
	protected ReplyMessage handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);


    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.DEFAULT)) {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    	else {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
	}

	/**
	 * Called when a AudioMessage is received
	 *
	 * @param event
	 * @return
	 * @throws IOException
	 */
	protected ReplyMessage handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);


    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.DEFAULT)) {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    	else {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    }

	/**
	 * Called when a VideoMessage is received
	 *
	 * @param event
	 * @return
	 * @throws IOException
	 */
	protected ReplyMessage handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) throws IOException {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);


    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.DEFAULT)) {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    	else {
			// セッション情報初期化
	        SessionData.setSessionBean(userId, new SessionBean());

	        // リプライ
	        TextMessage textMessage = new TextMessage(ILine.MSG101);
	        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
    	}
    }
}