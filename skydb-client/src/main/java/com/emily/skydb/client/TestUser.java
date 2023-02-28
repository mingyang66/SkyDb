package com.emily.skydb.client;

/**
 * @Description :  测试实体类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/25 5:21 PM
 */
public class TestUser {
    public String username;
    public String password;

    public TestUser() {
    }

    public TestUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
