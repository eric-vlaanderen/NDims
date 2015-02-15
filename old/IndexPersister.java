package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.Index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.eric.ndim.domain.persistence.DataHelper.closeQuietly;
import static com.eric.ndim.domain.persistence.DataHelper.getIndexFile;

/**
 * @author Eric Vlaanderen
 */
public class IndexPersister
{

	public static void storeIndex(final DB db, final String dataDirectory)
	{
		FileOutputStream os = null;
		try
		{
			File indexFile = getIndexFile(db.getName(), dataDirectory);
			Index index = db.getIndex();
			os = new FileOutputStream(indexFile);
			/for (Map.Entry<String, byte[]> entry : index.getItems())
			{
				writeLine(os, entry.getKey() + ":" + stringVal(entry.getValue()));
			}**/
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
