package com.eric.ndim.domain;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class NamedDimensions
{
	private final Map<String, Integer> namedDimensions = new HashMap<>();

	public NamedDimensions(NamedDimension... namedDimensionsList) {
		asList(namedDimensionsList).stream().forEach(nd -> namedDimensions.put(nd.getName(), nd.getDimension()));
	}

	public Integer getDimensionNumber(String dimension) {
		return namedDimensions.get(dimension);
	}
}
