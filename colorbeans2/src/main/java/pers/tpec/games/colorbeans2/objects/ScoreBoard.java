package pers.tpec.games.colorbeans2.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.MathExtend;
import pers.tpec.tpecview.widgets.Label;

public class ScoreBoard implements SceneObject {
    private static final int VIEWED_SCORE_INTERVAL = 5;
    private static final float VIEWED_SCORE_SPEED = 0.05f;

    private MainScene mainScene;

    private Label label;

    private int score;

    private int viewedScore;

    private int viewedScoreIndex;

    public ScoreBoard(MainScene mainScene) {
        this.mainScene = mainScene;
        label = new Label(new Rect(0, 0, 100, 100));
        label.setAlign(10)
                .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
                .setFontSize(25)
                .setFontColor(Color.RED);
    }

    public void addScoreByRemove(final int count) {
        int add = count * count - 8 * count + 25;
        this.score += add;
    }

    public void clearScore() {
        this.score = 0;
        this.viewedScore = 0;
        label.setText(String.valueOf(viewedScore));
    }

    @Override
    public void drawSelf(Canvas canvas) {
        label.drawSelf(canvas);
    }

    @Override
    public void logicSelf() {
        if (viewedScore != score) {
            viewedScoreIndex--;
            if (viewedScoreIndex <= 0) {
                viewedScoreIndex = VIEWED_SCORE_INTERVAL;
                float addf = (float) score * VIEWED_SCORE_SPEED;
                addf += (float) viewedScore * (1f - VIEWED_SCORE_SPEED);
                int add = (int) addf - viewedScore;
                if (Math.abs(add) < 1) {
                    add = MathExtend.sgn(add);
                }
                viewedScore += add;
                label.setText(String.valueOf(viewedScore));
            }
        }
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