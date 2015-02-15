package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.PersistableItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.eric.ndim.domain.PersistableItemUtil.getLength;
import static com.eric.ndim.domain.persistence.DataHelper.closeQuietly;
import static com.eric.ndim.domain.persistence.DataHelper.getItemsFile;

/**
 * @author Eric Vlaanderen
 */
public class ItemLoader
{
	private final ItemPersister itemPersister;

	public ItemLoader(final ItemPersister itemPersister)
	{
		this.itemPersister = itemPersister;
	}


	public void loadItemsIntoMemory(final DB db, final String dataDirectory)
	{
		FileInputStream is = null;
		try
		{
			File itemsFile = getItemsFile(db.getName(), dataDirectory);
			int keyLength = db.getKeyLength();
			int totalSize = getLength(keyLength, db.getObjectSize());
			is = new FileInputStream(itemsFile);
			byte[] itemBytes = new byte[totalSize];
			while (is.read(itemBytes, 0, totalSize) != -1)
			{
				db.loadItem(new PersistableItem(itemBytes, keyLength, db.getObjectSize()));
			}
			Thread persistenceThread = new Thread(new PersistenceDaemon(itemPersister, db, dataDirectory));
			persistenceThread.start();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			closeQuietly(is);
		}
	}
}
