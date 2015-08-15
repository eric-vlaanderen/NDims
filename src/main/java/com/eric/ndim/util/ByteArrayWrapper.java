package com.eric.ndim.util;

import java.util.Arrays;

/**
 * @author Eric Vlaanderen
 */
public final class ByteArrayWrapper
{
	private final byte[] data;

	public ByteArrayWrapper(byte[] data)
	{
		this.data = data;
	}

	@Override
	public boolean equals(Object other)
	{
		return other instanceof ByteArrayWrapper && Arrays.equals(data, ((ByteArrayWrapper) other).data);
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(data);
	}

	public byte[] getData()
	{
		return data;
	}
}