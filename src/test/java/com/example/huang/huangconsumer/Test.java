package com.example.huang.huangconsumer;

import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import com.example.huang.huangconsumer.service.ConsumerService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previous;
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private ConsumerService consumerService;

    @org.junit.Test
    public void test() throws ParseException {
        LocalDate now = LocalDate.now();

        LocalDate startTime = now.with(previous(DayOfWeek.MONDAY));
        String strStartDate = startTime.getYear()+"-"+startTime.getMonthValue()+"-"+startTime.getDayOfMonth();
        Date startDate = sdf.parse(strStartDate+" 00:00:00");
        LocalDate endTime = now.with(nextOrSame(DayOfWeek.THURSDAY));
        String strEndDate = endTime.getYear()+"-"+endTime.getMonthValue()+"-"+endTime.getDayOfMonth();
        Date endDate = sdf.parse(strEndDate+" 23:59:59");
        ConsumerPojo c = new ConsumerPojo();
        c.setStartTime(startDate);
        c.setEndTime(endDate);
        List<ConsumerPojo> list = consumerService.list(c);
        Optional<BigDecimal> opt = list.stream().map(ConsumerPojo::getHowMuch).reduce(BigDecimal::add);
        if (opt.isPresent()){
            System.out.println(opt.get());
        }
    }
}
