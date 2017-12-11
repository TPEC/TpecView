package pers.tpec.games.colorbeans2.objects;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.widgets.Label;

public class ScoreBoard implements SceneObject {
    private MainScene mainScene;

    private Label label;

    private int score;

    public ScoreBoard(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    public void addScoreByRemove(final int count) {

    }

    @Override
    public void drawSelf(Canvas canvas) {

    }

    @Override
    public void logicSelf() {

    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
