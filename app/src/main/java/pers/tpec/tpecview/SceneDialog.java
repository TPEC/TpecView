package pers.tpec.tpecview;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import pers.tpec.tpecview.controller.Border;
import pers.tpec.tpecview.controller.ControllerClassifier;

public abstract class SceneDialog extends Scene
        implements SceneObject, ControllerClassifier.OnClickListener {
    private Border border;
    private ControllerClassifier controllerClassifier;

    private boolean force;  //是否强制：否-点击其他地方可以直接关闭

    protected boolean isNull;

    public Border getBorder() {
        return border;
    }

    public SceneDialog setBorder(Border border) {
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
            public boolean inside(int x, int y) {
                return true;
            }
        }).setOnClickListener(this);
    }

    @Override
    public final void drawSelf(Canvas canvas) {
        draw(canvas);
    }

    @Override
    public final void logicSelf() {
        logic();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return !super.onTouch(event) && controllerClassifier.onTouch(event);
    }

    @Override
    public boolean click(int x, int y) {
        if (!force && !border.inside(x, y)) {
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