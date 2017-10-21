package eventHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import retrofit2.Response;
import sessionCtrl.SessionBean;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineMessagingClientImpl;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

public class LineUtil {

	private static final Logger LOGGER = Logger.getLogger(LineUtil.class.getName());



	/**
	 *
	 * When other messages not overridden as handle* is received.
	 *
	 * @param event
	 * @return
	 */
	protected static ReplyMessage handleDefaultMessageEvent(Event event) {
		return null;
	}

	protected static void reply(ReplyMessage replyMessage) throws IOException {
		LOGGER.debug("send reply replyMessage=" + replyMessage);
		if (replyMessage == null) {
			return;
		}

		Response<BotApiResponse> response = LineMessagingServiceBuilder
				.create(ILine.CHANNEL_ACCESS_TOKEN)
				.build()
				.replyMessage(replyMessage)
				.execute();

		// show network level message
		LOGGER.debug("send reply response=" + response.raw().toString());

	}

	/**
	 * Get User Profile
	 *
	 * @param userId
	 * @return
	 */
	protected static final UserProfileResponse getUserProfile(String userId) {

		final LineMessagingClient lineMessagingClient = new LineMessagingClientImpl(
				LineMessagingServiceBuilder
						.create(ILine.CHANNEL_ACCESS_TOKEN)
						.build()
				);
		try {
			UserProfileResponse userProfileResponse = lineMessagingClient.getProfile(userId).get();
			return userProfileResponse;

		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Get InputStream of contents such as images
	 *
	 * @param content
	 * @return
	 */
	protected static final InputStream getContentStream(MessageContent content) {

		String messageId = content.getId();

		final LineMessagingClient lineMessagingClient = new LineMessagingClientImpl(
				LineMessagingServiceBuilder
						.create(ILine.CHANNEL_ACCESS_TOKEN)
						.build()
				);

		try {
			MessageContentResponse res;
			res = lineMessagingClient.getMessageContent(messageId).get();

			final InputStream contentStream = res.getStream();
			return contentStream;

		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	protected static String getStatus(SessionBean bean) {

    	String status = "";

    	// ステータス取得
    	if (bean != null) {
	    	status = bean.getStatus();
	    	// ステータスが設定されていない場合は初期化
	    	if(status == null || status.length() == 0) {
	    		status = ILine.DEFAULT;
	    	}
    	}
    	else {
    		bean = new SessionBean();
    		status = ILine.DEFAULT;
    	}

    	return status;
	}

	protected static ConfirmTemplate getComfirmTemplate_Yes_No(String text) {

        Action actionYes = new PostbackAction(ILine.ACT_DSP_YES, ILine.ACT_VAL_YES);
        Action actionNo = new PostbackAction(ILine.ACT_DSP_NO, ILine.ACT_VAL_NO);
        List<Action> actions = Arrays.asList(actionYes, actionNo);

        // テンプレート作成
        return new ConfirmTemplate(text, actions);
	}
}
