package com.emily.skydb.core.protocol;

/**
 * @Description :  请求SQL参数
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/3/2 7:15 PM
 */
public class ReqDbItem {
    /**
     * 字段名
     */
    public String name;
    /**
     * 子单值
     */
    public String value;
    /**
     * 字段类型
     */
    public String valueType;

    public ReqDbItem() {
    }

    public ReqDbItem(String name, String value) {
        this(name, value, "string");
    }

    public ReqDbItem(String name, String value, String valueType) {
        this.name = name;
        this.value = value;
        this.valueType = valueType;
    }
}
