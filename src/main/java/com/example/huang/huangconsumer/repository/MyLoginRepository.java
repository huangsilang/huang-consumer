package com.example.huang.huangconsumer.repository;

import com.example.huang.huangconsumer.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyLoginRepository extends JpaRepository<User, String> {
}
