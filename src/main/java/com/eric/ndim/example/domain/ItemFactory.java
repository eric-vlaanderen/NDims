package com.eric.ndim.example.domain;

import com.eric.ndim.domain.factory.KeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemFactory
{
	private final KeyFactory keyFactory;

	@Autowired
	public ItemFactory(final KeyFactory keyFactory)
	{
		this.keyFactory = keyFactory;
	}

	public Item buildItem(final String name, final byte[] key)
	{
		return new Item(name, keyFactory.buildKey(key));
	}

	public Item buildItem(final String name)
	{
		return buildItem(name, keyFactory.buildKey());
	}
}
