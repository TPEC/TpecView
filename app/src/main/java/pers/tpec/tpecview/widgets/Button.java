package pers.tpec.tpecview.widgets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;

import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.controller.RectBorder;

public class Button implements SceneObject {
    private Rect rectDst;
    private Rect rectSrc;

    private boolean enabled;
    private boolean visible;

    private Bitmap bmpBg;

    private Paint paint;

    private ControllerClassifier controllerClassifier;

    public Button(@NonNull Rect rectDst) {
        controllerClassifier = new ControllerClassifier();
        setRectDst(rectDst);
        enabled = true;
        visible = true;
        paint = new Paint();
    }

    public Button setBmp(@NonNull Bitmap bmpBg) {
        this.bmpBg = bmpBg;
        return this;
    }

    public Button setRectDst(@NonNull Rect rectDst) {
        this.rectDst = rectDst;
        controllerClassifier.setBorder(new RectBorder(rectDst));
        return this;
    }

    public Button setEnabled(final boolean enabled) {
        this.enabled = enabled;
        controllerClassifier.setEnabled(enabled);
        if (enabled)
            paint = new Paint();
        else
            paint.setColorFilter(new ColorMatrixColorFilter(new float[]{
                    0.3086f, 0.6094f, 0.0820f, 0, 0,
                    0.3086f, 0.6094f, 0.0820f, 0, 0,
                    0.3086f, 0.6094f, 0.0820f, 0, 0,
                    0, 0, 0, 1, 0
            }));
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Button setVisible(final boolean visible) {
        this.visible = visible;
        return this;
    }

    public Button setOnClickListener(ControllerClassifier.OnClickListener onClickListener) {
        controllerClassifier.setOnClickListener(onClickListener);
        return this;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (visible && bmpBg != null) {
            if (!controllerClassifier.isClickDown()) {
                canvas.drawBitmap(bmpBg, rectSrc, rectDst, paint);
            } else {
                canvas.drawBitmap(bmpBg, rectSrc, rectDst, paint);
            }
        }
    }

    @Override
    public void logicSelf() {

    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return controllerClassifier.onTouch(event);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    private class ControllerClassifier extends pers.tpec.tpecview.controller.ControllerClassifier {
        public boolean isClickDown() {
            return super._click;
        }
    }
}
