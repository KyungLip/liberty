package com.liberty.download;

import android.text.TextUtils;

import com.liberty.ApiService;
import com.liberty.Liberty;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

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
    private BufferedInputStream bis;


    public static boolean downloadFileSyncProgress(String url, String path, String name, DownloadListener listener) throws IOException {
        ApiService apiService = Liberty.create(ApiService.class);
        Call<ResponseBody> call = apiService.get(url);
        Response<ResponseBody> response = call.execute();
        return writeToDisk(path, name, response.body(), listener);
    }

    public boolean downloadFileSync(String url, String path, String name) throws IOException {
        return downloadFileSyncProgress(url, path, name, null);
    }

    public static boolean downloadFileBreakPoint(String url, String path, String name, DownloadAdapter listener) throws IOException {
        ApiService apiService = Liberty.create(ApiService.class);
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

    public static void downloadBigFileAsync(String url, final String path, final String name, final DownloadListener listener) {
        ApiService apiService = Liberty.create(ApiService.class);
        Call<ResponseBody> call = apiService.downBigFile(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean writeToDisk = writeToDisk(path, name, response.body(), listener);
                if (listener != null) {
                    if (writeToDisk) {
                        listener.onSuccess();
                    } else {
                        listener.onFaile("下载出错!");
                    }
                }
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
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        return false;
    }

    public static boolean writeToDiskRanAc(String path, String fileName, ResponseBody body, DownloadListener listener) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
