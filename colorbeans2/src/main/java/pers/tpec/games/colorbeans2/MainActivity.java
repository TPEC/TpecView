package pers.tpec.games.colorbeans2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import pers.tpec.games.colorbeans2.scenes.MenuScene;
import pers.tpec.tpecview.ResManager;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.ViewManager;
import pers.tpec.tpecview.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {

    private TpecView tpecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        ViewManager.getInstance().setActivity(MainActivity.class);
        ResManager.getInstance().init(getResources());
        SharedPreferencesUtil.setSpName("ColorBeans2");

        tpecView = new TpecView(this.getBaseContext(), 720, 1280, TpecView.SCALEMOD_PRESERVE);
        MenuScene menuScene = new MenuScene(tpecView);
        tpecView.setScene(menuScene);

        setContentView(tpecView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return tpecView != null && tpecView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tpecView.onPause();
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        super.onResume();
        tpecView.onResume();
    }
}
