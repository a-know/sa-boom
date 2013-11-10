package com.aknow.saboom.controller;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.DeleteMessageService;
import com.aknow.saboom.util.UtilityMethods;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DeleteMessageController extends Controller {

    @Override
    public Navigation run() throws Exception {

        try{
            this.response.setContentType("application/json; charset=UTF-8");

            //入力情報を取得
            String keyValue = this.request.getParameter("key");

            Key key = KeyFactory.stringToKey(keyValue);

            DeleteMessageService service = new DeleteMessageService();

            Map<String, Boolean> map = new HashMap<String, Boolean>();
            map.put("result", service.deleteMessage(key));


            JSON.encode(map, this.response.getOutputStream());
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
