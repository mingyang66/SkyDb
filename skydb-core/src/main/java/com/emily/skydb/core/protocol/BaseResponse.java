package com.emily.skydb.core.protocol;


import com.emily.skydb.core.enums.HttpStatusType;

import java.io.Serializable;

/**
 * @program: SkyDb
 * @description: RPC调用响应数据
 * @author: Emily
 * @create: 2021/10/30
 */
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private int status;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应结果
     */
    private T data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> BaseResponse<T> buildResponse(T data) {
        return buildResponse(HttpStatusType.OK.getStatus(), HttpStatusType.OK.getMessage(), data);
    }

    public static <T> BaseResponse<T> buildResponse(int status, String message, T data) {
        BaseResponse response = new BaseResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
