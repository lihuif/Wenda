package com.example.Test.service;

import com.example.Test.dao.LoginTicketDAO;
import com.example.Test.dao.UserDAO;
import com.example.Test.model.LoginTicket;
import com.example.Test.model.User;
import com.example.Test.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> register(String username, String password){        //注册
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user != null){
            map.put("msg","用户名已注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(TestUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);
        // 登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }



    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!TestUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", user.getId());
        return map;
    }

    public String addLoginTicket(int userId){            //添加用户ticket的方法
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId); //userID
        Date now = new Date();
        now.setTime(3600*24*100 + now.getTime()); //过期时间，100天
        loginTicket.setExpired(now); //Date
        loginTicket.setStatus(0); //Status
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));//ticket
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
