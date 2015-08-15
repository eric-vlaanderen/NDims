package com.eric.ndim.example.controller;

import com.eric.ndim.domain.DB;
import com.eric.ndim.domain.NamedDimensions;
import com.eric.ndim.example.domain.Item;
import com.eric.ndim.example.domain.ItemFactory;
import com.eric.ndim.example.repository.ItemRepository;
import com.eric.ndim.util.ByteArrayWrapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author eric
 *
 */
@Controller
public class WebController
{
	private final ItemRepository repository;
	private final DB nDimsDb;
	private final ItemFactory itemFactory;
	private final NamedDimensions namedDimensions;

	@Autowired
	public WebController(final ItemRepository repository,
						 final DB nDimsDb,
						 final ItemFactory itemFactory,
						 final NamedDimensions namedDimensions)
	{
		this.repository = repository;
		this.nDimsDb = nDimsDb;
		this.itemFactory = itemFactory;
		this.namedDimensions = namedDimensions;
	}
	
	@RequestMapping(value="/createPerson", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void createPerson(@RequestParam("name") final String name)
	{
		Item item = itemFactory.buildItem(name);
		repository.save(item);
	}


//	@RequestMapping(value="/setTag", method=RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	public void setTag(@RequestParam("name") final String name,
//					   @RequestParam("tag") final String tag,
//					   @RequestParam("value") final String value)
//	{
//		Item item = repository.findByName(name);
//		Integer dimNo = namedDimensions.getDimensionNumber(tag);
//
//	}

	@RequestMapping(value="/near", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView nearPerson(@RequestParam("name") final String name)
	{
		Item item = repository.findByName(name);
		List<ByteArrayWrapper> people =  nDimsDb.getNearObjects(item.getNDKey(), 10, item.getId().toByteArray());
		List<Item> nearBy = new ArrayList<>();
		for (ByteArrayWrapper id : people)
		{
			nearBy.add(repository.findById(new ObjectId(id.getData())));
		}
		Map<String, Object> model = new LinkedHashMap<>();
		model.put("people", nearBy);
		for (Item p : nearBy)
		{
			model.put(p.getName() + "_color", p.getColor());
		}
		return new ModelAndView("example", model);
	}

	@RequestMapping(value="/move",  method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void movePerson(@RequestParam("name") final String name,
						   @RequestParam("dimension") final String dimension,
						   @RequestParam("direction") final int direction)
	{
		Item item = repository.findByName(name);
		Integer dimNo = namedDimensions.getDimensionNumber(dimension);
		byte[] newKey = nDimsDb.moveObject(dimNo, item.getNDKey(), item.getId().toByteArray(), direction);
		item.setNDKey(newKey);
		repository.save(item);
	}
}