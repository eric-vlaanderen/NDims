package com.eric.ndim.domain;

import com.eric.ndim.util.ByteArrayWrapper;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * 
 * @author eric
 *
 */
public class Items 
{
	private final Set<ByteArrayWrapper> items = new HashSet<>();
	
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
		return unmodifiableSet(items);
	}
}