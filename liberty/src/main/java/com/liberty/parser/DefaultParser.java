/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.liberty.parser;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.okhttpretrofit.exception.RemoteException;

import org.json.JSONException;

import java.io.IOException;

/**
 * 解析器的默认实现，当errno不为0时，抛出异常
 *
 * @author libin09
 */
public class DefaultParser<T> implements IApiResultParseable<T> {
    private static final String TAG = "DefaultParser";
    Class<T> tClass;

    public DefaultParser(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public T parse(String response) throws JSONException, IOException, RemoteException {
        final T responseModel;
        if (TextUtils.isEmpty(response)) {
            return null;
        }
        try {

            responseModel = new Gson().fromJson(response, tClass);
            Log.d(TAG, "DefaultParser:" + responseModel.toString());
            return responseModel;
        } catch (JsonSyntaxException e) {
            throw new JSONException(e.getMessage());
        } catch (JsonIOException e) {
            throw new IOException(e.getMessage());
        } catch (JsonParseException e) {
            throw new JSONException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new JSONException(e.getMessage());
        }
    }
}
