package com.liberty;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Author:LiuPen Created at 2017/5/25 09:48
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public interface ApiService {
    @GET
    Call<ResponseBody> get(@Url String url);

    @GET
    Call<ResponseBody> buildBodyGetCall(@Url String url, @QueryMap Map<String, String> map);

    @Streaming
    @GET
    Call<ResponseBody> downBigFile(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downBigFile(@Header("range") String range, @Url String url);

    @POST
    Call<ResponseBody> buildBodyPostCall(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> buildFiledsPostCall(@Url String url, @FieldMap Map<String, String> map);


    @Multipart
    @POST
    Call<ResponseBody> uploadFile(@Url String url, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<ResponseBody> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> maps);

}
