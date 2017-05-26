package com.liberty;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Author:LiuPen Created at 2017/5/24 14:30
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 * params.addParam("deviceid", SvmManager.getSvm().getId());
 * params.addParam("clientversion", String.valueOf(SvmManager.getSvm().getSoftwareVersion()));
 * params.addParam("configversion", String.valueOf(SvmInfoConfig.getConfigVersion()));
 * params.addParam("svmtype", SvmManager.getSvm().getSvmType());
 * params.addParam("osversion", android.os.Build.VERSION.RELEASE);  //添加系统版本号
 */
public interface GumpApiService {
    @GET
    Call<ResponseBody> buildBodyGetCall(@Url String url);

    @GET
    Call<ResponseBody> buildBodyGetCall(@Url String url, @QueryMap Map<String, String> map);

    @GET
    Call<ResponseBody> query(@Url String url, @Query("deviceid") String deviceid, @Query("clientversion") String ver, @Query("configversion") String cofigv, @Query("svmtype") String svm, @Query("osversion") String os);

    @POST
    Call<ResponseBody> buildBodyPostCall(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> getStringMap(@Url String url, @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> getString(@Url String url, @Field("deviceid") String deviceid, @Field("clientversion") String ver, @Field("configversion") String cofigv, @Field("svmtype") String svm, @Field("osversion") String os);


}
