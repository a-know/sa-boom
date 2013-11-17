package com.aknow.saboom.controller;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.TotalPlayCountByArtistMeta;
import com.aknow.saboom.model.TotalPlayCountByArtist;

import com.google.appengine.api.datastore.Key;

public class DeleteBugDataController extends Controller {

    @Override
    public Navigation run() throws Exception {
        //client 3.0.1-3.0.2のバグ対応
        //"|"をアーティスト名に含むデータを抹消する
        TotalPlayCountByArtistMeta pcbaMeta = new TotalPlayCountByArtistMeta();
        List<TotalPlayCountByArtist> pcbaList = Datastore.query(pcbaMeta).asList();

        List<Key> keyList = new ArrayList<Key>();

        for(TotalPlayCountByArtist e : pcbaList){
            if(e.getArtistName().contains("|")){
                keyList.add(e.getKey());
            }
        }
        System.out.println("***delete対象件数:" + keyList.size());

        Datastore.deleteAsync(keyList);

        return null;
    }
}
