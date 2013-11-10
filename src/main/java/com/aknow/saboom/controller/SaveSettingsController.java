package com.aknow.saboom.controller;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.model.User;
import com.aknow.saboom.service.SaveSettingsService;
import com.aknow.saboom.util.UtilityMethods;

public class SaveSettingsController extends Controller {

    @Override
    public Navigation run() throws Exception {
        try{
            this.response.setContentType("application/json; charset=UTF-8");

            //入力情報を取得
            String loginID = this.request.getParameter("loginID");
            String url = this.request.getParameter("url");
            String introduction = this.request.getParameter("introduction");
            String pm = this.request.getParameter("pm");
            String tw_option1 = this.request.getParameter("tw_option1");
            String tw_option2 = this.request.getParameter("tw_option2");
            String tw_option3 = this.request.getParameter("tw_option3");
            String tw_option4 = this.request.getParameter("tw_option4");
            String tw_option5 = this.request.getParameter("tw_option5");


            SaveSettingsService service = new SaveSettingsService();
            User user = service.saveSettings(loginID, url, introduction, pm, tw_option1, tw_option2, tw_option3, tw_option4, tw_option5);

            Map<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("loginID", user.getLoginId());
            userMap.put("url", user.getUrl());
            userMap.put("introduction", user.getIntroduction());
            userMap.put("isPrivate", user.getIsPrivate());
            userMap.put("twitter_option1", user.getIsTweetOption1());
            userMap.put("twitter_option2", user.getIsTweetOption2());
            userMap.put("twitter_option3", user.getIsTweetOption3());
            userMap.put("twitter_option4", user.getIsTweetOption4());
            userMap.put("twitter_option5", user.getIsTweetOption5());

            JSON.encode(userMap, this.response.getOutputStream());
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
