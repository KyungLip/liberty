
package com.liberty.parser;



import com.liberty.exception.RemoteException;

import org.json.JSONException;

import java.io.IOException;

public interface IApiResultParseable<T> {
    /**
     * 解析服务器响应
     *
     * @param response 服务器响应的内容字符串
     * @return T 解析response后得到的数据类型
     * @throws JSONException 解析服务器响应的JSON
     * @throws IOException   获取服务器响应异常
     */
    <T> T parse(String response) throws JSONException, IOException, RemoteException, RemoteException;
}
