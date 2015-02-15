package com.eric.ndim.example;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.exception.DuplicateDBException;
import com.eric.ndim.domain.persistence.DBLoader;
import com.eric.ndim.domain.persistence.DBPersister;
import com.eric.ndim.domain.persistence.ItemLoader;
import com.eric.ndim.domain.persistence.ItemPersister;
import com.eric.ndim.main.NDims;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.eric.ndim.util.ByteUtil.storageForm;

/**
 * 
 * @author eric
 *
 */
public class DBContext
{

	public static final String TITLE = "title";
	public static final String ARTIST = "artist";
	public static final String ALBUM = "album";
	public static final String LENGTH = "length";
	public static final String TRACK = "track";
	public static final String YEAR = "year";


	private static final DB db;

	private static final Map<String, Integer> namedDimensions = new HashMap<String, Integer>(3);

	private static final DBLoader dbLoader;
	private static final DBPersister dbPersister;
	private static final ItemLoader itemLoader;
	private static final ItemPersister itemPersister;

	static
	{
		namedDimensions.put(TITLE, 0);
		namedDimensions.put(ARTIST, 1);
		namedDimensions.put(ALBUM, 2);
		namedDimensions.put(LENGTH, 3);
		namedDimensions.put(TRACK, 4);
		namedDimensions.put(YEAR, 5);

		try
		{
			itemPersister = new ItemPersister();
			itemLoader = new ItemLoader(itemPersister);
			dbLoader = new DBLoader(itemLoader);
			dbPersister = new DBPersister(itemLoader);
			db = new NDims(dbLoader, dbPersister).getDb("mp3s", 6, 4, 4);
		}
		catch (DuplicateDBException | IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static byte[] createKey(final String name)
	{
		return storageForm(name.hashCode());
	}

	public static DB getDB()
	{
		return db;
	}

	public static Integer getDimensionNumber(String dimName)
	{
		return namedDimensions.get(dimName);
	}

}