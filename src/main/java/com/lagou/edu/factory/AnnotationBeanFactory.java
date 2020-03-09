package com.lagou.edu.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.lagou.edu.annotation.Autowired;
import com.lagou.edu.annotation.Component;
import com.lagou.edu.annotation.Transactional;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.utils.ClassScaner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注解专用bean工厂
 */

@PropertySource({"classpath:jdbc.properties"})
public class AnnotationBeanFactory {
    //存储创建的bean对象，key 为类名
    Map<String, Object> beans = new HashMap<>();
    ProxyFactory proxyFactory = new ProxyFactory();

    public static AnnotationBeanFactory annotationBeanFactory = new AnnotationBeanFactory();
    private AnnotationBeanFactory(){
        String[] s = {"com.lagou.edu"};
        createBeans(s);
    }

    public void createBeans(String[] packages) {
        //加入DataSource类
//        beans.put(DataSource.class.getName(), createDataSource());
        //从网上找来的解析包下所有类名的工具类
        Set<Class> classes = ClassScaner.scan(packages, null);

        //清理容器
        beans.clear();
        for (Class c : classes) {
            doComponent(c);
        }
    }

    private void doComponent(Class c) {
        Component component = (Component) c.getAnnotation(Component.class);


        //获取c的component注解,如果不为null且c不是注解，便实例化
        if (!c.isAnnotation() && component != null) {
            //获取注解元素值
            String name = component.value();
            if (name.length() < 1) {
                name = c.getName();
            }
            try {
                Object o = c.newInstance();
                beans.put(name, o);
                doAutowired(o);
                doTransactional(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //处理autowired注解
    private void doAutowired(Object o) {
        for (Field field : o.getClass().getDeclaredFields()) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                String name = autowired.value();
                String fieldName = field.getType().getName();
                if (name.length() < 1){
                    name = o.getClass().getName();
                }
                Object fieldObject = beans.get(fieldName);
                if (fieldObject != null) {
                    setField(field, o, fieldObject);
                } else {
                    doComponent(field.getType());
                    fieldObject = beans.get(fieldName);
                    setField(field, o, fieldObject);
                }
            }
        }
    }

    //处理事务注解
    private void doTransactional(Object o) {
        if (o.getClass().getDeclaredAnnotation(Transactional.class) != null) {
            if (o.getClass().getInterfaces().length > 0) {
                o = proxyFactory.getJdkProxy(o);
            } else {
                o = proxyFactory.getCglibProxy(o);
            }

            beans.put(o.getClass().getName(), o);
        }
    }

    //强制为私有属性赋值
    private void setField(Field field, Object o, Object fieldObject) {
        field.setAccessible(true);

        try {
            field.set(o, fieldObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    public Object getBean(String key){
        return beans.get(key);
    }
}
