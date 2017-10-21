package eventHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.LeaveEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.servlet.LineBotCallbackException;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;

public class PostEventMainHandler extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PostEventMainHandler.class.getName());

	/**
	 * httpServletRequest(Implicit Object)
	 */
	protected HttpServletRequest req;

	/**
	 * httpServletResponse(Implicit Object)
	 */
	protected HttpServletResponse res;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.req = req;
		this.res = res;
		super.service(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		final LineSignatureValidator validator = new LineSignatureValidator(ILine.CHANNEL_SECRET.getBytes(StandardCharsets.UTF_8));
		final LineBotCallbackRequestParser parser = new LineBotCallbackRequestParser(validator);

		final String signature = req.getHeader("X-Line-Signature");

		final byte[] jsonData = ByteStreams.toByteArray(req.getInputStream());

		final String jsonText = new String(jsonData, StandardCharsets.UTF_8);
		LOGGER.debug("RESPONSE JSON\n" + new JSONObject(jsonText).toString(4));

		CallbackRequest callbackRequest;

		try {
			callbackRequest = parser.handle(signature, jsonText);

			final List<Event> eventList = callbackRequest.getEvents();

			for (Event event : eventList) {

				LOGGER.debug(event);

				if (event == null) {
					continue;
				}

				if (event instanceof MessageEvent) {
					final MessageEvent<?> messageEvent = (MessageEvent<?>) event;
					MessageEventHandler meh = new MessageEventHandler();
					LineUtil.reply(meh.handleMessageEvent(messageEvent));
				}
				else if (event instanceof UnfollowEvent) {
					UnfollowEvent unfollowEvent = (UnfollowEvent) event;
					handleUnfollowEvent(unfollowEvent);
				}
				else if (event instanceof FollowEvent) {
					FollowEvent followEvent = (FollowEvent) event;
					LineUtil.reply(handleFollowEvent(followEvent));
				}
				else if (event instanceof JoinEvent) {
					final JoinEvent joinEvent = (JoinEvent) event;
					LineUtil.reply(handleJoinEvent(joinEvent));
				}
				else if (event instanceof LeaveEvent) {
					final LeaveEvent leaveEvent = (LeaveEvent) event;
					handleLeaveEvent(leaveEvent);
				}
				else if (event instanceof PostbackEvent) {
					final PostbackEvent postbackEvent = (PostbackEvent) event;
					PostbackEventHandler pbeh = new PostbackEventHandler();
					LineUtil.reply(pbeh.handlePostbackEvent(postbackEvent));
				}
				else if (event instanceof BeaconEvent) {
					final BeaconEvent beaconEvent = (BeaconEvent) event;
					LineUtil.reply(handleBeaconEvent(beaconEvent));
				}
			}
		} catch (LineBotCallbackException e) {
			e.printStackTrace();
		}
		res.setStatus(200);
	}

	@Override
    protected void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {
        LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

        PrintWriter out = res.getWriter(  );
        out.println("<h1>Hello</h1>");

        res.setStatus(200);
    }
	/**
	 * Called when a UnfollowEvent is received
	 *
	 * @param event
	 */
	protected void handleUnfollowEvent(UnfollowEvent event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		LineUtil.handleDefaultMessageEvent(event);
	}

	/**
	 * Called when a FollowEvent is received
	 *
	 * @param event
	 * @return
	 */
	protected ReplyMessage handleFollowEvent(FollowEvent event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());

		return LineUtil.handleDefaultMessageEvent(event);
	}

	/**
	 * Called when a JoinEvent is received
	 *
	 * @param event
	 * @return
	 */
	protected ReplyMessage handleJoinEvent(JoinEvent event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());
		return LineUtil.handleDefaultMessageEvent(event);
	}

	/**
	 * Called when a LeaveEvent is received
	 *
	 * @param event
	 */
	protected void handleLeaveEvent(LeaveEvent event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());
		LineUtil.handleDefaultMessageEvent(event);
	}

	/**
	 * Called when a BeaconEvent is received
	 *
	 * @param event
	 * @return
	 */
	protected ReplyMessage handleBeaconEvent(BeaconEvent event) {
		LOGGER.debug("START:" + new Object(){}.getClass().getEnclosingClass().getName() + "#" + new Object(){}.getClass().getEnclosingMethod().getName());
		return LineUtil.handleDefaultMessageEvent(event);

	}
}