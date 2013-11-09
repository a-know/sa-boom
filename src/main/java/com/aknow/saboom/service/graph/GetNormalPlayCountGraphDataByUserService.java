package com.aknow.saboom.service.graph;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.slim3.datastore.Datastore;

import com.aknow.saboom.meta.AmazonApiDataMeta;
import com.aknow.saboom.meta.PlayCountDataMeta;
import com.aknow.saboom.model.AmazonApiData;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.graph.NormalPlayCountGraphByUser;
import com.aknow.saboom.service.UserViewService;
import com.aknow.saboom.util.RankingMethod;
import com.google.appengine.api.blobstore.BlobstoreInputStream;


public class GetNormalPlayCountGraphDataByUserService {
    @SuppressWarnings({ "unchecked", "static-method" })
    public ArrayList<NormalPlayCountGraphByUser> getData(String targetLoginID, String normalViewValue) throws IOException, ClassNotFoundException{

        PlayCountDataMeta meta = new PlayCountDataMeta();

        PlayCountData data = Datastore.query(meta).filter(meta.loginId.equal(targetLoginID), meta.yyMMddHHmmss.equal(normalViewValue)).asSingle();


        //blobstoreからの情報の取得
        InputStream is = new BlobstoreInputStream(data.getBlobListKey());
        ZipInputStream zip_in = new ZipInputStream(is);
        zip_in.getNextEntry();
        ObjectInputStream ois = new ObjectInputStream(zip_in);
        ArrayList<Object> dataList = (ArrayList<Object>) ois.readObject();

        //曲別
        ArrayList<Object> sortedListBySongPlayCount = (ArrayList<Object>) dataList.get(0);
        //アルバム別
        //LinkedHashMap<String, Object> sortedListByAlbumPlayCount = (LinkedHashMap<String, Object>) dataList.get(1);
        //アーティスト別
        LinkedHashMap<String, Object> sortedListByArtistPlayCount = (LinkedHashMap<String, Object>) dataList.get(2);

        //単一ランキング情報の取り出し
        ArrayList<NormalPlayCountGraphByUser> resultList = RankingMethod.editNormalRankingInfoBySong(sortedListBySongPlayCount);
        resultList = RankingMethod.editNormalRankingInfoByArtist(sortedListByArtistPlayCount, resultList);
        
        ois.close();

        return resultList;
    }

    public ArrayList<NormalPlayCountGraphByUser> getAmazonData(ArrayList<NormalPlayCountGraphByUser> list){
        ArrayList<NormalPlayCountGraphByUser> returnList = new ArrayList<NormalPlayCountGraphByUser>();
        UserViewService service = new UserViewService();

        //imageURL,AmazonURLを、まずはdatastoreから取得。なければAPIで取得
        ArrayList<LinkedHashMap<String, String>> datastoreData = getApiData(list);
        ArrayList<LinkedHashMap<String, String>> apiData = service.getDataFromApi(datastoreData.get(0), datastoreData.get(1));

        LinkedHashMap<String, String> urlTop10Artist = apiData.get(0);
        LinkedHashMap<String, String> imagesTop10Artist = apiData.get(1);

        Set<Entry<String, String>> entrySet = urlTop10Artist.entrySet();

        int index = 0;
        ArrayList<NormalPlayCountGraphByUser> tmpList = new ArrayList<NormalPlayCountGraphByUser>();
        for(Entry<String, String> entry : entrySet){
            NormalPlayCountGraphByUser tmp = list.get(index);
            tmp.setUrl_byArtist(entry.getValue());
            tmpList.add(tmp);
            index++;
        }

        entrySet = imagesTop10Artist.entrySet();

        index = 0;
        for(Entry<String, String> entry : entrySet){
            NormalPlayCountGraphByUser tmp = tmpList.get(index);
            tmp.setImageUrl_byArtist(entry.getValue());
            returnList.add(tmp);
            index++;
        }

        return returnList;
    }

    @SuppressWarnings("static-method")
    public ArrayList<LinkedHashMap<String, String>> getApiData(ArrayList<NormalPlayCountGraphByUser> list){
        LinkedHashMap<String, String> imagesTop10Artist = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> urlTop10Artist = new LinkedHashMap<String, String>();
        AmazonApiDataMeta meta = new AmazonApiDataMeta();

        for(NormalPlayCountGraphByUser e : list){
            List<AmazonApiData> data = Datastore.query(meta).filter(meta.artistName.equal(e.getArtistName_byArtist())).asList();
            if(data.size() == 0){
                imagesTop10Artist.put(e.getArtistName_byArtist(), null);
                urlTop10Artist.put(e.getArtistName_byArtist(),null);
            }else{
                imagesTop10Artist.put(e.getArtistName_byArtist(), data.get(0).getImageUrl());
                urlTop10Artist.put(e.getArtistName_byArtist(),data.get(0).getUrl());
            }
        }

        ArrayList<LinkedHashMap<String, String>> returnList = new ArrayList<LinkedHashMap<String, String>>();
        returnList.add(urlTop10Artist);
        returnList.add(imagesTop10Artist);

        return returnList;
    }
}
