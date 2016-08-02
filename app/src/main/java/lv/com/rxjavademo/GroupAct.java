package lv.com.rxjavademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 16:59
 * Description:这个页面展示的操作符可用于组合多个Observables。
 * startWith( ) — 在数据序列的开头增加一项数据
 * merge( ) — 将多个Observable合并为一个
 * mergeDelayError( ) — 合并多个Observables，让没有错误的Observable都完成后再发射错误通知
 * zip( ) — 使用一个函数组合多个Observable发射的数据集合，然后再发射这个结果
 * and( ), then( ), and when( ) — (rxjava-joins) 通过模式和计划组合多个Observables发射的数据集合
 * combineLatest( ) — 当两个Observables中的任何一个发射了一个数据时，通过一个指定的函数组合每个Observable发射的最新数据（一共两个数据），然后发射这个函数的结果
 * join( ) and groupJoin( ) — 无论何时，如果一个Observable发射了一个数据项，只要在另一个Observable发射的数据项定义的时间窗口内，就将两个Observable发射的数据合并发射
 * switchOnNext( ) — 将一个发射Observables的Observable转换成另一个Observable，后者发射这些Observables最近发射的数据
 */
public class GroupAct extends AppCompatActivity {

    public static void startGroupAct(Activity activity) {
        activity.startActivity(new Intent(activity, GroupAct.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_group);
    }

    public void startWith(View view) {
        Observable.range(1, 10).startWith(0)
                .compose(RxSchedulers.<Integer>io_main())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void merge(View view) {
        Observable.merge(Observable.range(0, 2), Observable.just("lv", "tanxi"))
                .compose(RxSchedulers.<Serializable>io_main())
                .subscribe(new Action1<Serializable>() {
                    @Override
                    public void call(Serializable serializable) {
                        DLog.d(serializable);
                    }
                });
    }

    public void mergeDelayError(View view) {
        //产生0,5,10数列,最后会产生一个错误
        Observable<Long> errorObservable = Observable.error(new Exception("this is end!"));
        Observable<Long> observable1 = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                }).take(3).mergeWith(errorObservable.delay(3500, TimeUnit.MILLISECONDS));

        //产生0,10,20,30,40数列
        Observable<Long> observable2 = Observable.interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong + 11;
                    }
                }).take(5);

        Observable.mergeDelayError(observable1, observable2)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        DLog.d("Sequence complete.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.d("Error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        DLog.d("Next:" + aLong);
                    }
                });
    }

    public void zip(View view) {
        Observable<Integer> observable1 = Observable.just(10, 20, 30);
        Observable<Integer> observable2 = Observable.just(4, 8, 12, 16);
        Observable.zip(observable1, observable2, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                DLog.d("Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                DLog.d("Error: " + e.getMessage());
            }

            @Override
            public void onNext(Integer value) {
                DLog.d("Next:" + value);
            }
        });
        Observable.concat(observable1,observable2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }

    public void switchOnNext(View view) {
        Observable<Observable<Long>> observable = Observable.interval(0, 500, TimeUnit.MILLISECONDS).map(new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.interval(0, 200, TimeUnit.MILLISECONDS).map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                }).take(5);
            }
        }).take(2);

        Observable.switchOnNext(observable).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                DLog.d("Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                DLog.d("Error: " + e.getMessage());
            }

            @Override
            public void onNext(Long aLong) {
                DLog.d("Next:" + aLong);
            }
        });
    }

    public void join(View view) {
        Observable.just(1).join(Observable.just(2), new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.just(integer).delay(100, TimeUnit.MILLISECONDS);
            }
        }, new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.just(integer).delay(2000, TimeUnit.SECONDS);
            }
        }, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                DLog.d(integer);
            }
        });





        Observable.just(1L,2L).groupJoin(Observable.just(3L,4L), new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.just(aLong).delay(1600, TimeUnit.MILLISECONDS);
            }
        }, new Func1<Long, Observable<Long>>() {
            @Override
            public Observable<Long> call(Long aLong) {
                return Observable.just(aLong).delay(600, TimeUnit.MILLISECONDS);
            }
        }, new Func2<Long, Observable<Long>, Observable<Long>>() {
            @Override
            public Observable<Long> call(final Long aLong, Observable<Long> observable) {
                return observable.map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong2) {
                        return aLong + aLong2;
                    }
                });
            }
        }).subscribe(new Subscriber<Observable<Long>>() {
            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }

            @Override
            public void onError(Throwable e) {
                System.err.println("Error: " + e.getMessage());
            }

            @Override
            public void onNext(Observable<Long> observable) {
                observable.subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                      DLog.d("Next: " + aLong);
                    }
                });
            }
        });
    }

    public void combineLatest(View view) {//有个问题就是第一个Observable只显示了最后一个
        Observable.combineLatest(Observable.just(1,2), Observable.just("sda", "sadf"), new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return integer+s;
            }
        }).compose(RxSchedulers.<String>io_main())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        DLog.d(s);
                    }
                });
    }

    public void concat(View view) {
        Observable<Integer> observable1 = Observable.just(10, 20, 30);
        Observable<Integer> observable2 = Observable.just(4, 8, 12, 16);
        Observable.concat(observable1,observable2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DLog.d(integer);
                    }
                });
    }
}
