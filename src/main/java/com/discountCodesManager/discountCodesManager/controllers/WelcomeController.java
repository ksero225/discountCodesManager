package com.discountCodesManager.discountCodesManager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping(path = "/")
    public String welcome(){
        return "Welcome, this is discount codes manager. Look for Postman collections in 'postmanCollections' catalog for samples query";
    }
}
