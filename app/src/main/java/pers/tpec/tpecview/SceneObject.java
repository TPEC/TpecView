package pers.tpec.tpecview;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface SceneObject {
    void drawSelf(Canvas canvas);

    void logicSelf();

    boolean onTouch(MotionEvent event);

    boolean isNull();
}
