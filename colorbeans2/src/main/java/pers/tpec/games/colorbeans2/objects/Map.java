package pers.tpec.games.colorbeans2.objects;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.RectBorder;
import pers.tpec.tpecview.utils.SharedPreferencesUtil;
import pers.tpec.tpecview.utils.pathfinding.AStarPathFinding2d;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;
import pers.tpec.tpecview.widgets.gif.Gif;
import pers.tpec.tpecview.widgets.gif.SimpleGif;
import pers.tpec.tpecview.widgets.particles.ParticleFactory;
import pers.tpec.tpecview.widgets.words.FadedWords;

public class Map implements SceneObject, ControllerClassifier.OnClickListener {
    public static final int MAP_TOP = 280;

    private static final int STATE_NULL = 0;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_MOVING = 2;
    private static final int STATE_NEW = 3;
    private static final int STATE_REMOVE = 4;
    private static final int STATE_GAME_OVER = 5;

    private static final int movingInterval = 1;

    private int state;
    private int selectedId;

    private Bitmap bmpBeans;

    private MapGrid[] mgs;
    private int mgVoidLeft;

    private ControllerClassifier controllerClassifier;

    private MainScene mainScene;

    private List<Integer> movingPath;

    private int combo;


    public Map() {
        this.mainScene = GameScenes.getInstance().getMainScene();
        this.bmpBeans = mainScene.getBmp(mainScene.getBmpBeans());
        mgs = new MapGrid[81];
        Paint paint = new Paint();
        for (int i = 0; i < mgs.length; i++) {
            int ix = i % 9;
            int iy = i / 9;
            mgs[i] = new MapGrid(ix * 80, iy * 80 + MAP_TOP, paint);
        }
        controllerClassifier = new ControllerClassifier();
        controllerClassifier.setBorder(new RectBorder(0, MAP_TOP, 720, 720))
                .setOnClickListener(this);
        state = STATE_NULL;
    }

