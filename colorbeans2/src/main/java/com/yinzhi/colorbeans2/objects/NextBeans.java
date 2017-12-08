package com.yinzhi.colorbeans2.objects;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;

public class NextBeans implements SceneObject {
    public static final int BEAN_TYPE_COUNT = 6;

    private List<Integer> nextBeans = new ArrayList<>();

    public void generateNewRound(int amount) {
        Rand rand = new SimpleRand();
        nextBeans.clear();
        for (int i = 0; i < amount; i++) {
            nextBeans.add(rand.i(BEAN_TYPE_COUNT));
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
    public boolean isNull() {
        return false;
    }
}
