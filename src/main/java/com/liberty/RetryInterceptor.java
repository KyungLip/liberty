package com.liberty;


import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:LiuPen Created at 2017/7/3 15:53
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */

public class RetryInterceptor implements Interceptor {
    private static final String TAG = "RetryInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Logger.d(TAG, "请求的URL:" + ":" + request.url().toString());
        Logger.d(TAG, "请求:" + request.toString());
        Logger.d(TAG, "请求头:" + request.headers().toString());
        RetryWrapper retryWrapper = new RetryWrapper(chain, request);
        while (retryWrapper.isNeedRetry && !Thread.currentThread().isInterrupted()) {
            retryWrapper.execute();
        }
        return retryWrapper.response == null ? chain.proceed(request) : retryWrapper.response;
    }


    public static class RetryWrapper {
        private static final int MAX_RETRY_NUM = 2;//默认最多两次,加上默认的一次最多三次。
        private int curRetryNum = 0;
        public boolean isNeedRetry = true;
        public Request request;
        public Chain chain;
        public Response response;

        public RetryWrapper(Chain chain, Request request) {
            this.request = request;
            this.chain = chain;
        }

        private void execute() {
            boolean successful = false;
            Response response = null;
            try {
                response = chain.proceed(request);
                successful = response.isSuccessful();
            } catch (Exception e) {
                Logger.e(TAG, e.toString());
            } finally {
                if (successful) {
                    this.response = response;
                    isNeedRetry = false;
                } else {
                    if (curRetryNum < MAX_RETRY_NUM) {
                        curRetryNum++;
                        isNeedRetry = true;
                        Logger.d(TAG, "请求重试:" + curRetryNum);
                    } else {
                        isNeedRetry = false;
                    }
                }
                Logger.d(TAG, "请求结果:" + successful + ",      " + "请求的URL:" + ":" + request.url().toString());
            }

        }
    }
}
