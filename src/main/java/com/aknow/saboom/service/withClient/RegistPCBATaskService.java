package com.aknow.saboom.service.withClient;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.ArtistNameMeta;
import com.aknow.saboom.meta.PlayCountByArtistMeta;
import com.aknow.saboom.meta.RegistPCBATaskMeta;
import com.aknow.saboom.model.ArtistName;
import com.aknow.saboom.model.PlayCountByArtist;
import com.aknow.saboom.model.RegistPCBATask;
import com.aknow.saboom.util.PlayCountByArtistForTask;


public class RegistPCBATaskService {

    private static final Logger logger = Logger.getLogger(RegistPCBATaskService.class.getName());

    @SuppressWarnings("static-method")
    public void doTask(String key){

        ArrayList<String> artistNameList = new ArrayList<String>();
        RegistPCBATask tempTask = null;
        RegistPCBATaskMeta registPCBATaskMeta = RegistPCBATaskMeta.get();
        PlayCountByArtistMeta playCountByArtistMeta = PlayCountByArtistMeta.get();
        ArtistNameMeta artistNameMeta = ArtistNameMeta.get();

        tempTask = Datastore.query(registPCBATaskMeta).filter(registPCBATaskMeta.loginIDyyMMddHHmmssi.equal(key)).asSingle();

        if(tempTask == null){
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to read divided task.");
            }
            return;
        }


        //task start
        ArrayList<PlayCountByArtistForTask> playCountByArtistList = tempTask.getPlayCountByArtistList();
        String yyMMddHHmmss = tempTask.getYyMMddHHmmss();

        int size = playCountByArtistList.size();
        PlayCountByArtist tempElm = null;
        PlayCountByArtistForTask tempTaskElm = null;


        for(int i = 0; i < size; i++){

            PlayCountByArtist getElm = null;

            tempTaskElm = playCountByArtistList.get(i);

            artistNameList.add(tempTaskElm.getArtistName());

            tempElm = new PlayCountByArtist();
            tempElm.setArtistName(tempTaskElm.getArtistName());
            tempElm.setLoginID(tempTaskElm.getLoginID());
            tempElm.setPlayCount(tempTaskElm.getPlayCount());
            tempElm.setYyMMddHHmmss(tempTaskElm.getYyMMddHHmmss());

            getElm = Datastore.query(playCountByArtistMeta).filter(playCountByArtistMeta.loginID.equal(tempElm.getLoginID()), playCountByArtistMeta.artistName.equal(tempElm.getArtistName())).asSingle();

            if(getElm == null){
                //検索結果がなければ、登録
                Datastore.put(tempElm);
            }else{
                if(getElm.getYyMMddHHmmss().compareTo(yyMMddHHmmss) > 0){
                    //検索結果の方が、今回送られてきたものよりも新しければ、何もしない
                    continue;
                }else if(getElm.getYyMMddHHmmss().compareTo(yyMMddHHmmss) < 0){
                    //今回送られてきたものの方が現在登録されているものよりも新しければ、更新する
                    getElm.setPlayCount(tempElm.getPlayCount());
                    getElm.setYyMMddHHmmss(yyMMddHHmmss);
                    Datastore.put(getElm);
                }
            }
        }

        //タスク完了
        Datastore.delete(tempTask.getKey());


        //アーティスト名リストの更新
        for(int i = 0; i < artistNameList.size(); i++){
            int count = Datastore.query(artistNameMeta).filter(artistNameMeta.artistName.equal(artistNameList.get(i))).count();

            if(count == 0){
                //検索結果がなければ、登録
                ArtistName artistName = new ArtistName();
                artistName.setArtistName(artistNameList.get(i));
                Datastore.put(artistName);
            }
        }
    }
}
