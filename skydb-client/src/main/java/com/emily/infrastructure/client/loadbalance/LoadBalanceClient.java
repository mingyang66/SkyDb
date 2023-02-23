package com.emily.infrastructure.client.loadbalance;

import com.emily.infrastructure.client.SkyClientProperties;
import com.emily.infrastructure.client.pool.SkyConnection;

import java.util.List;

/**
 * @program: SkyDb
 * @description: 负载均衡实现类
 * @author: Emily
 * @create: 2021/11/01
 */
@Deprecated
public class LoadBalanceClient implements ServiceInstanceChooser {
    private LoadBalance loadBalance;
    private SkyClientProperties properties;

    public LoadBalanceClient(LoadBalance loadBalance, SkyClientProperties properties) {
        this.loadBalance = loadBalance;
        this.properties = properties;
    }

    @Override
    public SkyConnection choose(List<String> serviceAddress) {
        //获取服务器地址
        String address = loadBalance.selectServiceAddress(serviceAddress);
        //创建Rpc连接对象
        SkyConnection connection = new SkyConnection(properties);
        //建立Rpc连接
        connection.connect(address);
        return connection;
    }
}
