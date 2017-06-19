package com.liberty.download;

/**
 * Author:LiuPen Created at 2017/5/25 15:52
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public interface DownloadListener {
    void updateProgress(long progress, long totalSize);

    void onSuccess(String fileName);

    void onFaile(String msg);

}
