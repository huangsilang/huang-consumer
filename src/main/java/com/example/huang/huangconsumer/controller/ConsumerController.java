package com.example.huang.huangconsumer.controller;

import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import com.example.huang.huangconsumer.service.ConsumerService;
import com.example.huang.huangconsumer.task.CountTask;
import com.example.huang.huangconsumer.task.EmailTask;
import com.example.huang.huangconsumer.utils.PieChartUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/consumer/")
public class ConsumerController {
    private  Logger logger = LoggerFactory.getLogger(ConsumerController.class);
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    CountTask countTask;
    @Autowired
    EmailTask emailTask;
    @RequestMapping("/list/{page}/{size}")
    public Map<String,Page<ConsumerPojo>> list(@RequestBody ConsumerPojo consumerPojo,
                                               @PathVariable("page") int page, @PathVariable("size") int size){
        Map<String, Page<ConsumerPojo>> map = new HashMap<>();
        map.put("list",consumerService.pageList(consumerPojo, page, size));
        return map;
    }
    @PostMapping("/save")
    public Map<String,Object> save(@RequestBody ConsumerPojo consumerPojo){
        Map<String,Object> map = new HashMap<>();
        try {
            consumerService.save(consumerPojo);
            map.put("success",true);
        } catch (Exception e) {
            logger.error("保存异常",e);
            map.put("success",false);
        }
        return map;
    }
    @RequestMapping("/update/{id}/{howMuch}")
    public Map<String,Object> update(@PathVariable("id") String id, @PathVariable("howMuch") BigDecimal howMuch){
        Map<String,Object> map = new HashMap<>();
        try {
            consumerService.update(id,howMuch);
            map.put("success",true);
        } catch (Exception e) {
            logger.error("保存异常",e);
            map.put("success",false);
        }
        return map;
    }
    @DeleteMapping("/delete/{id}")
    public Map<String,Object> delete(@PathVariable("id") String id){
        Map<String,Object> map = new HashMap<>();
        try {
            System.out.println("---"+id);
            consumerService.delete(id);
            map.put("success",true);
        } catch (Exception e) {
            logger.error("保存异常",e);
            map.put("success",false);
        }
        return map;
    }



    @PostMapping("/img")
    public Map<String,Object> img(@RequestBody ConsumerPojo consumerPojo){
        Map<String,Object> map = new HashMap<>();
        try {
            PieChartUtils.deleteFile();
            List<ConsumerPojo> list = consumerService.list(consumerPojo);
            Optional<BigDecimal> reduce = list.stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
            String imgByName = PieChartUtils.createImgByName(list);
            String imgByProduct = PieChartUtils.createImgByProduct(list);
            map.put("success",true);
            map.put("imgByNamePath",imgByName);
            map.put("imgByProductPath",imgByProduct);
            map.put("countTotal",reduce.isPresent()?reduce.get():new BigDecimal(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/task")
    public Map<String,Object> task(@RequestBody ConsumerPojo consumerPojo){
        Map<String,Object> map = new HashMap<>();
        try {
            countTask.weekCount();
            map.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/task2")
    public Map<String,Object> task2(@RequestBody ConsumerPojo consumerPojo){
        Map<String,Object> map = new HashMap<>();
        try {
            countTask.weekCountTime();
            emailTask.execute();
            map.put("success",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
