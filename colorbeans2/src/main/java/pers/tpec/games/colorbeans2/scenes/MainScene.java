package pers.tpec.games.colorbeans2.scenes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import pers.tpec.game.colorbeans2.R;
import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.games.colorbeans2.objects.Map;
import pers.tpec.games.colorbeans2.objects.NextBeans;
import pers.tpec.games.colorbeans2.objects.ScoreBoard;
import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.TpecView;

public class MainScene extends Scene {
    private int soMap;
    private int soNextBeans;
    private int soScoreBoard;
    private int soGameOverScene;

    private Bitmap bmpBackground;
    private int bmpBeans;
    private int bmpBeanBg;

    private Paint paint;

    public MainScene(@NonNull TpecView tpecView) {
        super(tpecView);
        paint = new Paint();
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
        canvas.drawBitmap(bmpBackground, 0, 0, paint);
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
        bmpBeans = loadBmp(R.mipmap.beans);
        bmpBeanBg = loadBmp(R.mipmap.bgg);

        soNextBeans = addSceneObject(new NextBeans());
        soMap = addSceneObject(new Map());
        soScoreBoard = addSceneObject(new ScoreBoard());
        soGameOverScene = addSceneObject(
                new GameOverScene(tpecView)
                        .setBorder(new Rect(80, 240, 640, 1040)));

        if (!getMap().loadGame()) {
            getMap().newGame();
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
            if (!getMap().saveGame()) {
                return true;
            }
        }
        return false;
    }
}
