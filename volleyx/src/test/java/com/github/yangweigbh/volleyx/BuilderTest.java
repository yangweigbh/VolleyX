package com.github.yangweigbh.volleyx;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by yangwei on 2016/10/29.
 */
public class BuilderTest extends BaseTest {
    @Mock
    RequestQueue mockReqeustQueue;

    @Mock
    Observable mockObservable;

    @Before
    public void setUp() {

    }

    @Test
    public void subscribeOn() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);

        builder.subscribeOn(Schedulers.io());

        verify(builder, times(1)).get();
    }

    @Test
    public void observeOn() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);

        builder.subscribeOn(Schedulers.io());

        verify(builder, times(1)).get();
    }

    @Test
    public void subscribe() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.subscribe();

        verify(builder, times(1)).get();
    }

    @Test
    public void subscribe1() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.subscribe(new Action1() {
            @Override
            public void call(Object o) {

            }
        });

        verify(builder, times(1)).get();
    }

    @Test
    public void subscribe2() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.subscribe(new Action1() {
            @Override
            public void call(Object o) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });

        verify(builder, times(1)).get();
    }

    @Test
    public void subscribe3() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.subscribe(new Action1() {
            @Override
            public void call(Object o) {

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }, new Action0() {
            @Override
            public void call() {

            }
        });

        verify(builder, times(1)).get();
    }

    @Test
    public void subscribe4() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });

        verify(builder, times(1)).get();
    }

    @Test
    public void subscribe5() throws Exception {
        VolleyX.Builder builder = new VolleyX.Builder(mockRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.subscribe(new Observer() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });

        verify(builder, times(1)).get();
    }

    @Test
    public void createWithCumstomRequest() throws Exception {
        VolleyX.sInited = true;
        VolleyX.setRequestQueue(mockReqeustQueue);
        TestRequest2 testRequest = new TestRequest2(Request.Method.GET, "", null);
        VolleyX.Builder builder = new VolleyX.Builder(testRequest, "mListener1");

        builder.getRequestFuture();
        assertThat(testRequest.mListener1, is(instanceOf(RequestFuture.class)));
        assertThat(testRequest.getErrorListener(), is(instanceOf(RequestFuture.class)));
        assertThat(testRequest.getErrorListener(), is(sameInstance(testRequest.mListener1)));
        verify(mockReqeustQueue, times(1)).add(testRequest);
    }

    @Test
    public void get() throws Exception {
        VolleyX.sInited = true;
        VolleyX.setRequestQueue(mockReqeustQueue);
        TestRequest testRequest = new TestRequest(Request.Method.GET, "", null);
        VolleyX.Builder builder = new VolleyX.Builder(testRequest);
        builder = Mockito.spy(builder);
        doReturn(Mockito.mock(RequestFuture.class)).when(builder).getRequestFuture();

        builder.get().subscribe();

        verify(builder, times(1)).generateData();
        verify(builder, times(1)).getRequestFuture();
    }

    @Test
    public void getRequestFuture() throws Exception {
        VolleyX.sInited = true;
        VolleyX.setRequestQueue(mockReqeustQueue);
        TestRequest testRequest = new TestRequest(Request.Method.GET, "", null);
        VolleyX.Builder builder = new VolleyX.Builder(testRequest);

        builder.getRequestFuture();
        assertThat(testRequest.mListener, is(instanceOf(RequestFuture.class)));
        assertThat(testRequest.getErrorListener(), is(instanceOf(RequestFuture.class)));
        assertThat(testRequest.getErrorListener(), is(sameInstance(testRequest.mListener)));
        verify(mockReqeustQueue, times(1)).add(testRequest);
    }

    static class TestRequest extends Request<String> {
        Response.Listener<String> mListener;

        public TestRequest(int method, String url, Response.ErrorListener listener) {
            super(method, url, listener);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            return null;
        }

        @Override
        protected void deliverResponse(String response) {

        }
    }

    static class TestRequest2 extends Request<String> {
        Response.Listener<String> mListener1;

        public TestRequest2(int method, String url, Response.ErrorListener listener) {
            super(method, url, listener);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            return null;
        }

        @Override
        protected void deliverResponse(String response) {

        }
    }

}