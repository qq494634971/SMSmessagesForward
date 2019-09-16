package com.android.smsmessages;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SMSForwardHttpClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();
    private static String stringBody;

    public static void SendToDingding(String dingToken, String msgBody) {
        stringBody = "{\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"}}";

        RequestBody body = RequestBody.create(JSON, String.format(stringBody, msgBody));
        Request request = new Request.Builder()
                .url(String.format("https://oapi.dingtalk.com/robot/send?access_token=%s", dingToken))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.isSuccessful());
            System.out.println(response.body().string());
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                }
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++) {
//                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
//
//                System.out.println(response.body().string());
//            }
//
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                e.printStackTrace();
//            }
//        });
    }
}
