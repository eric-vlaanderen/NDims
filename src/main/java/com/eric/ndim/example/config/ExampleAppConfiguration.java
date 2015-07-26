package com.eric.ndim.example.config;

import com.eric.ndim.domain.NamedDimension;
import com.eric.ndim.domain.NamedDimensions;
import com.eric.ndim.domain.factory.DBFactory;
import com.eric.ndim.domain.factory.KeyFactory;
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
	private static final String DB_NAME = "lame";

	public static final String RED = "red";
	public static final String GREEN = "green";
	public static final String BLUE = "blue";

	private static final int DIMENSION_KEY_SIZE = 1;
	private static final int DIMENSIONS = 3;

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

	@Bean
	public KeyFactory keyFactory() {
		return new KeyFactory(DIMENSIONS, DIMENSION_KEY_SIZE);
	}

	@Bean
	public com.eric.ndim.domain.DB nDimsDb() throws Exception
	{
		return new DBFactory().createDB(DB_NAME, DIMENSIONS, 12, DIMENSION_KEY_SIZE);
	}

	@Bean
	public NamedDimensions namedDimensions() {
		return new NamedDimensions(new NamedDimension(RED, 0), new NamedDimension(GREEN, 1), new NamedDimension(BLUE, 2));
	}
}
