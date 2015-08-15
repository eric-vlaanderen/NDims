package com.eric.ndim.domain;


import com.eric.ndim.util.ByteArrayWrapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author eric
 *
 */
public class Dimension 
{
	private final int keySize;

	private ConcurrentHashMap<ByteArrayWrapper, Items> storage = new ConcurrentHashMap<>();

	public Dimension(final int keySize) {
		this.keySize = keySize;
	}

	public Set<ByteArrayWrapper> getObjects(byte[] key)
	{
		ensureSize(key);
		Set<ByteArrayWrapper> toReturn = new HashSet<>();
		Items found = storage.get(new ByteArrayWrapper(key));
		if (found != null)
		{
			toReturn = found.getItems();
		}
		return toReturn;
	}
	
	public void setObject(byte[] key, byte[] object)
	{
		ensureSize(key);
		Items items = new Items();
		items.addItem(object);
		ByteArrayWrapper baw = new ByteArrayWrapper(key);
		if ((items = storage.putIfAbsent(baw, items)) != null)
		{
			items.addItem(object); 
		}
	}
	
	public void moveObject(byte[] oldKey, byte[] newKey, byte[] object)
	{
		ensureSize(newKey);
		ensureSize(oldKey);
		removeObject(oldKey, object);
		setObject(newKey, object);
	}

	public void removeObject(byte[] key, byte[] object)
    {
		ByteArrayWrapper baw = new ByteArrayWrapper(key);
		Items found = storage.get(baw);
		if (found != null)
		{
			found.removeItem(object);
		}
    }
	
	private void ensureSize(byte[] key)
	{
		if (key.length != keySize)
		{
			throw new IllegalArgumentException("Key size wrong for given key: " + Arrays.toString(key) + ", size must be " + keySize + " bytes");
		}
	}
}