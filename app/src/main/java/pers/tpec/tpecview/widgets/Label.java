package pers.tpec.tpecview.widgets;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import pers.tpec.tpecview.SceneObject;

public class Label implements SceneObject {
    private Rect rectDst;
    private String string;

    @Override
    public void drawSelf(Canvas canvas) {

    }

    @Override
    public void logicSelf() {

    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
