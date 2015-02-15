package com.eric.ndim.domain;

import com.eric.util.wrapper.ByteArrayWrapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author eric
 *
 */
public class Items 
{
	private final Set<ByteArrayWrapper> items = new HashSet<ByteArrayWrapper>();
	
	public void addItem(byte[] item)
	{
		items.add(new ByteArrayWrapper(item));
	}
	
	public void removeItem(byte[] item)
	{
		items.remove(new ByteArrayWrapper(item));
	}
	
	public Set<ByteArrayWrapper> getItems()
	{
		return Collections.unmodifiableSet(items);
	}
}