    public void newGame() {
        mgVoidLeft = mgs.length;
        for (MapGrid mg : mgs) {
            mg.setValue(MapGrid.VALUE_VOID);
        }
        mainScene.getNextBeans().generateNewRound(5);
        createNewBeans();
        mainScene.getScoreBoard().clearScore();
        combo = 0;
//        gameOver();
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
        if (state == STATE_GAME_OVER) {
            return;
        }
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
                    Set<Integer> rl = checkRemove();
                    if (rl.isEmpty()) {
                        combo = 0;
                        createNewBeans();
                    } else {
                        combo++;
                        state = STATE_REMOVE;
                        mgVoidLeft += rl.size();
                        for (Integer i : rl) {
                            mgs[i].setStateRemove();
                        }
                        int addScore = mainScene.getScoreBoard().addScoreByRemove(rl.size(), combo);

                        int x = mgs[movingPath.get(movingIndex)].rectDst.centerX();
                        int y = mgs[movingPath.get(movingIndex)].rectDst.centerY();
                        if (x > 640) {
                            x = 640;
                        }
                        mainScene.addSceneObject2(
                                new FadedWords(
                                        x,
                                        y,
                                        "+" + String.valueOf(addScore / combo).trim(),
                                        0, -4f,
                                        15,
                                        new int[]{Color.WHITE, Color.argb(0, 255, 255, 255)},
                                        new float[]{0, 1f},
                                        50
                                )
                        );
                        if (combo >= 2) {
                            mainScene.addSceneObject2(
                                    new FadedWords(
                                            x,
                                            y + 60,
                                            "X" + String.valueOf(combo).trim(),
                                            0, -4f,
                                            15,
                                            new int[]{Color.YELLOW, Color.argb(0, 255, 255, 0)},
                                            new float[]{0, 1f},
                                            75
                                    )
                            );
                        }
                    }
                }
            }
        }
    }

    public boolean loadGame() {
        SharedPreferences sp = SharedPreferencesUtil.getSP(mainScene.getContext());
        String sm = sp.getString("savedMap", "");
        if (sm.isEmpty()) {
            return false;
        }
        mgVoidLeft = 0;
        for (int i = 0; i < sm.length(); i++) {
            mgs[i].setValue((int) sm.charAt(i));
            if (mgs[i].isVoid()) {
                mgVoidLeft++;
            }
        }
        combo = sp.getInt("combo", 0);
        mainScene.getScoreBoard().setScore(sp.getInt("savedScore", 0));
        mainScene.getScoreBoard().updateHighestScore();
        mainScene.getNextBeans().loadGame();
        if (mgVoidLeft == 0) {
            gameOver();
        } else {
            state = STATE_NULL;
            controllerClassifier.setEnabled(true);
        }
        return true;
    }

    public boolean saveGame() {
        if (state == STATE_NULL || state == STATE_SELECTED) {
            java.util.Map<String, Object> m = new HashMap<>();
            StringBuilder sm = new StringBuilder();
            for (MapGrid mg : mgs) {
                sm.append((char) mg.value);
            }
            m.put("savedMap", sm.toString());
            m.put("savedScore", mainScene.getScoreBoard().getScore());
            m.put("combo", combo);
            SharedPreferencesUtil.save(mainScene.getContext(), m);
            mainScene.getNextBeans().saveGame();
            return true;
        }
        return false;
    }

    public boolean cleanSavedGame() {
        java.util.Map<String, Object> m = new HashMap<>();
        m.put("savedMap", "");
        m.put("savedScore", 0);
        SharedPreferencesUtil.save(mainScene.getContext(), m);
        return true;
    }

    private void createNewBeans() {
        state = STATE_NEW;
        controllerClassifier.setEnabled(true);
        List<Integer> nb = mainScene.getNextBeans().getNextBeans();
        Rand rand = new SimpleRand();
        for (int i : nb) {
            int p;
            do {
                p = rand.i(81);
            } while (mgs[p].value != MapGrid.VALUE_VOID);
            mgs[p].setStateNew(i);
            mgVoidLeft--;
            if (mgVoidLeft <= 0) {
                break;
            }
        }
    }

    private void gameOver() {
        state = STATE_GAME_OVER;
        controllerClassifier.setEnabled(false);
        cleanSavedGame();
        mainScene.getGameOverScene().show();

        // TODO: 2017/12/8  
    }

    private void finishNewBeans() {
        if (state == STATE_NEW) {
            if (mgVoidLeft <= 0) {
                gameOver();
                return;
            }
            state = STATE_NULL;
            mainScene.getNextBeans().generateNewRound(3);
            Set<Integer> rl = checkRemove();
            if (!rl.isEmpty()) {
                state = STATE_REMOVE;
                mgVoidLeft += rl.size();
                for (Integer i : rl) {
                    mgs[i].setStateRemove();
                }
            }
        }
    }

    private void finishRemoveBeans() {
        if (state == STATE_REMOVE) {
            state = STATE_NULL;
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
                    mgs[i].setSelected(true);
                }
                break;
            case STATE_SELECTED:
                if (!mgs[i].isVoid()) {
                    mgs[selectedId].setSelected(false);
                    selectedId = i;
                    mgs[i].setSelected(true);
                } else {
                    AStarPathFinding2d pathFinding = new AStarPathFinding2d();
                    pathFinding.setMap(getMap(), 9);
                    movingPath = pathFinding.getPath(selectedId, i);
                    if (!movingPath.isEmpty()) {
                        mgs[selectedId].setSelected(false);
                        state = STATE_MOVING;
                        movingIndex = 0;
                        movingFrame = 1;
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

    private Set<Integer> checkRemove() {
        Set<Integer> r = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                int base = mgs[i * 9 + j].value;
                int comb = 1;
                if (base != MapGrid.VALUE_VOID) {
                    for (int k = j + 1; k < 9; k++) {
                        if (mgs[i * 9 + k].value == base) {
                            comb++;
                        } else {
                            break;
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
                        } else {
                            break;
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
                        } else {
                            break;
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
                        } else {
                            break;
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
                mainScene.getNextBeans().generateNewRound(3);
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
        static final int FADE_SPEED = 32;
        static final int VALUE_VOID = 0;

        Gif gifBg;
        Rect rectSrc;
        Rect rectDst;
        Paint paint;

        int value;
        int gridState;

        int flashIndex;

        MapGrid(int left, int top, Paint paint) {
            this.rectDst = new Rect(left, top, left + 80, top + 80);
            gifBg = new SimpleGif();
            gifBg.setMode(Gif.MODE_RETURN, false, 2)
                    .setPaint(paint)
                    .setRes(mainScene.getBmp(mainScene.getBmpBeanBg()), Gif.RES_MODE_HORIZON, 6)
                    .setRectDst(rectDst);
            this.paint = new Paint();
            this.rectSrc = new Rect(0, 0, 80, 80);
        }

        void setSelected(boolean selected) {
            if (selected) {
                gifBg.play();
            } else {
                gifBg.pause();
                gifBg.setFrame(0);
            }
        }

        void setStateNew(int color) {
            setValue(color);
            gridState = STATE_NEW;
            flashIndex = 0;
            paint.setAlpha(0);
            // TODO: 2017/12/8  
        }

        void setStateRemove() {
            gridState = STATE_REMOVE;
            flashIndex = 256 / FADE_SPEED;
            int color;
            switch (value) {
                case 1:
                    color = Color.argb(255, 159, 0, 0);
                    break;
                case 2:
                    color = Color.argb(255, 0, 159, 0);
                    break;
                case 3:
                    color = Color.argb(255, 0, 0, 159);
                    break;
                case 4:
                    color = Color.argb(255, 159, 159, 0);
                    break;
                case 5:
                    color = Color.argb(255, 159, 0, 159);
                    break;
                case 6:
                    color = Color.argb(255, 0, 159, 159);
                    break;
                case 7:
                    color = Color.argb(255, 255, 127, 0);
                    break;
                default:
                    color = 0;
            }
            mainScene.addSceneObject2(ParticleFactory.createFireworkEffects(
                    15, rectDst.centerX(), rectDst.centerY(), 6, 1.75f, color
            ).playSetNull(1));
            // TODO: 2017/12/8  
        }

        void setValue(int value) {
            this.value = value;
            if (value != VALUE_VOID) {
                rectSrc.offsetTo((value - 1) * 80, 0);
                paint.setAlpha(255);
            }
            // TODO: 2017/12/8
        }

        void drawSelf(Canvas canvas) {
            gifBg.drawSelf(canvas);
            if (value != VALUE_VOID) {
                canvas.drawBitmap(bmpBeans, rectSrc, rectDst, paint);
            }
        }

        @Override
        public void finish(Gif gif) {
            if (this.gridState == STATE_REMOVE) {
                setValue(VALUE_VOID);
            }
            gifFinish(gif);
        }

        void logicSelf() {
            gifBg.logicSelf();
            switch (gridState) {
                case STATE_NEW:
                    flashIndex++;
                    if (flashIndex >= 256 / FADE_SPEED) {
                        paint.setAlpha(255);
                        gridState = STATE_NULL;
                        finishNewBeans();
                    } else {
                        paint.setAlpha(FADE_SPEED * flashIndex);
                    }
                    break;
                case STATE_REMOVE:
                    flashIndex--;
                    if (flashIndex <= 0) {
                        paint.setAlpha(0);
                        gridState = STATE_NULL;
                        setValue(VALUE_VOID);
                        finishRemoveBeans();
                    } else {
                        paint.setAlpha(FADE_SPEED * flashIndex);
                    }
                    break;
            }
        }

        boolean isVoid() {
            return value == VALUE_VOID;
        }
    }
}
