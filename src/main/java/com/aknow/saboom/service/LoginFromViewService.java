package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.User;


public class LoginFromViewService {

    static final UserMeta userMeta = UserMeta.get();

    @SuppressWarnings("static-method")
    public int login(String loginID, String pass){

        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();
        if(user == null){//no user about input loginID
            return 1;
        }

        if (user.getPass().equals(pass)){//password(Hashed) matched : success
           return 0;
        }
        return 1;
    }
}
