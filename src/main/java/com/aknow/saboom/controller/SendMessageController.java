package com.aknow.saboom.controller;

import net.arnx.jsonic.JSON;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.model.Message;
import com.aknow.saboom.service.SendMessageService;

public class SendMessageController extends Controller {

    @Override
    public Navigation run() throws Exception {

        this.response.setContentType("application/json; charset=UTF-8");

        //入力情報を取得
        String to = this.request.getParameter("to");
        String sender = this.request.getParameter("sender");
        String content = this.request.getParameter("content");

        SendMessageService service = new SendMessageService();
        Message message = service.sendMessage(to, sender, content);

        JSON.encode(message, this.response.getOutputStream());
        return null;
    }
}
