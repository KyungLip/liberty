/*
 * NetworkTask.java
 * classes : com.gumpcome.kernel.net.NetworkTask
 * @author 李彬
 * V 1.0.0
 * Create at 2012-8-3 上午10:13:32
 */
package com.liberty;

import android.text.TextUtils;
import android.util.Log;

import com.okhttpretrofit.exception.RemoteException;
import com.okhttpretrofit.parser.IApiResultParseable;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * com.baidu.netdisk.io.NetworkAsyncTask
 *
 * @author <a href="mailto:libin09@baidu.com">李彬</a> <br/>
 *         联网通讯的任务，同时提供同步和异步的方案 <br/>
 *         create at 2012-8-3 上午10:13:32
 */
public class NetworkTask<T> {
    private static final String TAG = "NetworkTask";

    /**
     * gzip首部相关
     */
    private static final String ENCODING_GZIP = "gzip";

    /**
     * 使用同步方法执行http请求
     *
     * @param requestAndParser Object... 参数，其中,requestAndParser[0]是HttpUriRequest, http请求<br />
     *                         requestAndParser[1]是IApiResultParseable<T>, response的解析器
     * @return
     * @throws UnsupportedOperationException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     * @throws JSONException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public T send(Call call, IApiResultParseable requestAndParser) throws UnsupportedOperationException, KeyManagementException,
            UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException,
            JSONException, RemoteException {

        // 如果bduss为空就取消前一次操作，停止当前操作
        if (call == null) {
            Log.v(TAG, "call == null");
            return null;
        }
        Response<ResponseBody> response = call.execute();
        try {
            Log.d(TAG, "返回的数据Reponse:" + response.toString());
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "请求参数错误", e);
            throw new IOException("url IllegalArgumentException");
        }
        try {
            // 获取http响应代码
            final int statusCode = response.code();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // 错误的http请求
                Log.e(TAG, "server statusCode=" + statusCode);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
        T result = null;
        IApiResultParseable<T> parser = requestAndParser;
        if (parser != null && !TextUtils.isEmpty(response.body().string())) {
            // 调用解析器对response解析
            result = parser.parse(response.body().string());
        }
        return result;
    }


}
