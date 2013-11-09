package com.aknow.saboom.service.withClient;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import com.aknow.saboom.meta.ActivityMeta;
import com.aknow.saboom.meta.DiaryMeta;
import com.aknow.saboom.meta.MessageMeta;
import com.aknow.saboom.meta.PlayCountByArtistMeta;
import com.aknow.saboom.meta.PlayCountDataMeta;
import com.aknow.saboom.meta.TopUserInfoByArtistMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.User;
import com.aknow.saboom.util.UtilityMethods;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.memcache.MemcacheServiceException;


public class WithdrawService implements ServiceWithClient{

    private static final Logger logger = Logger.getLogger(WithdrawService.class.getName());

    static final UserMeta userMeta = UserMeta.get();
    static final PlayCountDataMeta playCountDataMeta = PlayCountDataMeta.get();
    static final PlayCountByArtistMeta playCountByArtistMeta = PlayCountByArtistMeta.get();
    static final TopUserInfoByArtistMeta topUserInfoByArtistMeta = TopUserInfoByArtistMeta.get();

    @Override
    public ArrayList<Object> execute(HttpServletRequest req,
        HttpServletResponse res, ArrayList<Object> inputList)
                throws ServletException, IOException, InterruptedException {

        //make list for output
        ArrayList<Object> outputList = new ArrayList<Object>();

        //get session
        HttpSession session = null;
        int n = 0;

        while(true){
            try{
                session = req.getSession(true);
                break;
            }catch(MemcacheServiceException e){
                Thread.sleep(100);
                n++;
                if(n > 5) throw e;
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
            //退会対象のユーザーなし
            outputList.add("1");
            return outputList;
        }

        if (user.getPass().equals(DigestUtils.sha256Hex(new String(binary)))){//password(Hashed) matched : success
            //ユーザー情報と再生回数情報の削除
            @SuppressWarnings("deprecation")
			GlobalTransaction gtx = Datastore.beginGlobalTransaction();

            ArrayList<PlayCountData> playCountDataList = (ArrayList<PlayCountData>) user.getPlayCountDataRefs().getModelList();

            for(int i = 0; i < playCountDataList.size(); i++){
                UtilityMethods.deleteBlob(playCountDataList.get(i).getBlobListKey());
                gtx.delete(playCountDataList.get(i).getKey());
            }

            //PCBA情報の削除
            List<Key> pcbaList = Datastore.query(playCountByArtistMeta).filter(playCountByArtistMeta.loginID.equal(loginID)).asKeyList();


            try{
                gtx.delete(pcbaList);
            }catch(Exception e){
                //件数が多いかなにかで削除失敗。
                //cronによるゴミデータの定期削除に任せるため、ここではno operation.
                //TODO cronによるゴミデータの定期削除
                if(logger.isLoggable(Level.SEVERE)){
                    logger.severe("fail to delete PCBA data with withdraw. user : " + loginID);
                }
            }

            //トップユーザー情報の削除
            List<Key> topUserInfoByArtistList = Datastore.query(topUserInfoByArtistMeta).filter(topUserInfoByArtistMeta.loginId.equal(loginID)).asKeyList();

            try{
                gtx.delete(topUserInfoByArtistList);
            }catch(Exception e){
                //件数が多いかなにかで削除失敗。
                //cronによるゴミデータの定期削除に任せるため、ここではno operation.
                //TODO cronによるゴミデータの定期削除
                if(logger.isLoggable(Level.SEVERE)){
                    logger.severe("fail to delete TopUserInfoByArtist with withdraw. user : " + loginID);
                }
            }

            //日記の削除
            DiaryMeta diaryMeta = new DiaryMeta();
            List<Key> diaryKeys = Datastore.query(diaryMeta).filter(diaryMeta.loginID.equal(loginID)).asKeyList();
            gtx.delete(diaryKeys);

            //メッセージの削除
            MessageMeta mmeta = new MessageMeta();
            List<Key> messageKeys = Datastore.query(mmeta).filter(mmeta.to.equal(loginID)).asKeyList();
            gtx.delete(messageKeys);

            //アクティビティの削除
            ActivityMeta activityMeta = new ActivityMeta();
            List<Key> activityKeys = Datastore.query(activityMeta).filter(activityMeta.loginID.equal(loginID)).asKeyList();
            gtx.delete(activityKeys);

            //最後に、ユーザー情報の削除
            try{
                gtx.delete(user.getKey());
                gtx.commit();
            }catch(Exception e){
                if(logger.isLoggable(Level.SEVERE)){
                    logger.severe("fail to delete User with withdraw. user : " + loginID);
                }
                outputList.add("9");//return code "9" when error occurs
                return outputList;
            }finally{
                if(gtx.isActive()){
                    gtx.rollback();
                }
            }

            //退会成功
            outputList.add("0");
            return outputList;

        }

        //退会対象のユーザーにログインできず
        outputList.add("1");
        return outputList;
    }
}
