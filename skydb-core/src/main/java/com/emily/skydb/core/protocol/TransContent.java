package com.emily.skydb.core.protocol;


import java.util.ArrayList;
import java.util.List;

/**
 * @program: SkyDb
 * @description: 自定义RPC传输协议
 * @author: Emily
 * @create: 2021/09/17
 */
public class TransContent {

    /**
     * sql语句唯一标识
     */
    public String dbTag;
    /**
     * 数据库标识
     */
    public String dbName;
    /**
     * sql语句占位符参数
     */
    public List<DbModelItem> params = new ArrayList<>();

}
