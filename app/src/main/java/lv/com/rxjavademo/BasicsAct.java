package lv.com.rxjavademo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 10:26
 * Description:创建
 * just( ) — 将一个或多个对象转换成发射这个或这些对象的一个Observable
 * from( ) — 将一个Iterable, 一个Future, 或者一个数组转换成一个Observable
 * repeat( ) — 创建一个重复发射指定数据或数据序列的Observable
 * repeatWhen( ) — 创建一个重复发射指定数据或数据序列的Observable，它依赖于另一个Observable发射的数据
 * create( ) — 使用一个函数从头创建一个Observable
 * defer( ) — 只有当订阅者订阅才创建Observable；为每个订阅创建一个新的Observable
 * range( ) — 创建一个发射指定范围的整数序列的Observable
 * interval( ) — 创建一个按照给定的时间间隔发射整数序列的Observable
 * timer( ) — 创建一个在给定的延时之后发射单个数据的Observable
 */
public class BasicsAct extends AppCompatActivity {

    public static void startBasicsAct(Activity activity) {
        activity.startActivity(new Intent(activity, BasicsAct.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_basics);
    }


    public void range(View view) {
        Observable.range(88,10).subscribe(new Observer<Integer>() {

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable arg0) {

            }

            @Override
            public void onNext(Integer arg0) {
               DLog.d(arg0);
            }
        });
    }

    public void defer(View view) {
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable call() {
                // 这里要返回一个Observable的实例对象,在这里用create的方法创建
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    // 这里还是create的用法
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("hello world");
                        subscriber.onCompleted();
                    }
                });
            }
            // 然后这里是订阅者,跟create一样
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                DLog.d("---- onCompleted ----");
            }

            @Override
            public void onError(Throwable e) {
                DLog.d("---- onError ----");
            }

            @Override
            public void onNext(String o) {
                DLog.d("---- onNext ----" + o);
            }
        });
    }

    public void repeat(View view) {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> array = new ArrayList<>();
                array.add("asdf1");
                array.add("asdf2");
                array.add("asdf3");
                subscriber.onNext(array);
                subscriber.onCompleted();
            }
        }).repeat(2)
                .compose(RxSchedulers.<List<String>>io_main())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        DLog.d(strings.size());
                    }
                });
    }

    public void timer(View view) {
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        DLog.d("asdfasdfasd");
                    }
                });
    }

    public void delay(View view) {
        Observable.just("asdf", "123", "asdf")
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        DLog.d(s);
                    }
                });
    }

    public void interval(View view){
        countdown(10);
    }

    public  Observable<Integer> countdown(int time) {
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);
    }

    public void from(View view) {
        String[] array = {"asdf", "123", "45"};
        Observable.from(array)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        DLog.d(s);
                    }
                });
    }

    public void just(View view) {
        Observable.just("asdf", "sdf", null)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        DLog.d(s);
                        DLog.d("运行在 " + Thread.currentThread().getName() + " 线程");
                    }
                });
    }

    //基础
    public void create(View view) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
                DLog.d("运行在 " + Thread.currentThread().getName() + " 线程");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        DLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.d(e.getMessage());
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        DLog.d(bitmap == null);
                        DLog.d("运行在 " + Thread.currentThread().getName() + " 线程");
                    }
                });
    }

}
