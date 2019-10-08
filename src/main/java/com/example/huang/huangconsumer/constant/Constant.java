package com.example.huang.huangconsumer.constant;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static Map<String, Date> map = new HashMap<>();

    public static int week =1;
    //public static String FILE_PHTH ="D:\\project2\\zaizhi\\xc-ui-pc-teach\\dist\\static\\images\\chart\\";
    public static String FILE_PHTH ="/opt/apache-tomcat-8.5.42/webapps/ROOT/static/images/chart/";
    public static String EMAIL_FILE_PHTH ="/opt/apache-tomcat-8.5.42/webapps/ROOT/static/images/";
    //按姓名统计
    public static String IMG_BY_NAME_PATH ="ImgByName.jpeg";
    //按产品统计
    public static String IMG_BY_PRODUCT_PATH ="ImgByProduct.jpeg";
    //按产品类型
    public static String IMG_BY_TYPE_PATH ="ImgByType.jpeg";
    //按是否必须
    public static String IMG_BY_IS_MUST_PATH ="ImgByIsMust.jpeg";

    public static String IMG_PATH="\\static\\images\\chart\\";

    public static String STSYEM_IP="http://47.101.10.186:8080";

    public static String EMAIL_IMG_PATH="\\static\\images\\";

    public static String TOTAL_NAME="共同消费";

    public static String  TO_EMAIL_ADDRESS="123456@qq.com";
    public static String EMAIL_ADDRESS="123456@163.com";

    public static String EMAIL_PASSWORD="123456";

    public static String TOP="亲爱的张漂亮小姐姐:";
    public static String LAST="黄先生";

    public static String getTypeName(int i){
        switch (i){
            case 0:
                return "衣";
            case 1:
                return "食";
            case 2:
                return "住";
            case 3:
                return "行";
            case 4:
                return "玩";
            case 5:
                return "学习";
            case 6:
                return "生活用品";
            default :
                return "其他";
        }
    }
    public static String getIsMust(int i){
        switch (i){
            case 0:
                return "必须";
            case 1:
                return "非必须";
            default :
                return "其他";
        }
    }


}
