package com.example.mjexco.flickrsearch.backend;

import com.loopj.android.http.*;

/**
 * Created by mj exco on 1/12/2018.
 */

public class HttpClient {
    private static final String BASE_URL = "https://api.flickr.com/services/";

    private static AsyncHttpClient asyncClient = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        asyncClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        asyncClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
