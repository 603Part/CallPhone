package com.phone.call.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.anderson.AndroidUtils;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class Application extends MultiDexApplication {
    private static Application application;
    private Context context = this;

    @Override
    public void onCreate() {
        application = (Application) getApplicationContext();

        //NoHttp
        NoHttp.initialize(this);
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("NoHttpSample");// 打印Log的tag。
        // NoHttp自定义配置：
        /**NoHttp.initialize(InitializationConfig.newBuilder(this)
                .connectionTimeout(30 * 1000) // 设置全局连接超时时间，单位毫秒，默认10s。
                .readTimeout(30 * 1000)   // 设置全局服务器响应超时时间，单位毫秒，默认10s。
                .cacheStore(// 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                        new DBCacheStore(this).setEnable(true) // 如果不使用缓存，设置setEnable(false)禁用。
                )
                .cookieStore( // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                        new DBCookieStore(this).setEnable(true) // 如果不维护cookie，设置false禁用。
                )
                // 配置网络层，URLConnectionNetworkExecutor，如果想用OkHttp：OkHttpNetworkExecutor。
                .networkExecutor(new URLConnectionNetworkExecutor())
                .build()
        );*/
        //AndroidUtils
        AndroidUtils.init(new AndroidUtils.getContextListener() {
            @Override
            public Context getContext() {
                return application;
            }
        });
        AndroidUtils.setTAG("log");

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        super.onCreate();
    }

    public static Application getInstance() {
        return application;
    }

}
