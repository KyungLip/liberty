package com.liberty.download;

/**
 * Created by 魏铮铮 on 15/1/6.
 */
 class HexUtil {
    /**
     * 将byte[]数组生成为HEXString
     *
     * @param bytes     内容数组
     * @param separator 连字符，如：00-11-FF
     * @param upperCase 是否使用大写字母
     * @return hex字符串，如果bytes为空，返回空串
     */
    public static String toHexString(byte[] bytes, String separator, boolean upperCase) {
        if (bytes == null) {
            return "";
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(0xFF & b); // SUPPRESS CHECKSTYLE
            if (upperCase) {
                str = str.toUpperCase();
            }
            if (str.length() == 1) {
                hexString.append("0");
            }
            hexString.append(str).append(separator);
        }
        return hexString.toString();
    }

    public static String toHexString(byte[] bytes, boolean upperCase) {
        return toHexString(bytes, "", upperCase);
    }

    public static String toHexUpperCaseString(byte[] bytes) {
        return toHexString(bytes, true);
    }

    public static String toHexLowerCaseString(byte[] bytes) {
        return toHexString(bytes, false);
    }

}
