package com.aknow.saboom.util;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.google.appengine.api.datastore.Key;

public class DatastoreCacheUtility {
	
	@SuppressWarnings("unchecked")
	public static <T> T get(T t, Key key){
		T entity = Memcache.get(key);
		if(entity == null){
			entity = (T) Datastore.get(t.getClass(), key);
			Memcache.put(key, entity);
		}
		return entity;
	}
	
	public static <T> List<T> get(T t, List<Key> keyList){
		List<T> entityList = new ArrayList<>();
		for(Key key : keyList){
			entityList.add(get(t, key));
		}
		return entityList;
	}
	
	public static <T> void put(T object, Key key){
		Datastore.put(object.getClass(), object);
		if(Memcache.get(key) != null){
			Memcache.delete(key);
		}
		Memcache.put(key, object);
	}

}
