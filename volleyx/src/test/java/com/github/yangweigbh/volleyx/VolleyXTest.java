package com.github.yangweigbh.volleyx;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import rx.Observable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by yangwei on 2016/10/29.
 */

public class VolleyXTest extends BaseTest {
    @Mock
    RequestQueue mockReqeustQueue;
    @Mock
    Request mockRequest;

    @Test
    public void testInit_context_null() throws Exception {
        thrown.expect(NullPointerException.class);

        VolleyX.init(null);
    }

    @Test
    public void testInit_context() throws Exception {
        VolleyX.init(RuntimeEnvironment.application);

        assertThat(VolleyX.DEFAULT_REQUESTQUEUE, is(not(nullValue())));
        assertThat(VolleyX.sRequestQueue, is(not(nullValue())));
        assertThat(VolleyX.sContext, is(not(nullValue())));
        assertThat(VolleyX.sInited, is(true));
    }

    @Test
    public void testFrom_without_init() throws Exception {
        thrown.expect(IllegalStateException.class);
        VolleyX.sInited = false;

        VolleyX.from(mockRequest);
    }

    @Test
    public void testFrom_nullrequest() throws Exception {
        thrown.expect(NullPointerException.class);
        VolleyX.sInited = true;

        VolleyX.from(null);
    }

    @Test
    public void testFrom() throws Exception {
        VolleyX.sInited = true;

        assertThat(VolleyX.from(mockRequest), is(instanceOf(Observable.class)));
    }

    @Test
    public void testFrom1_without_init() throws Exception {
        thrown.expect(IllegalStateException.class);
        VolleyX.sInited = false;

        VolleyX.from(mockRequest, "");
    }

    @Test
    public void testFrom1_nullrequest() throws Exception {
        thrown.expect(NullPointerException.class);
        VolleyX.sInited = true;

        VolleyX.from(null, "");
    }

    @Test
    public void testFrom1() throws Exception {
        VolleyX.sInited = true;

        assertThat(VolleyX.from(mockRequest, ""), is(instanceOf(Observable.class)));
    }

    @Test
    public void testSetRequestQueue() throws Exception {
        RequestQueue requestQueue = Mockito.mock(RequestQueue.class);
        VolleyX.setRequestQueue(requestQueue);

        assertThat(VolleyX.sRequestQueue, is(requestQueue));
    }

    @Test
    public void generateData_request_null() throws Exception {
        thrown.expect(NullPointerException.class);

        VolleyX.generateData(null);
    }

    @Test
    public void generateData1_request_null() throws Exception {
        thrown.expect(NullPointerException.class);

        VolleyX.generateData(null, "");
    }

    @Test
    public void getRequestFuture_request_null() throws Exception {
        thrown.expect(NullPointerException.class);

        VolleyX.getRequestFuture(null, "");
    }

    @Test
    public void getRequestFuture_customRequest() throws Exception {
        VolleyX.sInited = true;
        VolleyX.setRequestQueue(mockReqeustQueue);
        TestRequest2 testRequest = new TestRequest2(Request.Method.GET, "", null);

        VolleyX.getRequestFuture(testRequest, "mListener1");
        assertThat(testRequest.mListener1, is(instanceOf(RequestFuture.class)));
        assertThat(testRequest.getErrorListener(), is(instanceOf(RequestFuture.class)));
        assertTrue(testRequest.getErrorListener() == testRequest.mListener1);
        verify(mockReqeustQueue, times(1)).add(testRequest);
    }

    @Test
    public void getRequestFuture_Request() throws Exception {
        VolleyX.sInited = true;
        VolleyX.setRequestQueue(mockReqeustQueue);
        TestRequest testRequest = new TestRequest(Request.Method.GET, "", null);

        VolleyX.getRequestFuture(testRequest, null);
        assertThat(testRequest.mListener, is(instanceOf(RequestFuture.class)));
        assertThat(testRequest.getErrorListener(), is(instanceOf(RequestFuture.class)));
        assertTrue(testRequest.getErrorListener() == testRequest.mListener);
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