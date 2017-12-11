package pers.tpec.tpecview.controller;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public interface Controller {
    boolean onTouch(MotionEvent event);

    boolean onKeyDown(int keyCode, KeyEvent event);
}
