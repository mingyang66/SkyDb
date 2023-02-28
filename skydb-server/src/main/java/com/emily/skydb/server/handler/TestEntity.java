package com.emily.skydb.server.handler;

import java.io.Serializable;

/**
 * @Description :  测试实体类
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/2/25 5:21 PM
 */
public class TestEntity implements Serializable {
    public String username;
    public String password;

    public TestEntity() {
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
