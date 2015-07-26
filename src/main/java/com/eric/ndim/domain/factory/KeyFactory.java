package com.eric.ndim.domain.factory;

public class KeyFactory
{
	private final int keySize;
	private final byte[] defaultKey;

	public KeyFactory(final int dimensions, final int dimensionKeySize)
	{
		this.keySize = dimensionKeySize * dimensions;
		this.defaultKey = null;
	}
	public KeyFactory(final int keySize)
	{
		this.keySize = keySize;
		this.defaultKey = null;
	}

	public KeyFactory(byte[] defaultKey)
	{
		this.keySize = defaultKey.length;
		this.defaultKey = defaultKey;
	}

	public byte[] buildKey(final byte[] key)
	{
		byte[] actualKey = key;
		if (actualKey == null)
		{
			actualKey = buildKey();
		}
		return actualKey;
	}

	public byte[] buildKey()
	{
		return defaultKey == null ? new byte[keySize] : defaultKey;
	}

}
