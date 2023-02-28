package com.emily.skydb.core.protocol;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: 自定义RPC传输协议
 * @author: Emily
 * @create: 2021/09/17
 */
public class TransBody implements Serializable {

    /**
     * sql语句唯一标识
     */
    public String sqlId;
    /**
     * 数据库标识
     */
    public String dbName;
    /**
     * sql语句占位符参数
     */
    public Map<String, String> params = new LinkedHashMap<>();

}
