package com.aknow.saboom.controller.registPGCATask;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.withClient.RegistPCBATaskService;

public class IndexController extends Controller {

    @Override
    public Navigation run() throws Exception {
        RegistPCBATaskService service = new RegistPCBATaskService();


        // パラメータのkeyを取得
        String keyString = this.request.getParameter("taskInfo");

        service.doTask(keyString);

        return null;
    }
}
