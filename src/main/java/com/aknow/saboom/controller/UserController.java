package com.aknow.saboom.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.model.User;
import com.aknow.saboom.service.UserViewService;
import com.aknow.saboom.util.UtilityMethods;

public class UserController extends Controller {

    @SuppressWarnings("boxing")
    @Override
    public Navigation run() throws Exception {

        try{
            HttpSession session = this.request.getSession();

            String requestLoginId = this.request.getParameter("loginID");
            String loginIdInSession = (String) session.getAttribute("loginID");

            requestScope("loginID_this_page", requestLoginId);

            UserViewService service = new UserViewService();
            User requestUser = service.getUser(requestLoginId);

            if(requestUser == null){
                requestScope("errorCode_userView", 1);//1:要求されたＩＤのユーザーは存在しない
                return forward("/user.jsp");
            }

            if(requestUser.getIsPrivate()){
                if(requestUser.getLoginId().equals(loginIdInSession)){
                    requestScope("errorCode_userView", 0);//エラーなし・閲覧可能
                }else{
                    requestScope("errorCode_userView", 2);//2:非公開設定のため閲覧不可
                    return forward("/user.jsp");
                }
            }else{
                this.request.setAttribute("errorCode_userView", 0);//エラーなし・閲覧可能
            }

            requestScope("requestUser", requestUser);

            Integer see_twitter_success = (Integer) session.getAttribute("see_twitter_success");
            Integer see_twitter_release_success = (Integer) session.getAttribute("see_twitter_release_success");

            if(see_twitter_success != null){
                if(0 == see_twitter_success.intValue()){//まだ一度も連携完了確認をしていない状態でユーザーページを見ていたら
                    this.request.getSession().setAttribute("see_twitter_success", 1);
                }else{
                    this.request.getSession().setAttribute("twitter_success", 0);
                }
            }

            if(see_twitter_release_success != null){
                if(0 == see_twitter_release_success.intValue()){//まだ一度も連携解除完了確認をしていない状態でユーザーページを見ていたら
                    this.request.getSession().setAttribute("see_twitter_release_success", 1);
                }else{
                    this.request.getSession().setAttribute("twitter_release_success", 0);
                }
            }

            //アクセス数のカウントアップ＆取得
            if(requestUser.getLoginId().equals(loginIdInSession)){//アクセスしたのが本人であればカウントアップしない
                requestScope("accessCount", service.getAccessCountWithoutCountUp(requestUser.getLoginId()));
            }else{
                requestScope("accessCount", service.getAccessCountWithCountUp(requestUser.getLoginId()));
            }

            //日記の一覧取得
            requestScope("diaryList", service.getDiaryLabelList(requestUser.getLoginId(), service.getDiaryList(requestUser.getLoginId())));

            //このユーザーのお気に入りアーティストTOP10に関連する処理
            HashMap<String, Integer> playCountTop10Artist = service.getPlayCountTop10Artist(requestUser);
            requestScope("playCountTop10Artist", playCountTop10Artist);
            //imageURL,AmazonURLを、まずはdatastoreから取得。なければAPIで取得
            ArrayList<LinkedHashMap<String, String>> datastoreData = service.getApiData(requestUser);
            ArrayList<LinkedHashMap<String, String>> apiData = service.getDataFromApi(datastoreData.get(0), datastoreData.get(1));

            HashMap<String, String> urlTop10Artist = apiData.get(0);
            HashMap<String, String> imagesTop10Artist = apiData.get(1);

            requestScope("imagesTop10Artist", imagesTop10Artist);
            requestScope("urlTop10Artist", urlTop10Artist);

            //アクティビティ情報の取得
            requestScope("userActivityList", service.getUserActivity(requestLoginId));

            //アップロード済みの再生回数情報の取得
            requestScope("dataLabelList", service.getUserPlayCountData(requestLoginId).get(0));
            requestScope("dataValueList", service.getUserPlayCountData(requestLoginId).get(1));

            if(requestUser.getLoginId().equals(loginIdInSession)){//ログイン者＝閲覧対象であれば、お知らせ件数、メッセージ一覧を読み込み
                requestScope("infomationMap", service.getInfomationData(requestLoginId));
                requestScope("messageLabelList", service.getMessageData(requestLoginId).get(0));
                requestScope("messageValueList", service.getMessageData(requestLoginId).get(1));
            }
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }


        return forward("/user.jsp");
    }
}
