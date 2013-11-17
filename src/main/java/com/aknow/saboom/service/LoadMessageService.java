package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.model.Message;

import com.google.appengine.api.datastore.Key;


public class LoadMessageService {
    @SuppressWarnings("static-method")
    public Message loadMessage(Key key){

        Message m = Datastore.get(Message.class, key);

        m.setIsUnread(Boolean.FALSE);

        Datastore.put(m);

        return m;
    }
}
