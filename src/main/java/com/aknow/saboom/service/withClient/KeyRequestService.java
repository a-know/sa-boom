package com.aknow.saboom.service.withClient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aknow.saboom.util.KeyUtil;

import com.google.appengine.api.memcache.MemcacheServiceException;


public class KeyRequestService implements ServiceWithClient{

    private static final Logger logger = Logger.getLogger(KeyRequestService.class.getName());

    public ArrayList<Object> execute(HttpServletRequest req,
        HttpServletResponse res, ArrayList<Object> inputList)
                throws ServletException, IOException, InterruptedException {

        //make list for output
        ArrayList<Object> outputList = new ArrayList<Object>();

        //get the session
        HttpSession session = null;
        int i = 0;

        while(true){
            try{
                session = req.getSession(true);
                break;
            }catch(MemcacheServiceException e){
                Thread.sleep(100);
                i++;
                if(i > 5) throw e;
            }
        }

        //generating key-pair
        ArrayList<Object> keyList = null;
        try {
            keyList = KeyUtil.makeKeySet();
        } catch (NoSuchAlgorithmException e) {
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to generate key-pair : NoSuchAlgorithmException");
            }
            e.printStackTrace();
            outputList.add("9");//return code "9" when error occurs
            return outputList;
        }

        //set key-pair info to session and outputList
        session.setAttribute("binary", keyList.get(0));
        outputList.add("0");//return code "OK"
        outputList.add(keyList.get(1));

        return outputList;
    }

}
