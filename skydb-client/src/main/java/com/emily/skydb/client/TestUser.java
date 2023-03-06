package com.emily.skydb.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Description :  测试实体类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/25 5:21 PM
 */
public class TestUser {
    //public int id;
    private String name;
    private String colour;
    private int age;
    private LocalDateTime updateTime;
    private LocalDate year;
    private LocalTime dbTime;
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalTime getDbTime() {
        return dbTime;
    }

    public void setDbTime(LocalTime dbTime) {
        this.dbTime = dbTime;
    }

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
