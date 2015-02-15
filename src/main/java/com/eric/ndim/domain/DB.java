package com.eric.ndim.domain;

import com.eric.ndim.util.ByteUtil;
import com.eric.util.wrapper.ByteArrayWrapper;

import java.util.*;

/**
 *
 * @author eric
 *
 */
public class DB
{
	private final int MAX_CLOSENESS_VAL = 255;

	private final int dimensions;

	private final Map<Integer, Dimension> nDims_;

	private final String name;

	private final int objectSize;

	private final int dimensionKeySize;

	private final Index index = new Index();

	public DB(final String name, final int dimensions, final int objectSize, final int dimensionKeySize)
	{
		this.name = name;
		this.dimensions = dimensions;
		this.objectSize = objectSize;
		this.dimensionKeySize = dimensionKeySize;
		nDims_ = new HashMap<>(dimensions);
		for (int i = 0; i < dimensions; i++)
		{
			nDims_.put(i, new Dimension(dimensionKeySize));
		}
	}

	public Dimension getDimension(final int dimension)
	{
		return nDims_.get(dimension);
	}

	public byte[] buildKey()
	{
		return new NDimensionalKey(dimensions, dimensionKeySize).getKey();
	}

	public void storeObject(final byte[] id, final byte[] object)
	{
		ensureObjectSize(object);
		index.addItem(id, object);
		NDimensionalKey key = new NDimensionalKey(id, dimensionKeySize);
		for (int i = 0; i < dimensions; i++)
		{
			getDimension(i).setObject(key.getDimensionalKey(i), object);
		}
	}

	public void loadItem(final PersistableItem item)
	{
		index.loadItem(item);
		NDimensionalKey key = new NDimensionalKey(item.getKey(), dimensionKeySize);
		for (int i = 0; i < dimensions; i++)
		{
			getDimension(i).setObject(key.getDimensionalKey(i), item.getObject());
		}
	}

	public void removeObject(final byte[] id, final byte[] object)
	{
		NDimensionalKey key = new NDimensionalKey(id, dimensionKeySize);
		index.removeItem(object);
		for (int i = 0; i < dimensions; i++)
		{
			getDimension(i).removeObject(key.getDimensionalKey(i), object);
		}
	}

	public byte[] moveObject(final int dimension, final byte[] id, final byte[] object,
	        final int direction)
	{
		NDimensionalKey key = new NDimensionalKey(id, dimensionKeySize);

		byte[] oldKey = key.getDimensionalKey(dimension);
		byte[] newKey;
		if (direction < 0)
		{
			newKey = key.decrementDimension(dimension);
		}
		else
		{
			newKey = key.incrementDimension(dimension);
		}
		index.itemModified(object);
		getDimension(dimension).moveObject(oldKey, newKey, object);
		return key.getKey();
	}

	public byte[] moveObjectTo(final int dimension, final byte[] id, final byte[] object,
	        final int direction)
	{
		NDimensionalKey key = new NDimensionalKey(id, dimensionKeySize);

		byte[] oldKey = key.getDimensionalKey(dimension);
		byte[] newKey;
		if (direction < 0)
		{
			newKey = key.decrementDimension(dimension);
		}
		else
		{
			newKey = key.incrementDimension(dimension);
		}
		index.itemModified(object);
		getDimension(dimension).moveObject(oldKey, newKey, object);
		return key.getKey();
	}

	public List<ByteArrayWrapper> getNearObjects(final byte[] id, final int totalWanted, final byte[]... exclude)
	{
		NDimensionalKey key = new NDimensionalKey(id, dimensionKeySize);
		List<ByteArrayWrapper> excluded = createExcludesList(exclude);
		Map<ByteArrayWrapper, Integer> countMap = new HashMap<ByteArrayWrapper, Integer>();
		for (int dimNo = 0; dimNo < dimensions; dimNo++)
		{
			Map<ByteArrayWrapper, Integer> near = getNear(dimNo, key, totalWanted * dimensions);
			for (ByteArrayWrapper obj : near.keySet())
			{
				Integer val = near.get(obj);
				if (!excluded.contains(obj))
				{
					Integer i = countMap.get(obj);
					if (i == null)
					{
						countMap.put(obj, val);
					}
					else
					{
						countMap.put(obj, i + val);
					}
				}
			}
		}
		return sortedKeys(countMap, totalWanted);
	}

