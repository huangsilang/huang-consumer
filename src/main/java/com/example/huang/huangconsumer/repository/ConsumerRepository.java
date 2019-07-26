package com.example.huang.huangconsumer.repository;

import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends JpaRepository<ConsumerPojo, String> , JpaSpecificationExecutor<ConsumerPojo> {


}
