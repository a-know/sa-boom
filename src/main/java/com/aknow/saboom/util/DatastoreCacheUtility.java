package com.aknow.saboom.util;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.google.appengine.api.datastore.Key;
import com.aknow.saboom.util.UtilityMethods;

public class DatastoreCacheUtility {
	
	@SuppressWarnings("unchecked")
	public static <T> T getOrNull(T t, Key key) throws Exception{
		T entity = Memcache.get(key);
		if(entity == null){
			entity = (T) Datastore.getOrNull(t.getClass(), key);
			try{
				Memcache.put(key, entity);
			}catch(Exception e){
				UtilityMethods.sendAlertMail("DatastoreCacheUtility", e);
			}
		}
		return entity;
	}
	
	public static <T> List<T> get(T t, List<Key> keyList) throws Exception{
		List<T> entityList = new ArrayList<>();
		for(Key key : keyList){
			entityList.add(getOrNull(t, key));
		}
		return entityList;
	}
	
	public static <T> void put(T object, Key key) throws Exception{
		Datastore.put(object);
		if(Memcache.get(key) != null){
			Memcache.delete(key);
		}
		try{
			Memcache.put(key, object);
		}catch(Exception e){
			UtilityMethods.sendAlertMail("DatastoreCacheUtility", e);
		}
		
	}

}
