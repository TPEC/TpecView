package pers.tpec.tpecview;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface SceneObject {
    void drawSelf(Canvas canvas);

    void logicSelf();

    boolean onTouch(MotionEvent event);

    boolean onKeyDown(int keyCode, KeyEvent event);

    boolean isNull();
}
