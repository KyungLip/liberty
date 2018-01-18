
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
import java.util.NoSuchElementException;

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
            Logger.d(TAG, "call == null");
            return null;
        }
        Response<ResponseBody> response = null;
        T result = null;
        try {
            response = call.execute();
            if (response == null) {
                return null;
            }
            // 获取http响应代码
            final int statusCode = response.code();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // 错误的http请求
                Logger.e(TAG, "server statusCode=" + statusCode);
            }

            IApiResultParseable<T> parser = requestAndParser;

            if (parser != null && response != null) {
                // 调用解析器对response解析
                result = parser.parse(response.body().string());
            }
        } catch (JSONException | RemoteException | IOException e) {
            Logger.e(TAG, e.toString());
            throw e;
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        }
        return result;
    }


}
