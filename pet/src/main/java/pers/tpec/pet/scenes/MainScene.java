package pers.tpec.pet.scenes;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.logging.Logger;

import pers.tpec.pet.scenes.objects.Unit;
import pers.tpec.pet.utils.ViewResizer;
import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.widgets.Label;

public class MainScene extends Scene {
    private static final Logger LOG = Logger.getLogger(MainScene.class.getName());

    private ViewResizer viewResizer;
    private boolean viewMoving = false;

    public MainScene(@NonNull TpecView tpecView) {
        super(tpecView);
    }

    public MainScene setViewResizer(ViewResizer viewResizer) {
        this.viewResizer = viewResizer;
        return this;
    }

    public ViewResizer getViewResizer() {
        return viewResizer;
    }

    public Context getContext() {
        return tpecView.getActivityContext();
    }

    public boolean isViewMoving() {
        if (viewMoving) {
            viewMoving = false;
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        super.draw(canvas);
    }

    private int lastX, lastY, paramsX, paramsY;

    @Override
    public boolean onTouch(MotionEvent event) {
        if (viewResizer != null) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                paramsX = viewResizer.getX();
                paramsY = viewResizer.getY();
            } else if (action == MotionEvent.ACTION_MOVE) {
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                if (!viewMoving && dx * dx + dy * dy > 10 * 10) {
                    viewMoving = true;
                }
                if (viewMoving) {
                    viewResizer.offsetTo(paramsX + dx, paramsY + dy);
                }
            }
        }
        return super.onTouch(event);
    }

    public AssetManager getAssetManager() {
        return tpecView.getActivityContext().getAssets();
    }

    private int labelId;

    @Override
    public void load() {
        addSceneObject(new Unit(this));
        labelId = addSceneObject(
                new Label(new Rect(0, 0, 200, 200))
                        .setFontSize(30)
                        .setFontColor(Color.BLACK)
        );
    }

    public Label getLabel() {
        return (Label) getSceneObject(labelId);
    }

    @Override
    public void unload() {
        unloadBmpAll();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
