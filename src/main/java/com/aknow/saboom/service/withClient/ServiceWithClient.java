package com.aknow.saboom.service.withClient;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServiceWithClient{
	abstract ArrayList<Object> execute(HttpServletRequest req, HttpServletResponse res, ArrayList<Object> inputList)
	throws ServletException, IOException, InterruptedException;

}