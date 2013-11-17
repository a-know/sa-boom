package com.aknow.saboom.controller;

import javax.servlet.http.HttpSession;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.LoginFromViewService;
import com.aknow.saboom.util.UtilityMethods;

public class LoginController extends Controller {

    @SuppressWarnings("boxing")
    @Override
    public Navigation run() throws Exception {

        try{
            HttpSession session = this.request.getSession();

            String loginID = (String) this.request.getAttribute("login-ID");
            String pass = (String) this.request.getAttribute("pass-word");

            boolean hasError = false;
            session.setAttribute("no_loginID", false);
            session.setAttribute("no_pass", false);
            session.setAttribute("fail_login", false);
            session.setAttribute("logon", false);

            if(loginID == null || "".equals(loginID)){
                session.setAttribute("no_loginID", true);
                hasError = true;
            }else{
                session.setAttribute("no_loginID", false);
            }

            if(!hasError){
                if(pass == null || "".equals(pass)){
                    session.setAttribute("no_pass", true);
                    hasError = true;
                }else{
                    session.setAttribute("no_pass", false);
                }
            }

            if(!hasError){
                LoginFromViewService service = new LoginFromViewService();
                int loginResult = service.login(loginID, pass);

                if(loginResult == 0){
                    //ログイン成功
                    session.setAttribute("fail_login", false);
                    session.setAttribute("logon", true);
                }else if(loginResult == 1){
                    //ログイン失敗
                    session.setAttribute("fail_login", true);
                    session.setAttribute("logon", false);
                    hasError = true;
                }
            }


            session.setAttribute("loginID", loginID);
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return forward("/login/login.jsp");
    }
}
