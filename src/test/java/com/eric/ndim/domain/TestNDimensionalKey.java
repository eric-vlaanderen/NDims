package com.eric.ndim.domain;

import com.eric.ndim.util.ByteUtil;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * 
 * @author eric
 *
 */
public class TestNDimensionalKey
{

	@Test
	public void fuckWithBytes()
	{
		for (byte i = -128; i < 127; i++)
		{
			int val = ByteUtil.intVal(i);
			byte b = ByteUtil.byteVal(val);
			int val2 = ByteUtil.intVal(b);
			System.out.println(val);
			System.out.println(b);
			assertEquals(i, b);
			assertEquals(val, val2);
		}
	}

	@Test
	public void testKey()
	{
		{
			byte[] expected = { 0, 127 };
			NDimensionalKey key = new NDimensionalKey(2, 1);

			while (!toString(expected).equals(key.toString()))
			{
				key.decrementDimension(0);
				System.out.println(key);
			}

			byte[] expected2 = { 0, 0 };
			while (!toString(expected2).equals(key.toString()))
			{
				key.decrementDimension(1);
				System.out.println(key);
			}
			String min = key.toString();
			key.decrementDimension(1);
			key.decrementDimension(0);
			assertEquals(min, key.toString());
		}
		{
			byte[] expected = { -1, 127 };
			NDimensionalKey key = new NDimensionalKey(2, 1);

			while (!toString(expected).equals(key.toString()))
			{
				key.incrementDimension(0);
				System.out.println(key);
			}

			byte[] expected2 = { -1, -1 };
			while (!toString(expected2).equals(key.toString()))
			{
				key.incrementDimension(1);
				System.out.println(key);
			}
			String max = key.toString();
			key.incrementDimension(1);
			key.incrementDimension(0);
			assertEquals(max, key.toString());

		}
	}

	@Test
	public void testKey2()
	{
		{
			byte[] expected = { 0, 127, 0, 127, 127 };
			NDimensionalKey key = new NDimensionalKey(5, 1);

			while (!toString(expected).equals(key.toString()))
			{
				key.decrementDimension(0);
				key.decrementDimension(2);
				System.out.println(key);
			}

			byte[] expected2 = { 0, 127, 0, 127, 0 };
			while (!toString(expected2).equals(key.toString()))
			{
				key.decrementDimension(4);
				System.out.println(key);
			}
			String min = key.toString();
			key.decrementDimension(4);
			key.decrementDimension(2);
			key.decrementDimension(0);
			assertEquals(min, key.toString());
		}
		{
			byte[] expected = { -1, 127, 127, -1, 127 };
			NDimensionalKey key = new NDimensionalKey(5, 1);

			while (!toString(expected).equals(key.toString()))
			{
				key.incrementDimension(0);
				key.incrementDimension(3);
				System.out.println(key);
			}

			byte[] expected2 = { -1, 127, 127, -1, -1 };
			while (!toString(expected2).equals(key.toString()))
			{
				key.incrementDimension(4);
				System.out.println(key);
			}
			String max = key.toString();
			key.incrementDimension(0);
			key.incrementDimension(3);
			key.incrementDimension(4);
			assertEquals(max, key.toString());

		}
	}

	@Test
	public void lame() {
		ObjectId id = ObjectId.massageToObjectId("538c5f0ebf0000bf00bc2358");

		Date date = new Date(id._time() * 1000L);
		System.out.print(date);
	}

	private String toString(byte[] key)
	{
		return ByteUtil.stringVal(key);
	}

}
