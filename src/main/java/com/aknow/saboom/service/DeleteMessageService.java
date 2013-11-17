package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;


public class DeleteMessageService {

    @SuppressWarnings("static-method")
    public Boolean deleteMessage(Key key){

        Datastore.delete(key);

        return Boolean.TRUE;
    }

}
