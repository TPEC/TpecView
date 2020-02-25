package pers.tpec.tpecview;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import android.view.MotionEvent;

import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.RectBorder;
import pers.tpec.tpecview.controller.TrueBorder;

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

    public Rect getBorderRect_offset() {
        return new Rect(0, 0, border.getRect().width(), border.getRect().height());
    }

    public SceneDialog setBorder(RectBorder border) {
        this.border = border;
        controllerClassifier.setBorder(new RectBorder(getBorderRect_offset()));
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
        controllerClassifier.setBorder(new TrueBorder()).setOnClickListener(this);
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
        if (shown) {
            event.setLocation(event.getX() - getBorderRect().left, event.getY() - getBorderRect().top);
            if (super.onTouch(event)) {
                return true;
            } else {
                if (controllerClassifier.onTouch(event)) {
                    return true;
                } else {
                    event.setLocation(event.getX() + getBorderRect().left, event.getY() + getBorderRect().top);
                }
            }
        }
        return false;
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
