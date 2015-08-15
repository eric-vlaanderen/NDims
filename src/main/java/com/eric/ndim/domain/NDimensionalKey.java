package com.eric.ndim.domain;

import static com.eric.ndim.util.ByteUtil.*;

/**
 * 
 * @author eric
 *
 */
class NDimensionalKey
{

	private final int dimensions;

	private final int dimensionKeySize;

	private final byte key[];

	protected NDimensionalKey(final int dimensions, final int dimensionKeySize)
	{
		this.dimensions = dimensions;
		this.dimensionKeySize = dimensionKeySize;
		int keySize = this.dimensions * this.dimensionKeySize;
		key = new byte[keySize];
		for (int i = 0; i < keySize; i++)
		{
			key[i] = 127;
		}
	}

	protected NDimensionalKey(final NDimensionalKey other)
	{
		this.key = other.key.clone();
		this.dimensions = other.dimensions;
		this.dimensionKeySize = other.dimensionKeySize;
	}

	protected NDimensionalKey(final byte[] id, final int dimensionKeySize)
	{
		this.key = id;
		this.dimensions = id.length;
		this.dimensionKeySize = dimensionKeySize;
	}

	public byte[] getKey()
	{
		return key.clone();
	}

	public byte[] getDimensionalKey(int dimension)
	{
		if (dimension > (dimensions - 1))
		{
			throw new IllegalArgumentException("Dimension too high: " + dimension + " max dimension is " + (dimensions - 1));
		}

		byte[] dimKey = new byte[dimensionKeySize];
		int start = dimension * dimensionKeySize;
		int j = 0;
		for (int i = start; i < (start + dimensionKeySize); i++)
		{
			byte key = this.key[i];
			dimKey[j] = key;
			j++;
		}
		return dimKey;
	}

	public byte[] incrementDimension(int dimension)
	{
		byte[] currentKey = getDimensionalKey(dimension);
		boolean incremented = false;
		for (int i = 0; i < dimensionKeySize && !incremented; i++)
		{
			byte b = currentKey[i];
			if (intVal(b) < 255)
			{
				b += 1;
				incremented = true;
			}
			else if (!isMax(getDimensionalKey(dimension)))
			{
				b = 0;
			}
			key[(dimension * dimensionKeySize) + i] = b;
			currentKey[i] = b;
		}
		return currentKey;
	}

	public byte[] decrementDimension(int dimension)
	{
		byte[] currentKey = getDimensionalKey(dimension);
		boolean decremented = false;
		for (int i = 0; i < dimensionKeySize && !decremented; i++)
		{
			byte b = currentKey[i];
			if (intVal(b) > 0)
			{
				b -= 1;
				decremented = true;
			}
			else if (!isMin(getDimensionalKey(dimension)))
			{
				b = -128;
			}
			key[(dimension * dimensionKeySize) + i] = b;
			currentKey[i] = b;
		}
		return currentKey;
	}

	public String toString()
	{
		return stringVal(key);
	}
}