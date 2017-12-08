package com.yinzhi.colorbeans2.objects;

import android.graphics.Canvas;
import android.view.MotionEvent;

import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;

public class NextBeans implements SceneObject {
    public static final int BEAN_TYPE_COUNT = 6;

    private int[] nextBeans;

    public void generateNewRound() {
        Rand rand = new SimpleRand();
        for (int i = 0; i < nextBeans.length; i++) {
            nextBeans[i] = rand.i(BEAN_TYPE_COUNT);
        }
    }

    public int[] getNextBeans() {
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
    public boolean isNull() {
        return false;
    }
}
