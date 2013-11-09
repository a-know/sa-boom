package com.aknow.saboom.service.withClient;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.memcache.MemcacheServiceException;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.User;


public class LoginCheckRequestService implements ServiceWithClient{

    private static final Logger logger = Logger.getLogger(LoginCheckRequestService.class.getName());

    static final UserMeta userMeta = UserMeta.get();

    @Override
    public ArrayList<Object> execute(HttpServletRequest req,
            HttpServletResponse res, ArrayList<Object> inputList)
            throws ServletException, IOException, InterruptedException {

        //make list for output
        ArrayList<Object> outputList = new ArrayList<Object>();

        //get session
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

        //fail : session has already exists
        if(session.isNew()){
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to get session");
            }
            outputList.add("9");//return code "9" when error occurs
            return outputList;
        }

        //input password decryption
        Cipher cipher = null;
        EncodedKeySpec keySpec = null;
        KeyFactory keyFactory = null;
        byte[] binary = null;
        try {
            cipher = Cipher.getInstance("RSA");
            keyFactory = KeyFactory.getInstance("RSA");
            keySpec = new PKCS8EncodedKeySpec((byte[]) session.getAttribute("binary"));
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(keySpec) );
            binary = cipher.doFinal(((byte[])inputList.get(2)));
        } catch (Exception e) {
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to decryption. occurs exception is : " + e.toString());
            }
            e.printStackTrace();
            outputList.add("9");//return code "9" when error occurs
            return outputList;
        }

        //password check
        String loginID = (String) inputList.get(1);
        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();
        if(user == null){//no user about input loginID
            outputList.add("1");
            return outputList;
        }

        if (user.getPass().equals(DigestUtils.sha256Hex(new String(binary)))){//password(Hashed) matched : success
            outputList.add("0");
        }else{//password(Hashed) unmatched : fail
            outputList.add("1");
        }
        return outputList;
    }

}
