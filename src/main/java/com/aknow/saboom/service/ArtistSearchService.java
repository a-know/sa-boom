package com.aknow.saboom.service;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.PlayCountByArtistMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.PlayCountByArtist;
import com.aknow.saboom.model.User;


public class ArtistSearchService {

    @SuppressWarnings("static-method")
    public List<Object> getUserListByArtist(String artistName){
        PlayCountByArtistMeta meta1 = new PlayCountByArtistMeta();

        List<PlayCountByArtist> list = Datastore.query(meta1).filter(meta1.artistName.equal(artistName)).sort(meta1.playCount.desc).asList();

        UserMeta meta2 = new UserMeta();
        List<User> userList = new ArrayList<User>();
        List<Integer> countList = new ArrayList<Integer>();

        for(PlayCountByArtist e : list){
            User user = Datastore.query(meta2).filter(meta2.loginId.equal(e.getLoginID()), meta2.isPrivate.equal(Boolean.FALSE)).asSingle();
            if(!(user == null)){
                userList.add(user);
                countList.add(e.getPlayCount());
            }
        }

        List<Object> returnList = new ArrayList<Object>();
        returnList.add(userList);
        returnList.add(countList);

        return returnList;
    }
}
