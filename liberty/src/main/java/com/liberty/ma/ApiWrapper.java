package com.liberty.ma;

import android.content.Context;
import android.os.RemoteException;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.okhttpretrofit.Liberty;
import com.okhttpretrofit.RequestBodyUtil;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import okhttp3.RequestBody;

/**
 * Author:LiuPen Created at 2017/5/24 10:38
 * Email:liupeng@gumpcome.com
 * Corporation:广州甘来信息科技有限公司
 * 网址:www.gumphealth.com
 * Description: *         params.addParam("deviceid", SvmManager.getSvm().getId());
 * params.addParam("clientversion", String.valueOf(SvmManager.getSvm().getSoftwareVersion()));
 * params.addParam("configversion", String.valueOf(SvmInfoConfig.getConfigVersion()));
 * params.addParam("svmtype", SvmManager.getSvm().getSvmType());
 * params.addParam("osversion", android.os.Build.VERSION.RELEASE);  //添加系统版本号
 */
public class ApiWrapper {

    public Person getAliQrcode(Person person, final Context context) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException,
            KeyStoreException, IOException, JSONException, RemoteException, com.okhttpretrofit.exception.RemoteException {
        String url = ServerURL.getConfigUrl();
        Liberty.init("http://www.baidu.com/");
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("deviceid", "98011606006c");
        map.put("clientversion", "3074");
        map.put("configversion", "0");
        map.put("svmtype", "g6_1");
        map.put("osversion", "4.4");  //添加系统版本号
        Person person1 = new Person("nihao", 123123);
        String json = new Gson().toJson(person1);
        RequestBody jsonBody = RequestBodyUtil.createJsonBody(json);
//        Call<ResponseBody> call = ApiStores.apiService.buildBodyCall(url, jsonBody);
        url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496364483&di=6f6a4e303bc576b13eae7d927bcb8e96&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fac345982b2b7d0a22d6b74bfc1ef76094b369a75.jpg";
//        Call<ResponseBody> call = Liberty.get(url);
//        Response<ResponseBody> response = call.execute();
//        LibertyDownload.downloadFileSyncProgress(url, context.getFilesDir().getAbsolutePath(), "789.jpg", null);
//        LibertyDownload.writeToDisk(response.body(), null);
//        LibertyDownload.downloadFileBreakPoint(url, context.getFilesDir().getAbsolutePath(), "198.jpg", new DownloadAdapter() {
//            @Override
//            public void onSuccess() {
//                Log.e("Haha", "下载成功!");
//            }
//
//        });
        String s = new Liberty().get("http://www.baidu.com", person1, String.class);
        return new Person("123", 12);
    }

}
