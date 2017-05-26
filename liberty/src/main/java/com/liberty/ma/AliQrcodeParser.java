package com.liberty.ma;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.okhttpretrofit.parser.IApiResultParseable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

;

/**
 * Created by caowenbin on 2015/7/4.
 */
public class AliQrcodeParser implements IApiResultParseable<Person> {
    private static final String TAG = "AliQrcodeParser";

    @Override
    public Person parse(String result) throws JSONException, IOException {
        Person person = null;
        try {
            Log.d(TAG, "result:" + result);

            person = new Gson().fromJson(result, Person.class);
        } catch (JsonSyntaxException e) {
            JSONObject object = new JSONObject(result);
            final int errno = object.optInt("code");
            if (errno != 200) {
                Log.e(TAG, "errno:" + errno);
//                throw new RemoteException(errno, object.optString("msg"));
            }
            throw new JSONException(e.getMessage());
        } catch (JsonIOException e) {
            throw new IOException(e.getMessage());
        } catch (JsonParseException e) {
            throw new JSONException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new JSONException(e.getMessage());
        }

        return person == null ? null : person;
    }
}
