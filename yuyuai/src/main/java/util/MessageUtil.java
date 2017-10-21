package util;

import java.text.MessageFormat;

public class MessageUtil {

	public static String getMessage(String msg, String... args) {

		MessageFormat mf = new MessageFormat(msg);
		return mf.format(args);
	}
}