package com.aknow.saboom.service.withClient;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.Activity;
import com.aknow.saboom.model.User;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.memcache.MemcacheServiceException;


public class RegistRequestService implements ServiceWithClient{

    private static final Logger logger = Logger.getLogger(RegistRequestService.class.getName());

    @Override
    public ArrayList<Object> execute(HttpServletRequest req,
        HttpServletResponse res, ArrayList<Object> inputList)
                throws ServletException, IOException, InterruptedException {


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));

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

        //login ID from client already exists? check
        String loginID = (String) inputList.get(1);

        UserMeta userMeta = UserMeta.get();

        //input loginID is not exists yet, continue regist
        if(Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).count() == 0){
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

            //regist new user
            User user = new User();
            user.setKey(Datastore.allocateId(UserMeta.get()));
            user.setLoginId(loginID);
            user.setPass(DigestUtils.sha256Hex(new String(binary)));
            user.setIsPrivate((Boolean)inputList.get(3));
            Date date = calendar.getTime();
            user.setLastUploadDate(date);
            user.setFirstRegistDate(date);
            user.setLastUploadDataYyMMddHHmmss("000000000000");
            user.setTop10ArtistDataList(new ArrayList<String>());
            user.setTotalPlayCount(new Integer(0));
            user.setUploadCount(new Integer(0));
            user.setAccessCount(new Integer(0));
            user.setDiaryCount(new Integer(0));
            user.setIntroduction("（未設定）");
            user.setUrl("");
            user.setIsTweetOption1(Boolean.FALSE);
            user.setIsTweetOption2(Boolean.FALSE);
            user.setIsTweetOption3(Boolean.FALSE);
            user.setIsTweetOption4(Boolean.FALSE);
            user.setIsTweetOption5(Boolean.FALSE);
            user.setTwitterAccessToken("");
            user.setTwitterAccessTokenSecret("");

            Transaction tx = Datastore.beginTransaction();

            try{
                Datastore.put(tx, user);
                tx.commit();
            }catch(Exception e){
                if(logger.isLoggable(Level.SEVERE)){
                    logger.severe("fail to regist. occurs exception is : " + e.toString());
                }
                e.printStackTrace();
                outputList.add("9");//return code "9" when error occurs
                return outputList;
            }finally{
                if(tx.isActive()){
                    tx.rollback();
                }
            }

            //insert : activity information
            HashMap<Object,Object> activityInfo = new HashMap<Object, Object>();
            activityInfo.put("loginID", loginID);

            Activity activity = new Activity();
            activity.setActivityDate(date);
            activity.setActivityInfo(activityInfo);
            activity.setActivityCode("1");
            activity.setLoginID(loginID);
            if(user.getIsPrivate().booleanValue()){
                activity.setViewable(Boolean.FALSE);
            }else{
                activity.setViewable(Boolean.TRUE);
            }

            tx = Datastore.beginTransaction();

            try{
                Datastore.put(tx, activity);
                tx.commit();
            }catch(Exception e){
                //ignore any errors
                if(logger.isLoggable(Level.WARNING)){
                    logger.severe("fail to regist activity. occurs exception is : " + e.toString());
                }
                e.printStackTrace();
            }finally{
                if(tx.isActive()){
                    tx.rollback();
                }
            }

            if(!user.getIsPrivate().booleanValue()){
                //TODO
                //tweet on official twitter account
                //String tweetContents = "sa-boom!! ユーザー「" + activity.get("loginID") + "」が新規ユーザー登録を行いました！ようこそCDiT for Webへ！　http://sa-boom.appspot.com/　#sa-boom";
                //new TwitterUtil().doVariousTweet(1, tweetContents);
            }


            //successed, return code "0"
            outputList.add("0");
            return outputList;
        }
        //request loginID is already exisits
        outputList.add("1");
        return outputList;
    }

}
