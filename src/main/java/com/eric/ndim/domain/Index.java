package com.eric.ndim.domain;


import com.eric.ndim.util.ByteArrayWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import static com.eric.ndim.domain.PersistableItemUtil.getLength;

/**
 * @author Eric Vlaanderen
 */
public class Index
{
	private Semaphore semaphore = new Semaphore(1);

	private final ConcurrentHashMap<ByteArrayWrapper, PersistableItem> objects = new ConcurrentHashMap<>();

	private final AtomicLong end = new AtomicLong(0L);

	private final ConcurrentLinkedQueue<PersistableItem> changed = new ConcurrentLinkedQueue<>();

	private final ConcurrentLinkedQueue<Long> freeBlocks = new ConcurrentLinkedQueue<>();

	public boolean addItem(final byte[] key, final byte[] object)
	{
		try
		{
			boolean added = false;
			semaphore.acquire();
			if (!objects.containsKey(new ByteArrayWrapper(object)))
			{
				PersistableItem item;
				int length = getLength(key.length, object.length);
				if (!freeBlocks.isEmpty())
				{
					item = new PersistableItem(key, freeBlocks.poll(), object);
				}
				else
				{
					item = new PersistableItem(key, end.getAndAdd(length), object);
				}
				objects.put(new ByteArrayWrapper(object), item);
				changed.add(item);
				added = true;
			}
			return added;
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			semaphore.release();
		}
	}

	public void loadItem(final PersistableItem item)
	{
		try
		{
			semaphore.acquire();
			objects.put(new ByteArrayWrapper(item.getObject()), item);
			end.addAndGet(item.getLength());
			changed.add(item);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			semaphore.release();
		}
	}

	public void itemModified(final byte[] object)
	{
		PersistableItem persistableItem = objects.get(new ByteArrayWrapper(object));
		changed.add(persistableItem);
	}

	public int changedSize()
	{
		return changed.size();
	}

	public PersistableItem nextChanged()
	{
		return changed.poll();
	}

	public void removeItem(final byte[] object)
	{
		try
		{
			semaphore.acquire();
			PersistableItem persistableItem = objects.get(new ByteArrayWrapper(object));
			objects.remove(new ByteArrayWrapper(object));
			freeBlocks.add(persistableItem.getLocation());
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			semaphore.release();
		}
	}
}