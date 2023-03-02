package com.emily.skydb.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/ocean_sky?characterEncoding=utf-8&rewriteBatchedStatements=true";
        String user = "root";
        String password = "smallgrain";
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("get Connection SUCCESS !");
            }
            String sql = "INSERT INTO sailboat(name,colour,age) VALUES('田晓霞','女',26)";
            PreparedStatement statement = conn.prepareStatement(sql);
            int row = statement.executeUpdate();
            System.out.println("新增数据成功：" + row);

            String updateSql = "UPDATE sailboat s SET s.age=22 WHERE s.name='红心薯' ";
            statement = conn.prepareStatement(updateSql);
            row = statement.executeUpdate();
            System.out.println("更新数据条数：" + row);

            String querySql = "SELECT * FROM sailboat s";
            statement = conn.prepareStatement(querySql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "---" + rs.getString("name") + "---" + rs.getInt("age"));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
