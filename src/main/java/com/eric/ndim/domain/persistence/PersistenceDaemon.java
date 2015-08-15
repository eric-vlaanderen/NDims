package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;

/**
 * @author Eric Vlaanderen
 */
public class PersistenceDaemon implements Runnable
{
	private static final int sleepTime = 100;

	private final DB db;
	private final String dataDirectory;
	private final ItemPersister itemPersister;


	public PersistenceDaemon(final ItemPersister itemPersister, final DB db, final String dataDirectory)
	{
		this.itemPersister = itemPersister;
		this.db = db;
		this.dataDirectory = dataDirectory;
	}

	@Override
	public void run()
	{
		//noinspection InfiniteLoopStatement
		while(true)
		{
			itemPersister.persist(db, dataDirectory);
			quietSleep();
		}
	}

	private void quietSleep()
	{
		try
		{
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
			// SSsssshhhh i'm sleeping!
		}
	}
}
