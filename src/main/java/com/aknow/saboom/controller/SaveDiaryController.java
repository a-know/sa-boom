package com.aknow.saboom.controller;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.model.Diary;
import com.aknow.saboom.service.SaveDiaryService;
import com.aknow.saboom.util.UtilityMethods;

public class SaveDiaryController extends Controller {

    @Override
    public Navigation run() throws Exception {

        try{
            this.response.setContentType("application/json; charset=UTF-8");

            //入力情報を取得
            String from = this.request.getParameter("from");
            String to = this.request.getParameter("to");
            String title = this.request.getParameter("title");
            String content = this.request.getParameter("content");
            String loginID = this.request.getParameter("loginID");

            SaveDiaryService service = new SaveDiaryService();
            Diary diary = service.saveDiary(from, to, title, content, loginID);

            JSON.encode(diary, this.response.getOutputStream());
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
