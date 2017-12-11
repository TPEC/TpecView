package pers.tpec.games.colorbeans2.objects;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;

public class NextBeans implements SceneObject {
    public static final int BEAN_TYPE_COUNT = 6;

    private List<Integer> nextBeans = new ArrayList<>();

    private MainScene mainScene;

    public NextBeans(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    public void generateNewRound(int amount) {
        Rand rand = new SimpleRand();
        nextBeans.clear();
        for (int i = 0; i < amount; i++) {
            nextBeans.add(rand.i(1, BEAN_TYPE_COUNT + 1));
        }
    }

    public List<Integer> getNextBeans() {
        return nextBeans;
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
