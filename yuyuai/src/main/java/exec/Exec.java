package exec;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import eventHandler.PostEventMainHandler;

public class Exec {
	    public static void main(String[] args) throws Exception {

	        ServletHandler handler = new ServletHandler();

	        handler.addServletWithMapping(PostEventMainHandler.class, "/callback");

	        // loclahost:3000でJettyを起動
	        // ngrok http -region=ap 127.0.0.1:3000
	        Server jetty = new Server(3000);

	        jetty.setHandler(handler);
	        jetty.start();
	        jetty.join();
	    }
}