	private List<ByteArrayWrapper> createExcludesList(final byte[]... excludes)
	{
		List<ByteArrayWrapper> toReturn = new ArrayList<ByteArrayWrapper>();
		for (byte[] bytes : excludes)
		{
			toReturn.add(new ByteArrayWrapper(bytes));
		}
		return toReturn;
	}

	private Map<ByteArrayWrapper, Integer> getNear(final int dimNo, final NDimensionalKey key, final int wanted)
	{
		Dimension dim = nDims_.get(dimNo);
		byte[] keyForDim = key.getDimensionalKey(dimNo);
		Map<ByteArrayWrapper, Integer> near = new HashMap<ByteArrayWrapper, Integer>(wanted);

		NDimensionalKey decrementing = new NDimensionalKey(key);
		NDimensionalKey incrementing = new NDimensionalKey(key);
		addAllWithVal(dim, keyForDim, near, MAX_CLOSENESS_VAL);

		byte[] incKey = incrementing.incrementDimension(dimNo);
		int incremented = 1;
		byte[] decKey = decrementing.decrementDimension(dimNo);
		int decremented = 1;
		while (near.size() < wanted && !(ByteUtil.isMax(incKey) && ByteUtil.isMin(decKey)))
		{
			if (!ByteUtil.isMax(incKey))
			{
				addAllWithVal(dim, incKey, near, MAX_CLOSENESS_VAL / (incremented + 1));
				incKey = incrementing.incrementDimension(dimNo);
				incremented++;
			}
			if (!ByteUtil.isMin(decKey))
			{
				addAllWithVal(dim, decKey, near, MAX_CLOSENESS_VAL / (decremented + 1));
				decKey = decrementing.decrementDimension(dimNo);
				decremented++;
			}
		}
		return near;
	}

	private void addAllWithVal(final Dimension dim, final byte[] keyForDim, final Map<ByteArrayWrapper, Integer> near,
	        final int val)
	{
		for (ByteArrayWrapper obj : dim.getObjects(keyForDim))
		{
			near.put(obj, val);
		}
	}

	private List<ByteArrayWrapper> sortedKeys(final Map<ByteArrayWrapper, Integer> countMap, final int max)
	{
		List<ByteArrayWrapper> sorted = new ArrayList<ByteArrayWrapper>(max);
		sorted.addAll(countMap.keySet());
		Collections.sort(sorted, new Comparator<Object>()
		{
			@Override
			public int compare(Object o1, Object o2)
			{
				return countMap.get(o2).compareTo(countMap.get(o1));
			}
		});
		if (sorted.size() > max)
		{
			sorted = sorted.subList(0, max);
		}
		return sorted;
	}

	public Map<Integer, Dimension> getDimensions()
	{
		return nDims_;
	}

	public int getDimensionCount()
	{
		return dimensions;
	}

	public String getName()
	{
		return name;
	}

	public Index getIndex()
	{
		return index;
	}

	public int getKeyLength()
	{
		return dimensions * dimensionKeySize;
	}

	public int getObjectSize()
	{
		return objectSize;
	}

	private void ensureObjectSize(final byte[] object)
	{
		if (object.length != objectSize)
		{
			throw new IllegalArgumentException("Object size wrong for given object: " + object + ", size must be " + objectSize + " bytes");
		}
	}

	public int getDimensionKeySize()
	{
		return dimensionKeySize;
	}
}