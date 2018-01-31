package com.liberty.download;

import android.text.TextUtils;
import android.util.Log;

import com.liberty.ApiService;
import com.liberty.Liberty;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author:LiuPen Created at 2017/5/25 12:12
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class LibertyDownload {
    private static final String TAG = "LibertyDownload";

    private static HashMap<String, DownLoadTask> maps = new HashMap();

    protected static void removeTask(String md5) {
        if (!TextUtils.isEmpty(md5) && maps.containsKey(md5)) {
            maps.remove(md5);
        }
    }

    public static DownLoadTask download(String baseUrl, String url, String path, String name, DownloadAdapter listener) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
            return null;
        }
        String md5Str = MD5Util.createMD5Str(url);
        if (TextUtils.isEmpty(md5Str) || maps.containsKey(md5Str)) {//说明有相同的任务正在执行
            return null;
        }
        DownLoadTask task = new DownLoadTask(baseUrl, url, path, name, md5Str, listener);
        maps.put(md5Str, task);
        task.start();
        return task;
    }

    public static boolean downloadFileSyncProgress(String baseUrl, String url, String path, String name, DownloadListener listener) throws IOException {
        ApiService apiService = new Liberty.Builders().baseUrl(baseUrl).build().create(ApiService.class);
        Call<ResponseBody> call = apiService.get(url);
        Response<ResponseBody> response = call.execute();
        boolean writeToDisk = writeToDisk(path, name, response.body(), listener);
        return writeToDisk;
    }


    public static boolean downloadFileSync(String baseUrl, String url, String path, String name) throws IOException {
        return downloadFileSyncProgress(baseUrl, url, path, name, null);
    }

    public static boolean downloadFileBreakPoint(String baseUrl, String url, String path, String name, DownloadAdapter listener) throws IOException {
        ApiService apiService = new Liberty.Builders().baseUrl(baseUrl).build().create(ApiService.class);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, name);
        long downLength = file.length();
        if (file.exists()) {
        }
        String range = "bytes=" + downLength + "-";
        Call<ResponseBody> call = apiService.downBigFile(range, url);
        Response<ResponseBody> response = call.execute();
        return writeToDiskRanAc(path, name, response.body(), listener);
    }

    public static void downloadBigFileAsync(String baseUrl, String url, final String path, final String name, final DownloadListener listener) {
        ApiService apiService = new Liberty.Builders().baseUrl(baseUrl).build().create(ApiService.class);
        Call<ResponseBody> call = apiService.downBigFile(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                writeToDisk(path, name, response.body(), listener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFaile("下载出错!" + t.toString());
                }
            }
        });
    }

    public static boolean writeToDisk(String path, String fileName, ResponseBody body, DownloadListener listener) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(fileDir, fileName);
            if (file.exists()) {
                file.delete();
            }
            long fileSize = body.contentLength();
            long alreadyDownload = 0;
            byte[] buff = new byte[1024];
            int readLen = 0;
            is = body.byteStream();
            fos = new FileOutputStream(file);
            while ((readLen = is.read(buff)) != -1) {
                fos.write(buff, 0, readLen);
                alreadyDownload += readLen;
                if (listener != null) {
                    listener.updateProgress(alreadyDownload, fileSize);
                }
            }
            fos.flush();
            if (listener != null) {
                listener.onSuccess(file.getName());
            }
            return true;
        } catch (IOException e) {
            if (listener != null) {
                listener.onFaile("下载出错!");
            }
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (listener != null) {
            listener.onFaile("下载出错!");
        }
        return false;
    }

    private static boolean writeToDiskRanAc(String path, String fileName, ResponseBody body, DownloadListener listener) {
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
            while ((readLen = bis.read(buff)) != -1) {
                rw.write(buff, 0, readLen);
                alreadyDownload += readLen;
                if (listener != null) {
                    listener.updateProgress(alreadyDownload, fileSize);
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
