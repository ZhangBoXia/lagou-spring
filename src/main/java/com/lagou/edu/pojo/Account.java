package com.lagou.edu.pojo;

import com.lagou.edu.annotation.Autowired;
import com.lagou.edu.dao.impl.JdbcAccountDaoImpl;
import com.lagou.edu.factory.AnnotationBeanFactory;
import com.lagou.edu.service.impl.TransferServiceImpl;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author 应癫
 */
public class Account {

    private String cardNo;
    private String name;
    private int money;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCardNo() { return cardNo; }

    public void setCardNo(String cardNo) { this.cardNo = cardNo;}

    @Override
    public String toString() {
        return "Account{" +
                "cardNo='" + cardNo + '\'' +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Account account = new Account();
//        Class<?> name = Class.forName();
//        System.out.println(account.getClass().getDeclaredField("name").getType());
        String name = account.getClass().getName();
//        System.out.println(name);
//        System.out.println(TransferServiceImpl.class.getName());
        AnnotationBeanFactory annotationBeanFactory = AnnotationBeanFactory.annotationBeanFactory;
//        System.out.println(annotationBeanFactory.getBean("dd"));


    }
}
