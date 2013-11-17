package com.aknow.saboom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.aknow.saboom.model.graph.NormalPlayCountGraphByUser;
import com.aknow.saboom.model.graph.SaboomPlayCountGraphByUser;
import cdit.ElementForWeb;



public class RankingMethod {

    private static final String EMPTY_STRING = "";
    private static final String BLANK_STRING = " ";
    private static final String NO_NAME_ARTIST = "No Name Artist";
    private static final String NO_NAME_ALBUM = "No Name Album";
    private static final String NO_NAME_KAIHI = "No Name";
    private static final String STAR_STRING = "★";
    private static final int limitSize_song = 11;	//Web版専用，打ち切り位置の指定
    private static final int limitSize_artist = 10;	//Web版専用，打ち切り位置の指定
    private static final String COUNT_TAG = "Play Count";
    private static final String MOD_CNT = "modcnt";
    private static final String RATING_TAG = "Rating";
    private static final String BOU_STRING = "|";
    //private static final String ONE_STRING = "1";
    //private static final String ENCODING = "UTF-8";
    private static final String imageNew = "new";
    private static final String imageUp = "up";
    private static final String imageDown = "down";
    private static final String imageStay = "stay";

    public static ArrayList<NormalPlayCountGraphByUser> editNormalRankingInfoBySong(ArrayList<Object> sortedListBySongPlayCount) {

        //返却用リスト
        ArrayList<NormalPlayCountGraphByUser> rankingList = new ArrayList<NormalPlayCountGraphByUser>();
        //		//レート情報編集用文字列
        //		String rate = null;
        //取り出した各エレメント
        ElementForWeb tempElement = null;

        //前回処理エレメントの再生回数と順位を保持する変数
        int preCnt = -9999;
        int preRank = -9999;
        //ArrayListのサイズ分、もしくは100回、編集処理を繰り返し
        int size = sortedListBySongPlayCount.size() + 1;
        if(size > limitSize_song) size = limitSize_song;
        for (int i = 1; i < size; i++){
            //ランキングの各項目を入れるモデル。
            //0番目の要素：本当のランク。（同順位などもあり得るので）
            //1番目の要素：曲名
            //2番目の要素：アーティスト名
            //3番目の要素：レート情報
            //4番目の要素：再生回数
            NormalPlayCountGraphByUser model = new NormalPlayCountGraphByUser();
            //rate = EMPTY_STRING;
            //ArrayListから現在番目のObjectを取り出し、Elementとしてキャスト
            tempElement = (ElementForWeb)sortedListBySongPlayCount.get(i-1);

            //ランクをセット。
            //ただし、同再生回数の場合は順位を同じくする。
            if(preCnt == tempElement.getPlayCount().intValue()){
                model.setRank_bySong(Integer.valueOf(preRank));
            }else{
                model.setRank_bySong(Integer.valueOf(i));
                preRank = i;
                preCnt = tempElement.getPlayCount().intValue();
            }

            model.setSongName_bySong(tempElement.getSongName());
            model.setArtist_bySong(tempElement.getArtistName());

            //レートの★表現を作るためのカウント変数を作成。
            //レートは0～100（曲単位ならば20刻み）のため、20で割った商を
            //繰り返し回数とする。
            int rateLoopCnt = tempElement.getRating().intValue() / 20;

            StringBuffer rate = new StringBuffer();
            //ループカウントの回数だけ★を追加。
            for (int j = 0; j < rateLoopCnt; j++){
                rate.append(STAR_STRING);
            }

            //できた★の連結文字列をレートとしてセット。
            model.setRate_bySong(rate.toString());

            //再生回数をセット
            model.setPlaycount_bySong(tempElement.getPlayCount());

            //最後に，編集したリストをレコードとして返却用リストにセット
            rankingList.add(i - 1,model);
        }
        return rankingList;
    }

