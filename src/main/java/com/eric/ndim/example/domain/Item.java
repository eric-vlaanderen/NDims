package com.eric.ndim.example.domain;

import com.eric.ndim.util.ByteUtil;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

/**
 * @author eric
 */
@SuppressWarnings("serial")
public class Item extends BasicDBObject
{

	public Item(final BasicDBObject object)
	{
		this.putAll(object.toMap());
	}

	public Item(final String name, final byte[] key)
	{
		put("ndKey", key);
		put("name", name);
	}

	public String getName()
	{
		return (String) get("name");
	}

	public void setName(final String name)
	{
		put("name", name);
	}

	public ObjectId getId()
	{
		return ObjectId.massageToObjectId(get("_id"));
	}

	public void setId(final ObjectId id)
	{
		put("_id", id);
	}

	public byte[] getNDKey()
	{
		return (byte[]) get("ndKey");
	}

	public void setNDKey(final byte[] ndKey)
	{
		put("ndKey", ndKey);
	}

	public String getColor()
	{
		byte[] key = getNDKey();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < key.length; i++)
		{
			String hex = Integer.toHexString(ByteUtil.intVal(key[i]));
			if (hex.length() == 1)
			{
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}