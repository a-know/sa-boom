package com.aknow.saboom.controller;

import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.util.UtilityMethods;

public class UserSearchController extends Controller {

    @Override
    public Navigation run() throws Exception {
        try{
            HttpSession session = this.request.getSession();

            String loginID = (String) this.request.getAttribute("login-ID");

            if(loginID == null || "".equals(loginID)) return forward("/");

            session.setAttribute("user_search_loginID", loginID);
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return forward("/userSearch/userSearch.jsp");
    }
}
