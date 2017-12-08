package com.yinzhi.colorbeans2.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.List;

import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.RectBorder;
import pers.tpec.tpecview.utils.pathfinding.AStarPathFinding2d;
import pers.tpec.tpecview.widgets.gif.Gif;
import pers.tpec.tpecview.widgets.gif.SimpleGif;

public class Map implements SceneObject, ControllerClassifier.OnClickListener, Gif.OnFinishListener {
    private static final int MAP_TOP = 100;

    private static final int STATE_NULL = 0;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_MOVING = 2;
    private static final int STATE_NEW = 3;
    private static final int STATE_REMOVE = 4;

    private int state;
    private int selectedId;

    private MapGrid[] mgs;
    private ControllerClassifier controllerClassifier;

    private NextBeans nextBeans;

    public Map(NextBeans nextBeans) {
        mgs = new MapGrid[81];
        Paint paint = new Paint();
        Rect rectSrc = new Rect(0, 0, 80, 80);
        for (int i = 0; i < mgs.length; i++) {
            int ix = i % 9;
            int iy = i / 9;
            mgs[i] = new MapGrid(ix * 80, iy * 80 + MAP_TOP, this);
            mgs[i].paint = paint;
            mgs[i].rectSrc = rectSrc;
        }
        controllerClassifier = new ControllerClassifier();
        controllerClassifier.setBorder(new RectBorder(0, MAP_TOP, 720, 720))
                .setOnClickListener(this);
        state = STATE_NULL;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        for (MapGrid mg : mgs) {
            mg.drawSelf(canvas);
        }
    }

    @Override
    public void logicSelf() {
        for (MapGrid mg : mgs) {
            mg.logicSelf();
        }

    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return controllerClassifier.onTouch(event);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean click(int x, int y) {
        int ix = x / 80;
        int iy = (y - MAP_TOP) / 80;
        int i = iy * 9 + ix;
        switch (state) {
            case STATE_NULL:
                if (!mgs[i].isVoid()) {
                    state = STATE_SELECTED;
                    selectedId = i;
                }
                break;
            case STATE_SELECTED:
                if (!mgs[i].isVoid()) {
                    selectedId = i;
                } else {
                    AStarPathFinding2d pathFinding = new AStarPathFinding2d();
                    pathFinding.setMap(getMap(), 9);
                    List<Integer> path = pathFinding.getPath(selectedId, i);
                    if (!path.isEmpty()) {
                        state = STATE_MOVING;
                        // TODO: 2017/12/7 moving init
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private int[] getMap() {
        int[] r = new int[81];
        for (int i = 0; i < 81; i++) {
            r[i] = mgs[i].value;
        }
        return r;
    }

    @Override
    public void finish(Gif gif) {
        switch (state) {
            case STATE_NEW:
                nextBeans.generateNewRound();
                state = STATE_NULL;
                break;
            case STATE_REMOVE:
                state = STATE_NEW;
                // TODO: 2017/12/7
                break;
            default:
                break;
        }
    }

    class MapGrid {
        static final int VALUE_VOID = 0;

        Gif gif;
        Bitmap bmpBg;
        Rect rectSrc;
        Rect rectDst;
        Paint paint;

        int value;

        public MapGrid(int left, int top, Gif.OnFinishListener onFinishListener) {
            this.rectDst = new Rect(left, top, left + 80, top + 80);
            gif = new SimpleGif();
            gif.setOnFinishListener(onFinishListener);
        }

        void drawSelf(Canvas canvas) {
            if (bmpBg != null) {
                canvas.drawBitmap(bmpBg, rectSrc, rectDst, paint);
            }
            gif.drawSelf(canvas);
        }

        void logicSelf() {
            gif.logicSelf();
        }

        boolean isVoid() {
            return value == VALUE_VOID;
        }
    }
}
