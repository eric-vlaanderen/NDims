package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;

import java.io.*;

import static com.eric.ndim.domain.persistence.DataHelper.*;

/**
 * @author Eric Vlaanderen
 */
public class DBLoader
{
	private final ItemLoader itemLoader;

	public DBLoader(final ItemLoader itemLoader)
	{
		this.itemLoader = itemLoader;
	}


	public boolean dbExists(final String dbName, final String dataDirectory)
	{
		File dbFile = getDBFile(dbName, dataDirectory);
		return dbFile.exists();
	}

	public DB loadDB(final String dbName, final String dataDirectory)
	{
		FileInputStream is = null;
		InputStreamReader in = null;
		BufferedReader reader =  null;
		try
		{
			File dbFile = getOrCreateDBFile(dbName, dataDirectory);
			is = new FileInputStream(dbFile);
			in = new InputStreamReader(is, "UTF-8");
			reader =  new BufferedReader(in);
			String name = reader.readLine();
			int dimensionCount = Integer.valueOf(reader.readLine());
			int objectSize = Integer.valueOf(reader.readLine());
			int dimensionKeySize = Integer.valueOf(reader.readLine());
			DB db = new DB(name, dimensionCount, objectSize, dimensionKeySize);
			itemLoader.loadItemsIntoMemory(db, dataDirectory);
			return db;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			closeQuietly(is);
			closeQuietly(in);
			closeQuietly(reader);
		}
	}
}
