
package com.liberty;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class DeviceInfo {
    /**
     * 系统SDK的版本
     */
    public static final int VERSION = Build.VERSION.SDK_INT;
    private static final String TAG = "DeviceUtils";
    private static final long MAX_MEM = 512 * 1024L;

    /**
     * 获取设备名称
     *
     * @return
     */
    public static String geDeviceModel() {
        return Build.MODEL;
    }

    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    /**
     * 系统API是否高于19
     *
     * @return true 大于等于19
     */
    public static boolean isAboveKitkat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    /**
     * 是否为低端手机
     *
     * @return 根据内存判断，如果内存低于 512 * 1024L，则认为是低端机
     */
    public static boolean isCannotShowImage() {
        long tolalMem = getTolalMem();
        boolean flag = tolalMem <= MAX_MEM;
        Log.i(TAG, "内存是否小于 " + MAX_MEM + "? " + flag);
        return flag;
    }

    /**
     * 获取当前设备总内存
     *
     * @return 内存数 获取失败则为0
     */
    public static long getTolalMem() {
        long mTotal = 0;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        if (content != null) {
            content = content.substring(content.indexOf(':') + 1, content.indexOf('k')).trim();
            mTotal = Integer.parseInt(content);
        }
        return mTotal;
    }

    /**
     * 判断是否是小米系统的手机
     */
    public static boolean isXiaomiDevice() {
        Log.v(TAG, "build.version=" + Build.VERSION.RELEASE);
        return Build.VERSION.RELEASE.toLowerCase().contains("miui");
    }

    /**
     * 获得手机是不是设置了GPS开启状态
     *
     * @param context
     * @return true：gps开启，false：GPS未开启
     */
    public static boolean isGpsOpen(Context context) {
        if (context == null) {
            return false;
        }
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) {
            return false;
        }
        try {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ignore) {
            return false;
        }
    }

    /**
     * 计算当前VM的max size
     */
    public static float getVMMaxSize() {
        long maxMemory = Runtime.getRuntime().maxMemory();

        return (maxMemory / 1024f / 1024f);
    }

    /**
     * 获取当前VM Heap size
     */
    public static float getCurrentVMHeapSize() {
        Runtime rTime = Runtime.getRuntime();
        long usedVMHeapSize = rTime.totalMemory();

        return (usedVMHeapSize / 1024f / 1024f);
    }

    public static String getDCIMpath() {
        return Environment.DIRECTORY_DCIM;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString(); // 3ms
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, "WifiPreference IpAddress", ex);
        } catch (Exception e) {
            Log.e(TAG, "WifiPreference IpAddress", e);
        }

        return "";
    }
}
