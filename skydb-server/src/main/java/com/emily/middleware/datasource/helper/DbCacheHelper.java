package com.emily.middleware.datasource.helper;

import com.emily.middleware.datasource.entity.MiddleWare;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description :  数据库中间件缓存帮助类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/4 4:03 PM
 */
public class DbCacheHelper {
    public static final Map<String, MiddleWare> CACHE = new HashMap<>();
}
