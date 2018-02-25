package pers.tpec.tpecview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import pers.tpec.tpecview.controller.Border;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.RectBorder;

public abstract class SceneDialog extends Scene
        implements SceneObject, ControllerClassifier.OnClickListener {
    private RectBorder border;
    private ControllerClassifier controllerClassifier;

    private boolean force;  //是否强制：否-点击其他地方可以直接关闭

    protected boolean isNull;

    private boolean shown = false;

    public void show() {
        this.load();
        shown = true;
    }

    public SceneDialog hide() {
        shown = false;
        this.unload();
        return this;
    }

    public Rect getBorderRect() {
        return border.getRect();
    }

    public SceneDialog setBorder(RectBorder border) {
        this.border = border;
        return this;
    }

    public boolean isForce() {
        return force;
    }

    public SceneDialog setForce(boolean force) {
        this.force = force;
        return this;
    }

    public SceneDialog(@NonNull TpecView tpecView) {
        super(tpecView);
        isNull = false;
        controllerClassifier = new ControllerClassifier();
        controllerClassifier.setBorder(new Border() {
            @Override
            public boolean contains(int x, int y) {
                return true;
            }
        }).setOnClickListener(this);
    }

    @Override
    public final void drawSelf(Canvas canvas) {
        if (shown) {
            canvas.translate(getBorderRect().left, getBorderRect().top);
            draw(canvas);
            canvas.translate(-getBorderRect().left, -getBorderRect().top);
        }
    }

    @Override
    public final void logicSelf() {
        if (shown) {
            logic();
        }
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return shown && !super.onTouch(event) && controllerClassifier.onTouch(event);
    }

    @Override
    public boolean click(int x, int y) {
        if (!force && !border.contains(x, y)) {
            isNull = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isNull() {
        return isNull;
    }
}
