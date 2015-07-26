package com.eric.ndim.domain;

public class NamedDimension
{
	private final String name;
	private final int dimension;

	public NamedDimension(final String name, final int dimension)
	{
		this.name = name;
		this.dimension = dimension;
	}

	public String getName()
	{
		return name;
	}

	public int getDimension()
	{
		return dimension;
	}
}
