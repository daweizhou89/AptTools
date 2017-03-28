package com.github.daweizhou89.encryptconstant;

/**
 *
 * Created by daweizhou89 on 2016/10/7.
 */

public class EncryptUtil {

    public static String encodeString(String value, String key) {
        if (value == null || key == null) {
            return value;
        }
        byte[] keyBytes = key.getBytes();
        byte[] byteArray = value.getBytes();
        int pos = 0;
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = (byte) (byteArray[i] ^ keyBytes[pos++]);
            if (pos >= keyBytes.length) {
                pos = 0;
            }
        }
        return new String(byteArray);
    }

}
