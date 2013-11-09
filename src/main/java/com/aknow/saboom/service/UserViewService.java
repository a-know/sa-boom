package com.aknow.saboom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slim3.datastore.Datastore;
import org.slim3.util.DateUtil;

import com.aknow.saboom.meta.ActivityMeta;
import com.aknow.saboom.meta.AmazonApiDataMeta;
import com.aknow.saboom.meta.DiaryMeta;
import com.aknow.saboom.meta.MessageMeta;
import com.aknow.saboom.meta.PlayCountByArtistMeta;
import com.aknow.saboom.meta.PlayCountDataMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.Activity;
import com.aknow.saboom.model.AmazonApiData;
import com.aknow.saboom.model.Diary;
import com.aknow.saboom.model.Message;
import com.aknow.saboom.model.PlayCountByArtist;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.User;
import com.aknow.saboom.util.AmazonHelper;
import com.aknow.saboom.util.FreshPub;

import com.google.appengine.api.datastore.KeyFactory;


public class UserViewService {

    static final UserMeta userMeta = UserMeta.get();
    static final PlayCountByArtistMeta pcbaMeta = PlayCountByArtistMeta.get();

    @SuppressWarnings("static-method")
    public User getUser(String loginID){
        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();
        return user;
    }

    @SuppressWarnings("static-method")
    public HashMap<String, Integer> getPlayCountTop10Artist(User user){

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for(String e : user.getTop10ArtistDataList()){
            PlayCountByArtist pcba = Datastore.query(pcbaMeta).filter(pcbaMeta.loginID.equal(user.getLoginId()), pcbaMeta.artistName.equal(e)).asSingle();
            if(pcba == null){
                map.put(e, new Integer(0));
            }else{
                map.put(e, pcba.getPlayCount());
            }
        }
        return map;
    }

    @SuppressWarnings("static-method")
    public ArrayList<LinkedHashMap<String, String>> getApiData(User user){
        LinkedHashMap<String, String> imagesTop10Artist = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> urlTop10Artist = new LinkedHashMap<String, String>();
        AmazonApiDataMeta meta = new AmazonApiDataMeta();

        for(String e : user.getTop10ArtistDataList()){
            List<AmazonApiData> data = Datastore.query(meta).filter(meta.artistName.equal(e)).asList();
            if(data.size() == 0){
                imagesTop10Artist.put(e, null);
                urlTop10Artist.put(e,null);
            }else{
                imagesTop10Artist.put(e, data.get(0).getImageUrl());
                urlTop10Artist.put(e,data.get(0).getUrl());
            }
        }

        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
        list.add(urlTop10Artist);
        list.add(imagesTop10Artist);

        return list;
    }



    @SuppressWarnings("static-method")
    public ArrayList<LinkedHashMap<String, String>> getDataFromApi(LinkedHashMap<String, String> urlMap, LinkedHashMap<String, String> imageUrlMap){

        String separater = "@@##@@";
        StringBuffer artistBuffer = new StringBuffer(separater);


        for(Map.Entry<String, String> e : urlMap.entrySet()) {
            if(e.getValue() == null){
                artistBuffer.append(e.getKey());
                artistBuffer.append(separater);
            }
        }

        AmazonHelper helper = new AmazonHelper(artistBuffer.toString());
        List<FreshPub> freshPubs = null;

        try{
            freshPubs = helper.getFreshPubs();
        }catch(Exception e){
            throw new RuntimeException(e);
        }


        for(FreshPub e : freshPubs){
            if(urlMap.get(e.getArtist()) == null){
                urlMap.put(e.getArtist(), e.getUrl());
                imageUrlMap.put(e.getArtist(), e.getImageUrl());
                //取得できたデータのdatastoreへの登録
                AmazonApiData data = new AmazonApiData();
                data.setArtistName(e.getArtist());
                data.setUrl(e.getUrl());
                data.setImageUrl(e.getImageUrl());
                Datastore.put(data);
            }
        }

        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
        list.add(urlMap);
        list.add(imageUrlMap);

        return list;
    }


    @SuppressWarnings("static-method")
    public ArrayList<Activity> getUserActivity(String loginID) {
        ActivityMeta meta = ActivityMeta.get();
        return (ArrayList<Activity>) Datastore.query(meta).filter(meta.loginID.equal(loginID)).sort(meta.activityDate.desc).limit(10).asList();
    }


