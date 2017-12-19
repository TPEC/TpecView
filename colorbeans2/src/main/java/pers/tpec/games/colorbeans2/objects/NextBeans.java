package pers.tpec.games.colorbeans2.objects;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.SharedPreferencesUtil;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;

public class NextBeans implements SceneObject {
    public static final int BEAN_TYPE_COUNT = 6;

    private static final int NEXT_BEAN_TOP = 150;

    private List<Integer> nextBeans = new ArrayList<>();

    private MainScene mainScene;

    private Bitmap bmpBeans;
    private Bitmap bmpBeanBg;

    private Rect rectSrc, rectDst;
    private Paint paint;

    public NextBeans() {
        this.mainScene = GameScenes.getInstance().getMainScene();
        bmpBeans = mainScene.getBmp(mainScene.getBmpBeans());
        bmpBeanBg = mainScene.getBmp(mainScene.getBmpBeanBg());
        rectSrc = new Rect(0, 0, 80, 80);
        rectDst = new Rect(0, 0, 80, 80);
        paint = new Paint();
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

    public void loadGame() {
        SharedPreferences sp = SharedPreferencesUtil.getSP(mainScene.getContext());
        StringBuilder nbd = new StringBuilder();
        Rand rand = new SimpleRand();
        for (int i = 0; i < 3; i++) {
            nbd.append((char) rand.i(1, BEAN_TYPE_COUNT + 1));
        }
        String nb = sp.getString("savedNextBeans", nbd.toString());
        nextBeans.clear();
        for (int i = 0; i < nb.length(); i++) {
            nextBeans.add((int) nb.charAt(i));
        }
    }

    public void saveGame() {
        StringBuilder nb = new StringBuilder();
        for (Integer i : nextBeans) {
            int j = i;
            nb.append((char) j);
        }
        java.util.Map<String, Object> m = new HashMap<>();
        m.put("savedNextBeans", nb.toString());
        SharedPreferencesUtil.save(mainScene.getContext(), m);
    }

    @Override
    public void drawSelf(Canvas canvas) {
        rectDst.offsetTo(360 - 40 * nextBeans.size(), NEXT_BEAN_TOP);
        for (Integer i : nextBeans) {
            rectSrc.offsetTo(0, 0);
            canvas.drawBitmap(bmpBeanBg, rectSrc, rectDst, paint);
            rectSrc.offsetTo((i - 1) * 80, 0);
            canvas.drawBitmap(bmpBeans, rectSrc, rectDst, paint);
            rectDst.offset(80, 0);
        }
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
