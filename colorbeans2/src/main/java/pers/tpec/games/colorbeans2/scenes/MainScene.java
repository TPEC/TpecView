package pers.tpec.games.colorbeans2.scenes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import java.lang.reflect.InvocationTargetException;

import pers.tpec.game.colorbeans2.R;
import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.games.colorbeans2.objects.Map;
import pers.tpec.games.colorbeans2.objects.NextBeans;
import pers.tpec.games.colorbeans2.objects.ScoreBoard;
import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.RectBorder;

public class MainScene extends Scene {
    public static final int PARAM_NEWGAME = 0;
    public static final int PARAM_LOADGAME = 1;

    private int soMap;
    private int soNextBeans;
    private int soScoreBoard;
    private int soGameOverScene;

    private Bitmap bmpBackground;
    private int bmpBeans;
    private int bmpBeanBg;

    private Paint paint;

    private final Rect bmpBgRect = new Rect(0, 0, 720, Map.MAP_TOP);

    public MainScene(@NonNull TpecView tpecView) {
        super(tpecView);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        GameScenes.getInstance().setMainScene(this);
    }

    public NextBeans getNextBeans() {
        return (NextBeans) getSceneObject(soNextBeans);
    }

    public Map getMap() {
        return (Map) getSceneObject(soMap);
    }

    public ScoreBoard getScoreBoard() {
        return (ScoreBoard) getSceneObject(soScoreBoard);
    }

    public GameOverScene getGameOverScene() {
        return (GameOverScene) getSceneObject(soGameOverScene);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bmpBackground, bmpBgRect, bmpBgRect, paint);
        canvas.drawRect(0, Map.MAP_TOP + 720, 720, 1280, paint);
        super.draw(canvas);
    }

    public int getBmpBeans() {
        return bmpBeans;
    }

    public int getBmpBeanBg() {
        return bmpBeanBg;
    }

    public int addSceneObject2(SceneObject sceneObject) {
        return addSceneObject(sceneObject);
    }

    public Context getContext() {
        return tpecView.getContext();
    }

    @Override
    public void load() {
        bmpBackground = getBmp(loadBmp(R.mipmap.bgl));
        bmpBeans = loadBmp(R.mipmap.beans2);
        bmpBeanBg = loadBmp(R.mipmap.bgg);

        soNextBeans = addSceneObject(new NextBeans());
        soMap = addSceneObject(new Map());
        soScoreBoard = addSceneObject(new ScoreBoard());
        soGameOverScene = addSceneObject(
                new GameOverScene(tpecView)
                        .setBorder(new RectBorder(80, 240, 560, 800)));
        if (param == PARAM_NEWGAME) {
            getMap().newGame();
        } else {
            if (!getMap().loadGame()) {
                getMap().newGame();
            }
        }
    }

    @Override
    public void unload() {
        clearSceneObject();
//        unloadBmpAll();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getMap().saveGame()) {
                try {
                    switchScene(MenuScene.class);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
