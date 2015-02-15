package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.ByteArrayWrapper;
import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.Dimension;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @author Eric Vlaanderen
 */
public class DimensionPersister
{

	private static final int maxChangeSize = 1;


	public static void persist(final DB db, final String dataDirectory)
	{
		createDBDirs(db, dataDirectory);
		Map<Integer, Dimension> dimensions = db.getDimensions();
		for (Map.Entry<Integer, Dimension> entry : dimensions.entrySet())
		{
			Dimension dim = entry.getValue();
			Integer i = entry.getKey();
			while (dim.changedSize() >= maxChangeSize)
			{
				File dimensionDir = getDimensionDir(db.getName(), i, dataDirectory);
				store(dimensionDir, dim);
			}
		}
	}

	private static void store(final File dimDir, final Dimension dim)
	{
		FileOutputStream os = null;
		try
		{
			ByteArrayWrapper baw = dim.nextChanged();
			String fileName = getFileName(baw);
			File writeTo = new File(dimDir, "~" + fileName);
			if (!writeTo.exists())
			{
				writeTo.createNewFile();
			}
			Set<String> objects = dim.getObjects(baw.getData());
			os = new FileOutputStream(writeTo);
			for (Object object : objects)
			{
				writeLine(os, object.toString());
			}
			File dest = new File(dimDir, fileName);
			if (dest.exists())
			{
				dest.delete();
			}
			writeTo.renameTo(dest);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
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