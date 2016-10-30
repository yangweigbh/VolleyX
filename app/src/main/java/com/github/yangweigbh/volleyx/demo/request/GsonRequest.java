/**
 * Copyright 2013 Mani Selvaraj
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

package com.github.yangweigbh.volleyx.demo.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom implementation of Request<T> class which converts the HttpResponse obtained to Java class objects.
 * Uses GSON library, to parse the response obtained.
 * Ref - JsonRequest<T>
 * @author Mani Selvaraj
 */

public class GsonRequest<T> extends Request<T> {
    private final Listener<T> mListener1;
    private Type mType;

    private Gson mGson;
    private Class<T> mJavaClass;

    public GsonRequest(String url, Class<T> cls, Listener<T> listener,
                       ErrorListener errorListener) {
        this(Method.GET, url, cls, listener, errorListener);
    }

    public GsonRequest(String url, TypeToken<T> token, Listener<T> listener,
                       ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mGson = new Gson();
        mType = token.getType();
        mListener1 = listener;
    }

    public GsonRequest(int method, String url, Class<T> cls, Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mJavaClass = cls;
        mListener1 = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener1.onResponse(response);
    }
    
    private Map<String, String> headers = new HashMap<String, String>();
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
    
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (mType == null && mJavaClass == null) return Response.error(new ParseError());
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T parsedGSON = null;
            if (mType != null) {
                parsedGSON = mGson.fromJson(jsonString, mType);
            } else {
                parsedGSON = mGson.fromJson(jsonString, mJavaClass);
            }
            return Response.success(parsedGSON,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        }
    }
}
