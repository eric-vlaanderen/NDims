package com.eric.ndim.example.main;

import com.eric.util.webserver.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleApp extends WebServer
{
	private static final Logger LOG = LoggerFactory.getLogger(ExampleApp.class);

	public static void main(String[] args) throws Exception
	{
		new ExampleApp().startJetty(9090);
	}

}
