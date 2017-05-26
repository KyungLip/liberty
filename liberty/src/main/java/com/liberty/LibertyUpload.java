package com.liberty;


import com.okhttpretrofit.parser.DefaultParser;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Author:LiuPen Created at 2017/5/25 16:38
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class LibertyUpload {
    public String uploadFile(String url, String descr, String filename, File file) throws IOException {
        ApiService apiService = Liberty.create(ApiService.class);
        MultipartBody.Part part = MultiPartBodyUtil.createMultiPart(descr, filename, file);
        RequestBody description = RequestBodyUtil.createMultiBody(descr);
        Call<ResponseBody> call = apiService.uploadFile(url, description, part);
        try {
            return new NetworkTask<String>().send(call, new DefaultParser(String.class));
        } catch (Exception e) {
            return "";
        }
    }
}
