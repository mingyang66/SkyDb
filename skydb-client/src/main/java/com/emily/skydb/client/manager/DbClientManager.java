package com.emily.skydb.client.manager;

import com.emily.skydb.client.helper.DbDataHelper;
import com.emily.skydb.client.pool.ChannelPoolClient;
import com.emily.skydb.client.pool.PoolProperties;
import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.protocol.TransContent;
import com.emily.skydb.core.protocol.TransHeader;
import com.emily.skydb.core.utils.UUIDUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: RPC客户端代理配置类
 * @author: Emily
 * @create: 2021/09/22
 */
public class DbClientManager {

    private static ChannelPoolClient channelPoolClient = new ChannelPoolClient(new PoolProperties());

    /**
     * 查询操作
     *
     * @param transContent 请求参数
     * @param cls          响应数据类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> executeQuery(TransContent transContent, Class<T> cls) throws Exception {
        TransHeader transHeader = new TransHeader(UUIDUtils.randomSimpleUUID());
        List<Map<String, DbModelItem>> list = channelPoolClient.sendRequest(transHeader, transContent, new TypeReference<>() {
        });
        return DbDataHelper.getDbEntity(list, cls);
    }

    /**
     * 更新、删除、插入操作
     *
     * @param transContent 请求参数
     * @return 影响行数
     * @throws Exception
     */
    public static int executeUpdate(TransContent transContent) throws Exception {
        TransHeader transHeader = new TransHeader(UUIDUtils.randomSimpleUUID());
        int rows = channelPoolClient.sendRequest(transHeader, transContent, new TypeReference<Integer>() {
        });
        return rows;
    }
}
