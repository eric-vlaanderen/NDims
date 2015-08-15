package com.eric.ndim.example.domain;

import com.mongodb.DBObject;

import java.util.Iterator;

/**
 * 
 * @author eric
 *
 * @param <T>
 */
public class DBObjectIterator<T extends DBObject> implements Iterator<T>
{
	private final Iterator<DBObject> base;

	public DBObjectIterator(final Iterator<DBObject> base)
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