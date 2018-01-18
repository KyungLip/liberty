package com.liberty;

import android.util.Log;

import java.io.IOException;

/**
 * Created by yangz on 2016/12/5.
 */

public class UnknownHostManager {
    private static final String TAG = "PROTOCOL_HTTP";
    private static final String PROTOCOL_HTTP = "http://";

    private boolean isUnknownHost = false;
    private long unknownStartTime;

    private static UnknownHostManager unknownHostManager;

    private UnknownHostManager() {
    }

    public static UnknownHostManager getInstance() {
        if (unknownHostManager == null) {
            synchronized (UnknownHostManager.class) {
                if (unknownHostManager == null) {
                    unknownHostManager = new UnknownHostManager();
                    return unknownHostManager;
                }
            }
        }
        return unknownHostManager;
    }

    public synchronized boolean check(String domain) {
        if (!isUnknownHost) return false;
        long currentTime = System.currentTimeMillis();
        if (currentTime - unknownStartTime > 1800000) {     //超过30分钟
            refreshHostStatus(domain);
        }
        return isUnknownHost;
    }

    synchronized void parseHost(String url) {
        if (url.contains(PROTOCOL_HTTP)) {
            url = url.replace(PROTOCOL_HTTP, "");
        }
        if (url.contains("/")) {
            url = url.substring(0, url.indexOf("/"));
        }
        refreshHostStatus(url);
    }

    private void refreshHostStatus(String domain) {
        isUnknownHost = !pingDomain(domain);
        if (isUnknownHost) {
            unknownStartTime = System.currentTimeMillis();
        }
    }

    private boolean pingDomain(String domain) {
        Process process;
        int status = 0;
        try {
            process = Runtime.getRuntime().exec("ping -c 1 -w 2 " + domain);
            status = process.waitFor();
        } catch (IOException | InterruptedException e) {
            Logger.e("ServerURL", "ping域名 " + domain + " 异常: " + e.getMessage());
        }
        if (status != 0) {
            Logger.e("ServerURL", domain + " ping不通");
        }
        return status == 0;
    }
}
