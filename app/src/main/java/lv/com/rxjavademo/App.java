package lv.com.rxjavademo;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 10:43
 * Description:
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("lvtanxi");
    }
}
