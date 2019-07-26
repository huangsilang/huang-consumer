package com.example.huang.huangconsumer.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "week_count")
public class WeekCount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "account")
    private BigDecimal account;
    @Column(name = "week")
    private int week;
    @Column(name = "type")
    private int type;
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
}
