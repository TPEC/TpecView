package com.yinzhi.colorbeans2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.yinzhi.colorbeans2.scenes.MenuScene;

import pers.tpec.tpecview.ResManager;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.ViewManager;

public class MainActivity extends AppCompatActivity {

    private TpecView tpecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewManager.getInstance().setActivity(MainActivity.class);
        ResManager.getInstance().init(getResources());

        tpecView = new TpecView(this.getBaseContext(), 720, 1280, TpecView.SCALEMOD_PRESERVE);
        MenuScene menuScene = new MenuScene(tpecView);
        tpecView.setScene(menuScene);

        setContentView(tpecView);
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
