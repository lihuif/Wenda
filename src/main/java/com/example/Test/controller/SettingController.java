package com.example.Test.controller;

import com.example.Test.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SettingController {
    @Autowired
    WendaService wendaService;

    @RequestMapping(path ={"/Setting"} )
    @ResponseBody
    public String index(HttpSession httpSession){
        return "Setting OK! " +wendaService.getMessage(1);
    }
}
