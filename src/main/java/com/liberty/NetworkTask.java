
package com.liberty;

import android.util.Log;

import com.liberty.exception.RemoteException;
import com.liberty.parser.IApiResultParseable;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


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
    public T send(Call<ResponseBody> call, IApiResultParseable requestAndParser) throws
            JSONException, RemoteException, IOException {

        // 如果bduss为空就取消前一次操作，停止当前操作
        if (call == null) {
            Log.v(TAG, "call == null");
            return null;
        }
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if (response == null) {
            return null;
        }
//        try {
//            Log.d(TAG, "返回的数据Reponse:" + response.body().contentLength() + ":" + response.toString() + "---Body---:" + response.body().toString() + "---errbody---:" + response.errorBody().toString());
//        } catch (IllegalArgumentException e) {
//            Log.w(TAG, "请求参数错误", e);
//            throw new IOException("url IllegalArgumentException");
//        }
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
        try {
            if (parser != null && response != null) {
                // 调用解析器对response解析
                result = parser.parse(response.body().string());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }


}
