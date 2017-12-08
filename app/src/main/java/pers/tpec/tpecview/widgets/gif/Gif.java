package pers.tpec.tpecview.widgets.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import pers.tpec.tpecview.SceneObject;

public abstract class Gif implements SceneObject {
    public static final int MODE_STATIC = 0;
    public static final int MODE_ONEWAY = 1;
    public static final int MODE_ONEWAY_ROUND = 2;
    public static final int MODE_RETURN = 3;

    private Bitmap bmpRes;
    private Rect rectSrc;
    private RectF rectDst;
    private Paint paint;

    private int mode;
    private int frameCount;
    private int frameIndex;
    private boolean reverse;
    private int interval;
    private int intervalCount;

    private boolean ready;
    private boolean play;
    private boolean visible;

    private OnFinishListener onFinishListener;

    public Gif() {
        paint = new Paint();
        rectSrc = new Rect(0, 0, 1, 1);
        reverse = false;
        visible = true;
    }

    public Gif setRectDst(final RectF rectDst) {
        this.rectDst = rectDst;
        checkReady();
        return this;
    }

    public Gif setMode(final int mode, final boolean reverse, final int interval) {
        this.mode = mode;
        this.reverse = reverse;
        this.interval = interval;
        intervalCount = interval;
        checkReady();
        return this;
    }

    public Gif setRes(final Bitmap bmpRes, final int frameCount) {
        this.bmpRes = bmpRes;
        this.frameCount = frameCount;
        this.frameIndex = 0;
        rectSrc.left = 0;
        rectSrc.top = 0;
        rectSrc.right = bmpRes.getWidth() / frameCount;
        rectSrc.bottom = bmpRes.getHeight();
        checkReady();
        return this;
    }

    public Gif setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
        return this;
    }

    public Gif setPaint(final Paint paint) {
        this.paint = paint;
        checkReady();
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public Gif setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public boolean isPlay() {
        return play;
    }

    public void play() {
        play = true;
    }

    public void play(int frameIndex) {
        this.frameIndex = frameIndex;
        play();
    }

    public void pause() {
        play = false;
    }

    private void checkReady() {
        ready = bmpRes != null && rectSrc != null && rectDst != null & paint != null;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (ready && visible) {
            canvas.drawBitmap(bmpRes, rectSrc, rectDst, paint);
        }
    }

    @Override
    public void logicSelf() {
        if (play) {
            intervalCount--;
            if (intervalCount <= 0) {
                intervalCount = interval;
                switch (mode) {
                    case MODE_ONEWAY:
                        if (!reverse) {
                            frameIndex++;
                            if (frameIndex >= frameCount) {
                                frameIndex = frameCount - 1;
                                play = false;
                                if (onFinishListener != null) {
                                    onFinishListener.finish(this);
                                }
                            }
                        } else {
                            frameIndex--;
                            if (frameIndex < 0) {
                                frameIndex = 0;
                                play = false;
                                if (onFinishListener != null) {
                                    onFinishListener.finish(this);
                                }
                            }
                        }
                        break;
                    case MODE_ONEWAY_ROUND:
                        if (!reverse) {
                            frameIndex++;
                            if (frameIndex >= frameCount) {
                                frameIndex = 0;
                            }
                        } else {
                            frameIndex--;
                            if (frameIndex < 0) {
                                frameIndex = frameCount - 1;
                            }
                        }
                        break;
                    case MODE_RETURN:
                        if (!reverse) {
                            frameIndex++;
                            if (frameIndex >= frameCount - 1) {
                                reverse = true;
                            }
                        } else {
                            frameIndex--;
                            if (frameIndex <= 0) {
                                reverse = false;
                            }
                        }
                        break;
                    default:
                        break;
                }
                // TODO: 2017/12/1 rectSrc.offset
            }
        }
    }

    public interface OnFinishListener {
        void finish(Gif gif);
    }
}
