package com.emily.skydb.core;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: 自定义RPC传输协议
 * @author: Emily
 * @create: 2021/09/17
 */
public class SkyRequest implements Serializable {

    /**
     * 事务唯一标识, 36位
     */
    private String traceId;
    /**
     * sql语句唯一标识
     */
    private String sqlId;
    /**
     * 数据库标识
     */
    private String dbName;
    /**
     * sql语句占位符参数
     */
    private Map<String, String> params = new LinkedHashMap<>();

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
