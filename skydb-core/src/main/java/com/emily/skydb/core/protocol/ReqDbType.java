package com.emily.skydb.core.protocol;

/**
 * @Description :  请求SQL操作方式
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/3/2 7:18 PM
 */
public class ReqDbType {
    public final static String SELECT = "select";

    public final static String INSERT = "insert";

    public final static String UPDATE = "update";

    public final static String DELETE = "delete";

    public final static String TRANSACTION = "transaction";
}
