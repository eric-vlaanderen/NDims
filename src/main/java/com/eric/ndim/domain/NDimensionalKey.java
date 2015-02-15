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

	private final byte k_[];

	protected NDimensionalKey(final int dimensions, final int dimensionKeySize)
	{
		this.dimensions = dimensions;
		this.dimensionKeySize = dimensionKeySize;
		k_ = new byte[this.dimensions];
		for (int i = 0; i < this.dimensions; i++)
		{
			k_[i] = 127;
		}
	}

	protected NDimensionalKey(final NDimensionalKey other)
	{
		this.k_ = other.k_.clone();
		this.dimensions = other.dimensions;
		this.dimensionKeySize = other.dimensionKeySize;
	}

	protected NDimensionalKey(final byte[] id, final int dimensionKeySize)
	{
		this.k_ = id;
		this.dimensions = id.length;
		this.dimensionKeySize = dimensionKeySize;
	}

	public byte[] getKey()
	{
		return k_.clone();
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
			dimKey[j] = k_[i];
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
			k_[(dimension * dimensionKeySize) + i] = b;
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
			k_[(dimension * dimensionKeySize) + i] = b;
			currentKey[i] = b;
		}
		return currentKey;
	}

	public String toString()
	{
		return stringVal(k_);
	}
}