package com.aknow.saboom.controller;

import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.util.UtilityMethods;

public class LogoutController extends Controller {

    @Override
    public Navigation run() throws Exception {
        try{
            HttpSession session = this.request.getSession(true);
            session.invalidate();
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return forward("/login/login.jsp");
    }
}
