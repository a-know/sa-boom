package com.aknow.saboom.controller;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.LoadDiaryService;
import com.aknow.saboom.util.UtilityMethods;

public class LoadDiaryController extends Controller {

    @Override
    public Navigation run() throws Exception {

        try{
            this.response.setContentType("application/json; charset=UTF-8");

            //入力情報を取得
            String from = this.request.getParameter("from");
            String to = this.request.getParameter("to");
            String loginID = this.request.getParameter("loginID");

            LoadDiaryService service = new LoadDiaryService();

            JSON.encode(service.loadDiary(from, to, loginID), this.response.getOutputStream());
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
