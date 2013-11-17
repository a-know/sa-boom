package com.aknow.saboom.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.aknow.saboom.meta.UserMeta;
import com.aknow.saboom.model.User;
import com.aknow.saboom.util.UtilityMethods;

public class CallbackController extends Controller {

    @SuppressWarnings("boxing")
    @Override
    public Navigation run() throws Exception {
        try{
            //セッションからtwitterオブジェクトとRequestTokenの取出
            AccessToken accessToken = null;
            Twitter twitter = (Twitter) this.request.getSession().getAttribute("twitter");
            RequestToken requestToken = (RequestToken) this.request.getSession().getAttribute("reqToken");
            String verifier = this.request.getParameter("oauth_verifier");
            String loginID = (String) this.request.getSession().getAttribute("loginID");

            //AccessTokenの取得
            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                this.request.getSession().removeAttribute("reqToken");
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            //AccessToken/Secret保存
            if(accessToken != null){
                UserMeta meta = new UserMeta();
                User user = Datastore.query(meta).filter(meta.loginId.equal(loginID)).asSingle();
                user.setTwitterAccessToken(accessToken.getToken());
                user.setTwitterAccessTokenSecret(accessToken.getTokenSecret());
                Datastore.put(user);
            }

            this.request.getSession().setAttribute("twitter_success", 1);//twitter連携成功
            this.request.getSession().setAttribute("see_twitter_success", 0);//twitter連携成功を確認済みか？


            this.response.sendRedirect("http://sa-boom.appspot.com/user/" + loginID);
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
