package com.aknow.saboom.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.TwitterReleaseService;
import com.aknow.saboom.util.UtilityMethods;

public class TwitterReleaseController extends Controller {

    @SuppressWarnings("boxing")
    @Override
    public Navigation run() throws Exception {

        try{
            String loginID = (String) this.request.getSession().getAttribute("loginID");

            TwitterReleaseService service = new TwitterReleaseService();

            service.releaseTwitterTokens(loginID);

            this.request.getSession().setAttribute("twitter_release_success", 1);//twitter連携解除成功
            this.request.getSession().setAttribute("see_twitter_release_success", 0);//twitter連携解除成功を確認済みか？

            this.response.sendRedirect("/user/" + loginID);
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
