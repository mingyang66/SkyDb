package com.emily.infrastructure.core;

import java.io.*;

/**
 * @Description :
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/2/23 2:37 PM
 */
public class ObjectUtils {
    public static byte[] serialize(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bo);
        oos.writeObject(obj);
        return bo.toByteArray();
    }
    public static <T> T deserialize(byte[] bytes) {
        T object = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            object = (T)ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return object;
    }

}
