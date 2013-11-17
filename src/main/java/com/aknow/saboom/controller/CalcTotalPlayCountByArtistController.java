package com.aknow.saboom.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import com.aknow.saboom.service.CalcTotalPlayCountByArtistService;
import com.aknow.saboom.util.UtilityMethods;

public class CalcTotalPlayCountByArtistController extends Controller {

    @Override
    public Navigation run() throws Exception {

        try{
            CalcTotalPlayCountByArtistService service = new CalcTotalPlayCountByArtistService();

            service.calc();
        }catch(Exception e){
            throw UtilityMethods.sendAlertMail(this.getClass().getName(), e);
        }

        return null;
    }
}
