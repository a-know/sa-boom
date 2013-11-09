package com.aknow.saboom.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.DiaryMeta;
import com.aknow.saboom.meta.PlayCountDataMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.Activity;
import com.aknow.saboom.model.Diary;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.User;
import com.aknow.saboom.util.TwitterUtil;


public class SaveDiaryService {

    public Diary saveDiary(String from, String to, String title, String content, String loginID){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));

        //不要なタグの置き換え、ハイパーリンク化など
        String replace_title = null;
        String replace_content = null;

        replace_title = title.replaceAll("<", "&lt;");
        replace_title = replace_title.replaceAll(">", "&gt;");
        replace_content = content.replaceAll("<", "&lt;");
        replace_content = replace_content.replaceAll(">", "&gt;");

        boolean countUp = false;
        DiaryMeta meta = new DiaryMeta();
        Diary diary = Datastore.query(meta).filter(meta.loginID.equal(loginID), meta.from.equal(from), meta.to.equal(to)).asSingle();
        if(diary == null) {
            diary = new Diary();
            countUp = true;
        }

        diary.setFrom(from);
        diary.setTo(to);
        diary.setLoginID(loginID);
        diary.setSaveDate(calendar.getTime());
        diary.setTitle(replace_title);
        diary.setContent(replace_content);

        Datastore.put(diary);


        UserMeta userMeta = new UserMeta();
        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();

        if(countUp){
            user.setDiaryCount(Integer.valueOf(user.getDiaryCount().intValue() + 1));
            Datastore.put(user);
        }

        //アクティビティの登録
        HashMap<Object, Object> activityInfo = new HashMap<Object, Object>();
        Activity activity = new Activity();

        activityInfo.put("loginID", loginID);
        if(from.equals(to)){
            activity.setActivityCode("5");//通常日記
            activityInfo.put("label", getDataLabel(loginID, from));
        }else{
            activity.setActivityCode("6");//サブーン日記
            activityInfo.put("label_from", getDataLabel(loginID, from));
            activityInfo.put("label_to", getDataLabel(loginID, to));
        }
        Date uploadDate = calendar.getTime();

        activity.setActivityDate(uploadDate);
        activity.setActivityInfo(activityInfo);
        activity.setLoginID(loginID);
        if(user.getIsPrivate().booleanValue()){
            activity.setViewable(Boolean.FALSE);
        }else{
            activity.setViewable(Boolean.TRUE);
        }

        Datastore.put(activity);


        //twitter連携
        if(!user.getIsPrivate().booleanValue() && user.getTwitterAccessToken() != null && !user.getTwitterAccessToken().equals("") && user.getIsTweetOption4().booleanValue()){
            if(from.equals(to)){
                String tweetContents = "再生回数情報『" + activityInfo.get("label") + "』に対して、日記を書いたよ！　#saboom http://sa-boom.appspot.com/user/" + user.getLoginId();
                new TwitterUtil().doTweet(tweetContents, user);
            }
        }

        if(!user.getIsPrivate().booleanValue() && user.getTwitterAccessToken() != null && !user.getTwitterAccessToken().equals("") && user.getIsTweetOption5().booleanValue()){
            if(!from.equals(to)){
                String tweetContents = "再生回数情報『" + activityInfo.get("label_from") + "』から『" + activityInfo.get("label_to") + "』に対して、差分日記を書いたよ！　#saboom http://sa-boom.appspot.com/user/" + user.getLoginId();
                new TwitterUtil().doTweet(tweetContents, user);
            }
        }


        return diary;
    }

    @SuppressWarnings("static-method")
    private String getDataLabel(String loginID, String yyMMddHHmmss){
        PlayCountDataMeta meta = new PlayCountDataMeta();
        PlayCountData data = Datastore.query(meta).filter(meta.loginId.equal(loginID), meta.yyMMddHHmmss.equal(yyMMddHHmmss)).asSingle();
        return data.getInfoName();
    }
}
