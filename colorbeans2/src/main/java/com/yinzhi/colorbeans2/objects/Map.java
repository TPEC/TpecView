package com.yinzhi.colorbeans2.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.RectBorder;
import pers.tpec.tpecview.utils.pathfinding.AStarPathFinding2d;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;
import pers.tpec.tpecview.widgets.gif.Gif;
import pers.tpec.tpecview.widgets.gif.SimpleGif;

public class Map implements SceneObject, ControllerClassifier.OnClickListener {
    private static final int MAP_TOP = 100;

    private static final int STATE_NULL = 0;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_MOVING = 2;
    private static final int STATE_NEW = 3;
    private static final int STATE_REMOVE = 4;

    private static final int movingInterval = 10;

    private int state;
    private int selectedId;

    private MapGrid[] mgs;
    private int mgVoidLeft;

    private ControllerClassifier controllerClassifier;

    private NextBeans nextBeans;

    private List<Integer> movingPath;


    public Map(NextBeans nextBeans) {
        mgs = new MapGrid[81];
        Paint paint = new Paint();
        Rect rectSrc = new Rect(0, 0, 80, 80);
        for (int i = 0; i < mgs.length; i++) {
            int ix = i % 9;
            int iy = i / 9;
            mgs[i] = new MapGrid(ix * 80, iy * 80 + MAP_TOP);
            mgs[i].paint = paint;
            mgs[i].rectSrc = rectSrc;
        }
        controllerClassifier = new ControllerClassifier();
        controllerClassifier.setBorder(new RectBorder(0, MAP_TOP, 720, 720))
                .setOnClickListener(this);
        state = STATE_NULL;
        this.nextBeans = nextBeans;
    }

    public void newGame() {
        mgVoidLeft = mgs.length;
        for (MapGrid mg : mgs) {
            mg.setValue(MapGrid.VALUE_VOID);
        }
        nextBeans.generateNewRound(4);
        createNewBeans();
    }

    @Override
    public void drawSelf(Canvas canvas) {
        for (MapGrid mg : mgs) {
            mg.drawSelf(canvas);
        }
    }

    private int movingFrame;
    private int movingIndex;

    @Override
    public void logicSelf() {
        for (MapGrid mg : mgs) {
            mg.logicSelf();
        }
        if (state == STATE_MOVING) {
            movingFrame--;
            if (movingFrame <= 0) {
                movingFrame = movingInterval;
                mgs[movingPath.get(movingIndex + 1)].setValue(mgs[movingPath.get(movingIndex)].value);
                mgs[movingPath.get(movingIndex)].setValue(MapGrid.VALUE_VOID);
                movingIndex++;
                if (movingIndex == movingPath.size() - 1) {
                    List<Integer> rl = checkRemove();
                    if (rl.isEmpty()) {
                        createNewBeans();
                    } else {
                        state = STATE_REMOVE;
                        mgVoidLeft += rl.size();
                        for (Integer i : rl) {
                            mgs[i].setStateRemove();
                        }
                        // TODO: 2017/12/8 add score
                    }
                }
            }
        }
    }

    private void createNewBeans() {
        state = STATE_NEW;
        List<Integer> nb = nextBeans.getNextBeans();
        Rand rand = new SimpleRand();
        for (Integer i : nb) {
            int p;
            do {
                p = rand.i(81);
            } while (mgs[p].value != MapGrid.VALUE_VOID);
            mgs[p].setStateNew(i);
            mgVoidLeft--;
            if (mgVoidLeft == 0) {
                gameOver();
                break;
            }
        }
    }

    private void gameOver() {
        // TODO: 2017/12/8  
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
                    movingPath = pathFinding.getPath(selectedId, i);
                    if (!movingPath.isEmpty()) {
                        state = STATE_MOVING;
                        movingIndex = 0;
                        movingFrame = movingInterval;
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

    private List<Integer> checkRemove() {
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int base = mgs[i * 9 + j].value;
                int comb = 1;
                if (base != MapGrid.VALUE_VOID) {
                    for (int k = j + 1; k < 9; k++) {
                        if (mgs[i * 9 + k].value == base) {
                            comb++;
                        }
                    }
                    if (comb >= 5) {
                        for (int k = 0; k < comb; k++) {
                            r.add(i * 9 + j + k);
                        }
                        break;
                    }
                }
            }
            for (int j = 0; j < 5; j++) {
                int base = mgs[i + j * 9].value;
                int comb = 1;
                if (base != MapGrid.VALUE_VOID) {
                    for (int k = j + 1; k < 9; k++) {
                        if (mgs[i + k * 9].value == base) {
                            comb++;
                        }
                    }
                    if (comb >= 5) {
                        for (int k = 0; k < comb; k++) {
                            r.add(i + (j + k) * 9);
                        }
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int base = mgs[i * 9 + j].value;
                int comb = 1;
                if (base != MapGrid.VALUE_VOID) {
                    for (int k = 1; i + k < 9 && j + k < 9; k++) {
                        if (mgs[(i + k) * 9 + j + k].value == base) {
                            comb++;
                        }
                    }
                    if (comb >= 5) {
                        for (int k = 0; k < comb; k++) {
                            r.add((i + k) * 9 + j + k);
                        }
                        break;
                    }
                }
            }
            for (int j = 4; j < 9; j++) {
                int base = mgs[i * 9 + j].value;
                int comb = 1;
                if (base != MapGrid.VALUE_VOID) {
                    for (int k = 1; i + k < 9 && j - k >= 0; k++) {
                        if (mgs[(i + k) * 9 + j - k].value == base) {
                            comb++;
                        }
                    }
                    if (comb >= 5) {
                        for (int k = 0; k < comb; k++) {
                            r.add((i + k) * 9 + j - k);
                        }
                        break;
                    }
                }
            }
        }
        return r;
    }

    public void gifFinish(Gif gif) {
        switch (state) {
            case STATE_NEW:
                nextBeans.generateNewRound(3);
                state = STATE_NULL;
                break;
            case STATE_REMOVE:
                state = STATE_NULL;
                break;
            default:
                break;
        }
    }

    class MapGrid implements Gif.OnFinishListener {
        static final int VALUE_VOID = 0;

        Gif gif;
        Bitmap bmpBg;
        Rect rectSrc;
        Rect rectDst;
        Paint paint;

        int value;
        int gridState;

        MapGrid(int left, int top) {
            this.rectDst = new Rect(left, top, left + 80, top + 80);
            gif = new SimpleGif();
            gif.setOnFinishListener(this);
        }

        void setStateNew(int color) {
            // TODO: 2017/12/8  
        }

        void setStateRemove() {
            gridState = STATE_REMOVE;
            // TODO: 2017/12/8  
        }

        void setValue(int value) {
            this.value = value;
            // TODO: 2017/12/8
        }

        void drawSelf(Canvas canvas) {
            if (bmpBg != null) {
                canvas.drawBitmap(bmpBg, rectSrc, rectDst, paint);
            }
            gif.drawSelf(canvas);
        }

        @Override
        public void finish(Gif gif) {
            if (this.gridState == STATE_REMOVE) {
                setValue(VALUE_VOID);
            }
            gifFinish(gif);
        }

        void logicSelf() {
            gif.logicSelf();
        }

        boolean isVoid() {
            return value == VALUE_VOID;
        }
    }
}
