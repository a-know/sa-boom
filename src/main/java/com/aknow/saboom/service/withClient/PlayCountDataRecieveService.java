package com.aknow.saboom.service.withClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import org.slim3.memcache.Memcache;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.Activity;
import com.aknow.saboom.model.PlayCountData;
import com.aknow.saboom.model.RegistPCBATask;
import com.aknow.saboom.model.User;
import com.aknow.saboom.util.Consts;
import com.aknow.saboom.util.PlayCountByArtistForTask;
import com.aknow.saboom.util.TwitterUtil;
import com.aknow.saboom.util.UtilityMethods;

import cdit.PlayCountByArtist;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.apphosting.api.ApiProxy.RequestTooLargeException;


public class PlayCountDataRecieveService implements ServiceWithClient{

    private static final Logger logger = Logger.getLogger(PlayCountDataRecieveService.class.getName());

    static final UserMeta userMeta = UserMeta.get();

    @SuppressWarnings("unchecked")
	@Override
    public ArrayList<Object> execute(HttpServletRequest req,
        HttpServletResponse res, ArrayList<Object> inputList)
                throws ServletException, IOException, InterruptedException {


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));

        //make list for output
        ArrayList<Object> outputList = new ArrayList<Object>();

        //get session
        HttpSession session = null;
        int n = 0;

        while(true){
            try{
                session = req.getSession(true);
                break;
            }catch(MemcacheServiceException e){
                Thread.sleep(100);
                n++;
                if(n > 5) throw e;
            }
        }

        //fail : session has already exists
        if(session.isNew()){
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to get session");
            }
            outputList.add("9");//return code "9" when error occurs
            return outputList;
        }


        //get data from client
        String loginIdyyMMddHHmmss = (String) inputList.get(1);
        String loginID = (String) inputList.get(2);
        String yyMMddHHmmss = (String) inputList.get(3);
        String dataName = (String) inputList.get(4);
        Integer totalPlayCount = (Integer) inputList.get(5);
        @SuppressWarnings("rawtypes")
        ArrayList sortedListBySongPlayCount = (ArrayList) inputList.get(6);
        @SuppressWarnings("rawtypes")
        LinkedHashMap sortedMapByAlbumPlayCount = (LinkedHashMap) inputList.get(7);
        @SuppressWarnings("rawtypes")
        LinkedHashMap sortedMapByArtistPlayCount = (LinkedHashMap) inputList.get(8);
        @SuppressWarnings("rawtypes")
        ArrayList playCountByArtistList = (ArrayList) inputList.get(9);
        @SuppressWarnings("rawtypes")
        ArrayList top10ArtistDataList = (ArrayList) inputList.get(10);

        //再生回数情報のアップロードは、一日(24時間)に一回まで。
        User user = Datastore.query(userMeta).filter(userMeta.loginId.equal(loginID)).asSingle();//clientとの連携の仕組み上、ここでの検索結果無しは考慮しない。
        if(!user.getLastUploadDataYyMMddHHmmss().equals("000000000000")){
            if(differenceDays(calendar.getTime(), user.getLastUploadDate()) < 86400000){
                outputList.add("8");
                return outputList;
            }
        }

        //当該ユーザーのアップロードした情報のうち、最新の情報の年月日時分秒
        String lastUploadDataYyMMddHHmmss = user.getLastUploadDataYyMMddHHmmss();
        //送信されてきた再生回数情報が既に登録されていないかどうかを確認、
        //登録されていなければ登録作業を続行する
        if(user.getRegistedDataLabel().contains(yyMMddHHmmss)){
            //既に登録済み・登録不可・リターンコード1（既に登録されている）で返送
            outputList.add("1");
            return outputList;
        }


        //再生回数情報のblobstoreへの登録（トランザクション外で実施）
        ArrayList<Object> blobList = new ArrayList<Object>();
        blobList.add(sortedListBySongPlayCount);
        blobList.add(sortedMapByAlbumPlayCount);
        blobList.add(sortedMapByArtistPlayCount);
        BlobKey blobListKey = UtilityMethods.registBlob(blobList);


        @SuppressWarnings("deprecation")
		GlobalTransaction gtx = Datastore.beginGlobalTransaction();
        user = gtx.get(userMeta, user.getKey());
        int currentTotalPlayCount = 0;
        //送られてきた情報が今までに無い最新のものであれば、
        //ユーザ情報に持っている総再生回数情報とＴＯＰ１０アーティスト情報を更新する
        if(user.getLastUploadDataYyMMddHHmmss().compareTo(yyMMddHHmmss) < 0){
            //送られてきた年月日時分秒の方が新しい
            //　＝現時点で保持している情報よりも新しいので、付随するユーザー情報も更新

            user.setLastUploadDataYyMMddHHmmss(yyMMddHHmmss);
            currentTotalPlayCount = user.getTotalPlayCount().intValue();
            user.setTotalPlayCount(totalPlayCount);
            user.setTop10ArtistDataList(top10ArtistDataList);
        }
        ArrayList<String> dataLabels = user.getRegistedDataLabel();
        dataLabels.add(yyMMddHHmmss);
        user.setRegistedDataLabel(dataLabels);
        //アップロード回数のカウントアップ
        user.setUploadCount(new Integer(user.getUploadCount().intValue() + 1));
        user.setLastUploadDate(calendar.getTime());

        //PlayCountDataの生成・登録
        PlayCountData data = new PlayCountData();
        data.setLoginId(loginID);
        data.setTotalPlayCount(totalPlayCount);
        data.setYyMMddHHmmss(yyMMddHHmmss);
        data.setInfoName(dataName);
        data.setBlobListKey(blobListKey);
        data.getUserRef().setModel(user);

        //双方向1対多の関係で登録
        try{
            gtx.put(user, data);
            gtx.commit();
        }catch(RequestTooLargeException e){
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to regist PlayCountData. RequestTooLargeException.");
            }
            //例外発生・登録不可・リターンコード9（送信情報が大きすぎる）で返送
            //登録済みのblobを削除する
            UtilityMethods.deleteBlob(blobListKey);
            outputList.add("2");
            return outputList;
        }catch(Exception e){
            if(logger.isLoggable(Level.SEVERE)){
                logger.severe("fail to upload. occurs exception is : " + e.toString());
            }
            e.printStackTrace();
            UtilityMethods.deleteBlob(blobListKey);
            outputList.add("9");//return code "9" when error occurs
            return outputList;
        }finally{
            if(gtx.isActive()){
                gtx.rollback();
            }
        }

        //データアップロードのアクティビティ登録

        HashMap<Object, Object> activityInfo = new HashMap<Object, Object>();
        activityInfo.put("loginID", loginID);
        Date uploadDate = calendar.getTime();

        Activity activity = new Activity();
        activity.setActivityDate(uploadDate);
        activity.setActivityInfo(activityInfo);
        activity.setActivityCode("2");
        activity.setLoginID(loginID);
        if(user.getIsPrivate().booleanValue()){
            activity.setViewable(Boolean.FALSE);
        }else{
            activity.setViewable(Boolean.TRUE);
        }

        try{
            Datastore.put(activity);
        }catch(Exception e){
            if(logger.isLoggable(Level.WARNING)){
                logger.severe("fail to upload activity insert. occurs exception is : " + e.toString());
            }
            //ここでの例外は無視。アクティビティが登録できないだけなので
        }
        if(!user.getIsPrivate().booleanValue() && user.getTwitterAccessToken() != null && !user.getTwitterAccessToken().equals("") && user.getIsTweetOption1().booleanValue()){
            String tweetContents = "sa-boom!!に、再生回数情報『" + dataName + "』をアップロードしたよ！　#saboom http://sa-boom.appspot.com/user/" + user.getLoginId();
            new TwitterUtil().doTweet(tweetContents, user);
        }


        //送られてきた情報が今までに無い最新のものであるときのみ実施。
        if(lastUploadDataYyMMddHHmmss.compareTo(yyMMddHHmmss) < 0){

            //memcache delete
            Memcache.delete(Consts.TotalPlayCountByUser_KEY + loginID);
            Memcache.delete(Consts.NormalPlayCountGraphDataByUser_KEY + loginID);
            Memcache.delete(Consts.UserRankingGraphData_KEY);
            Memcache.delete(Consts.PlayCountTop10ArtistByUser_KEY + user.getLoginId());
            Memcache.delete(Consts.ApiDataByUser_KEY + user.getLoginId());
            Memcache.delete(Consts.DataFromApiByUser_KEY + user.getLoginId());


            if(currentTotalPlayCount != 0){
                //規定再生回数超過のアクティビティ登録判別・登録実施
                activityInfo = null;
                Date activityDate = null;

                for(int i = 0; i < UtilityMethods.ACTIVITY_OVER_CNT_LIST.length;i++){
                    int kijun = new Integer(UtilityMethods.ACTIVITY_OVER_CNT_LIST[i]).intValue();
                    if(currentTotalPlayCount < kijun && kijun < totalPlayCount.intValue()){
                        activityInfo = new HashMap<Object, Object>();
                        activityInfo.put("loginID", loginID);
                        activityInfo.put("overCnt", new Integer(kijun));

                        activityDate = calendar.getTime();
                    }
                }
                if(activityDate != null){//規定回数超過が認められたとき、アクティビティ登録を実施

                    activity = new Activity();
                    activity.setActivityDate(activityDate);
                    activity.setActivityInfo(activityInfo);
                    activity.setActivityCode("3");
                    activity.setLoginID(loginID);

                    if(user.getIsPrivate().booleanValue()){
                        activity.setViewable(Boolean.FALSE);
                    }else{
                        activity.setViewable(Boolean.TRUE);
                    }

                    try{
                        Datastore.put(activity);
                    }catch(Exception e){
                        if(logger.isLoggable(Level.WARNING)){
                            logger.severe("fail to play count over activity insert. occurs exception is : " + e.toString());
                        }
                        //ここでの例外は無視。アクティビティが登録できないだけなので
                    }
                    //twitter連携
                    if(!user.getIsPrivate().booleanValue() && user.getTwitterAccessToken() != null && !user.getTwitterAccessToken().equals("") && user.getIsTweetOption2().booleanValue()){
                        String tweetContents = "私のiTunes 総再生回数が" + activityInfo.get("overCnt") + "回を超えたよ！　#saboom http://sa-boom.appspot.com/user/" + user.getLoginId();
                        new TwitterUtil().doTweet(tweetContents, user);
                    }
                }
            }

            //アーティスト毎の再生回数情報の更新
            //playCountByArtistListを分割
            int size = playCountByArtistList.size();
            //client 3.0.1-3.0.2のバグ対応-start
            if(size > 0){
                PlayCountByArtist e = (PlayCountByArtist) playCountByArtistList.get(0);
                if(e.getArtistName().contains("|")){//本来ふさわしくない情報をPCBAListとして渡されていたら、集計無視（「さざなみCD|スピッツ」がアーティスト名として集計されているバグ）
                    System.out.println("@@@アーティスト名に|あり。" + loginID + "  registPCBATask実行せず。");
                    size = 0;
                }
            }
            //client 3.0.1-3.0.2のバグ対応-end
            int cnt = 0;
            ArrayList<PlayCountByArtistForTask> dividedList = new ArrayList<PlayCountByArtistForTask>();
            ArrayList<ArrayList<PlayCountByArtistForTask>> taskTargetList = new ArrayList<ArrayList<PlayCountByArtistForTask>>();
            PlayCountByArtist tempPCBA = null;

            for(int i = 0; i < size; i++){
                if(cnt < 20){
                    tempPCBA = (PlayCountByArtist) playCountByArtistList.get(i);
                    if(tempPCBA.getPlayCount().intValue() > 0){
                        dividedList.add(cnt,new PlayCountByArtistForTask(tempPCBA.getloginIDArtistName(), tempPCBA.getArtistName(), tempPCBA.getLoginID(), tempPCBA.getPlayCount(), tempPCBA.getYyMMddHHmmss()));
                        cnt++;
                    }
                }else{
                    taskTargetList.add(dividedList);
                    dividedList = new ArrayList<PlayCountByArtistForTask>();
                    cnt = 0;
                    i--;
                }
            }
            if(dividedList.size() > 0){
                taskTargetList.add(dividedList);
            }

            RegistPCBATask tempTask = null;

            for(int i = 0; i < taskTargetList.size(); i++){

                tempTask = new RegistPCBATask();
                tempTask.setLoginIDyyMMddHHmmssi(user.getLoginId() + yyMMddHHmmss + i);
                tempTask.setPlayCountByArtistList(taskTargetList.get(i));
                tempTask.setYyMMddHHmmss(yyMMddHHmmss);

                Datastore.put(tempTask);

                TaskOptions taskOptions = TaskOptions.Builder.withParam("taskInfo", loginIdyyMMddHHmmss + i).url("/registPCBATask/");

                Queue defaultQueue = QueueFactory.getDefaultQueue();
                defaultQueue.add(taskOptions);
            }

        }//送られてきた情報が最新のものだった場合・ここまで

        //delete memcache
        Memcache.delete(Consts.UserListByArtist_KEY);
        Memcache.delete(Consts.UserRankingOfUploadCount_KEY);

        outputList.add("0");
        return outputList;
    }

    /**
     * 2つの日付の差を求めます。
     * java.util.Date 型の日付 date1 - date2 が何ミリ秒かを返します。
     *
     * 計算方法は以下となります。
     * 1.最初に2つの日付を long 値に変換します。
     * 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの
     * 　経過ミリ秒数となります。
     * 2.次にその差を求めます。
     * 3.上記の計算で出た数量を 1 日の時間で割ることで
     * 　日付の差を求めることができます。
     * 　※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
     *
     * @param date1    日付 java.util.Date
     * @param date2    日付 java.util.Date
     * @return    2つの日付の差
     */
    public static long differenceDays(Date date1,Date date2) {
        long datetime1 = date1.getTime();
        long datetime2 = date2.getTime();
        //long one_date_time = 1000 * 60 * 60 * 24;
        long diffDays = datetime1 - datetime2;
        return diffDays;
    }
}
