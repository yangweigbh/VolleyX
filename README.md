# VolleyX
Volley + RxJava, use Volley the Rx way

## Motivation
Some android developer is still using volley as their network library, and it should be combined with RxJava to make the best out of it. There is another Volley + RxJava library, [here](https://github.com/kymjs/RxVolley), but it integrated the volley src code and modify it, which makes volley can not be upgraded independently. This library integrate with volley independently and you can using volley the original way.

##Usage

 `compile 'com.github.yangweigbh:volleyx:1.0.0'`
 
 `compile 'io.reactivex:rxjava:x.y.z'`
 
 Init in the application onCreate, then everything is OK
 
 ```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VolleyX.init(this);
    }
}
 ```
 Create request as usual, instead don't set the response listener and the error listner cause we will do it RxWay, use `VolleyX.from(requsest)` to create a Observable
 
 ```java
final StringRequest request = new StringRequest(URL, null, null);
VolleyX.from(request).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError " + Log.getStackTraceString(e));
            }

            @Override
            public void onNext(String result) {
                Log.d(TAG, "onNext");
            }
        });
 ```
 
 If you have custom request, the pass the response listener field name as 2nd param to the from
 
```java
GsonRequest<Result> request = new GsonRequest<>(URL, Result.class, null, null);
VolleyX.from(request, "mListener1").subscribeOn(Schedulers.io())
         .subscribe(new Observer<Result>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError " + Log.getStackTraceString(e));
            }

            @Override
            public void onNext(Result result) {
                Log.d(TAG, "onNext");
            }
        });
```
 By default, the VolleyX use the default volley request queue, you can set custom requestqueue with
 
 ```java
 VolleyX.setRequestQueue(your custom queue);
 ```
 
