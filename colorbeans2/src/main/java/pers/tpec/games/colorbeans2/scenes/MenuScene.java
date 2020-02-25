package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;

import pers.tpec.game.colorbeans2.R;
import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.tpecview.ResManager;
import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.TrueBorder;
import pers.tpec.tpecview.widgets.Button;
import pers.tpec.tpecview.widgets.Label;
import pers.tpec.tpecview.widgets.particles.ParticleFactory;

public class MenuScene extends Scene implements ControllerClassifier.OnClickListener {
    private ControllerClassifier controllerClassifier;

    private int soNewGameBtn;
    private int soLoadGameBtn;
    private int soGameStaticBtn;

    public MenuScene(@NonNull TpecView tpecView) {
        super(tpecView);
        GameScenes.getInstance().setMenuScene(this);

        controllerClassifier = new ControllerClassifier().setBorder(new TrueBorder()).setOnClickListener(this);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        super.draw(canvas);
    }

    @Override
    public void logic() {
        super.logic();
    }

    @Override
    public void load() {
        soNewGameBtn = addSceneObject(
                new Button(new Rect(60, 60, 660, 180))
                        .setBmp(ResManager.getInstance().decodeResource(R.mipmap.beans))
                        .setOnClickListener(new ControllerClassifier.OnClickListener() {
                            @Override
                            public boolean click(int x, int y) {
                                try {
                                    switchScene(MainScene.class, MainScene.PARAM_NEWGAME);
                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        })
        );

        addSceneObject(
                new Label(((Button) getSceneObject(soNewGameBtn)).getRectDst())
                        .setAntiAlias(true)
                        .setFontSize(50)
                        .setFontColor(Color.WHITE)
                        .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
                        .setText("新游戏")
        );

        soLoadGameBtn = addSceneObject(
                new Button(new Rect(60, 200, 660, 640))
                        .setBmp(ResManager.getInstance().decodeResource(R.mipmap.beans))
                        .setOnClickListener(new ControllerClassifier.OnClickListener() {
                            @Override
                            public boolean click(int x, int y) {
                                try {
                                    switchScene(MainScene.class, MainScene.PARAM_LOADGAME);
                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        })
        );

        addSceneObject(
                new Label(((Button) getSceneObject(soLoadGameBtn)).getRectDst())
                        .setAntiAlias(true)
                        .setFontSize(50)
                        .setFontColor(Color.WHITE)
                        .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
                        .setText("继续游戏")
        );

        soGameStaticBtn = addSceneObject(
                new Button(new Rect(60, 660, 660, 780))
                        .setBmp(ResManager.getInstance().decodeResource(R.mipmap.beans))
                        .setOnClickListener(new ControllerClassifier.OnClickListener() {
                            @Override
                            public boolean click(int x, int y) {
                                return true;
                            }
                        })
        );

        addSceneObject(
                new Label(((Button) getSceneObject(soGameStaticBtn)).getRectDst())
                        .setAntiAlias(true)
                        .setFontSize(50)
                        .setFontColor(Color.WHITE)
                        .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
                        .setText("游戏统计")
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event) || controllerClassifier.onTouch(event);
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
        addSceneObject(ParticleFactory.createFireworkEffects(64f, x, y, 8f, 2.5f, Color.GREEN).playSetNull(2));
        return true;
    }
}
