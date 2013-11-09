package com.aknow.saboom.service;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.DiaryMeta;
import com.aknow.saboom.model.Diary;


public class LoadDiaryService {
    @SuppressWarnings("static-method")
    public Diary loadDiary(String from, String to, String loginID){

        DiaryMeta meta = new DiaryMeta();
        Diary diary = Datastore.query(meta).filter(meta.loginID.equal(loginID), meta.from.equal(from), meta.to.equal(to)).asSingle();

        if(diary == null){
            diary = new Diary();
            diary.setTitle("");
        }

        return diary;
    }

}
