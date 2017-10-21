package eventHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import sessionCtrl.SessionBean;
import sessionCtrl.SessionData;
import util.MessageUtil;

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
	protected ReplyMessage handleMessageEvent(MessageEvent<?> messageEvent) throws IOException {

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
	 */
	protected ReplyMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
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