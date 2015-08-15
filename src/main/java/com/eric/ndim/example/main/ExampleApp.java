package com.eric.ndim.example.main;

public class ExampleApp extends WebServer
{
	public static void main(String[] args) throws Exception
	{
		new ExampleApp().startJetty(9090);
	}
}
