package com.liberty;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liberty.exception.RemoteException;
import com.liberty.parser.DefaultParser;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Author:LiuPen Created at 2017/5/23 16:15
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class Liberty {
    private static final String TAG = "Liberty";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static ApiService apiService;

    public static void init(String baseUrl) {
        retrofit = buildRetrofit(baseUrl);
        apiService = retrofit.create(ApiService.class);
    }

    public static <T> T create(Class<T> tClass) {
        if (retrofit == null) {
            throw new ExceptionInInitializerError("Retrofit not init!");
        }
        return retrofit.create(tClass);
    }

    public static Retrofit buildRetrofit(String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (okHttpClient == null) {
            okHttpClient = initOkHttpClient();
        }
        retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }

    private static OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        okBuilder.connectTimeout(HttpConstants.CONN_TIME_OUT, TimeUnit.MILLISECONDS);
        okBuilder.readTimeout(HttpConstants.READ_TIME_OUT, TimeUnit.MILLISECONDS);
        List<Interceptor> interceptors = okBuilder.interceptors();
        if (interceptors != null) {
            interceptors.clear();//如果OkHttpClient已经设置了拦截器那么清空。
        }
        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
//                String url = request.url().encodedPath();
                Log.d(TAG, "请求的URL:" + ":" + request.url().toString());
                Log.d(TAG, "请求:" + request.toString());
                return chain.proceed(request);
            }
        });
//        if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder.addInterceptor(logger);
//        }
//        try {
//            registerHttps(okBuilder);
//        } catch (Exception e) {
//            Log.e(TAG, "支持Https错误" + e.toString());
//        }
        return okBuilder.build();
    }

    /**
     * 添加https支持
     *
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static void registerHttps(OkHttpClient.Builder builder) throws NoSuchAlgorithmException, KeyManagementException {
        final X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String authType) throws CertificateException {
                if (xcs == null || xcs.length <= 0 || TextUtils.isEmpty(authType)) {
                    throw new CertificateException("not trusted");
                }
                for (X509Certificate xc : xcs) {
                    xc.checkValidity();
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        HostnameVerifier hnv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        final SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory socketFactory = sslcontext.getSocketFactory();
        builder.sslSocketFactory(socketFactory, tm);
    }

    public static <T> T get(String url, Class<T> t) throws IOException, RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException {
        Call<ResponseBody> call = apiService.get(url);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public static <T> T get(String url, Map<String, String> params, Class<T> t) throws IOException, RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException {
        Call<ResponseBody> call = apiService.buildBodyGetCall(url, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public static <T> T getWithHeader(String url, Map<String, String> params, Class<T> t) throws IOException, RemoteException, JSONException {
        Call<ResponseBody> call = apiService.getWithHeaderNoParamCall(url, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public static <T> T getWithHeaderAndParm(String url, Map<String, String> header, Map<String, String> params, Class<T> t) throws IOException, RemoteException, JSONException {
        Call<ResponseBody> call = apiService.getWithHeaderHasParamCall(url, header, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public static <T> T post(String url, Object o, Class<T> t) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        RequestBody body = RequestBodyUtil.createJsonBody(o);
        Call<ResponseBody> call = apiService.buildBodyPostCall(url, body);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public static <T> T postForm(String url, Map<String, String> params, Class<T> t) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        Call<ResponseBody> call = apiService.buildFiledsPostCall(url, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }
}
