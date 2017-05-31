package com.liberty;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author:LiuPen Created at 2017/5/24 20:03
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class MultiPartBodyUtil {
    public static MultipartBody.Part createMultiPart(String description, String filename, File file) {
        RequestBody requestBody = RequestBodyUtil.createMultiPartBody(file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("", filename, requestBody);
        return part;
    }
}
