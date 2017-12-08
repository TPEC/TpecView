package com.yinzhi.colorbeans2.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.widgets.Button;

public class MenuScene extends Scene {
    private Bitmap bmpBackground;
    private Paint paint;

    public MenuScene(@NonNull TpecView tpecView) {
        super(tpecView);

        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        if (bmpBackground != null) {
            canvas.drawBitmap(bmpBackground, 0, 0, paint);
        }
        super.draw(canvas);
    }

    @Override
    public void load() {
        addSceneObject(new Button().setOnClickListener(new ControllerClassifier.OnClickListener() {
            @Override
            public boolean click(int x, int y) {
                return false;
            }
        }), 1);

    }

    @Override
    public void unload() {
        clearSceneObject();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
