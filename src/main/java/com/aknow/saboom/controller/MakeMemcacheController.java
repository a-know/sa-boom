package com.aknow.saboom.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.S3QueryResultList;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.model.TotalPlayCountByArtist;

public class MakeMemcacheController extends Controller {

    @Override
    public Navigation run() throws Exception {

        TotalPlayCountByArtistMeta meta = new TotalPlayCountByArtistMeta();
        
        S3QueryResultList<TotalPlayCountByArtist> list = Datastore.query(meta).limit(1000).asQueryResultList();
        makeCache(list);
        
        while(list.hasNext()){
        	String cursor = list.getEncodedCursor();
        	list = Datastore.query(meta).encodedStartCursor(cursor).limit(1000).asQueryResultList();
        	makeCache(list);
        }
        return null;
    }
    
    private <T> void makeCache(S3QueryResultList<TotalPlayCountByArtist> list){
    	for(TotalPlayCountByArtist e : list){
    		if(Memcache.get(e.getKey()) == null){
    			Memcache.put(e.getKey(), e);
    		}
    	}
    }
}