    @SuppressWarnings("static-method")
    public ArrayList<ArrayList<String>> getUserPlayCountData(String loginID){
        ArrayList<String> dataLabelList = new ArrayList<String>();
        ArrayList<String> dataValueList = new ArrayList<String>();

        PlayCountDataMeta meta = new PlayCountDataMeta();
        List<PlayCountData> list = Datastore.query(meta).filter(meta.loginId.equal(loginID)).sort(meta.yyMMddHHmmss.asc).asList();

        for(PlayCountData e : list){
            dataLabelList.add(e.getInfoName());
            dataValueList.add(e.getYyMMddHHmmss());
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(dataLabelList);
        result.add(dataValueList);

        return result;
    }

    @SuppressWarnings("static-method")
    public ArrayList<ArrayList<String>> getMessageData(String loginID){
        ArrayList<String> messageLabelList = new ArrayList<String>();
        ArrayList<String> messageValueList = new ArrayList<String>();

        MessageMeta meta = new MessageMeta();
        List<Message> list = Datastore.query(meta).filter(meta.to.equal(loginID)).sort(meta.sendDate.desc).asList();

        StringBuffer bf = null;
        String pattern = "yyyy/MM/dd HH:mm";

        for(Message e : list){
            bf = new StringBuffer();
            bf.append(e.getSender());
            bf.append("さんから（");
            bf.append(DateUtil.toString(e.getSendDate(),pattern));
            bf.append(" に送信）");
            messageLabelList.add(bf.toString());
            messageValueList.add(KeyFactory.keyToString(e.getKey()));
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        result.add(messageLabelList);
        result.add(messageValueList);

        return result;
    }

    @SuppressWarnings("static-method")
    public Map<String, Integer> getInfomationData(String loginID){
        Map<String, Integer> map = new HashMap<String, Integer>();


        MessageMeta mmeta = new MessageMeta();
        int unreadMessageCount = Datastore.query(mmeta).filter(mmeta.to.equal(loginID), mmeta.isUnread.equal(Boolean.TRUE)).count();

        //総おしらせ件数を計算（将来的には友だち申請数も加算したり？）
        int totalInfomationCount = unreadMessageCount;

        map.put("totalInfomationCount", Integer.valueOf(totalInfomationCount));
        map.put("unreadMessageCount", Integer.valueOf(unreadMessageCount));

        return map;
    }

    @SuppressWarnings("static-method")
    public Integer getAccessCountWithoutCountUp(String loginID){


        UserMeta meta = new UserMeta();
        Integer accessCount = Datastore.query(meta).filter(meta.loginId.equal(loginID)).asSingle().getAccessCount();

        return accessCount;
    }

    @SuppressWarnings("static-method")
    public Integer getAccessCountWithCountUp(String loginID){


        UserMeta meta = new UserMeta();
        User user = Datastore.query(meta).filter(meta.loginId.equal(loginID)).asSingle();

        int accessCount = user.getAccessCount().intValue();
        accessCount++;

        user.setAccessCount(Integer.valueOf(accessCount));
        Datastore.put(user);

        return Integer.valueOf(accessCount);
    }

    @SuppressWarnings("static-method")
    public List<Diary> getDiaryList(String loginID){
        DiaryMeta meta = new DiaryMeta();
        return Datastore.query(meta).filter(meta.loginID.equal(loginID)).sort(meta.saveDate.desc).limit(5).asList();
    }

    public List<HashMap<String, Object>> getDiaryLabelList(String loginID, List<Diary> diaryList){
        List<HashMap<String, Object>> returnList = new ArrayList<HashMap<String, Object>>();

        for(Diary d : diaryList){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("from", getDataLabel(loginID, d.getFrom()));
            map.put("to", getDataLabel(loginID, d.getTo()));
            map.put("title", d.getTitle());
            map.put("date", d.getSaveDate());
            returnList.add(map);
        }

        return returnList;
    }

    @SuppressWarnings("static-method")
    private String getDataLabel(String loginID, String yyMMddHHmmss){
        PlayCountDataMeta meta = new PlayCountDataMeta();
        PlayCountData data = Datastore.query(meta).filter(meta.loginId.equal(loginID), meta.yyMMddHHmmss.equal(yyMMddHHmmss)).asSingle();
        return data.getInfoName();
    }
}
