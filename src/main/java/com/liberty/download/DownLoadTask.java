package com.liberty.download;

import android.text.TextUtils;
import android.util.Log;

import com.liberty.ApiService;
import com.liberty.Liberty;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;

/**
 * Author:LiuPen Created at 2017/6/5 14:41
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class DownLoadTask extends Thread {
    public static final String TAG = "DownLoadTask";
    private AtomicBoolean isDownload = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private String md5Str;
    private String url;
    private String path;
    private String name;
    private DownloadAdapter listener;
    private String baseUrl;

    DownLoadTask(String baseUrl, String url, String path, String name, String md5Str, DownloadAdapter listener) {
        this.baseUrl = baseUrl;
        this.url = url;
        this.path = path;
        this.name = name;
        this.md5Str = md5Str;
        this.listener = listener;
        isDownload.set(true);
        MD5Util.createMD5Str(url);
    }

    @Override
    public void run() {
        File file = null;
        RandomAccessFile rw = null;
        BufferedReader bufferedReader = null;
        ResponseBody responseBody = null;
        try {
            if (isRunning.get()) return;
            isRunning.set(true);
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(url) || TextUtils.isEmpty(name) || TextUtils.isEmpty(md5Str)) {
                if (listener != null) {
                    listener.onFaile(path + name + "");
                }
                LibertyDownload.removeTask(md5Str);
                return;
            }
            ApiService apiService = new Liberty.Builders().baseUrl(baseUrl).build().create(ApiService.class);
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            file = new File(fileDir, name);
            String md5Digest = MD5Util.getMD5Digest(file.getPath());
            if (md5Str.equalsIgnoreCase(md5Digest)) {
                if (listener != null) {
                    listener.onSuccess(path + name);
                    LibertyDownload.removeTask(md5Str);
                }
                return;
            }

            long downLength = 0;
            File lengthFile = null;
            if (!file.exists()) {//文件不存在
                responseBody = apiService.downBigFile(url).execute().body();
                rw = new RandomAccessFile(file, "rw");
                rw.setLength(responseBody.contentLength());
                rw.close();
            } else {//文件存在
                lengthFile = new File(path, md5Str + ".txt");
                if (lengthFile.exists()) {
                    bufferedReader = new BufferedReader(new FileReader(lengthFile));
                    String readLine = bufferedReader.readLine();
                    if (!TextUtils.isEmpty(readLine)) {
                        downLength = Long.parseLong(readLine);
                    }
                }
                if (downLength > file.length()) {
                    if (lengthFile.exists()) {
                        lengthFile.delete();
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                }
                String range = "bytes=" + downLength + "-";
                responseBody = apiService.downBigFile(range, url).execute().body();
            }
            boolean writeToDisk = writeToDiskRanAc(path, name, downLength, responseBody, listener);
            if (listener != null) {
                if (writeToDisk) {
                    listener.onSuccess(path + name);
                    if (lengthFile != null) {
                        lengthFile.delete();
                    }
                } else {
                    listener.onFaile(path + name);
                }
                LibertyDownload.removeTask(md5Str);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFaile(path + name);
            }
            LibertyDownload.removeTask(md5Str);
        } finally {
            try {
                if (responseBody != null) {
                    responseBody.close();
                }
                if (rw != null) {
                    rw.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public void stopTask() {
        isDownload.set(false);
        LibertyDownload.removeTask(md5Str);
    }

    private boolean writeToDiskRanAc(String path, String fileName, long alreadLength, ResponseBody body, DownloadListener listener) {

        BufferedInputStream bis = null;
        RandomAccessFile rw = null;
        BufferedWriter bufferedWriter = null;
        try {
            boolean isNew = false;
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(fileDir, fileName);
            long alreadyDownload = 0;
            if (!(alreadLength > 0)) {
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
                rw.seek(alreadLength);
            }
            File lengthFile = new File(path, md5Str + ".txt");
            bufferedWriter = new BufferedWriter(new FileWriter(lengthFile, false));
            while (isDownload.get() && (readLen = bis.read(buff)) != -1) {
                rw.write(buff, 0, readLen);
                alreadyDownload += readLen;
                bufferedWriter.write(alreadyDownload + "");
                bufferedWriter.flush();
                if (listener != null) {
                    listener.updateProgress(alreadyDownload, fileSize);
                }
            }
            if (listener != null) {
                listener.updateProgress(alreadyDownload, fileSize);
            }
            if (lengthFile != null) {
                lengthFile.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (body != null) {
                    body.close();
                }
                if (rw != null) {
                    rw.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
