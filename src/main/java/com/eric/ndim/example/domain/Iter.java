package com.eric.ndim.example.domain;

import java.util.Iterator;

import com.mongodb.DBObject;

/**
 * 
 * @author eric
 *
 * @param <T>
 */
public class Iter<T extends DBObject> implements Iterator<T>
{
	private final Iterator<DBObject> base;

	public Iter(final Iterator<DBObject> base)
	{
		this.base = base;
	}

	@Override
    public boolean hasNext()
    {
	    return base.hasNext();
    }

	@SuppressWarnings("unchecked")
    @Override
    public T next()
    {
	    return (T) base.next();
    }

	@Override
    public void remove()
    {
		base.remove();
    }
}