package com.liberty.download;

import android.text.TextUtils;
import android.util.Log;

import com.liberty.ApiService;
import com.liberty.Liberty;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:LiuPen Created at 2017/6/5 14:41
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class DownLoadTask {
    public static final String TAG = "DownLoadTask";
    private AtomicBoolean isDownload = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private String identify;
    private String url;
    private String path;
    private String name;
    private DownloadAdapter listener;

    public DownLoadTask(String url, String path, String name, String identify, DownloadAdapter listener) {
        this.url = url;
        this.path = path;
        this.name = name;
        this.identify = identify;
        this.listener = listener;
        isDownload.set(true);
        MD5Util.createMD5Str(url);
    }

    public synchronized void start() {
        File file = null;
        String path = "";
        try {
            if (isRunning.get()) return;
            isRunning.set(true);

            ApiService apiService = Liberty.create(ApiService.class);
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            file = new File(fileDir, name);
            path = file.getAbsolutePath();
            long downLength = file.length();
            if (file.exists()) {
            }
            String range = "bytes=" + downLength + "-";
            Call<ResponseBody> call = apiService.downBigFile(range, url);
            Response<ResponseBody> response = call.execute();
            boolean writeToDisk = writeToDiskRanAc(path, name, response.body(), listener);
            if (listener != null) {
                if (writeToDisk) {
                    listener.onSuccess(path);
                } else {
                    listener.onFaile(path);
                }
                LibertyDownload.removeTask(identify);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFaile(path);

            }
            LibertyDownload.removeTask(identify);
            Log.e(TAG, e.toString());
        }

    }

    public void stop() {
        isDownload.set(false);
    }

    private boolean writeToDiskRanAc(String path, String fileName, ResponseBody body, DownloadListener listener) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        BufferedInputStream bis = null;
        RandomAccessFile rw = null;
        try {
            boolean isNew = false;
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(fileDir, fileName);
            long alreadyDownload = 0;
            if (file.exists()) {
                alreadyDownload = file.length();
            } else {
                isNew = true;
            }
            long fileSize = body.contentLength();
            byte[] buff = new byte[1024];
            int readLen = 0;
            bis = new BufferedInputStream(body.byteStream());
            rw = new RandomAccessFile(file, "rw");
            if (isNew) {
                rw.seek(0);
            } else {
                rw.seek(alreadyDownload);
            }
            while (isDownload.get() && (readLen = bis.read(buff)) != -1) {
                rw.write(buff, 0, readLen);
                alreadyDownload += readLen;
                if (listener != null) {
                    listener.updateProgress(alreadyDownload, fileSize);
                }
            }
            if (listener != null) {
                listener.updateProgress(alreadyDownload, fileSize);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rw != null) {
                    rw.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
