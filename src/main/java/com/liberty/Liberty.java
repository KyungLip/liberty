package com.liberty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liberty.exception.RemoteException;
import com.liberty.parser.DefaultParser;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
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
    private String TAG = "Liberty";
    private ApiService apiService;
    private Retrofit retrofit;

    private Liberty(Retrofit retrofit, ApiService apiService) {
        this.apiService = apiService;
        this.retrofit = retrofit;
    }


    public static final class Builders {
        private String TAG = "Liberty.Builders";
        private OkHttpClient okHttpClient;
        private long connectTimeout = HttpConstants.CONN_TIME_OUT;
        private long readTimeout = HttpConstants.READ_TIME_OUT;
        private String baseUrl;

        public Builders connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builders readimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builders baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builders debug(boolean isDebug) {
            Logger.debug(isDebug);
            return this;
        }

        public Liberty build() {
            Retrofit retrofit = buildRetrofit(baseUrl);
            return new Liberty(retrofit, retrofit.create(ApiService.class));
        }

        public Retrofit buildRetrofit(String baseUrl) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            if (okHttpClient == null) {
                okHttpClient = initOkHttpClient();
            }
            return new Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();

        }

        private OkHttpClient initOkHttpClient() {
            Builder okBuilder = new Builder();
            okBuilder = getUnsafeOkHttpBuilder(okBuilder);
            okBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            okBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
            List<Interceptor> interceptors = okBuilder.interceptors();
            if (interceptors != null) {
                interceptors.clear();//如果OkHttpClient已经设置了拦截器那么清空。
            }
            if (Logger.isDebug()) {
                okBuilder.addInterceptor(new RetryInterceptor());
                HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
                logger.setLevel(HttpLoggingInterceptor.Level.BODY);
                okBuilder.addInterceptor(logger);
            }
            return okBuilder.build();
        }

        public Builder getUnsafeOkHttpBuilder(Builder okBuilder) {

            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
            };

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("SSL");

                sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

            } catch (NoSuchAlgorithmException e) {
                Logger.e(TAG, e.toString());
            } catch (KeyManagementException e) {
                Logger.e(TAG, e.toString());
            }
            HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            okBuilder
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .hostnameVerifier(DO_NOT_VERIFY);
            return okBuilder;
        }
    }


    public <T> T create(Class<T> tClass) {
        if (retrofit == null) {
            throw new ExceptionInInitializerError("Retrofit not init!");
        }
        return retrofit.create(tClass);
    }


    public <T> T get(String url, Class<T> t) throws IOException, RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException {
        Call<ResponseBody> call = apiService.get(url);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T get(String url, Map<String, String> params, Class<T> t) throws IOException, RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException {
        Call<ResponseBody> call = apiService.buildBodyGetCall(url, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T getWithHeader(String url, Map<String, String> params, Class<T> t) throws IOException, RemoteException, JSONException {
        Call<ResponseBody> call = apiService.getWithHeaderNoParamCall(url, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T getWithHeaderAndParm(String url, Map<String, String> header, Map<String, String> params, Class<T> t) throws IOException, RemoteException, JSONException {
        Call<ResponseBody> call = apiService.getWithHeaderHasParamCall(url, header, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T post(String url, Object o, Class<T> t) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        RequestBody body = RequestBodyUtil.createJsonBody(o);
        Call<ResponseBody> call = apiService.buildBodyPostCall(url, body);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T postForm(String url, Map<String, String> params, Class<T> t) throws RemoteException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, JSONException, KeyStoreException, IOException {
        Call<ResponseBody> call = apiService.buildFiledsPostCall(url, params);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T postWithEncoding(String url, String encodingType, byte[] bytes, Class<T> t) throws IOException, RemoteException, JSONException {
        RequestBody body = RequestBodyUtil.createBytesBody(bytes);
        Call<ResponseBody> call = apiService.postWithHeaderEncoding(url, encodingType, body);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }

    public <T> T postWithHeader(String url, Map<String, String> headers, Object o, Class<T> t) throws IOException, RemoteException, JSONException {
        RequestBody body = RequestBodyUtil.createJsonBody(o);
        Call<ResponseBody> call = apiService.postWithHeader(url, headers, body);
        return new NetworkTask<T>().send(call, new DefaultParser<>(t));
    }
}
