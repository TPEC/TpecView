package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.Border;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.utils.SharedPreferencesUtil;
import pers.tpec.tpecview.widgets.particles.ParticleFactory;

public class MenuScene extends Scene implements ControllerClassifier.OnClickListener {
    private ControllerClassifier controllerClassifier;

    public MenuScene(@NonNull TpecView tpecView) {
        super(tpecView);
        GameScenes.getInstance().setMenuScene(this);

        controllerClassifier = new ControllerClassifier().setBorder(new Border() {
            @Override
            public boolean inside(int x, int y) {
                return true;
            }
        }).setOnClickListener(this);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return controllerClassifier.onTouch(event) || super.onTouch(event);
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

    @Override
    public boolean click(int x, int y) {
        addSceneObject(ParticleFactory.createFireworkEffects(64f, x, y, 4f, 2.5f, Color.GREEN).playSetNull(2));
        return true;
    }
}