    /**
     * アーティスト別ランキング情報の編集を行うメソッド。
     * 「アーティスト名－Map」のLinkedHashMapを受け取り、テーブルに編集する。
     * LinkedHashMapであるため、「格納順＝順位順」。
     * @param sortedMapByArtistPlayCount
     */
    @SuppressWarnings("rawtypes")
    public static ArrayList<NormalPlayCountGraphByUser> editNormalRankingInfoByArtist(LinkedHashMap<String, Object> sortedMapByArtistPlayCount, ArrayList<NormalPlayCountGraphByUser> inputList) {

        //返却用リスト
        ArrayList<NormalPlayCountGraphByUser> rankingList = new ArrayList<NormalPlayCountGraphByUser>();
        //インデックス
        int index = 0;

        String name = null;
        Map<?, ?> valMap = null;
        StringBuffer rateStar = null;

        //LinkedHashMapのサイズ分、テーブル編集処理を繰り返す
        Iterator<?> sortArtMapIt = sortedMapByArtistPlayCount.entrySet().iterator();

        //画面に返すサイズを決定
        int size = sortedMapByArtistPlayCount.size();
        if(size > limitSize_artist) size = limitSize_artist;

        int rnk = 1;
        //前回順位・再生回数を保持する変数
        int preRnk = -9999;
        int preCnt = -9999;
        while (sortArtMapIt.hasNext()) {

            //制限サイズに来てたら抜ける
            if (index == size) break;

            //0番目の要素：本当のランク。（同順位などもあり得るので）
            //1番目の要素：アーティスト名
            //2番目の要素：空き（他のリストと位置関係を合わせるため）
            //3番目の要素：レート情報
            //4番目の要素：再生回数
            NormalPlayCountGraphByUser model = inputList.get(index);

            Map.Entry mapEntry = (Map.Entry) sortArtMapIt.next();
            //キー名称＝アーティスト名を取得
            name = (String)mapEntry.getKey();
            //アーティスト毎の統計情報マップ
            valMap = (Map<?, ?>)mapEntry.getValue();

            //現在のアーティストの合計再生回数の取得
            int thisCnt = Integer.parseInt(valMap.get(COUNT_TAG).toString());
            //現在のアーティストの合計レートを取得
            int ratesum = ((Integer)valMap.get(RATING_TAG)).intValue();
            //合計レートから平均レートを算出
            int rateavg = ratesum / ((Integer)valMap.get(MOD_CNT)).intValue();

            //平均レートから★文字列を作成
            int rateLoop = rateavg / 16;
            if(rateLoop > 5) rateLoop = 5;
            rateStar = new StringBuffer();
            for (int i = 0; i < rateLoop; i++) rateStar.append(STAR_STRING);
            rateStar.append(BLANK_STRING);
            rateStar.append(rateavg);

            //ランキングテーブルの各情報をセットする。
            //ただし、前順位と再生回数が同じ場合、同順位とする。
            if (preCnt == thisCnt){
                model.setRank_byArtist(Integer.valueOf(preRnk));
                rnk++;
            }else{
                preRnk = rnk;
                model.setRank_byArtist(Integer.valueOf(rnk++));
                preCnt = thisCnt;
            }


            if(name.indexOf(BOU_STRING) == -1){
                model.setArtistName_byArtist(name);
            }else{
                model.setArtistName_byArtist(name.substring(0,name.indexOf(BOU_STRING)));
            }
            model.setRate_byArtist(rateStar.toString());
            model.setPlaycount_byArtist(Integer.valueOf(thisCnt));

            //最後に，編集したリストをレコードとして返却用リストにセット
            rankingList.add(index++,model);
        }
        return rankingList;
    }

