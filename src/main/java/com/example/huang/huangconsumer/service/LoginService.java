package com.example.huang.huangconsumer.service;

import com.example.huang.huangconsumer.pojo.User;
import com.example.huang.huangconsumer.repository.MyLoginRepository;
import com.example.huang.huangconsumer.utils.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private MyLoginRepository myLoginRepository;

    public boolean login(User userInfo) throws Exception {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("password","userId");
        Example<User> example = Example.of(userInfo,matcher);
        Optional<User> opt = myLoginRepository.findOne(example);
        boolean boo = opt.isPresent();
        if (boo){
            User user = opt.get();
            boo=Md5Utils.verify(userInfo.getPassword(),user.getKey(),user.getPassword());
        }
        return true;
    }



    public static void main(String[] args) throws Exception {
        //String replace = UUID.randomUUID().toString().replace("-", "");
        String s = Md5Utils.md5("123456", "e1255110568d456e88e95141a37f1012");
        System.out.println(s);
    }

}
