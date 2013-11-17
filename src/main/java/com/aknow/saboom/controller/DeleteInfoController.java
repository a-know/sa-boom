package com.aknow.saboom.controller;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.DeleteInfoService;
import com.aknow.saboom.util.UtilityMethods;

public class DeleteInfoController extends Controller {

    @Override
    public Navigation run() throws Exception {

        try{
            this.response.setContentType("application/json; charset=UTF-8");

            //入力情報を取得
            String date = this.request.getParameter("date");
            String loginID = this.request.getParameter("loginID");

            DeleteInfoService service = new DeleteInfoService();
            service.deleteInfo(date, loginID);
            
            Map<String, Boolean> result = new HashMap<>();
            result.put("result", Boolean.TRUE);

            JSON.encode(result, this.response.getOutputStream());
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