    /**
     * 差分元と差分先の曲別リストを受け取り、その差分情報を作成する。
     */
    @SuppressWarnings("rawtypes")
    public static ArrayList<SaboomPlayCountGraphByUser> editSabunRankingInfoBySong(ArrayList<?> fromList, ArrayList<?> toList) {
        //差分元情報の取得
        int moto_size = fromList.size();
        ElementForWeb tempElement = null;
        ElementForWeb updateElement = null;
        HashMap<String, ElementForWeb> motoMap = new HashMap<String, ElementForWeb>();
        String tempkey = null;

        //重複回避のためのロジック・ここから
        for(int i = 0; i < moto_size; i++){
            tempElement = (ElementForWeb) fromList.get(i);
            if(tempElement.getArtistName().equals(NO_NAME_ARTIST) || tempElement.getArtistName().equals(NO_NAME_ALBUM)){
                tempkey = tempElement.getSongName() + BOU_STRING + NO_NAME_KAIHI + BOU_STRING + tempElement.getAlbumName();
            }else{
                tempkey = tempElement.getSongName() + BOU_STRING + tempElement.getArtistName() + BOU_STRING + tempElement.getAlbumName();
            }
            if(motoMap.containsKey(tempkey)){//全て一緒のときは合算
                updateElement = motoMap.get(tempkey);
                tempElement.setPlayCount(Integer.valueOf((updateElement.getPlayCount()).intValue() + (tempElement.getPlayCount()).intValue()));
                motoMap.put(tempkey,tempElement);
            }else{
                motoMap.put(tempkey,tempElement);
            }
        }

        //差分先情報の取得
        int saki_size = toList.size();
        HashMap<String, ElementForWeb> sakiMap = new HashMap<String, ElementForWeb>();

        for(int i = 0; i < saki_size; i++){
            tempElement = (ElementForWeb) toList.get(i);
            if(tempElement.getArtistName().equals(NO_NAME_ARTIST) || tempElement.getArtistName().equals(NO_NAME_ALBUM)){
                tempkey = tempElement.getSongName() + BOU_STRING + NO_NAME_KAIHI + BOU_STRING + tempElement.getAlbumName();
            }else{
                tempkey = tempElement.getSongName() + BOU_STRING + tempElement.getArtistName() + BOU_STRING + tempElement.getAlbumName();
            }
            if(sakiMap.containsKey(tempkey)){
                updateElement = sakiMap.get(tempkey);
                tempElement.setPlayCount(Integer.valueOf((updateElement.getPlayCount()).intValue() + (tempElement.getPlayCount()).intValue()));
                sakiMap.put(tempkey,tempElement);
            }else{
                sakiMap.put(tempkey,tempElement);
            }
        }
        //重複回避のためのロジック・ここまで



        //差分情報の作成
        Iterator<?> sakiMapIt = sakiMap.entrySet().iterator();
        Map.Entry mapEntry = null;
        String songName = null;
        ElementForWeb motoElement = null;
        ArrayList<ElementForWeb> beforeSortList = new ArrayList<ElementForWeb>();

        while (sakiMapIt.hasNext()) {
            mapEntry = (Map.Entry) sakiMapIt.next();
            songName = (String)mapEntry.getKey();
            tempElement = (ElementForWeb)mapEntry.getValue();

            motoElement = motoMap.get(songName);
            if(motoElement == null){//差分先マップに入っている曲名で差分元から取得できない＝その期間に新規追加された曲の場合
                beforeSortList.add(tempElement);
            }else{
                tempElement.setPlayCount(new Integer(tempElement.getPlayCount().intValue() - motoElement.getPlayCount().intValue()));
                beforeSortList.add(tempElement);
            }
        }

        //再生回数の差分リストを並び替え
        ArrayList<SaboomPlayCountGraphByUser> sortedListBySongPlayCountForSabun = new ArrayList<SaboomPlayCountGraphByUser>();
        SaboomPlayCountGraphByUser valueElm = null;
        int maxCnt = -9999;
        int maxIndex = -9999;
        int i = 0;
        int count = 0;
        int size = limitSize_song;

        if(beforeSortList.size() < limitSize_song) size = beforeSortList.size();

        //while(beforeSortList.size() > 0){
        while(count < size){//Web版は作成順位を抑える
            //ランキングの各項目を入れる要素。
            //0番目の要素：本当のランク。（同順位などもあり得るので）
            //1番目の要素：曲名
            //2番目の要素：アーティスト名
            //3番目の要素：レート情報
            //4番目の要素：再生回数
            valueElm = new SaboomPlayCountGraphByUser();

            maxCnt = -9999;
            maxIndex = -9999;
            for(i = 0; i < beforeSortList.size(); i++){

                tempElement = beforeSortList.get(i);

                if(tempElement.getPlayCount().intValue() > maxCnt){
                    maxCnt = tempElement.getPlayCount().intValue();
                    maxIndex = i;
                }
            }

            count++;
            ElementForWeb temp = beforeSortList.get(maxIndex);



            //レートの★表現を作るためのカウント変数を作成。
            //レートは0～100（曲単位ならば20刻み）のため、20で割った商を
            //繰り返し回数とする。
            int rateLoopCnt = temp.getRating().intValue() / 20;

            StringBuffer rate = new StringBuffer();
            //ループカウントの回数だけ★を追加。
            for (int j = 0; j < rateLoopCnt; j++){
                rate.append(STAR_STRING);
            }


            //ランキングの各項目を格納
            valueElm.setRank_bySong(Integer.valueOf(count));
            valueElm.setSongName_bySong(temp.getSongName());
            valueElm.setArtist_bySong(temp.getArtistName());
            valueElm.setRate_bySong(rate.toString());
            valueElm.setPlaycount_bySong(temp.getPlayCount());

            beforeSortList.remove(maxIndex);

            sortedListBySongPlayCountForSabun.add(valueElm);
        }

        return sortedListBySongPlayCountForSabun;
    }

