package com.eric.ndim.domain;

import com.eric.util.wrapper.ByteArrayWrapper;

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
	
	private ConcurrentHashMap<ByteArrayWrapper, Items> st_ = new ConcurrentHashMap<ByteArrayWrapper, Items>();

	public Dimension(final int keySize) {
		this.keySize = keySize;
	}

	public Set<ByteArrayWrapper> getObjects(byte[] key)
	{
		ensureSize(key);
		Set<ByteArrayWrapper> toReturn = new HashSet<ByteArrayWrapper>();
		Items found = st_.get(new ByteArrayWrapper(key));
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
		if ((items = st_.putIfAbsent(baw, items)) != null)
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
		Items found = st_.get(baw);
		if (found != null)
		{
			found.removeItem(object);
		}
    }
	
	private void ensureSize(byte[] key)
	{
		if (key.length != keySize)
		{
			throw new IllegalArgumentException("Key size wrong for given key: " + key + ", size must be " + keySize + " bytes");
		}
	}
}