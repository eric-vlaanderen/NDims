package com.eric.ndim.domain.persistence;

import com.eric.ndim.domain.Dimension;
import com.eric.ndim.domain.DB;

import java.io.*;
import java.util.Map;

import static com.eric.ndim.domain.persistence.DataHelper.createDBDirs;
import static com.eric.ndim.domain.persistence.DataHelper.fromFileName;
import static com.eric.ndim.domain.persistence.DataHelper.getDimensionDir;

/**
 * @author Eric Vlaanderen
 */
public class DimensionLoader
{

	public void load(final DB db, final String dataDirectory)
	{
		createDBDirs(db, dataDirectory);
		Map<Integer, Dimension> dimensions = db.getDimensions();
		for (Map.Entry<Integer, Dimension> entry : dimensions.entrySet())
		{
			Dimension dim = entry.getValue();
			Integer i = entry.getKey();
			File dimDir = getDimensionDir(db.getName(), i, dataDirectory);
			loadDimension(dimDir, i, dim);
		}
	}

	private void loadDimension(final File dimDir, final Integer i, final Dimension dim)
	{
		File[] files = dimDir.listFiles();
		for (File file : files)
		{
			try
			{
				String name = file.getName();
				byte[] bytes = fromFileName(name);
				FileInputStream is = new FileInputStream(file);
				BufferedReader reader =  new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line;
				while ((line = reader.readLine()) != null)
				{
					dim.setObject(bytes, line);
				}
			}
			catch (UnsupportedEncodingException e)
			{
				throw new RuntimeException(e);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public byte[] fromFileName(final String name)
	{
		String[] split = name.split("_");
		byte[] bytes = new byte[split.length];
		for (int i = 0; i < split.length; i++)
		{
			Integer val = Integer.valueOf(split[i]);
			bytes[i] = ByteUtil.byteVal(val);
		}
		return bytes;
	}

	public String getFileName(final ByteArrayWrapper baw)
	{
		byte[] data = baw.getData();
		StringBuilder sb = new StringBuilder();
		for (byte b : data)
		{
			sb.append(ByteUtil.intVal(b));
			sb.append("_");
		}
		return sb.toString();
	}

	public File getDimensionDir(final String dbName, final Integer dimNo, final String dataDirectory)
	{
		File dimDir = new File(getDataDir(dbName, dataDirectory), dimNo + "/");
		if(!dimDir.exists())
		{
			dimDir.mkdirs();
		}
		return dimDir;
	}
}
