package com.lagou.edu.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author 应癫
 */
public class DruidUtils {

//    private DruidUtils(){
//    }

    private static DruidDataSource druidDataSource = new DruidDataSource();


    static {
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql:///mybatis?useUnicode=true&characterEncoding=utf8");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("zhang519");

    }

    public static DruidDataSource getInstance() {
        return druidDataSource;
    }

    @Test
    public void test() throws SQLException {
        DruidUtils.getInstance().getConnection();
    }
}
