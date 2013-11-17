package com.aknow.saboom.controller.requestFromClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.withClient.KeyRequestService;
import com.aknow.saboom.service.withClient.LoginCheckRequestService;
import com.aknow.saboom.service.withClient.PlayCountDataRecieveService;
import com.aknow.saboom.service.withClient.RegistRequestService;
import com.aknow.saboom.service.withClient.ServiceWithClient;
import com.aknow.saboom.service.withClient.WithdrawService;
import com.aknow.saboom.util.UtilityMethods;

public class IndexController extends Controller {

    @SuppressWarnings({ "unchecked" })
    @Override
    public Navigation run() throws Exception {

        try{
            //get inputStream from sa-boom client
            ObjectInputStream in = new ObjectInputStream(this.request.getInputStream());

            //get input-data from sa-boom client
            ArrayList<Object> inputList = null;
            inputList = (ArrayList<Object>) in.readObject();

            //select the service to start by the function-number from client
            ServiceWithClient service = null;

            //start
            String functionCode = (String) inputList.get(0);

            if("1".equals(functionCode)){
                service = new KeyRequestService();
            }else if("2".equals(functionCode)){
                service = new RegistRequestService();
            }else if("3".equals(functionCode)){
                service = new LoginCheckRequestService();
            }else if("4".equals(functionCode)){
                service = new PlayCountDataRecieveService();
            }else if("5".equals(functionCode)){
                service = new WithdrawService();
            }


            ArrayList<Object> outputList = service.execute(this.request, this.response, inputList);

            //return result to client
            ObjectOutputStream out = new ObjectOutputStream(this.response.getOutputStream());
            out.writeObject(outputList);
            out.flush();
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
        //return forward("index.jsp");
    }
}
