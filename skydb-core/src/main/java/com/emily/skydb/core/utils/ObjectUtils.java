package com.emily.skydb.core.utils;

import java.io.*;

/**
 * @Description :
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/23 2:37 PM
 */
public class ObjectUtils {
    public static byte[] serialize(Serializable obj) throws Exception {
        if (obj == null) {
            return new byte[0];
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
            object = (T) ois.readObject();
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
