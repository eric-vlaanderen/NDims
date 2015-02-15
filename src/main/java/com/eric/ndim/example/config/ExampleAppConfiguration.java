package com.eric.ndim.example.config;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.net.UnknownHostException;

@Configuration
@ComponentScan("com.eric.ndim.example")
@EnableWebMvc
public class ExampleAppConfiguration
{
	private Mongo mongo;

	private FreeMarkerConfigurer configurer;

	private FreeMarkerViewResolver viewResolver;

	@Bean
	public Mongo mongo() throws UnknownHostException
	{
		ServerAddress addr = new ServerAddress("localhost", 27017);
		MongoOptions options = new MongoOptions();
		options.autoConnectRetry = true;
		options.maxAutoConnectRetryTime = 10;
		mongo = new Mongo(addr, options);
		return mongo;
	}

	@Bean
	public DB db() throws UnknownHostException
	{
		return mongo().getDB("example");
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfig()
	{
		configurer = new FreeMarkerConfigurer();
		configurer.setTemplateLoaderPath("WEB-INF/freemarker");
		return configurer;
	}

	@Bean
	public FreeMarkerViewResolver viewResolver()
	{
		viewResolver = new FreeMarkerViewResolver();
		viewResolver.setCache(true);
		viewResolver.setPrefix("");
		viewResolver.setSuffix(".ftl");
		return viewResolver;
	}
}
