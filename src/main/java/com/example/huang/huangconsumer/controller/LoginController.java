package com.example.huang.huangconsumer.controller;

import com.example.huang.huangconsumer.pojo.User;
import com.example.huang.huangconsumer.service.LoginService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;
    @RequestMapping("/login")
    public Map<String,Object> login(@RequestBody User userInfo, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            map.put("success",loginService.login(userInfo));
            String token = UUID.randomUUID().toString().replace("-", "");
            request.getSession().setAttribute(token,userInfo);
            map.put("token",token);
        } catch (Exception e) {
            logger.error("登录异常",e);
        }
        return map;
    }

 /*   public static void main(String[] args) {
        java.util.concurrent.ConcurrentHashMap m = new java.util.concurrent.ConcurrentHashMap();
        m.put("qq","ww");
        m.put("ww","ww");
        m.put("ee","ww");
        m.put("qw","ww");
        m.put("qa","ww");
        m.put("aq","ww");
        m.put("aw","ww");
        m.put("az","ww");
        m.put("sa","ww");
        m.put("as","ww");
    }*/
    public static void main(String[] args) {
        TreeNode tn = new TreeNode();
        System.out.println();
    }
    @Data
    public static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; };
      TreeNode(){};

  }
}
