package lv.com.rxjavademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 10:45
 * Description:转换操作
 * map( ) — 对序列的每一项都应用一个函数来变换Observable发射的数据序列
 * flatMap( ), concatMap( ), and flatMapIterable( ) — 将Observable发射的数据集合变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable
 * switchMap( ) — 将Observable发射的数据集合变换为Observables集合，然后只发射这些Observables最近发射的数据
 * scan( ) — 对Observable发射的每一项数据应用一个函数，然后按顺序依次发射每一个值
 * groupBy( ) — 将Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
 * buffer( ) — 它定期从Observable收集数据到一个集合，然后把这些数据集合打包发射，而不是一次发射一个
 * window( ) — 定期将来自Observable的数据分拆成一些Observable窗口，然后发射这些窗口，而不是每次发射一项
 * cast( ) — 在发射之前强制将Observable发射的所有数据转换为指定类型
 */
public class OperationalAct extends AppCompatActivity {

    public static void startOperationalAct(Activity activity) {
        activity.startActivity(new Intent(activity, OperationalAct.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_operational);
    }


    /**
     * map 对序列的每一项都应用一个函数来变换Observable发射的数据序列
     */
    public void map(View view) {
        Observable.just("asd", "asdf")
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return TextUtils.equals("asd", s);
                    }
                }).compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        DLog.d(aBoolean);
                    }
                });
    }

    /**
     * 将Observable发射的数据集合变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable(无顺序)
     */
    public void flatMap(View view) {
        Observable.just("1", "2").flatMap(new Func1<String, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(final String s) {
                return Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(Integer.valueOf(s));
                        subscriber.onCompleted();
                    }
                });
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
     * 将Observable发射的数据集合变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable(有顺序)
     */
    public void concatMap(View view) {
        Observable.just("1", "2").concatMap(new Func1<String, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(final String s) {
                return Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(Integer.valueOf(s));
                        subscriber.onCompleted();
                    }
                });
            }
        }).compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void switchMap(View view) {
        Observable.just("A", "B", "C", "D", "E").switchMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.just(s);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                DLog.d("------>onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                DLog.d("------>onError()" + e);
            }

            @Override
            public void onNext(String s) {
                DLog.d("------>onNext:" + s);
            }
        });
    }

    /**
     * 对Observable发射的每一项数据应用一个函数，然后按顺序依次发射每一个值
     */
    public void scan(View view) {
        Observable.range(1, 5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer sum, Integer item) {
                        //参数sum就是上一次的计算结果
                        return sum + item;
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                DLog.d("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                DLog.d("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                DLog.d("Sequence complete.");
            }
        });
    }

    /**
     * 将Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
     */
    public void groupBy(View view) {
        Observable.range(1, 6).groupBy(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer % 2;
            }
        })//
                .compose(RxSchedulers.<GroupedObservable<Integer, Integer>>io_main())
                .subscribe(new Action1<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void call(GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {
                        final int key = integerIntegerGroupedObservable.getKey();
                        integerIntegerGroupedObservable//
                                .compose(RxSchedulers.<Integer>io_main())
                                .subscribe(new Action1<Integer>() {
                                    @Override
                                    public void call(Integer integer) {
                                        DLog.d(key + "key" + integer + "val");
                                    }
                                });
                    }
                });
    }

    /**
     * 它定期从Observable收集数据到一个集合，然后把这些数据集合打包发射，而不是一次发射一个
     */
    public void buffer(View view) {
        Observable.range(1,10).buffer(3).compose(RxSchedulers.<List<Integer>>io_main())
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        DLog.d(integers.size());
                    }
                });

        Observable.range(1, 10).buffer(2,2).compose(RxSchedulers.<List<Integer>>io_main())
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        DLog.d(integers.size()+">>>>>>>>>>");
                    }
                });
    }

    /**
     * 在发射之前强制将Observable发射的所有数据转换为指定类型
     */
    public void window(View view) {
        Observable.range(1,5).window(3).compose(RxSchedulers.<Observable<Integer>>io_main())
                .subscribe(new Action1<Observable<Integer>>() {
            @Override
            public void call(Observable<Integer> integerObservable) {
                DLog.d(">>>>");
                integerObservable.compose(RxSchedulers.<Integer>io_main())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                DLog.d(integer);
                            }
                        });
            }
        });
    }

    public void cast(View view) {
        Observable.just(new TestBean()).cast(TestBean.class).compose(RxSchedulers.<TestBean>io_main())
                .subscribe(new Action1<TestBean>() {
                    @Override
                    public void call(TestBean testBean) {
                        DLog.d(testBean);
                    }
                });
    }
}
