package com.yangms.taketaxi;

        import org.junit.Test;

        import java.io.File;
        import java.io.IOException;

        import okhttp3.Cache;
        import okhttp3.CacheControl;
        import okhttp3.Interceptor;
        import okhttp3.MediaType;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

/**
 * Created by maosheng on 2018/9/15.
 */

public class TestOkHttp3 {
    public TestOkHttp3() {
        super();
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 测试OkHttp Get请求
     **/
    @Test
    public void testGet() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://httpbin.org/get")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("response:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试OkHttp Post请求
     **/
    @Test
    public void testPost() {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "{\"name\":\"android\"}");
        Request request = new Request.Builder()
                .url("https://httpbin.org/post")
                .head()
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("response:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试拦截器
     */
    @Test
    public void testInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long start = System.currentTimeMillis();
                Request request = chain.request();
                Response response = chain.proceed(request);
                long end = System.currentTimeMillis();
                System.out.println("请求时间：" + (end - start));
                return response;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Request request = new Request.Builder()
                .url("https://httpbin.org/get")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println("response:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试缓存
     */
    @Test
    public void testCache() {
        Cache cache = new Cache(new File("cache.cache"), 1024 * 1024);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        Request request = new Request.Builder()
                .url("https://httpbin.org/get")
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Response responseCache = response.cacheResponse();
            Response responseNet = response.networkResponse();
            if (responseCache != null) {
                System.out.println("responseCache:" + responseCache);
            }
            if (responseNet != null) {
                System.out.println("responseNet:" + responseNet);
            }
            System.out.println("response:" + response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}