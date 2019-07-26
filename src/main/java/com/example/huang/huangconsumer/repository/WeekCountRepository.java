package com.example.huang.huangconsumer.repository;

import com.example.huang.huangconsumer.pojo.WeekCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WeekCountRepository extends JpaRepository<WeekCount, String>, JpaSpecificationExecutor<WeekCount> {
}
