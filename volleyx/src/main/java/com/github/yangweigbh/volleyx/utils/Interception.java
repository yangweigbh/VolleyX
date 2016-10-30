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
package com.github.yangweigbh.volleyx.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Interception {

    private static interface Intercepted {
    }

    public static abstract class InterceptionHandler<T> implements InvocationHandler {
        private T mDelegatee;

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            Object obj2 = null;
            try {
                obj2 = method.invoke(delegatee(), objArr);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                throw e3.getTargetException();
            }
            return obj2;
        }

        protected T delegatee() {
            return this.mDelegatee;
        }

        void setDelegatee(T t) {
            this.mDelegatee = t;
        }
    }

    public static <T> T proxy(Object obj, Class<T> cls, InterceptionHandler<T> interceptionHandler) throws IllegalArgumentException {
        if (obj instanceof Intercepted) {
            return (T) obj;
        }
        interceptionHandler.setDelegatee((T) obj);
        return (T) Proxy.newProxyInstance(Interception.class.getClassLoader(), new Class[]{cls, Intercepted.class}, interceptionHandler);
    }

    public static <T> T proxy(Object obj, InterceptionHandler<T> interceptionHandler, Class<?>... clsArr) throws IllegalArgumentException {
        interceptionHandler.setDelegatee((T) obj);
        return (T) Proxy.newProxyInstance(Interception.class.getClassLoader(), clsArr, interceptionHandler);
    }

    private Interception() {
    }
}
