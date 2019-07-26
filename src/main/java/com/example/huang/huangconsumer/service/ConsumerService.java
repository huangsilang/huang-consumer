package com.example.huang.huangconsumer.service;

import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import com.example.huang.huangconsumer.repository.ConsumerRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ConsumerService {
    private Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private final String path = "D:\\222.xlsx";
    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private EntityManager entityManager;
    public Page<ConsumerPojo> pageList(ConsumerPojo cp,int pageNum,int pageSize) {
        Specification<ConsumerPojo> sp = null;
        if (null != cp) {
             sp = (root, query, cb) -> {
                List<Predicate> list = new ArrayList();
                if (StringUtils.isNotBlank(cp.getName())) {
                    list.add(cb.equal(root.get("name"), cp.getName()));
                }
                if (null != cp.getStartTime()) {
                    list.add(cb.greaterThanOrEqualTo(root.get("date").as(Date.class), cp.getStartTime()));
                }
                if (null != cp.getEndTime()) {
                    list.add(cb.lessThanOrEqualTo(root.get("date").as(Date.class), cp.getEndTime()));
                }
                if (StringUtils.isNotBlank(cp.getProduct())) {
                    list.add(cb.like(root.get("product"), "%"+cp.getProduct()+"%"));
                }
                if (StringUtils.isNotBlank(cp.getIsMust())) {
                    list.add(cb.equal(root.get("isMust"), cp.getIsMust()));
                }
                if (StringUtils.isNotBlank(cp.getType())) {
                    list.add(cb.equal(root.get("type"), cp.getType()));
                }
                 list.add(cb.equal(root.get("status"), "0"));
                return cb.and(list.toArray(new Predicate[list.size()]));
            };
        }
        Sort.Direction sort =  Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, sort, "date");
        return consumerRepository.findAll(sp,pageable);
    }

    public void save(ConsumerPojo consumerPojo) throws Exception {
        ConsumerPojo save = consumerRepository.save(consumerPojo);
    }
    @Transactional
    public void update(String id, BigDecimal howMuch) {
        String sql ="update consumer set how_much=:howMuch where id=:id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("howMuch",howMuch);
        query.setParameter("id",id);
        query.executeUpdate();
    }

    @Transactional
    public void delete(String id) {
        String sql ="update consumer set status=1 where id=:id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("id",id);
        int i = query.executeUpdate();

    }

    public List<ConsumerPojo> list(ConsumerPojo cp) {
        Specification<ConsumerPojo> sp = null;
        if (null != cp) {
            sp = (root, query, cb) -> {
                List<Predicate> list = new ArrayList();
                if (StringUtils.isNotBlank(cp.getName())) {
                    list.add(cb.equal(root.get("name"), cp.getName()));
                }
                if (null != cp.getStartTime()) {
                    list.add(cb.greaterThanOrEqualTo(root.get("date").as(Date.class), cp.getStartTime()));
                }
                if (null != cp.getEndTime()) {
                    list.add(cb.lessThanOrEqualTo(root.get("date").as(Date.class), cp.getEndTime()));
                }
                if (StringUtils.isNotBlank(cp.getProduct())) {
                    list.add(cb.like(root.get("product"), "%"+cp.getProduct()+"%"));
                }
                if (StringUtils.isNotBlank(cp.getIsMust())) {
                    list.add(cb.equal(root.get("isMust"), cp.getIsMust()));
                }
                if (StringUtils.isNotBlank(cp.getType())) {
                    list.add(cb.equal(root.get("type"), cp.getType()));
                }
                list.add(cb.equal(root.get("status"), "0"));
                return cb.and(list.toArray(new Predicate[list.size()]));
            };
        }
        return consumerRepository.findAll(sp);
    }

    public void statistics(List<ConsumerPojo> list){

    }


    /*
    //从excel中获取数据
    public List<ConsumerPojo> list(ConsumerPojo consumerPojo) {
        List<ConsumerPojo> list =null;
        try {
            String name = consumerPojo.getName();
            String product = consumerPojo.getProduct();
            Date startTime = consumerPojo.getStartTime();
            Date endTime = consumerPojo.getEndTime();
            String type = consumerPojo.getType();
            String isMust = consumerPojo.getIsMust();
            list =  OutExcelUtils.readExcel(path);
            if (StringUtils.isNotBlank(name)){
                list = list.stream().filter(l -> name.equals(l.getName())).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(product)){
                list =list.stream().filter(l->l.getProduct().contains(product)).collect(Collectors.toList());
            }
            if (startTime!=null){
                list =list.stream().filter(l->startTime.getTime()<=l.getTimeLong()).collect(Collectors.toList());
            }
            if (endTime!=null){
                list =list.stream().filter(l->endTime.getTime()>=l.getTimeLong()).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(type)){
                list =list.stream().filter(l->type.equals(l.getType())).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(isMust)){
                list =list.stream().filter(l->isMust.equals(l.getIsMust())).collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("查询异常",e);
        }
    return list;
    }


    public boolean save(ConsumerPojo consumerPojo) throws Exception {
            return OutExcelUtils.outExcel(consumerPojo,path);
    }
*/


}
