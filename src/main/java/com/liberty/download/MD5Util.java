package com.liberty.download;

import android.text.TextUtils;
import android.util.Log;

import com.liberty.download.HexUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 魏铮铮 on 15/1/6.
 */
class MD5Util {
    private static final String TAG = "MD5Util";

    public static String getMD5DigestByContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("MD5");
            digester.reset();
            digester.update(content.getBytes(), 0, content.length());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
        if (digester != null) {
            return HexUtil.toHexLowerCaseString(digester.digest());
        } else {
            return null;
        }
    }

    public static byte[] createMD5(byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("MD5");
            digester.reset();
            digester.update(content, 0, content.length);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
        if (digester != null) {
            return digester.digest();
        } else {
            return null;
        }
    }

    public static String createMD5WithHex(byte[] content, boolean upperCase) {
        byte[] reslut = createMD5(content);
        if (reslut == null || reslut.length == 0) {
            return "";
        } else {
            return HexUtil.toHexString(reslut, upperCase);
        }
    }

    public static String createMD5WithHex(String content, boolean upperCase) {
        byte[] reslut = createMD5(content);
        if (reslut == null || reslut.length == 0) {
            return "";
        } else {
            return HexUtil.toHexString(reslut, upperCase);
        }
    }

    public static byte[] createMD5(String context, String charsetName) {
        if (TextUtils.isEmpty(context)) {
            return null;
        }
        try {
            return createMD5(context.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public static byte[] createMD5(String content) {
        return createMD5(content, "UTF-8");
    }

    public static String createMD5Str(String content) {
        return createMD5WithHex(content, "UTF-8");
    }

    public static String createMD5WithHex(String content, String charsetName) {
        byte[] reslut = createMD5(content, charsetName);
        if (reslut == null || reslut.length == 0) {
            return "";
        } else {
            return HexUtil.toHexUpperCaseString(reslut);
        }
    }

    /**
     * 计算文件的md5
     */
    public static String getMD5Digest(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            return "";
        }
        FileInputStream inputStream = null;
        MessageDigest digester = null;

        try {
            inputStream = new FileInputStream(filePath);
            digester = MessageDigest.getInstance("MD5");

            int length;
            byte[] buffer = new byte[1024 * 32];

            while ((length = inputStream.read(buffer)) > 0) {
                digester.update(buffer, 0, length);
            }

        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return "";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
        }

        if (digester != null) {
            return HexUtil.toHexUpperCaseString(digester.digest());
        } else {
            return "";
        }
    }

}
