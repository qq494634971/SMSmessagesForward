package com.android.smsmessages;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SMSForwardHttpClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String DDOPENAPI = "https://oapi.dingtalk.com/robot/send?access_token=%s";
    private static OkHttpClient client = new OkHttpClient();
    private static final String stringBodyFormat = "{\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"},\"at\": {\"isAtAll\": true}}";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void SendToDingding(String dingToken, String msgBody) {
        RequestBody body = RequestBody.create(JSON, String.format(stringBodyFormat, msgBody));
        Request request = new Request.Builder().url(String.format(DDOPENAPI, dingToken)).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            Log.d("DEBUG", "发送内容"+msgBody);
            Log.d("DEBUG", "responseIsSuccessful="+response.isSuccessful());
            Log.d("DEBUG", "responseBody"+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
