package com.example.Test.controller;

import com.example.Test.model.User;
import com.example.Test.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Controller
public class IndexController {
    @Autowired
    WendaService wendaService;

    @RequestMapping(path ={"/", "/index"} )
    @ResponseBody
    public String index(HttpSession httpSession){
        return wendaService.getMessage(2) + " Hello Spring!" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path ={"/vm"}, method = {RequestMethod.GET})
    public String template(){
        return "index";
    }

   @RequestMapping(path ={"profile/{groupId}/{userId}"} )
    @ResponseBody
    public String profile(@PathVariable("userId")  int userId,
                          @PathVariable("groupId")  String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key){
        return String.format("Profile page of %s / %d t:%d k:%s", groupId, userId, type, key);
    }

    @RequestMapping(path ={"/home"})
    public String template(Model m){
/*        List<String> list = Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        Map<String,String> map = new HashMap<String, String>();
        User user = new User("LSX");

        map.put("1","Hello");
        m.addAttribute("user",user);
        m.addAttribute("colors",list);
        m.addAttribute("userMap",map);
        m.addAttribute("value","11111");*/
        return "index";
    }

    @RequestMapping(path ={"/request"} )
    @ResponseBody
    public String request(Model model, HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId){
        StringBuffer sb = new StringBuffer();
        sb.append("COOKIEVALUE "+sessionId+"<br>");
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getClass()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getRequestURI()+"<br>");
        return sb.toString();
    }

    @RequestMapping(path ={"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                           HttpSession httpSession){
        httpSession.setAttribute("msg","I love Xinxin");
        RedirectView red = new RedirectView("/", true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"})
    @ResponseBody
    public String admin(@RequestParam("key") String key){ //需要一个key=?的参数
        if (key.equals("admin")){
            return "hello admin";
        }
        throw new IllegalArgumentException("参数错误");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error: "+e.getMessage();
    }
}
