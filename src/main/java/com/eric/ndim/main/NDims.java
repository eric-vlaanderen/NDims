package com.eric.ndim.main;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.exception.DuplicateDBException;
import com.eric.ndim.domain.persistence.DBLoader;
import com.eric.ndim.domain.persistence.DBPersister;

import java.io.IOException;

import static com.eric.ndim.domain.persistence.DataHelper.deleteDB;

/**
 * @author Eric Vlaanderen
 */
public class NDims
{
	private final static String defaultDataDirectory = "/home/eric/eric/work/data/ndims/";

	private final String dataDirectory;
	private final DBLoader dbLoader;
	private final DBPersister dbPersister;

	public NDims(DBLoader dbLoader, DBPersister dbPersister)
	{
		this(dbLoader, dbPersister, defaultDataDirectory);
	}

	public NDims(DBLoader dbLoader, DBPersister dbPersister, String dataDirectory)
	{
		this.dbLoader = dbLoader;
		this.dbPersister = dbPersister;
		this.dataDirectory = dataDirectory;
	}

	public DB getDb(final String name,
					final int dimensions,
					final int objectSize,
					final int dimensionKeySize) throws DuplicateDBException, IOException
	{
		DB db;
		if (dbLoader.dbExists(name, dataDirectory))
		{
			db = dbLoader.loadDB(name, dataDirectory);
			if (db.getDimensionCount() != dimensions)
			{
				throw new DuplicateDBException("DB exists with different number of dimensions");
			}
		}
		else
		{
			db = dbPersister.createDB(name, dimensions, objectSize, dimensionKeySize, dataDirectory);
		}
		return db;
	}

	public boolean deleteDb(final String name) throws IOException
	{
		return deleteDB(name, dataDirectory);
	}

}
