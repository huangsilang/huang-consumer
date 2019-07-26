package com.example.huang.huangconsumer.task;

import com.example.huang.huangconsumer.constant.Constant;
import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import com.example.huang.huangconsumer.pojo.WeekCount;
import com.example.huang.huangconsumer.service.ConsumerService;
import com.example.huang.huangconsumer.service.WeekCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previous;
@Service
public class CountTask {
    private Logger logger = LoggerFactory.getLogger(CountTask.class);
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private WeekCountService weekCountService;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");



    private int week =1;
    /**
     * 计算时间
     */
    public void weekCountTime(){
        try {
            LocalDate now = LocalDate.now();
            LocalDate startTime = now.with(previous(DayOfWeek.SATURDAY));
            String strStartDate = startTime.getYear()+"-"+startTime.getMonthValue()+"-"+startTime.getDayOfMonth();
            Date startDate = sdf.parse(strStartDate+" 00:00:00");
            //LocalDate endTime = now.with(previous(DayOfWeek.SUNDAY));
            LocalDate endTime = now.with(nextOrSame(DayOfWeek.FRIDAY));
            String strEndDate = endTime.getYear()+"-"+endTime.getMonthValue()+"-"+endTime.getDayOfMonth();
            Date endDate = sdf.parse(strEndDate+" 23:59:59");
            Constant.map.put("weekStartTime",startDate);
            Constant.map.put("weekEndTime",endDate);
        } catch (ParseException e) {
            logger.error("时间计算异常",e);
        }
    }
    /**
     *  统计数据
     */
    @Scheduled(cron = "0 00 08 ? * SAT")
    public void weekCount(){
        try {
            weekCountTime();
            try {
                week = weekCountService.findWeek();
            } catch (Exception e) {
                week=1;
            }
            ConsumerPojo c = new ConsumerPojo();
            c.setStartTime(Constant.map.get("weekStartTime"));
            c.setEndTime(Constant.map.get("weekEndTime"));
            List<ConsumerPojo> list = consumerService.list(c);
            countTotal(list);
            countByProduct(list);
            countByName(list);
            countByType(list);
            countByIsMust(list);
        } catch (Exception e) {
            logger.error("统计异常",e);
        }
    }

    /**
     * 按照产品统计
     * @param list
     */
    public void countByProduct(List<ConsumerPojo> list){
        Map<String, List<ConsumerPojo>> map = list.stream().collect(Collectors.groupingBy(ConsumerPojo::getProduct));
        for (String s:map.keySet()) {//按照产品分组
            Optional<BigDecimal> opt = map.get(s).stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
            BigDecimal bigDecima = opt.isPresent() ? opt.get() : new BigDecimal(0);
            WeekCount weekCount = new WeekCount();
            weekCount.setName(Constant.TOTAL_NAME);
            weekCount.setType(2);
            weekCount.setTypeName(s);
            weekCount.setWeek(week);
            weekCount.setAccount(bigDecima);
            weekCount.setStartTime(Constant.map.get("weekStartTime"));
            weekCount.setEndTime(Constant.map.get("weekEndTime"));
            weekCountService.insert(weekCount);
            //dataset.setValue(s+"-"+bigDecima, bigDecima);
        }
    }

    /**
     * 按照姓名统计
     * @param list
     */
    public void countByName(List<ConsumerPojo> list){
        Map<String, List<ConsumerPojo>> map = list.stream().collect(Collectors.groupingBy(ConsumerPojo::getName));
        for (String s:map.keySet()) {
            Optional<BigDecimal> opt = map.get(s).stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
            WeekCount weekCount = new WeekCount();
            weekCount.setName(s);
            weekCount.setType(1);
            weekCount.setTypeName(s);
            weekCount.setWeek(week);
            weekCount.setAccount(opt.isPresent() ? opt.get() : new BigDecimal(0));
            weekCount.setStartTime(Constant.map.get("weekStartTime"));
            weekCount.setEndTime(Constant.map.get("weekEndTime"));
            weekCountService.insert(weekCount);
        }
    }
    /**
     * 按照是否必须统计
     * @param list
     */
    public void countByIsMust(List<ConsumerPojo> list){
        Map<String, List<ConsumerPojo>> map = list.stream().collect(Collectors.groupingBy(ConsumerPojo::getName));
        for (String s:map.keySet()) {
            /**
             * 按照是否必须统计
             */
            Map<String, List<ConsumerPojo>> isMustMap = map.get(s).stream().collect(Collectors.groupingBy(ConsumerPojo::getIsMust));
            for (String k : isMustMap.keySet()){
                Optional<BigDecimal> optional = isMustMap.get(k).stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
                WeekCount weekCount = new WeekCount();
                weekCount.setName(s);
                weekCount.setType(4);
                weekCount.setWeek(week);
                weekCount.setTypeName(Constant.getIsMust(Integer.valueOf(k)));
                weekCount.setAccount(optional.isPresent() ? optional.get() : new BigDecimal(0));
                weekCount.setStartTime(Constant.map.get("weekStartTime"));
                weekCount.setEndTime(Constant.map.get("weekEndTime"));
                weekCountService.insert(weekCount);
            }
        }
    }




    /**
     * 按照分类
     * @param list
     */
    public void countByType(List<ConsumerPojo> list){
        Map<String, List<ConsumerPojo>> map = list.stream().collect(Collectors.groupingBy(ConsumerPojo::getType));
        for (String s:map.keySet()) {//按照产品分组
            Optional<BigDecimal> opt = map.get(s).stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
            BigDecimal bigDecima = opt.isPresent() ? opt.get() : new BigDecimal(0);
            WeekCount weekCount = new WeekCount();
            weekCount.setName(Constant.TOTAL_NAME);
            weekCount.setType(3);
            weekCount.setTypeName(Constant.getTypeName(Integer.valueOf(s)));
            weekCount.setWeek(week);
            weekCount.setAccount(bigDecima);
            weekCount.setStartTime(Constant.map.get("weekStartTime"));
            weekCount.setEndTime(Constant.map.get("weekEndTime"));
            weekCountService.insert(weekCount);
            //dataset.setValue(s+"-"+bigDecima, bigDecima);
        }
    }
    /**
     * 计算总的消费金额
     * @param list
     * @return
     */
    public void countTotal(List<ConsumerPojo> list){
        WeekCount weekCount = new WeekCount();
        weekCount.setName(Constant.TOTAL_NAME);
        weekCount.setStartTime(Constant.map.get("weekStartTime"));
        weekCount.setEndTime(Constant.map.get("weekEndTime"));
        weekCount.setType(0);
        weekCount.setTypeName(Constant.TOTAL_NAME);
        weekCount.setWeek(week);
        Optional<BigDecimal> opt = list.stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
        weekCount.setAccount(opt.isPresent()?opt.get(): new BigDecimal(0));
        weekCountService.insert(weekCount);
    }

}
