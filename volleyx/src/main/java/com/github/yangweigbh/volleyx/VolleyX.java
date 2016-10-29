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

/**
 * Created by yangwei on 2016/10/29.
 */
public class VolleyX {
    private static final String DEFAULT_DEX_DIR = "dx";

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
    public static <T> Builder from(Request<T> request) {
        if (!sInited) throw new IllegalStateException("call init first");
        if (request == null) throw new NullPointerException("request can not be null");
        return new Builder(request);
    }

    /**
     * Create request builder using custom volley request
     *
     * @param request custom volley request
     * @param listernerField the field name of result callback listener
     * @param <T>
     * @return builder for chain call
     */
    public static <T> Builder from(Request<T> request, String listernerField) {
        if (!sInited) throw new IllegalStateException("call init first");
        if (request == null) throw new NullPointerException("request can not be null");
        return new Builder(request, listernerField);
    }

    /**
     * Set the volley request queue. If not set, the default request queue of volley is used.
     * restore the default volley request queue by calling {@link #setRequestQueue(VolleyX.DEFAULT_REQUESTQUEUE)}
     *
     * @param queue
     */
    public static void setRequestQueue(RequestQueue queue) {
        if (!sInited) throw new IllegalStateException("call init first");

        if (queue != null)
            sRequestQueue = queue;
    }

    public static class Builder<T> {
        private static final String DEFAULT_LISTENER_FIELD = "mListener";
        private static final String DEFAULT_ERROR_LISTENER_FIELD = "mErrorListener";

        private String mListernerField = null;
        private Request<T> mRequest;
        Observable<T> mObservable;

        Builder(Request<T> request) {
            this.mRequest = request;
        }

        Builder(Request<T> request, String listernerField) {
            this.mRequest = request;
            this.mListernerField = listernerField;
        }

        public Builder subscribeOn(Scheduler scheduler) {
            if (mObservable == null) {
                get();
            }
            mObservable = mObservable.subscribeOn(scheduler);
            return this;
        }

        public Builder observeOn(Scheduler scheduler) {
            if (mObservable == null) {
                get();
            }
            mObservable = mObservable.observeOn(scheduler);
            return this;
        }

        public Subscription subscribe(Observer<T> observer) {
            if (mObservable == null) {
                get();
            }
            return mObservable.subscribe(observer);
        }

        public Subscription subscribe(Subscriber<T> subscriber) {
            if (mObservable == null) {
                get();
            }
            return mObservable.subscribe(subscriber);
        }

        public final Subscription subscribe() {
            if (mObservable == null) {
                get();
            }
            return mObservable.subscribe();
        }

        public final Subscription subscribe(Action1<? super T> onNext) {
            if (mObservable == null) {
                get();
            }
            return mObservable.subscribe(onNext);
        }

        public final Subscription subscribe(Action1<? super T> onNext, Action1<Throwable> onError) {
            if (mObservable == null) {
                get();
            }
            return mObservable.subscribe(onNext, onError);
        }

        public final Subscription subscribe(Action1<? super T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
            if (mObservable == null) {
                get();
            }
            return mObservable.subscribe(onNext, onError, onCompleted);
        }

        public Observable<T> get() {
            mObservable = Observable.defer(() -> {
                try {
                    return Observable.just(generateData());
                } catch (InterruptedException | ExecutionException | IOException e) {
                    VolleyXLog.e(e.getMessage());
                    return Observable.error(e);
                }
            });
            return mObservable;
        }

        T generateData() throws InterruptedException, ExecutionException, IOException {
            RequestFuture<T> future = getRequestFuture();

            return future.get();
        }

         RequestFuture<T> getRequestFuture() {
            RequestFuture<T> future = RequestFuture.newFuture();
            if (mRequest == null) {
                throw new IllegalStateException("you should set request first to use this method");
            }

            String listenerFieldName = this.mListernerField != null? mListernerField : DEFAULT_LISTENER_FIELD;
            String errorListenerFieldName = DEFAULT_ERROR_LISTENER_FIELD;
            try {
                Hack.HackedClass hackedClass = Hack.into(mRequest.getClass());
                hackedClass.field(listenerFieldName).set(mRequest, future);
                hackedClass.field(errorListenerFieldName).set(mRequest, future);
            } catch (Hack.HackDeclaration.HackAssertionException e) {
                throw new IllegalStateException("the field name of your class is not correct: " + e.getHackedFieldName());
            }

            sRequestQueue.add(mRequest);
            return future;
        }
    }
}
