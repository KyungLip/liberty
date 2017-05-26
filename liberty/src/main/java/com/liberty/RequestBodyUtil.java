package com.liberty;

import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author:LiuPen Created at 2017/5/24 15:31
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description:
 */
public class RequestBodyUtil {

    /**
     * 根据给定的Json串创建请求体
     *
     * @param jsonStr Json字符串
     * @return RequestBody
     */
    public static RequestBody createJsonBody(String jsonStr) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_JSON), jsonStr);
    }
    /**
     * 根据给定的字符串创建请求体
     *
     * @param str J字符串
     * @return RequestBody
     */
    public static RequestBody createMultiBody(String str) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_MULTIPART_FORMDATA), str);
    }
    /**
     * 根据给定的对象创建请求体
     *
     * @param object Object对象
     * @return RequestBody
     */
    public static RequestBody createJsonBody(Object object) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_JSON), new Gson().toJson(object));
    }

    /**
     * @param file Object对象
     * @return RequestBody
     */
    public static RequestBody createMultiPartBody(File file) {
        return RequestBody.create(MediaType.parse(HttpContentType.CONTENT_TYPE_APPLICATION_MULTIPART_FORMDATA),file);
    }
}
