package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.widgets.Button;

public class MenuScene extends Scene {
    private Paint paint;

    public MenuScene(@NonNull TpecView tpecView) {
        super(tpecView);

        paint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void logic() {
        super.logic();
        try {
            switchScene(MainScene.class);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {

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
