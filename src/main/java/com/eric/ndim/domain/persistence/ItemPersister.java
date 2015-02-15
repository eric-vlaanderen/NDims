package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.Index;
import com.eric.ndim.domain.PersistableItem;

import java.io.IOException;
import java.io.RandomAccessFile;

import static com.eric.ndim.domain.persistence.DataHelper.closeQuietly;

/**
 * @author Eric Vlaanderen
 */
public class ItemPersister
{

	private static final int maxChangedCount = 1;

	public void persist(final DB db, final String dataDirectory)
	{
		Index index = db.getIndex();
		if (index.changedSize() >= maxChangedCount)
		{
			PersistableItem item = index.nextChanged();
			if (item != null)
			{
				persistItem(db.getName(), dataDirectory, item);
			}
		}
	}

	public void persistItem(final String dbName, final String dataDirectory, final PersistableItem item)
	{
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(DataHelper.getItemsFile(dbName, dataDirectory), "rw");
			raf.seek(item.getLocation());
			raf.write(item.toBytes());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			closeQuietly(raf);
		}
	}
}
