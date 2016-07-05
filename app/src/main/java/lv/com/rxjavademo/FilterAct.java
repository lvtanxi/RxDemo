package lv.com.rxjavademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 14:57
 * Description:这个页面展示的操作符可用于过滤和选择Observable发射的数据序列。
 * filter( ) — 过滤数据
 * takeLast( ) — 只发射最后的N项数据
 * last( ) — 只发射最后的一项数据
 * lastOrDefault( ) — 只发射最后的一项数据，如果Observable为空就发射默认值
 * takeLastBuffer( ) — 将最后的N项数据当做单个数据发射
 * skip( ) — 跳过开始的N项数据
 * skipLast( ) — 跳过最后的N项数据
 * take( ) — 只发射开始的N项数据
 * first( ) and takeFirst( ) — 只发射第一项数据，或者满足某种条件的第一项数据
 * firstOrDefault( ) — 只发射第一项数据，如果Observable为空就发射默认值
 * elementAt( ) — 发射第N项数据
 * elementAtOrDefault( ) — 发射第N项数据，如果Observable数据少于N项就发射默认值
 * sample( ) or throttleLast( ) — 定期发射Observable最近的数据
 * throttleFirst( ) — 定期发射Observable发射的第一项数据
 * throttleWithTimeout( ) or debounce( ) — 只有当Observable在指定的时间后还没有发射数据时，才发射一个数据
 * timeout( ) — 如果在一个指定的时间段后还没发射数据，就发射一个异常
 * distinct( ) — 过滤掉重复数据
 * distinctUntilChanged( ) — 过滤掉连续重复的数据
 * ofType( ) — 只发射指定类型的数据
 * ignoreElements( ) — 丢弃所有的正常数据，只发射错误或完成通知
 */
public class FilterAct extends AppCompatActivity {

    public static void startFilterAct(Activity activity) {
        activity.startActivity(new Intent(activity, FilterAct.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_filter);
    }


    /**
     * 过滤数据
     */
    public void filter(View view) {
        Observable.range(1, 3)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 == 0;
                    }
                }).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 只发射最后的N项数据
     */
    public void takeLast(View view) {
        Observable.range(1, 3).takeLast(2).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 只发射最后的一项数据
     */
    public void last(View view) {
        Observable.range(1, 3).last().compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 只发射最后的一项数据，如果Observable为空就发射默认值
     */
    public void lastOrDefault(View view) {
        Observable.range(1, 3).lastOrDefault(2).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
        Observable.range(0, 0).lastOrDefault(3).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 将最后的N项数据当做单个数据发射
     */
    public void takeLastBuffer(View view) {
        Observable.range(1, 10).takeLastBuffer(3).compose(RxSchedulers.<List<Integer>>io_main())
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        DLog.d(integers.size());
                    }
                });
    }

    /**
     * 跳过开始的N项数据
     */
    public void skip(View view) {
        Observable.range(1, 10).skip(2).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }


    /**
     * 跳过最后的N项数据
     */
    public void skipLast(View view) {
        Observable.range(1, 10).skipLast(2).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 只发射开始的N项数据
     */
    public void take(View view) {
        Observable.range(0, 50).take(3).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     *  只发射第一项数据，或者满足某种条件的第一项数据
     */
    public void firstAndTakeFirst(View view) {
        Observable.range(0, 50).first().compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });

        Observable.range(0, 50).takeFirst(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer == 10;
            }
        }).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     *  只发射第一项数据，如果Observable为空就发射默认值
     */
    public void firstOrDefault(View view) {
        Observable.range(0, 0).firstOrDefault(10).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
        Observable.range(0, 1).firstOrDefault(10).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 发射第N项数据
     */
    public void elementAt(View view) {
        Observable.range(0, 50).elementAt(10).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }


    /**
     * 发射第N项数据，如果Observable数据少于N项就发射默认值
     */
    public void elementAtOrDefault(View view) {
        Observable.range(0, 5).elementAtOrDefault(10,100).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 定期发射Observable最近的数据
     */
    public void sampleOrThrottleLast(View view) {
        Observable.range(0, 10).sample(5, TimeUnit.SECONDS)
                .compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
        Observable.range(0, 10).throttleLast(5, TimeUnit.SECONDS)
                .compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 定期发射Observable发射的第一项数据
     */
    public void throttleFirst(View view) {
        Observable.range(0, 10).throttleFirst(5, TimeUnit.SECONDS)
                .compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    /**
     * 只有当Observable在指定的时间后还没有发射数据时，才发射一个数据
     */
    public void throttleWithTimeoutOrDebounce(View view) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(i);
                    }
                    int sleep = 100;
                    if (i % 3 == 0) {
                        sleep = 300;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .throttleWithTimeout(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                try {
                    //产生结果的间隔时间分别为100、200、300...900毫秒
                    for (int i = 1; i < 10; i++) {
                        subscriber.onNext(i);
                        Thread.sleep(i * 100);
                    }
                    subscriber.onCompleted();
                }catch(Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .debounce(400, TimeUnit.MILLISECONDS)  //超时时间为400毫秒
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void timeout(View view) {
        Observable.timer(5,TimeUnit.SECONDS)
                .timeout(2,TimeUnit.SECONDS)
                .compose(RxSchedulers.<Long>io_main())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.d(e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        DLog.d(aLong);
                    }
                });
    }

    public void distinct(View view) {
        Observable.just(1,1,2,2,3,3,4,4).distinct().compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void distinctUntilChanged(View view) {
        Observable.just(1,1,2,2,3,3,4,4).distinctUntilChanged().compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void ofType(View view) {
        Observable.just(1,"1").ofType(Integer.class)
                .compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void ignoreElements(View view) {
        Observable.just(1,"1").ofType(Integer.class)
                .ignoreElements()
                .compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        DLog.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }
}
