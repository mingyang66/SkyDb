package com.emily.skydb.client;

import java.math.BigDecimal;

/**
 * @Description :  继承子类
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/3/6 2:05 PM
 */
public class TestUserSub extends TestUser {
    private final Object object = new Object();

    private BigDecimal price;

    public Object getObject() {
        return object;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
