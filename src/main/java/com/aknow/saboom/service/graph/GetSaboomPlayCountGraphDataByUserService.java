package com.aknow.saboom.service.graph;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.zip.ZipInputStream;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.PlayCountDataMeta;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.graph.SaboomPlayCountGraphByUser;
import com.aknow.saboom.util.RankingMethod;

import com.google.appengine.api.blobstore.BlobstoreInputStream;


public class GetSaboomPlayCountGraphDataByUserService {
    @SuppressWarnings({ "unchecked", "static-method" })
    public ArrayList<SaboomPlayCountGraphByUser> getData(String loginID, String from, String to) throws IOException, ClassNotFoundException{

        PlayCountDataMeta meta = new PlayCountDataMeta();

        //まずはサブーン元情報の取得----------------------------------------------------------------------
        PlayCountData data = Datastore.query(meta).filter(meta.loginId.equal(loginID), meta.yyMMddHHmmss.equal(from)).asSingle();
        //blobstoreからの情報の取得
        InputStream is = new BlobstoreInputStream(data.getBlobListKey());
        ZipInputStream zip_in = new ZipInputStream(is);
        zip_in.getNextEntry();
        ObjectInputStream ois = new ObjectInputStream(zip_in);
        ArrayList<Object> dataListFrom = (ArrayList<Object>) ois.readObject();

        //曲別
        ArrayList<Object> sortedListBySongPlayCountFrom = (ArrayList<Object>) dataListFrom.get(0);
        //アルバム別
        //LinkedHashMap<String, Object> sortedListByAlbumPlayCountFrom = (LinkedHashMap<String, Object>) dataListFrom.get(1);
        //アーティスト別
        LinkedHashMap<String, Object> sortedListByArtistPlayCountFrom = (LinkedHashMap<String, Object>) dataListFrom.get(2);
        //----------------------------------------------------------------------


        //続いてサブーン先情報の取得----------------------------------------------------------------------
        data = Datastore.query(meta).filter(meta.loginId.equal(loginID), meta.yyMMddHHmmss.equal(to)).asSingle();
        //blobstoreからの情報の取得
        is = new BlobstoreInputStream(data.getBlobListKey());
        zip_in = new ZipInputStream(is);
        zip_in.getNextEntry();
        ois = new ObjectInputStream(zip_in);
        ArrayList<Object> dataListTo = (ArrayList<Object>) ois.readObject();

        //曲別
        ArrayList<Object> sortedListBySongPlayCountTo = (ArrayList<Object>) dataListTo.get(0);
        //アルバム別
        //LinkedHashMap<String, Object> sortedListByAlbumPlayCountTo = (LinkedHashMap<String, Object>) dataListTo.get(1);
        //アーティスト別
        LinkedHashMap<String, Object> sortedListByArtistPlayCountTo = (LinkedHashMap<String, Object>) dataListTo.get(2);
        //----------------------------------------------------------------------


        //サブーンランキング情報の取り出し
        ArrayList<SaboomPlayCountGraphByUser> resultList = RankingMethod.editSabunRankingInfoBySong(sortedListBySongPlayCountFrom, sortedListBySongPlayCountTo);
        resultList = RankingMethod.editSabunRankingInfoByArtist(sortedListByArtistPlayCountFrom, sortedListByArtistPlayCountTo, resultList);
        resultList = RankingMethod.editHikakuInfoBySong(sortedListBySongPlayCountFrom, sortedListBySongPlayCountTo, resultList);
        resultList = RankingMethod.editHikakuInfoByArtist(sortedListByArtistPlayCountFrom, sortedListByArtistPlayCountTo, resultList);

        ois.close();
        
        return resultList;
    }
}
