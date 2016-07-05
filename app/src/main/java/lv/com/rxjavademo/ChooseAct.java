package lv.com.rxjavademo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 15:05
 * Description:
 */
public class ChooseAct extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_choose);
    }

    public void toBasicsAct(View view) {
        BasicsAct.startBasicsAct(this);
    }

    public void toOperational(View view) {
        OperationalAct.startOperationalAct(this);
    }

    public void toFilter(View view) {
        FilterAct.startFilterAct(this);
    }

    public void toGroup(View view) {
        GroupAct.startGroupAct(this);
    }
}
