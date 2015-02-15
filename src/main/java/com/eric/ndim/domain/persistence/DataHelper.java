package com.eric.ndim.domain.persistence;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Eric Vlaanderen
 */
public class DataHelper
{
	public static void writeLine(final FileOutputStream outputStream, final String string) throws IOException
	{
		byte[] toWrite = (string + "\n").getBytes("UTF-8");
		outputStream.write(toWrite);
	}

	public static File getOrCreateDBFile(final String dbName, final String dataDirectory) throws IOException
	{
		File dbFile = getDBFile(dbName, dataDirectory);
		if (!dbFile.exists())
		{
			dbFile.createNewFile();
		}
		return dbFile;
	}

	public static boolean deleteDB(final String dbName, final String dataDirectory) throws IOException
	{
		File dbDir = getDataDir(dbName, dataDirectory);
		File itemsFile = getItemsFile(dbName, dataDirectory);
		File dbFile = getDBFile(dbName, dataDirectory);
		boolean exists = dbFile.exists() | dbDir.exists() | itemsFile.exists();
		dbFile.delete();
		itemsFile.delete();
		dbDir.delete();
		return exists;
	}

	public static File getDBFile(final String dbName, final String dataDirectory)
	{
		File dataDir = getDataDir(dbName, dataDirectory);
		return new File(dataDir, dbName + ".db");
	}

	public static File getDataDir(final String dbName, final String dataDirectory)
	{
		File dataDir = new File(dataDirectory + (dataDirectory.endsWith(File.separator) ? "" : File.separator) + dbName);
		if (!dataDir.exists())
		{
			dataDir.mkdirs();
		}
		return dataDir;
	}

	public static File getItemsFile(final String dbName, final String dataDirectory) throws IOException
	{
		File dataDir = getDataDir(dbName, dataDirectory);
		File indexFile = new File(dataDir, "items");
		if (!indexFile.exists())
		{
			indexFile.createNewFile();
		}
		return indexFile;
	}

	public static void closeQuietly(final Closeable os)
	{
		try
		{
			if (os != null)
			{
				os.close();
			}
		}
		catch (Exception e)
		{
			// w/e
		}
	}
}