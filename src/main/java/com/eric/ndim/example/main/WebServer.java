package com.eric.ndim.example.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;

/**
 * @author eric
 */
public class WebServer
{
	ServletContextHandler getServletContextHandler() throws IOException
	{
		WebAppContext wac = new WebAppContext();
		wac.setResourceBase("src/main/webapp");
		wac.setDescriptor("WEB-INF/web.xml");
		wac.setContextPath("/");
		wac.setParentLoaderPriority(true);
		return wac;
	}

	public void startJetty(int port) throws Exception
	{
		Server server = new Server(port);
		server.setHandler(getServletContextHandler());
		server.start();
		server.join();
	}
}
