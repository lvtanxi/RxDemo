package lv.com.rxjavademo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init("lvtanxi");
        countdown(10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                DLog.d(">>>>>>>>>>>>"+integer);
            }
        });
    }
    public static Observable<Integer> countdown(int time) {
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

    private void from() {
        String[] array={"asdf","123","45"};
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

    private void just() {
        Observable.just("asdf","sdf",null)
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
    private void base() {
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
