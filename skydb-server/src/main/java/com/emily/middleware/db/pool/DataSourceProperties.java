package com.emily.middleware.db.pool;

/**
 * @Description :  数据库基础属性配置
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/1 2:36 PM
 */
public class DataSourceProperties {
    /**
     * 数据库标识
     */
    private String dbType;
    /**
     * 驱动
     */
    private String driver;
    /**
     * 地址
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
