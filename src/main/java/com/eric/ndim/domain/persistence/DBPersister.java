package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.eric.ndim.domain.persistence.DataHelper.*;

/**
 * @author Eric Vlaanderen
 */
public class DBPersister
{
	private final ItemLoader itemLoader;

	public DBPersister(final ItemLoader itemLoader)
	{
		this.itemLoader = itemLoader;
	}

	public DB createDB(final String name, final int dimensions, final int objectSize, final int dimensionKeySize, final String dataDirectory) throws IOException
	{
		DB db = new DB(name, dimensions, objectSize, dimensionKeySize);
		persist(db, dataDirectory);
		itemLoader.loadItemsIntoMemory(db, dataDirectory);
		return db;
	}

	private void persist(final DB db, final String dataDirectory)
	{
		FileOutputStream os = null;
		String dbName = db.getName();
		getDataDir(dbName, dataDirectory);
		try
		{
			File dbFile = getOrCreateDBFile(dbName, dataDirectory);
			os = new FileOutputStream(dbFile);
			writeLine(os, dbName);
			writeLine(os, "" + db.getDimensionCount());
			writeLine(os, "" + db.getObjectSize());
			writeLine(os, "" + db.getDimensionKeySize());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			closeQuietly(os);
		}

	}
}