    /**
     * 差分元と差分先のアーティスト別エレメントマップを受け取り、その差分情報を作成する。
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ArrayList<SaboomPlayCountGraphByUser> editSabunRankingInfoByArtist(LinkedHashMap<String, Object> fromList, LinkedHashMap<String, Object> toList, ArrayList<SaboomPlayCountGraphByUser> resultList) {

        ArrayList<SaboomPlayCountGraphByUser> returnList = new ArrayList<SaboomPlayCountGraphByUser>();

        Iterator<?> sakiMapIt = toList.entrySet().iterator();
        Map.Entry mapEntry = null;
        String artistName = null;
        HashMap<String, Integer> tempMap = null;
        HashMap<?, ?> motoMap = null;
        HashMap<String, HashMap<String, Integer>> beforeSortMap = new HashMap<String, HashMap<String, Integer>>();

        while (sakiMapIt.hasNext()) {
            mapEntry = (Map.Entry) sakiMapIt.next();
            artistName = (String)mapEntry.getKey();
            tempMap = (HashMap<String, Integer>)mapEntry.getValue();

            motoMap = (HashMap<?, ?>)fromList.get(artistName);

            if(motoMap == null){
                beforeSortMap.put(artistName,tempMap);
            }else{
                tempMap.put(COUNT_TAG,Integer.valueOf(tempMap.get(COUNT_TAG).intValue() - ((Integer)motoMap.get(COUNT_TAG)).intValue()));
                beforeSortMap.put(artistName,tempMap);
            }
        }

        //beforeSortMap（アルバム名（区切り文字つき）－hashmap）をLinkedHashMapに並び替える処理

        SaboomPlayCountGraphByUser valueElm = null;

        Iterator<?> beforeSortMapIt = null;
        String maxName = null;
        HashMap<?, ?> temp = null;

        int size = limitSize_artist;

        if(beforeSortMap.size() < limitSize_artist) size = beforeSortMap.size();


        for(int i = 0;i < size;){//Web版はランキング回数を抑える

            //0番目の要素：本当のランク。（同順位などもあり得るので）
            //1番目の要素：アーティスト名
            //2番目の要素：空き（他のリストと位置関係を合わせるため）
            //3番目の要素：レート情報
            //4番目の要素：再生回数
            valueElm = resultList.get(i);

            beforeSortMapIt = beforeSortMap.entrySet().iterator();
            int maxCnt = -9999;
            maxName = EMPTY_STRING;

            while (beforeSortMapIt.hasNext()) {
                mapEntry = (Map.Entry) beforeSortMapIt.next();
                artistName = (String)mapEntry.getKey();
                tempMap = (HashMap<String, Integer>)mapEntry.getValue();
                if(tempMap.get(COUNT_TAG).intValue() > maxCnt){
                    maxCnt = tempMap.get(COUNT_TAG).intValue();
                    maxName = artistName;
                }
            }
            if(!maxName.equals(EMPTY_STRING)){

                temp = beforeSortMap.get(maxName);


                //現在のアーティストの合計レートを取得
                int ratesum = ((Integer)temp.get(RATING_TAG)).intValue();
                //合計レートから平均レートを算出
                int rateavg = ratesum / ((Integer)temp.get(MOD_CNT)).intValue();
                //平均レートから★文字列を作成
                int rateLoop = rateavg / 16;
                if(rateLoop > 5) rateLoop = 5;
                StringBuffer rateStar = new StringBuffer();
                for (int j = 0; j < rateLoop; j++) rateStar.append(STAR_STRING);
                rateStar.append(BLANK_STRING);
                rateStar.append(rateavg);


                valueElm.setRank_byArtist(Integer.valueOf(i + 1));
                valueElm.setArtistName_byArtist(maxName);
                valueElm.setRate_byArtist(rateStar.toString());
                valueElm.setPlaycount_byArtist((Integer) temp.get(COUNT_TAG));


                returnList.add(valueElm);
                beforeSortMap.remove(maxName);
                i++;
            }else{
                //対象をとばかす
                beforeSortMap.remove(maxName);
            }
        }
        return returnList;
    }

    /**
     * 曲別の比較情報を編集する。
     * オリジナルソースで言うところの「editTableHikakumoto」と「editHikakuSakiInfo」と「editTableHikakusaki」をあわせたもの。
     */
    @SuppressWarnings("rawtypes")
    public static ArrayList<SaboomPlayCountGraphByUser> editHikakuInfoBySong(ArrayList<Object> fromList, ArrayList<Object> toList, ArrayList<SaboomPlayCountGraphByUser> resultList){


        //返却用リスト
        ArrayList<SaboomPlayCountGraphByUser> returnList = new ArrayList<SaboomPlayCountGraphByUser>();

        //オリジナルソース・editTableHikakumoto//////////////////////////////////////////////////////////////////////////////////////
        //比較のための情報
        HashMap<String, HikakuInfo> rebuildHikakumotoMapBySong = new HashMap<String, HikakuInfo>();

        //前回処理エレメントの再生回数と順位を保持する変数
        int preCnt = -9999;
        int preRank = -9999;
        HikakuInfo hikakuInfo = null;
        HikakuInfo chofukuHikakuInfo = null;

        //Object[] data = null;
        Integer tempPreRank = null;
        ElementForWeb tempElement = null;
        String tempKey = null;

        //ArrayListのサイズ分、編集処理を繰り返し
        int fromSize = fromList.size();

        for (int i = 1; i < fromSize + 1; i++){

            //ArrayListから現在番目のObjectを取り出し、Elementとしてキャスト
            tempElement = (ElementForWeb)fromList.get(i-1);

            //ランクをセット。
            //ただし、同再生回数の場合は順位を同じくする。
            if(preCnt == tempElement.getPlayCount().intValue()){
                tempPreRank = Integer.valueOf(preRank);
            }else{
                tempPreRank = Integer.valueOf(i);
                preRank = i;
                preCnt = tempElement.getPlayCount().intValue();
            }

            hikakuInfo = new HikakuInfo();
            hikakuInfo.setPreRank(tempPreRank);
            hikakuInfo.setPreCnt(tempElement.getPlayCount());
            if(tempElement.getArtistName().equals(NO_NAME_ARTIST) || tempElement.getArtistName().equals(NO_NAME_ALBUM)){
                tempKey = tempElement.getSongName() + BOU_STRING + NO_NAME_KAIHI + BOU_STRING + tempElement.getAlbumName();
            }else{
                tempKey = tempElement.getSongName() + BOU_STRING + tempElement.getArtistName() + BOU_STRING + tempElement.getAlbumName();
            }
            if(rebuildHikakumotoMapBySong.containsKey(tempKey)){
                chofukuHikakuInfo = rebuildHikakumotoMapBySong.get(tempKey);
                chofukuHikakuInfo.setPreCnt(Integer.valueOf(chofukuHikakuInfo.getPreCnt().intValue() + hikakuInfo.getPreCnt().intValue()));
                rebuildHikakumotoMapBySong.put(tempKey,chofukuHikakuInfo);
            }else{
                //比較のための情報をセット
                rebuildHikakumotoMapBySong.put(tempKey,hikakuInfo);
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //オリジナルソース・editHikakuSakiInfo/////////////////////////////////////////////////////////////////////////////////////////

        //比較のための情報
        HashMap<String, ElementForWeb> rebuildHikakusakiMapBySong = new LinkedHashMap<String, ElementForWeb>();

        int toSize = toList.size();

        ElementForWeb chofukuElement = null;

        for(int i = 0;i < toSize; i++){
            tempElement = (ElementForWeb)toList.get(i);
            if(tempElement.getArtistName().equals(NO_NAME_ARTIST) || tempElement.getArtistName().equals(NO_NAME_ALBUM)){
                tempKey = tempElement.getSongName() + BOU_STRING + NO_NAME_KAIHI + BOU_STRING + tempElement.getAlbumName();
            }else{
                tempKey = tempElement.getSongName() + BOU_STRING + tempElement.getArtistName() + BOU_STRING + tempElement.getAlbumName();
            }
            if(rebuildHikakusakiMapBySong.containsKey(tempKey)){
                chofukuElement = rebuildHikakusakiMapBySong.get(tempKey);
                chofukuElement.setPlayCount(Integer.valueOf(chofukuElement.getPlayCount().intValue() + tempElement.getPlayCount().intValue()));
                rebuildHikakusakiMapBySong.put(tempKey,chofukuElement);
            }else{
                rebuildHikakusakiMapBySong.put(tempKey,tempElement);
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //オリジナルソース・editTableHikakusaki////////////////////////////////////////////////////////////////////////////////////////

        //前回処理エレメントの再生回数と順位を保持する変数
        preCnt = -9999;
        Integer preRankData = Integer.valueOf(preCnt);


        Integer motoRank = null;
        hikakuInfo = null;
        tempKey = null;
        ElementForWeb getElement = null;

        Integer tempZeroElement = null;

        //要素
        SaboomPlayCountGraphByUser valueElm = null;

        //LinkedHashMapのサイズ分、編集処理を繰り返し
        Iterator<?> sortAlbMapIt = rebuildHikakusakiMapBySong.entrySet().iterator();

        int size = limitSize_song;

        if(rebuildHikakusakiMapBySong.size() < limitSize_song) size = rebuildHikakusakiMapBySong.size();

        for(int i = 1; i < size ;i++){
            Map.Entry mapEntry = (Map.Entry) sortAlbMapIt.next();

            //曲名＋アルバム名＋アーティスト名
            tempKey = (String)mapEntry.getKey();
            //element
            getElement = (ElementForWeb)mapEntry.getValue();

            //ランキングの各項目を入れる要素。
            //0番目の要素：本当のランク。（同順位などもあり得るので）（前回順位付き）
            //1番目の要素：曲名
            //2番目の要素：アーティスト名
            //3番目の要素：レート情報
            //4番目の要素：再生回数
            //5番目の要素：差分回数
            valueElm = resultList.get(i-1);

            //曲名をセット。
            valueElm.setSongName_hikakuBySong(getElement.getSongName());
            //アーティスト名をセット。
            valueElm.setArtistName_hikakuBySong(getElement.getArtistName());

            //比較元のランクを取得
            hikakuInfo = rebuildHikakumotoMapBySong.get(tempKey);
            if(hikakuInfo == null) hikakuInfo = new HikakuInfo();
            motoRank = hikakuInfo.getPreRank();

            //レートの★表現を作るためのカウント変数を作成。
            //レートは0～100（曲単位ならば20刻み）のため、20で割った商を
            //繰り返し回数とする。
            int rateLoopCnt = getElement.getRating().intValue() / 20;

            StringBuffer rate = new StringBuffer();

            //ループカウントの回数だけ★を追加。
            for (int j = 0; j < rateLoopCnt; j++){
                rate.append(STAR_STRING);
            }

            //できた★の連結文字列をレートとしてセット。
            valueElm.setRate_hikakuBySong(rate.toString());


            int now = getElement.getPlayCount().intValue() + hikakuInfo.getPreCnt().intValue();

            //再生回数
            valueElm.setPlaycount_hikakuBySong(Integer.valueOf(now));
            //差分回数をセット
            valueElm.setSabuncount_hikakuBySong(getElement.getPlayCount());
            //比較元の再生回数をセット
            valueElm.setMotocount_hikakuBySong(hikakuInfo.getPreCnt());

            //ランクをセット。
            //ただし、同再生回数の場合は順位を同じくする。
            if(preCnt == now){
                tempZeroElement = preRankData;
            }else{
                tempZeroElement = Integer.valueOf(i);
                preRankData = Integer.valueOf(i);
                preCnt = now;
            }

            //元ランクが0＝新登場の曲の場合、アイコンはUP
            if(motoRank.intValue() == 0){
                valueElm.setRankImage_hikakuBySong(imageNew);
            }
            //ランクを比較。比較元のランクの方が大きい（＝順位が下がった）場合
            else if(motoRank.compareTo(tempZeroElement) < 0){
                valueElm.setRankImage_hikakuBySong(imageDown);
            }else if(motoRank.compareTo(tempZeroElement) > 0){
                valueElm.setRankImage_hikakuBySong(imageUp);
            }else{
                valueElm.setRankImage_hikakuBySong(imageStay);
            }

            //順位情報を編集
            valueElm.setRank_hikakuBySong_now(tempZeroElement);
            valueElm.setRank_hikakuBySong_pre(motoRank);

            returnList.add(valueElm);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return returnList;
    }


    /**
     * アーティスト別の比較情報を編集する。
     * オリジナルソースで言うところの「editArtistRankingTableHikakumoto」と「editArtistRankingTableHikakusaki」をあわせたもの。
     */
    @SuppressWarnings("rawtypes")
    public static ArrayList<SaboomPlayCountGraphByUser> editHikakuInfoByArtist(LinkedHashMap<String, Object> fromList, LinkedHashMap<String, Object> toList,ArrayList<SaboomPlayCountGraphByUser> resultList){

        //返却用リスト
        ArrayList<SaboomPlayCountGraphByUser> returnList = new ArrayList<SaboomPlayCountGraphByUser>();
        //要素
        SaboomPlayCountGraphByUser valueElm = null;

        //オリジナルソース・editArtistRankingTableHikakumoto/////////////////////////////////////////////////////////////////////
        //比較のための情報の初期化
        HashMap<String, HikakuInfo> rebuildHikakumotoMapByArtist = new HashMap<String, HikakuInfo>();

        //LinkedHashMapのサイズ分、テーブル編集処理を繰り返す
        Iterator<?> sortArtMapIt1 = fromList.entrySet().iterator();
        int rnk = 1;
        //前回順位・再生回数を保持する変数
        int preRnk = -9999;
        int preCnt = -9999;

        Integer tempZeroElement = null;

        HikakuInfo hikakuInfo = null;

        Integer tempPreRank = null;
        String name = null;
        Map<?, ?> valMap = null;

        while (sortArtMapIt1.hasNext()) {

            Map.Entry mapEntry = (Map.Entry) sortArtMapIt1.next();
            //キー名称＝アーティスト名を取得
            name = (String)mapEntry.getKey();
            //アーティスト毎の統計情報マップ
            valMap = (Map<?, ?>)mapEntry.getValue();

            //現在のアーティストの合計再生回数の取得
            int thisCnt = Integer.valueOf(valMap.get(COUNT_TAG).toString()).intValue();

            //ランキングテーブルの各情報をセットする。
            //ただし、前順位と再生回数が同じ場合、同順位とする。
            if (preCnt == thisCnt){
                tempPreRank = Integer.valueOf(preRnk);
                rnk++;
            }else{
                preRnk = rnk;
                tempPreRank = Integer.valueOf(rnk++);
                preCnt = thisCnt;
            }

            hikakuInfo = new HikakuInfo();
            hikakuInfo.setPreRank(tempPreRank);
            hikakuInfo.setPreCnt(Integer.valueOf(valMap.get(COUNT_TAG).toString()));

            //比較のための情報をセット
            rebuildHikakumotoMapByArtist.put(name,hikakuInfo);
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //オリジナルソース・editArtistRankingTableHikakusaki///////////////////////////////////////////////////////////////////////

        //LinkedHashMapのサイズ分、テーブル編集処理を繰り返す
        Iterator<?> sortArtMapIt2 = toList.entrySet().iterator();

        //前回順位・再生回数を保持する変数
        preRnk = -9999;
        preCnt = -9999;

        Integer motoRank = null;
        hikakuInfo = null;

        int size = limitSize_artist;

        if(toList.size() < limitSize_artist) size = toList.size();

        for(rnk = 1; rnk <= size; rnk++){
            //各項目を格納する要素
            //0番目の要素：本当のランク。（同順位などもあり得るので）
            //1番目の要素：アーティスト名
            //2番目の要素：空き（他のリストと位置関係を合わせるため）
            //3番目の要素：レート情報
            //4番目の要素：再生回数
            //5番目の要素：差分回数
            valueElm = resultList.get(rnk - 1);

            Map.Entry mapEntry = (Map.Entry) sortArtMapIt2.next();
            //キー名称＝アーティスト名を取得
            name = (String)mapEntry.getKey();
            //アーティスト毎の統計情報マップ
            valMap = (Map<?, ?>)mapEntry.getValue();


            //比較のための情報を取得
            hikakuInfo = rebuildHikakumotoMapByArtist.get(name);
            if(hikakuInfo == null) hikakuInfo = new HikakuInfo();
            motoRank = hikakuInfo.getPreRank();


            //現在のアーティストの合計再生回数の取得
            int thisCnt = Integer.valueOf(valMap.get(COUNT_TAG).toString()).intValue();
            //現在のアーティストの合計レートを取得
            int ratesum = ((Integer)valMap.get(RATING_TAG)).intValue();
            //合計レートから平均レートを算出
            int rateavg = ratesum / ((Integer)valMap.get(MOD_CNT)).intValue();
            //valMap.put(RATING_TAG, Integer.valueOf(rateavg));
            //valMap.put(MOD_CNT, Integer.valueOf(ONE_STRING));

            int now = thisCnt + hikakuInfo.getPreCnt().intValue();


            //平均レートから★文字列を作成
            int rateLoop = rateavg / 16;
            if(rateLoop > 5) rateLoop = 5;

            StringBuffer rate = new StringBuffer();
            for (int i = 0; i < rateLoop;i++) rate.append(STAR_STRING);
            rate.append(" ");
            rate.append(rateavg);
            valueElm.setRate_hikakuByArtist(rate.toString());

            //ランキングテーブルの各情報をセットする。
            //ただし、前順位と再生回数が同じ場合、同順位とする。
            if (preCnt == now){
                tempZeroElement = Integer.valueOf(preRnk);
            }else{
                preRnk = rnk;
                tempZeroElement = Integer.valueOf(rnk);
                preCnt = now;
            }

            //元ランクがnull＝新登場の曲の場合、アイコンはUP
            if(motoRank.intValue() == 0){
                valueElm.setRankImage_hikakuByArtist(imageNew);
            }
            //ランクを比較。比較元のランクの方が大きい（＝順位が下がった）場合
            else if(motoRank.compareTo(tempZeroElement) > 0){
                valueElm.setRankImage_hikakuByArtist(imageUp);
            }else if(motoRank.compareTo(tempZeroElement) < 0){
                valueElm.setRankImage_hikakuByArtist(imageDown);
            }else{
                valueElm.setRankImage_hikakuByArtist(imageStay);
            }

            //順位情報を編集
            valueElm.setRank_hikakuByArtist_now(tempZeroElement);
            valueElm.setRank_hikakuByArtist_pre(motoRank);

            //アーティスト名を編集
            valueElm.setArtistName_hikakuByArtist(name);

            //再生回数を編集
            valueElm.setPlaycount_hikakuByArtist(Integer.valueOf(now));
            valueElm.setSabuncount_hikakuByArtist((Integer)valMap.get(COUNT_TAG));
            //比較元の再生回数をセット
            valueElm.setMotocount_hikakuByArtist(hikakuInfo.getPreCnt());

            returnList.add(valueElm);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return returnList;
    }
}