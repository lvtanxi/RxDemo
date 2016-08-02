package lv.com.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * User: 吕勇
 * Date: 2016-08-02
 * Time: 10:24
 * Description:
 */
public class LActivty extends AppCompatActivity{
    private CompositeSubscription mCompositeSubscription;
    private  Subscription mSubscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_l);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSubscription= Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        ChooseAct.startChooseAct(LActivty.this);
                        finish();
                    }
                });
        addSubscription(mSubscription);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompositeSubscription.remove(mSubscription);
    }
    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscription);
    }
}
