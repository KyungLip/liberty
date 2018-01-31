package com.liberty.download;

import android.text.TextUtils;

import com.liberty.ApiService;
import com.liberty.Liberty;
import com.liberty.Logger;

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
    private String lengthFileName;

    DownLoadTask(String baseUrl, String url, String path, String name, String md5Str, DownloadAdapter listener) {
        this.baseUrl = baseUrl;
        this.url = url;
        this.path = path;
        this.name = name;
        this.md5Str = md5Str;
        this.listener = listener;
        isDownload.set(true);
        lengthFileName = md5Str + ".txt";
    }

    @Override
    public void run() {
        if (isRunning.get()) return;
        isRunning.set(true);
        File downloadFile;
        ResponseBody responseBody = null;
        long fileSize;
        try {
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(url) || TextUtils.isEmpty(name) || TextUtils.isEmpty(md5Str)) {
                LibertyDownload.removeTask(md5Str);
                if (listener != null) {
                    listener.onFaile(path + name);
                }
                return;
            }
            ApiService apiService = new Liberty.Builders().connectTimeout(60 * 1000L).readimeout(60 * 1000L).baseUrl(baseUrl).build().create(ApiService.class);
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            downloadFile = new File(fileDir, name);
            String md5Digest = MD5Util.getMD5Digest(downloadFile.getPath());
            if (md5Str.equalsIgnoreCase(md5Digest)) {
                if (listener != null) {
                    listener.onSuccess(path + name);
                    LibertyDownload.removeTask(md5Str);
                }
                return;
            }
            long downLength = 0;
            if (!downloadFile.exists()) {//文件不存在
                responseBody = apiService.downBigFile(url).execute().body();
                fileSize = responseBody.contentLength();
                Logger.d(TAG, "视频文件不存在！");
            } else {//文件存在
                fileSize = downloadFile.length();
                Logger.d(TAG, "视频文件存在！" + fileSize);
                File lengthFile = new File(path, lengthFileName);
                downLength = readDownloadedLength(lengthFile);
                if (downLength > fileSize) {
                    downLength = 0;
                    delFile(lengthFile);
                    delFile(downloadFile);
                }
                Logger.d(TAG, "读取到的下载进度:" + downLength);
                String range = "bytes=" + downLength + "-" + fileSize;
                responseBody = apiService.downBigFile(range, url).execute().body();
            }
            boolean writeToDisk = writeToDiskRanAc(path, name, downLength, fileSize, responseBody, listener);
            if (listener != null) {
                if (writeToDisk) {
                    listener.onSuccess(path + name);
                } else {
                    listener.onFaile(path + name);
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onFaile(path + name);
            }
            Logger.d(TAG, e.toString());
        } finally {
            try {
                isRunning.set(false);
                LibertyDownload.removeTask(md5Str);
                if (responseBody != null) {
                    responseBody.close();
                }
            } catch (Exception e) {
                Logger.e(TAG, e.toString(), e);
            }
        }
    }

    public void stopTask() {
        isDownload.set(false);
        LibertyDownload.removeTask(md5Str);
    }

    private boolean writeToDiskRanAc(String path, String fileName, long alreadyDownload, long fileSize, ResponseBody body, DownloadListener listener) {
        boolean isRecordBreakpoint = true;
        BufferedInputStream bis = null;
        RandomAccessFile rw = null;
        try {
            boolean isNew = false;
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(fileDir, fileName);
            if (!(alreadyDownload > 0)) {
                isNew = true;
            }
            byte[] buff = new byte[1024];
            int readLen;
            bis = new BufferedInputStream(body.byteStream());
            rw = new RandomAccessFile(file, "rw");
            if (isNew) {
                rw.seek(0);
                rw.setLength(fileSize);
            } else {
                rw.seek(alreadyDownload);
            }
            while (isDownload.get() && (readLen = bis.read(buff)) != -1) {
                rw.write(buff, 0, readLen);
                alreadyDownload += readLen;
                if (listener != null) {
                    listener.updateProgress(alreadyDownload, fileSize);
                }
                Logger.d(TAG, "下载进度:" + alreadyDownload + "/" + fileSize);
            }
            if (listener != null) {
                listener.updateProgress(alreadyDownload, fileSize);
            }
            isRecordBreakpoint = false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                BufferedWriter bufferedWriter = null;
                File lengthFile = new File(path, md5Str + ".txt");
                if (isRecordBreakpoint) {
                    Logger.d(TAG, "记录下载进度!" + alreadyDownload);
                    bufferedWriter = new BufferedWriter(new FileWriter(lengthFile, false));
                    bufferedWriter.write(alreadyDownload + "");
                    bufferedWriter.flush();
                } else if (lengthFile.exists()) {
                    Logger.d(TAG, "删除文件下载断点记录文件！");
                    lengthFile.delete();
                }
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

    private long readDownloadedLength(File lengthFile) {
        if (lengthFile.exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(lengthFile));
                String readLine = bufferedReader.readLine();
                if (!TextUtils.isEmpty(readLine)) {
                    return Long.parseLong(readLine);
                }
            } catch (Exception e) {
                Logger.e(TAG, "读取已下载进度出错!");
                return 0L;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Logger.e(TAG, e.toString());
                    }
                }
            }
        }

        return 0L;
    }

    private boolean delFile(File file) {
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                return file.delete();
            }
        }
        return true;
    }

}
