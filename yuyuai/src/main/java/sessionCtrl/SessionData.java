package sessionCtrl;

import java.util.HashMap;

public class SessionData {

	public static HashMap<String, SessionBean> m = new HashMap<String, SessionBean>();

	public static SessionBean getSessionBean(String key) {
		SessionBean bean = m.get(key);
		if (bean == null) {
			bean = new SessionBean();
		}
		return bean;
	}

	public static void setSessionBean(String key, SessionBean bean) {
		m.put(key, bean);
	}

}
