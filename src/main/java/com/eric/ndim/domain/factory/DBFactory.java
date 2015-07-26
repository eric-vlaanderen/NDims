package com.eric.ndim.domain.factory;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.exception.DuplicateDBException;
import com.eric.ndim.domain.persistence.DBLoader;
import com.eric.ndim.domain.persistence.DBPersister;
import com.eric.ndim.domain.persistence.ItemLoader;
import com.eric.ndim.domain.persistence.ItemPersister;
import com.eric.ndim.main.NDims;

import java.io.IOException;

public class DBFactory
{
	private final ItemPersister itemPersister;
	private final ItemLoader itemLoader;
	private final DBLoader dbLoader;
	private final DBPersister dbPersister;
	private final NDims nDims;

	public DBFactory()
	{
		this.itemPersister = new ItemPersister();
		this.itemLoader = new ItemLoader(itemPersister);
		this.dbLoader = new DBLoader(itemLoader);
		this.dbPersister = new DBPersister(itemLoader);
		this.nDims = new NDims(dbLoader, dbPersister);
	}

	public DB createDB(final String name, int dimensions, int objectSize, int dimensionKeySize) throws DuplicateDBException, IOException
	{
		return nDims.getDb(name, dimensions, objectSize, dimensionKeySize);
	}
}
