package com.example.huang.huangconsumer.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.huang.huangconsumer.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class LoginInterceptor implements HandlerInterceptor {

    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String body = requestWrapper.getBody();
        HttpSession session = request.getSession();
        if(StringUtils.isBlank(body)){
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        String token = jsonObject.getString("token");
        if(StringUtils.isBlank(token)){
            return false;
        }
        User user = (User)session.getAttribute(token);
       if (user == null){
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
            return false;
        }else {
            return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
        }
    }

}
