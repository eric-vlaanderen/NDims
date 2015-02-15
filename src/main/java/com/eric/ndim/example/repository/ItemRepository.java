package com.eric.ndim.example.repository;

import com.eric.ndim.example.DBContext;
import com.eric.ndim.example.domain.Iter;
import com.eric.ndim.example.domain.Item;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public class ItemRepository
{

	private final DBCollection dbCollection;

	@Autowired
	public ItemRepository(final DB db)
	{
		this.dbCollection = db.getCollection("person");
	}
	
	public Item findById(final ObjectId id)
	{
		return new Item((BasicDBObject)dbCollection.findOne((id(id))));
	}

	public Item findByName(final String name)
	{
		return new Item((BasicDBObject)dbCollection.findOne(name(name)));
	}

	public Iterator<Item> findAll()
	{
		return new Iter<Item>(dbCollection.find().iterator());
	}
	
	public void save(final Item item)
	{
		dbCollection.save(item);
		DBContext.getDB().storeObject(item.getNDKey(), item.getId().toByteArray());
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