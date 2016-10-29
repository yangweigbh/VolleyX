package com.github.yangweigbh.volleyx;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by yangwei on 2016/10/29.
 */

public class VolleyXTest extends BaseTest {

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

        assertThat(VolleyX.from(mockRequest), is(instanceOf(VolleyX.Builder.class)));
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

        assertThat(VolleyX.from(mockRequest, ""), is(instanceOf(VolleyX.Builder.class)));
    }

    @Test
    public void testSetRequestQueue() throws Exception {
        RequestQueue requestQueue = Mockito.mock(RequestQueue.class);
        VolleyX.setRequestQueue(requestQueue);

        assertThat(VolleyX.sRequestQueue, is(requestQueue));
    }
}