package com.eric.ndim.domain;

public class PersistableItemUtil
{
	public static final int LOCATION_LENGTH = 8;

	public static int getLength(int keyLength, int itemLength)
	{
		return LOCATION_LENGTH + keyLength + itemLength;
	}
}
