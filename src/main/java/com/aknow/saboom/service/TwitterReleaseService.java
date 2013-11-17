package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.User;


public class TwitterReleaseService {
    @SuppressWarnings("static-method")
    public boolean releaseTwitterTokens(String loginID){

        UserMeta meta = new UserMeta();
        User user = Datastore.query(meta).filter(meta.loginId.equal(loginID)).asSingle();
        user.setTwitterAccessToken("");
        user.setTwitterAccessTokenSecret("");
        Datastore.put(user);

        return true;
    }
}
