package pers.tpec.tpecview.widgets.words;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.ColorUtil;

public class FadedWords implements SceneObject {
    protected float vx, vy;
    protected int frame;
    protected int frameIndex;
    protected String text;
    protected boolean isNull;

    protected float x, y;
    protected Path path;

    protected Paint paint;
    protected int[] color;
    protected float[] colorAtTime;
    protected int colorIndex;

    public FadedWords(float x, float y, String text, float vx, float vy, int frame, int[] color, float[] colorAtTime, float fontSize) {
        this.vx = vx;
        this.vy = vy;
        this.frame = frame;
        this.text = text;
        this.isNull = false;
        this.x = x;
        this.y = y;
        this.path = null;
        this.color = color;
        this.colorAtTime = colorAtTime;
        init();
        paint.setTextSize(fontSize);
    }

    private void init() {
        frameIndex = 0;
        colorIndex = 0;
        paint = new Paint();
        paint.setColor(color[0]);
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (path == null) {
            canvas.drawText(text, x, y, paint);
        } else {
            canvas.drawTextOnPath(text, path, x, y, paint);
        }
    }

    @Override
    public void logicSelf() {
        if (!isNull) {
            frameIndex++;
            if (frameIndex >= frame) {
                isNull = true;
                return;
            }
            x += vx;
            y += vy;
            float tp = (float) frameIndex / (float) frame;
            if (tp < colorAtTime[colorIndex]) {
                paint.setColor(ColorUtil.getColorBetween(
                        color[colorIndex - 1],
                        color[colorIndex],
                        (tp - colorAtTime[colorIndex - 1]) / (colorAtTime[colorIndex] - colorAtTime[colorIndex - 1])
                ));
            } else {
                paint.setColor((tp == colorAtTime[colorIndex]) ?
                        color[colorIndex] :
                        ColorUtil.getColorBetween(
                                color[colorIndex],
                                color[colorIndex + 1],
                                (tp - colorAtTime[colorIndex]) / (colorAtTime[colorIndex + 1] - colorAtTime[colorIndex])
                        )
                );
                colorIndex++;
            }
        }
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public boolean isNull() {
        return isNull;
    }
}
