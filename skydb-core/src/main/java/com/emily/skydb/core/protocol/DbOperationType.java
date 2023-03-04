package com.emily.skydb.core.protocol;

/**
 * @Description :  请求SQL操作方式
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/2 7:18 PM
 */
public class DbOperationType {
    public final static String SELECT = "select";

    public final static String INSERT = "insert";

    public final static String UPDATE = "update";

    public final static String DELETE = "delete";

    public final static String TRANSACTION = "transaction";
}
