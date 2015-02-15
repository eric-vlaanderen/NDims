package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.DB;

import java.io.*;

import static com.eric.ndim.domain.persistence.DataHelper.closeQuietly;
import static com.eric.ndim.domain.persistence.DataHelper.getIndexFile;
import static com.eric.ndim.util.ByteUtil.byteVal;

/**
 * @author Eric Vlaanderen
 */
public class IndexLoader
{


	public static void loadIndex(final DB db, final String dataDirectory)
	{
		FileInputStream is = null;
		InputStreamReader in = null;
		BufferedReader reader =  null;
		try
		{
			File indexFile = getIndexFile(db.getName(), dataDirectory);
			is = new FileInputStream(indexFile);
			in = new InputStreamReader(is, "UTF-8");
			reader =  new BufferedReader(in);
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] bits = line.split(":");
				String keyString = bits[0];
				String val = bits[1];
				if (bits.length > 2)
				{
					for (int i = 2; i < bits.length; i++)
					{
						val += bits[i];
					}
				}
				byte[] key = byteVal(keyString);
				db.storeObject(key, val);
			}
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
