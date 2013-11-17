package com.aknow.saboom.controller;

import org.slim3.controller.router.RouterImpl;

public class AppRouter extends RouterImpl {

    public AppRouter() {
        addRouting("/user/{loginID}", "/user?loginID={loginID}");
    }
}