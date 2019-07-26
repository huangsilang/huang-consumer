package com.example.huang.huangconsumer.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "consumer")
public class ConsumerPojo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;
    @Column(name = "userid")
    private String userid;
    @Column(name = "name")
    private String name;
    @Column(name = "product")
    private String product;
    @Column(name = "howMuch")
    private BigDecimal howMuch;
    @Column(name = "date")
    private Date date;
    @Column(name = "status")
    private String status="0";
    @Transient
    private Date startTime;
    @Transient
    private Date endTime;
    @Transient
    private String time;
    @Transient
    private long timeLong;
    @Column(name = "type")
    private String type;
    //是否必须
    @Column(name = "isMust")
    private String isMust;
    @Column(name = "remark")
    private String remark;

}
