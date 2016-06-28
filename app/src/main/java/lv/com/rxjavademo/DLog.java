package lv.com.rxjavademo;

import com.orhanobut.logger.Logger;

/**
 * Esf
 * Created by maxmal on 15/11/3.
 */
public class DLog {

    private static boolean sDebug = BuildConfig.DEBUG;


    public static void d(Object message, Object... args) {
        if (sDebug){
            if(null==message)message="打印了空消息";
            Logger.i(message.toString(), args);
        }
    }
}
