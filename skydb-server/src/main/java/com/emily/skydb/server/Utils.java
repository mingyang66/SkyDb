package com.emily.skydb.server;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description :
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/4/10 3:30 PM
 */
public class Utils {
    private static DruidDataSource druidDataSource;
    static {
        try {
            //方式2：通过创建一个druidDatasouurce，后面手动完成数据源的初始化
            druidDataSource=new DruidDataSource();
            druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/ocean_sky?characterEncoding=utf-8&rewriteBatchedStatements=true&yearIsDateType=false");
            druidDataSource.setUsername("root");
            druidDataSource.setPassword("smallgrain");
            //可选设置
            druidDataSource.setMaxActive(20);
            druidDataSource.setInitialSize(10);
            druidDataSource.setMaxWait(5000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()  {
        try {
            return druidDataSource.getConnection();
        } catch (SQLException e) {
            //如果连接出现异常，则抛出一个运行时异常
            throw new RuntimeException("连接池出现异常.");
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();
        Statement stat = connection.createStatement();
        String sql = "SELECT convert(s.price/s.age, decimal(15,8)) as bprice,s.* FROM sailboat s";
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getBigDecimal("bprice"));
            System.out.println(rs.getString("bprice"));
        }
        connection.close();
        stat.close();
        connection.close();
    }
}
