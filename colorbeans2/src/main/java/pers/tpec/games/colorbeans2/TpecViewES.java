package pers.tpec.games.colorbeans2;

import android.content.Context;

import pers.tpec.tpecview.TpecView;

public class TpecViewES extends TpecView {
    /**
     * @param context      context of activity
     * @param windowWidth  target window width
     * @param windowHeight target window height
     * @param scaleMod     @SCALEMOD
     */
    public TpecViewES(Context context, int windowWidth, int windowHeight, int scaleMod) {
        super(context, windowWidth, windowHeight, scaleMod);
    }

    @Override
    public void run() {
        long startTime = System.nanoTime() - targetInterval;
        while (threadFlag) {
            if (sceneBack != null) {
                if (scene != null) {
                    scene.unload();
                }
                scene = sceneBack;
                sceneBack = null;
                setController(scene);
                if (!preloadScene) {
                    scene.load();
                }
                LOG.info("switch to scene:" + scene.toString());
            }
            if (scene != null) {
                scene.logic();
            }
            draw();
            long st = (targetInterval - (System.nanoTime() - startTime)) / 1000000;
            if (st > 0) {
                try {
                    Thread.sleep(st);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startTime = System.nanoTime();
        }
    }
}
