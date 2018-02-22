package com.example.Test.controller;

import com.example.Test.async.EventModel;
import com.example.Test.async.EventProducer;
import com.example.Test.async.EventType;
import com.example.Test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path ={"/reg/"}, method = {RequestMethod.POST})
    public String seg(Model model, @RequestParam("username") String username,        //注册
                      @RequestParam(value = "next", required = false) String next,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      @RequestParam("password") String password,
                      HttpServletResponse response){
        try {
            Map<String, String> map = userService.register(username,password);
            if (map.containsKey("ticket")){   //
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5); //设置cookie的过期时间
                }
                response.addCookie(cookie);
                if(!StringUtils.isEmpty(next)){
                    return "redirect::"+next;
                }
                return "redirect:/";
            }else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path ={"/login/"}, method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,        //登录
                        @RequestParam("password") String password,
                        @RequestParam(value = "next", required = false) String next,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme, //记住登录
                        HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username,password);
            if (map.containsKey("ticket")){                   //如果未登录
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("username", username).setExt("email", "1356955130@qq.com")
                        .setActorId((int)map.get("userId")));
                if(!StringUtils.isEmpty(next)){  //如果next不为空
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path ={"/reglogin"}, method = {RequestMethod.GET})
    public String seg(Model model,
                      @RequestParam(value = "next", required = false) String next){
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path ={"/logout"}, method = {RequestMethod.GET})
    public String seg(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
