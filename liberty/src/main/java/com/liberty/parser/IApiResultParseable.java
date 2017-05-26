/*
 * IApiResultParseable.java
 * classes : com.baidu.netdisk.io.IApiResultParseable
 * @author 李彬
 * V 1.0.0
 * Create at 2012-8-3 上午10:20:19
 */
package com.liberty.parser;


import com.okhttpretrofit.exception.RemoteException;

import org.json.JSONException;

import java.io.IOException;


/**
 * com.gumpcome.kernel.net.parser.IApiResultParseable
 *
 * @author <a href="mailto:libin09@baidu.com">李彬</a> <br/>
 *         解析服务器响应接口 create at 2012-8-3 上午10:20:19
 */
public interface IApiResultParseable<T> {
    /**
     * 解析服务器响应
     *
     * @param response 服务器响应的内容字符串
     * @return T 解析response后得到的数据类型
     * @throws JSONException 解析服务器响应的JSON
     * @throws IOException   获取服务器响应异常
     */
    <T> T parse(String response) throws JSONException, IOException, RemoteException;
}
