package com.eric.ndim.domain;

import com.eric.ndim.util.ByteUtil;

import java.nio.ByteBuffer;

/**
 * @author Eric Vlaanderen
 */
public class PersistableItem
{
	private long location;

	private byte[] object;

	private byte[] key;

	public PersistableItem(final byte[] key, final long location, final byte[] object)
	{
		this.location = location;
		this.object = object;
		this.key = key;
	}

	public PersistableItem(final byte[] bytes, final int keyLength, final int objectSize)
	{
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		this.location = buffer.getLong();
		this.key = new byte[keyLength];
		this.object = new byte[objectSize];
		buffer.get(this.getKey(), 0, keyLength);
		buffer.get(this.getObject(), 0, objectSize);
	}

	public long getLocation()
	{
		return location;
	}

	public byte[] getObject()
	{
		return object;
	}

	public byte[] getKey()
	{
		return key;
	}

	public int getLength()
	{
		return PersistableItemUtil.getLength(key.length, object.length);
	}

	public byte[] toBytes()
	{
		ByteBuffer buffer = ByteBuffer.allocate(getLength());
		byte[] locationBytes = ByteUtil.storageForm(location);
		buffer.put(locationBytes);
		buffer.put(key);
		buffer.put(object);
		return buffer.array();
	}
}