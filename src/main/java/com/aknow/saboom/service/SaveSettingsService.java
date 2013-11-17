package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.User;


public class SaveSettingsService {

    @SuppressWarnings("static-method")
    public User saveSettings(String loginID, String  url, String introduction, String pm,
            String tw_option1, String tw_option2, String tw_option3, String tw_option4, String tw_option5){

        UserMeta meta = new UserMeta();

        User user = Datastore.query(meta).filter(meta.loginId.equal(loginID)).asSingle();

        //不要なタグの置き換え、ハイパーリンク化など
        String replace_url = null;
        String replace_introduction = null;

        replace_url = url.replaceAll("<", "&lt;");
        replace_url = replace_url.replaceAll(">", "&gt;");
        replace_introduction = introduction.replaceAll("<", "&lt;");
        replace_introduction = replace_introduction.replaceAll(">", "&gt;");

        user.setUrl(replace_url);
        user.setIntroduction(replace_introduction);

        if("true".equals(pm)){
            user.setIsPrivate(Boolean.TRUE);
        }else{
            user.setIsPrivate(Boolean.FALSE);
        }

        if("true".equals(tw_option1)){
            user.setIsTweetOption1(Boolean.TRUE);
        }else{
            user.setIsTweetOption1(Boolean.FALSE);
        }

        if("true".equals(tw_option2)){
            user.setIsTweetOption2(Boolean.TRUE);
        }else{
            user.setIsTweetOption2(Boolean.FALSE);
        }

        if("true".equals(tw_option3)){
            user.setIsTweetOption3(Boolean.TRUE);
        }else{
            user.setIsTweetOption3(Boolean.FALSE);
        }

        if("true".equals(tw_option4)){
            user.setIsTweetOption4(Boolean.TRUE);
        }else{
            user.setIsTweetOption4(Boolean.FALSE);
        }

        if("true".equals(tw_option5)){
            user.setIsTweetOption5(Boolean.TRUE);
        }else{
            user.setIsTweetOption5(Boolean.FALSE);
        }

        Datastore.put(user);

        return user;
    }
}
