package gang.com.screencontrol.okhttp;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by xiaogangzai on 2017/5/24.
 * 官方建议okhttpclient单例模式
 */

public class OkHttpHelper {
    private static OkHttpClient okHttpClient;
    private Gson gson;

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        //解析用
        gson = new Gson();
    }


    public static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    public void get(String url, BaseCallBack callBack) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        doRequest(request, callBack);
    }

    public void post(String url, Map<String, String> params, BaseCallBack callBack) {
        Request request = buildRequest(url, params, HttpMethodType.POST);

        doRequest(request, callBack);
    }

    //封装一个request方法用于请求
    //不要用tostring方法，他是把对象转成字符串，string把流转成字符串
    public void doRequest(Request request, final BaseCallBack callBack) {
        //用来弹出对话框
        callBack.onRequestBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(response);
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callBack.mType == String.class) {
                        callBack.onSucess(response, resultStr);
                    } else {
                        try {
                            Object obj = gson.fromJson(resultStr, callBack.mType);
                            callBack.onSucess(response, obj);
                        } catch (com.google.gson.JsonParseException e) { // Json解析的错误
                            callBack.onError(response, response.code(), e);
                        }
                    }
                } else {
                    callBack.onError(response, response.code(), null);
                }
                //gson.fromJson(response.body().string(), callBack.mType);
            }

            //调用网络之前需要一个对话框
        });
    }

    private Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = builderFormData(params);
        }
        return builder.build();
    }

    //在okhttp中okhttp3已经没有了FormEncodingBuilder，可以用下面这个类代替它：FormBody.Builder
    //构建post表单的方法
    private RequestBody builderFormData(Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();

        if (params != null) {

            for (Map.Entry<String, String> entry : params.entrySet()) {

                builder.add(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();

    }


    //写一个枚举类型判断是get方法还是post方法
    enum HttpMethodType {
        GET,
        POST
    }
}
