package pers.tpec.tpecview.widgets;

import android.graphics.Canvas;
import android.view.MotionEvent;

import pers.tpec.tpecview.SceneObject;

public class Label implements SceneObject {
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
    public boolean isNull() {
        return false;
    }
}
