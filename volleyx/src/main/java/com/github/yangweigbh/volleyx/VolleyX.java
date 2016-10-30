/*
 * Copyright (C) 2016 yangweigbh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yangweigbh.volleyx;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.github.yangweigbh.volleyx.utils.Hack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by yangwei on 2016/10/29.
 */
public class VolleyX {
    private static final String DEFAULT_DEX_DIR = "dx";
    private static final String DEFAULT_LISTENER_FIELD = "mListener";
    private static final String DEFAULT_ERROR_LISTENER_FIELD = "mErrorListener";

    public static RequestQueue DEFAULT_REQUESTQUEUE;
    static RequestQueue sRequestQueue;
    static Context sContext;
    static boolean sInited = false;

    /**
     * Init the VolleyX enviroment
     *
     * @param context application context
     */
    public static void init(Context context) {
        if (context == null) throw new NullPointerException("context can not be null");
        sContext = context;

        DEFAULT_REQUESTQUEUE = Volley.newRequestQueue(sContext);
        sRequestQueue = DEFAULT_REQUESTQUEUE;

        sInited = true;
    }

    /**
     * Create request builder using volley request
     *
     * @param request volley request
     * @param <T> request type
     * @return builder for chain call
     */
    public static <T> Observable<T> from(final Request<T> request) {
        if (!sInited) throw new IllegalStateException("call init first");
        if (request == null) throw new NullPointerException("request can not be null");
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                try {
                    return Observable.just(generateData(request));
                } catch (InterruptedException | ExecutionException | IOException e) {
                    VolleyXLog.e(e.getMessage());
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Create request builder using custom volley request
     *
     * @param request custom volley request
     * @param listernerField the field name of result callback listener
     * @param <T>
     * @return builder for chain call
     */
    public static <T> Observable<T> from(final Request<T> request, final String listernerField) {
        if (!sInited) throw new IllegalStateException("call init first");
        if (request == null) throw new NullPointerException("request can not be null");
        return Observable.defer(new Func0<Observable<T>>() {

            @Override
            public Observable<T> call() {
                try {
                    return Observable.just(generateData(request, listernerField));
                } catch (InterruptedException | ExecutionException | IOException e) {
                    VolleyXLog.e(e.getMessage());
                    return Observable.error(e);
                }
            }
        });
    }

    static <T> T generateData(Request<T> request) throws InterruptedException, ExecutionException, IOException {
        return generateData(request, DEFAULT_LISTENER_FIELD);
    }

    static <T> T generateData(Request<T> request, String listernerField) throws InterruptedException, ExecutionException, IOException {
        if (request == null) throw new NullPointerException("request can not be null");
        RequestFuture<T> future = getRequestFuture(request, listernerField);

        return future.get();
    }

    static <T> RequestFuture<T> getRequestFuture(Request<T> request, String listernerField) {
        if (request == null) throw new NullPointerException("request can not be null");
        RequestFuture<T> future = RequestFuture.newFuture();

        String listenerFieldName = TextUtils.isEmpty(listernerField)? DEFAULT_LISTENER_FIELD: listernerField;
        String errorListenerFieldName = DEFAULT_ERROR_LISTENER_FIELD;
        try {
            Hack.HackedClass hackedClass = Hack.into(request.getClass());
            hackedClass.field(listenerFieldName).set(request, future);
            hackedClass.field(errorListenerFieldName).set(request, future);
        } catch (Hack.HackDeclaration.HackAssertionException e) {
            throw new IllegalStateException("the field name of your class is not correct: " + e.getHackedFieldName());
        }

        sRequestQueue.add(request);
        return future;
    }

    /**
     * Set the volley request queue. If not set, the default request queue of volley is used.
     * restore the default volley request queue by calling setRequestQueue(VolleyX.DEFAULT_REQUESTQUEUE)}
     *
     * @param queue
     */
    public static void setRequestQueue(RequestQueue queue) {
        if (!sInited) throw new IllegalStateException("call init first");

        if (queue != null)
            sRequestQueue = queue;
    }
}
