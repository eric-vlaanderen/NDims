package com.eric.ndim.example.repository;

import com.eric.ndim.example.domain.Item;
import com.eric.ndim.example.domain.Iter;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public class ItemRepository
{

	private final DBCollection dbCollection;
	private final com.eric.ndim.domain.DB nDimsDb;

	@Autowired
	public ItemRepository(final DB db, final com.eric.ndim.domain.DB nDimsDb)
	{
		this.nDimsDb = nDimsDb;
		this.dbCollection = db.getCollection("person");
		dbCollection.ensureIndex(new BasicDBObject("name", 1), "nameIndex", true);
	}
	
	public Item findById(final ObjectId id)
	{
		return new Item((BasicDBObject) dbCollection.findOne((id(id))));
	}

	public Item findByName(final String name)
	{
		return new Item((BasicDBObject) dbCollection.findOne(name(name)));
	}

	public Iterator<Item> findAll()
	{
		return new Iter<Item>(dbCollection.find().iterator());
	}
	
	public void save(final Item item)
	{
		dbCollection.save(item);
		storeObjectInNDims(findByName(item.getName()));
	}

	private void storeObjectInNDims(final Item item)
	{
		nDimsDb.storeObject(item.getNDKey(), item.getId().toByteArray());
	}

	public void delete(final ObjectId id)
	{
		dbCollection.remove(id(id));
	}
	
	private DBObject name(final String name)
    {
		return new BasicDBObject("name", name);
    }
	
	private DBObject id(final ObjectId id)
    {
	    return new BasicDBObject("_id", id);
    }
}