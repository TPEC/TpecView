package pers.tpec.pet.scenes.objects;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.io.IOException;

import pers.tpec.pet.MainActivity;
import pers.tpec.pet.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.controller.TrueBorder;
import pers.tpec.tpecview.utils.rand.Rand;
import pers.tpec.tpecview.utils.rand.SimpleRand;

public class Unit implements SceneObject, ControllerClassifier.OnClickListener {
    private static final Rand rand = new SimpleRand();

    private static final int STATE_DO_NOTHING = 0;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_SLEEP = 2;

    private static final String[] GIF_PATH = new String[]{
            "pikachu_do_nothing.gif",
            "pikachu_running.gif",
            "pikachu_sleep.gif",
            "pikachu_playing_gba.gif",
            "pikachu_eat_berry.gif",
            "pikachu_dance.gif"
    };

    private static final int[] STATE_FRAME_INTERVAL = new int[]{
            3,
            2,
            4,
            4,
            2,
            2
    };

    private static final int[] STATE_CHANGE_ROUND_MIN = new int[]{
            25,
            20,
            50,
            25,
            20,
            1
    };

    private static final int[] STATE_CHANGE_ROUND_MAX = new int[]{
            100,
            50,
            75,
            50,
            40,
            1
    };

    private ControllerClassifier controllerClassifier;
    private MainScene rootScene;

    private int intervalMax;
    private int intervalIndex;
    private int gifRound;
    private int gifRoundMax;

    private int state;
    private float pf0, pf1;//params float

    private boolean reverse;

    private TpecGifDrawable gifDrawable;
    private TpecGifDrawable[] gifs;
    private float gifScale;

    private Bitmap bmpGif;
    private Matrix bmpMatrix;
    private Paint paint;

    public Unit(MainScene rootScene) {
        this.rootScene = rootScene;
        controllerClassifier = new ControllerClassifier();
        controllerClassifier.setBorder(new TrueBorder()).setOnClickListener(this);

        gifScale = 1.0f;
        paint = new Paint();
        bmpMatrix = new Matrix();

        gifs = new TpecGifDrawable[GIF_PATH.length];
        for (int i = 0; i < gifs.length; i++) {
            try {
                gifs[i] = new TpecGifDrawable(rootScene.getAssetManager(), GIF_PATH[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            setState(STATE_DO_NOTHING);
            gifRoundMax = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGifScale(float gifScale) {
        this.gifScale = gifScale;
        bmpMatrix = new Matrix();
        bmpMatrix.postScale(gifScale, gifScale);
        if (reverse) {
            bmpMatrix.postScale(-1, 1);
            bmpMatrix.postTranslate((float) gifDrawable.getWidth() * gifScale, 0);
        }
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (bmpGif != null) {
            canvas.drawBitmap(bmpGif, bmpMatrix, paint);
        }
    }

    private void setReverse(boolean reverse) {
        if (this.reverse != reverse) {
            bmpMatrix.postScale(-1, 1);
            bmpMatrix.postTranslate((float) gifDrawable.getWidth() * gifScale, 0);
        }
        this.reverse = reverse;
    }

    @Override
    public void logicSelf() {
        if (gifDrawable != null) {
            intervalIndex++;
            if (intervalIndex >= intervalMax) {
                intervalIndex = 0;
                if (gifDrawable.getCurrentFrameIndex() == 0) {
                    if (gifRoundMax > 0) {
                        gifRound++;
                        if (gifRound >= gifRoundMax) {
                            changeState();
                        }
                    }
                }
                bmpGif = gifDrawable.getNextFrame();
                rootScene.getLabel().setText(
                        String.valueOf(gifDrawable.getCurrentFrameIndex()) + "\n" +
                                String.valueOf(gifRoundMax - gifRound));
            }
        }
        switch (state) {
            case STATE_RUNNING:
                if (reverse) {
                    if (rootScene.getViewResizer().getX() <= 0) {
                        setReverse(!reverse);
                    } else {
                        offsetView((int) (-pf0 * gifScale), (int) pf1);
                    }
                } else {
                    if (rootScene.getViewResizer().getXRight() <= 0) {
                        setReverse(!reverse);
                    } else {
                        offsetView((int) (pf0 * gifScale), (int) pf1);
                    }
                }
                if (rootScene.getViewResizer().getY() <= 0 || rootScene.getViewResizer().getYBottom() <= 0) {
                    pf1 = -pf1;
                }
                break;
        }
    }

    private void changeState() {
        int stateNext;
        do {
            stateNext = rand.i(STATE_FRAME_INTERVAL.length);
        } while (stateNext == state);
        try {
            setState(stateNext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void offsetView(final int x, final int y) {
        ((Activity) rootScene.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootScene.getViewResizer().offset(x, y);
            }
        });
    }

    private void resizeView(final int width, final int height) {
        ((Activity) rootScene.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootScene.getViewResizer().resize(width, height);
            }
        });
    }

    private void setState(int state) throws IOException {
        this.state = state;
        gifDrawable = gifs[state];
        bmpGif = gifDrawable.getCurrentFrame();
        setGifScale(gifScale);
        intervalIndex = 0;
        gifRound = 0;
        gifRoundMax = rand.i(STATE_CHANGE_ROUND_MIN[state], STATE_CHANGE_ROUND_MAX[state]);
        setReverse(rand.bln());
        intervalMax = STATE_FRAME_INTERVAL[state];
        switch (state) {
            case STATE_RUNNING:
                pf0 = rand.f(3, 6);
                pf1 = rand.f(-3, 3);
                break;
        }
        resizeView(gifDrawable.getWidth(), gifDrawable.getHeight());
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return controllerClassifier.onTouch(event);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_INTERVAL = 1000;

    @Override
    public boolean click(int x, int y) {
        if (!rootScene.isViewMoving()) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_INTERVAL) {
                Intent intent = new Intent(rootScene.getContext(), MainActivity.class);
                rootScene.getContext().startActivity(intent);
            } else {
                changeState();
            }
            lastClickTime = clickTime;
            return true;
        }
        return false;
    }
}
