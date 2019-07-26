package com.example.huang.huangconsumer.task;

import com.example.huang.huangconsumer.constant.Constant;
import com.example.huang.huangconsumer.pojo.ConsumerPojo;
import com.example.huang.huangconsumer.pojo.WeekCount;
import com.example.huang.huangconsumer.service.WeekCountService;
import com.example.huang.huangconsumer.utils.PieChartUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmailTask {

    private Logger logger = LoggerFactory.getLogger(EmailTask.class);
    @Autowired
    private WeekCountService weekCountService;
    @Autowired
    private CountTask countTask;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");

    int week=1;

    @Scheduled(cron = "0 00 09 ? * SAT")
    public void execute() {
        try {
            countTask.weekCountTime();
            long lo = System.currentTimeMillis();
            ConsumerPojo consumerPojo = new ConsumerPojo();
            consumerPojo.setStartTime(Constant.map.get("weekStartTime"));
            consumerPojo.setEndTime(Constant.map.get("weekEndTime"));
            try {
                week = weekCountService.findWeek();
            } catch (Exception e) {
                week=1;
            }
            List<WeekCount> list = weekCountService.list(week);//获取最近的四周数据

            if(CollectionUtils.isEmpty(list)){
                logger.info("没有消费记录");
                return ;
            }
            //根据星期分类
            Map<Integer,List<WeekCount>> map = new HashMap<>();
            //每星期总的消费
            List<WeekCount> listTotal = list.stream().filter(l -> l.getType() == 0).sorted(Comparator.comparing(WeekCount::getWeek).reversed()).collect(Collectors.toList());
            //按名字
            List<WeekCount> listByName = list.stream().filter(l -> l.getType() == 1).sorted(Comparator.comparing(WeekCount::getWeek).reversed()).collect(Collectors.toList());

            List<WeekCount> listByProduct = list.stream().filter(l -> l.getType() == 2).sorted(Comparator.comparing(WeekCount::getWeek).reversed()).collect(Collectors.toList());

            List<WeekCount> listByType = list.stream().filter(l -> l.getType() == 3).sorted(Comparator.comparing(WeekCount::getWeek).reversed()).collect(Collectors.toList());

            List<WeekCount> listByIsMust = list.stream().filter(l -> l.getType() == 4).sorted(Comparator.comparing(WeekCount::getWeek).reversed()).collect(Collectors.toList());
            StringBuffer sb = new StringBuffer();
            sb.append(Constant.TOP).append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            sb.append("本周总消费：").append(listTotal.get(0).getAccount()).append("元<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            listByName.stream().filter(f->week==f.getWeek()).forEach(e->
                    sb.append(e.getName()).append("消费：").append(e.getAccount()).append("元<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
            //剩余的都做成图

            String imgByNameUrl = img(listByName,lo + Constant.IMG_BY_NAME_PATH,"本周张漂亮小姐姐和黄先生的消费情况");
            String imgByProductUrl = img(listByProduct,lo + Constant.IMG_BY_PRODUCT_PATH,"本周购买产品详情");
            String imgByTypeUrl = img(listByType,lo + Constant.IMG_BY_TYPE_PATH,"本周分类统计");
            String imgByIsMustUrl = img(listByIsMust,lo + Constant.IMG_BY_IS_MUST_PATH,"本周是否必须统计");
            sb.append("<img src=\""+imgByNameUrl+"\" alt=\"\" />");
            sb.append("<img src=\""+imgByProductUrl+"\" alt=\"\" />");
            sb.append("<img src=\""+imgByTypeUrl+"\" alt=\"\" />");
            sb.append("<img src=\""+imgByIsMustUrl+"\" alt=\"\" />");
            sb.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">♥LOVE YOU♥</font>").
                    append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(Constant.LAST);
            //邮件主题
            String subject = sdfTime.format(Constant.map.get("weekStartTime"))+"到"+sdfTime.format(Constant.map.get("weekEndTime"))+"消费情况";

            Map<String, String> uriVariables = new HashMap<String, String>();
            //发送电子邮件
            uriVariables.put("subject", subject);
            uriVariables.put("context", sb.toString() );
            uriVariables.put("toemail", Constant.EMAIL_ADDRESS);
            //uriVariables.put("ccemail", ccemail);
            uriVariables.put("fileName", subject);
            logger.info("发送邮箱:"+uriVariables);
            executeAsyncExcel(uriVariables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String img(List<WeekCount> list,String fileName,String name){
        DefaultPieDataset dataset = new DefaultPieDataset( );
        list.stream().filter(f -> week == f.getWeek()).forEach(e->
                        dataset.setValue(e.getType()==4?e.getName()+"-"+e.getAccount()+"元("+e.getTypeName()+")" : e.getTypeName()+"-"+e.getAccount()+"元",e.getAccount())
                );
        String url = Constant.STSYEM_IP+Constant.EMAIL_IMG_PATH+fileName;//访问服务图片地址
       return PieChartUtils.jFreeChart( name,  dataset, Constant.EMAIL_FILE_PHTH+fileName,url);
    }

    public void executeAsyncExcel(Map<String, String> param) {
        try {
            final String subject = String.valueOf(param.get("subject"));
            final String context = String.valueOf(param.get("context"));
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", "smtp.163.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Constant.EMAIL_ADDRESS, Constant.EMAIL_PASSWORD);
                }
            });
            MimeMessage message = new MimeMessage(session);
            // 设置邮件的发件人、收件人、主题
            // 附带发件人名字
            message.setFrom(new InternetAddress(Constant.EMAIL_ADDRESS));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(Constant.EMAIL_ADDRESS, false));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Constant.TO_EMAIL_ADDRESS,  false));
            message.setSubject(subject);
            // 文本部分
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent("<html><head></head><body>" + context + "</body></html>",
                    "text/html;charset=UTF-8");
            // 整合内容
            MimeMultipart mmp = new MimeMultipart();
            mmp.addBodyPart(textPart);
            message.setContent(mmp);
            Transport.send(message);
            logger.info("sendEmail success and end!");
        } catch (Exception e) {
            logger.error("邮件发送异常",e);
        }
    }

}
