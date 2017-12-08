package com.yinzhi.colorbeans2.scenes;

import android.support.annotation.NonNull;

import com.yinzhi.colorbeans2.objects.Map;
import com.yinzhi.colorbeans2.objects.NextBeans;

import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;

public class MainScene extends Scene {
    private int[][] map;

    private int soMap;
    private int soNextBeans;

    public MainScene(@NonNull TpecView tpecView) {
        super(tpecView);

    }

    @Override
    public void load() {
        NextBeans nextBeans = new NextBeans();
        soNextBeans = addSceneObject(nextBeans);
        soMap = addSceneObject(new Map(nextBeans));
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
