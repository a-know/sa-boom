package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.DiaryMeta;
import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.Diary;
import com.aknow.saboom.model.User;


public class DeleteDiaryService {
    @SuppressWarnings("static-method")
    public Diary deleteDiary(String from, String to, String loginID){

        DiaryMeta meta = new DiaryMeta();
        Diary diary = Datastore.query(meta).filter(meta.loginID.equal(loginID), meta.from.equal(from), meta.to.equal(to)).asSingle();

        if(diary == null){
            diary = new Diary();
            diary.setTitle("");
        }else{
            Datastore.delete(diary.getKey());

            UserMeta userMeta = new UserMeta();
            User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();
            user.setDiaryCount(Integer.valueOf(user.getDiaryCount().intValue() - 1));
            Datastore.put(user);
        }

        return diary;
    }
}
