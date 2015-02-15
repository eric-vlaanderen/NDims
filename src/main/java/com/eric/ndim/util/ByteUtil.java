package com.eric.ndim.util;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;

/**
 *
 * @author eric
 *
 */
public class ByteUtil
{

	public static boolean isMax(byte[] key)
	{
		boolean isMax = true;
		for (int i = 0; i < key.length && isMax; i++)
		{
			if (intVal(key[i]) != 255)
			{
				isMax = false;
			}
		}
		return isMax;
	}

	public static boolean isMin(byte[] key)
	{
		boolean isMin = true;
		for (int i = 0; i < key.length && isMin; i++)
		{
			if (intVal(key[i]) != 0)
			{
				isMin = false;
			}
		}
		return isMin;
	}

	public static int intVal(byte b)
	{
		return b & 0x000000ff;
	}

	public static byte byteVal(int v)
	{
		byte b;
		if (v > 127)
		{
			int val = 1 - (257 - v);
			b = Byte.valueOf("" + val);
		}
		else
		{
			b = (byte) v;
		}
		return b;
	}

	public static byte[] byteVal(String str)
	{
		String[] parts = str.split("-");
		byte[] val = new byte[parts.length];
		for (int i = 0; i < parts.length; i++)
		{
			int item = Integer.valueOf(parts[i]);
			val[i] = ByteUtil.byteVal(item);
		}
		return val;
	}

	public static String stringVal(final byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		for(byte aByte : bytes)
		{
			sb.append(intVal(aByte)).append("-");
		}
		return sb.toString();
	}

	public static byte[] storageForm(final Long value)
	{
		return allocate(8).putLong(value).array();
	}

	public static Long fromStorageToLong(final byte[] value)
	{
		return wrap(value).getLong();
	}

	public static byte[] storageForm(final Integer value)
	{
		return allocate(4).putInt(value).array();
	}

	public static Integer fromStorageToInteger(final byte[] value)
	{
		return wrap(value).getInt();
	}
}
