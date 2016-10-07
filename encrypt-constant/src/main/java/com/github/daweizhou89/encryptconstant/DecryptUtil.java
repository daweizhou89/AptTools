package com.github.daweizhou89.encryptconstant;

/**
 * Created by daweizhou89 on 2016/10/7.
 */

public class DecryptUtil {

    private static final byte XOR_KEY = 0x66;

    public static String decodeString(String value) {
        if (value == null) {
            return null;
        }
        byte[] byteArray = value.getBytes();
        for (int i = 0; i < byteArray.length; ++i) {
            byteArray[i] = (byte) (byteArray[i] ^ XOR_KEY);
        }
        return new String(byteArray);
    }

}
