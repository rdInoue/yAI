package eventHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import sessionCtrl.SessionBean;
import sessionCtrl.SessionData;

import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;

public class PostbackEventHandler {

	private static final Logger LOGGER = Logger.getLogger(PostbackEventHandler.class.getName());

	/**
	 * Called when a PostbackEvent is received
	 *
	 * @param event
	 * @return
	 */
	protected ReplyMessage handlePostbackEvent(PostbackEvent event) throws IOException {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		// 基本情報を収集
    	// ユーザーID
    	String userId = event.getSource().getUserId();
    	LOGGER.debug("userId=" + userId);

    	// セッション取得
    	SessionBean bean = SessionData.getSessionBean(userId);

    	String status = LineUtil.getStatus(bean);


    	// ステータスDEFAULTの場合
    	if (status.equals(ILine.JYH_BTN_START)) {

            PostbackContent postbackContent = event.getPostbackContent();

            // PostbackActionで設定したdataを取得する
            String data = postbackContent.getData();

            if (ILine.ACT_VAL_YES.equals(data)) {
				// セッション更新
				bean.setStatus(ILine.JYH_BTN_CHOICE);
		        SessionData.setSessionBean(userId, bean);

				// テンプレート利用準備
				// 画像URL
		        // TODO 画像差し替え
		        String thumbnailImageUrl = "https://riversun.github.io/img/riversun_256.png";
		        // タイトル
		        String title = "住所変更";
		        // 本文
		        String text = ILine.MSG003;
		        // 選択肢
		        Action actionPlace = new PostbackAction(ILine.ACT_DSP_LOC, ILine.ACT_VAL_LOC);
		        Action actionPict = new PostbackAction(ILine.ACT_DSP_PICT, ILine.ACT_VAL_PICT);
		        Action actionInp = new PostbackAction(ILine.ACT_DSP_INP, ILine.ACT_VAL_INP);
		        Action actionExt = new PostbackAction(ILine.ACT_DSP_STP, ILine.ACT_VAL_STP);
		        List<Action> actions = Arrays.asList(actionPlace, actionPict, actionInp, actionExt);

		        // テンプレートに設定
		        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(thumbnailImageUrl, title, text, actions);

		        // リプライ
		        return new ReplyMessage(event.getReplyToken(), new TemplateMessage(title, buttonsTemplate));
            }
            else if (ILine.ACT_VAL_NO.equals(data)) {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG004);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
    	}
    	else if (status.equals(ILine.JYH_BTN_CHOICE)) {
            PostbackContent postbackContent = event.getPostbackContent();

            // PostbackActionで設定したdataを取得する
            String data = postbackContent.getData();

            if (ILine.ACT_VAL_LOC.equals(data)) {
				// セッション更新
				bean.setStatus(ILine.JYH_WAITE_LOCATION);
		        SessionData.setSessionBean(userId, bean);

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG005);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
            else if (ILine.ACT_VAL_PICT.equals(data)) {
				// セッション更新
				bean.setStatus(ILine.JYH_WAITE_PICT);
		        SessionData.setSessionBean(userId, bean);

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG006);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
            else if (ILine.ACT_VAL_INP.equals(data)) {
				// セッション更新
				bean.setStatus(ILine.JYH_WAITE_ADRINPUT);
		        SessionData.setSessionBean(userId, bean);

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG007);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
            else if (ILine.ACT_VAL_STP.equals(data)) {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG004);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
    	}
    	else if (status.equals(ILine.JYH_BTN_CNFADR)) {
            PostbackContent postbackContent = event.getPostbackContent();

            // PostbackActionで設定したdataを取得する
            String data = postbackContent.getData();

            if (ILine.ACT_VAL_YES.equals(data)) {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG012);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
            else if (ILine.ACT_VAL_ADD.equals(data)) {
				// セッション更新
				bean.setStatus(ILine.JYH_WAITE_ADD);
		        SessionData.setSessionBean(userId, bean);

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG010);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
            else if (ILine.ACT_VAL_NO.equals(data)) {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG011);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
    	}
//    	else if (status.equals(ILine.JYUHEN_ADRCMF)) {
//            PostbackContent postbackContent = event.getPostbackContent();
//
//            // PostbackActionで設定したdataを取得する
//            String data = postbackContent.getData();
//
//            if (ILine.ACT_VAL_YES.equals(data)) {
//				// セッション更新
//				bean.setStatus(ILine.JYUHEN_LADTCMF);
//		        SessionData.setSessionBean(userId, bean);
//
//		        // リプライ
//		        TextMessage textMessage = new TextMessage(ILine.MSG008_1 + bean.getAddress() + ILine.MSG008_2);
//		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
//            }
//            else if (ILine.ACT_VAL_ADD.equals(data)) {
//				// セッション更新
//				bean.setStatus(ILine.JYH_WAITE_ADD);
//		        SessionData.setSessionBean(userId, bean);
//
//		        // リプライ
//		        TextMessage textMessage = new TextMessage(ILine.MSG010);
//		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
//            }
//            else if (ILine.ACT_VAL_NO.equals(data)) {
//				// セッション更新
//				bean.setStatus(ILine.DEFAULT);
//		        SessionData.setSessionBean(userId, bean);
//
//		        // リプライ
//		        TextMessage textMessage = new TextMessage(ILine.MSG011);
//		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
//            }
//
//    	}
    	else if (status.equals(ILine.JYH_BTN_LASTCNF)) {
            PostbackContent postbackContent = event.getPostbackContent();

            // PostbackActionで設定したdataを取得する
            String data = postbackContent.getData();

            if (ILine.ACT_VAL_YES.equals(data)) {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG012);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
            else if (ILine.ACT_VAL_NO.equals(data)) {
				// セッション情報初期化
		        SessionData.setSessionBean(userId, new SessionBean());

		        // リプライ
		        TextMessage textMessage = new TextMessage(ILine.MSG011);
		        return new ReplyMessage(event.getReplyToken(), Arrays.asList(textMessage));
            }
    	}
    	else {

    	}

		return LineUtil.handleDefaultMessageEvent(event);
	}
}