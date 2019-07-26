package com.example.huang.huangconsumer.service;

import com.example.huang.huangconsumer.pojo.WeekCount;
import com.example.huang.huangconsumer.repository.WeekCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class WeekCountService {

    @Autowired
    private WeekCountRepository weekCountRepository;

    @Autowired
    private EntityManager entityManager;

    public WeekCount insert(WeekCount weekCount) {
        return weekCountRepository.save(weekCount);
    }

    public Integer findWeek() {
        String sql ="select max(week) from week_count";
        Query query = entityManager.createNativeQuery(sql);
        return (int)query.getSingleResult();
    }

    public List<WeekCount> list(int week) {
        String sql ="from WeekCount where week>=:week1 and week<=:week2";
        Query query = entityManager.createQuery(sql);
        query.setParameter("week1", week - 3);
        query.setParameter("week2", week);
        return query.getResultList();
    }
}
