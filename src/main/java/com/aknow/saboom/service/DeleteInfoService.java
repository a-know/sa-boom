package com.aknow.saboom.service;

import java.util.ArrayList;
import java.util.List;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.DiaryMeta;
import com.aknow.saboom.meta.PlayCountDataMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.User;

import com.google.appengine.api.datastore.Key;


public class DeleteInfoService {
    @SuppressWarnings("static-method")
    public PlayCountData deleteInfo(String date, String loginID){

        PlayCountDataMeta pcdMeta = new PlayCountDataMeta();
        DiaryMeta diaryMeta = new DiaryMeta();

        //まずは削除対象に紐付く日記群の削除
        List<Key> keys = Datastore.query(diaryMeta).filter(diaryMeta.loginID.equal(loginID), diaryMeta.from.equal(date)).asKeyList();
        Datastore.delete(keys);

        keys = Datastore.query(diaryMeta).filter(diaryMeta.loginID.equal(loginID), diaryMeta.to.equal(date)).asKeyList();
        Datastore.delete(keys);

        //再生回数情報の削除
        PlayCountData data = Datastore.query(pcdMeta).filter(pcdMeta.loginId.equal(loginID), pcdMeta.yyMMddHHmmss.equal(date)).asSingle();
        Datastore.delete(data.getKey());

        //アップロード回数の減算
        UserMeta userMeta = new UserMeta();
        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();
        user.setUploadCount(Integer.valueOf(user.getUploadCount().intValue() - 1));

        ArrayList<String> dataLabels = user.getRegistedDataLabel();
        dataLabels.remove(dataLabels.indexOf(date));
        user.setRegistedDataLabel(dataLabels);

        Datastore.put(user);

        return data;
    }
}
