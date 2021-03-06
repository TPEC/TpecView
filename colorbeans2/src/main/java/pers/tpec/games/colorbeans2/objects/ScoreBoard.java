package pers.tpec.games.colorbeans2.objects;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.tpecview.SceneObject;
import pers.tpec.tpecview.utils.SharedPreferencesUtil;
import pers.tpec.tpecview.widgets.Label;

public class ScoreBoard implements SceneObject {
    private static final int SCORE_BOARD_TOP = 1050;

    private static final int VIEWED_SCORE_INTERVAL = 1;
    private static final float VIEWED_SCORE_SPEED = 0.2f;
    private static final float VIEWED_SCORE_SPEED_MIN = 0.1f;

    private MainScene mainScene;

    private Label lblScoreLabel;
    private Label lblScore;
    private Label lblAverageScore;

    private List<Score> highestScore = new ArrayList<>();

    private int score;
    private float viewedScore;

    private long gameCount;
    private double averageScore;
    private int averageScoreX;

    private int viewedScoreIndex;

    private Rect progressBar;
    private Paint paintPB;
    private Paint paintLine;
    private Paint paintLineAverage;
    private List<Integer> highestScoreX = new ArrayList<>();
    private List<Label> lblHighestScore = new ArrayList<>();

    public ScoreBoard() {
        this.mainScene = GameScenes.getInstance().getMainScene();
        lblScoreLabel = new Label(new Rect(0, SCORE_BOARD_TOP, 720, SCORE_BOARD_TOP + 120));
        lblScoreLabel.setAlign(36, 0, 36, 0)
                .setBackground(Color.argb(127, 255, 255, 255))
                .setAlignStyle(Label.ALIGN_STYLE_LEFT, Label.ALIGN_STYLE_LEFT)
                .setFontSize(36)
                .setAntiAlias(true)
                .setFontStyle(null, Label.FONT_STYLE_BOLD)
                .setFontColor(Color.BLACK)
                .setText("得分：");
        lblScore = new Label(new Rect(0, SCORE_BOARD_TOP + 50, 720, SCORE_BOARD_TOP + 120));
        lblScore.setAlign(24)
                .setAlignStyle(Label.ALIGN_STYLE_LEFT, Label.ALIGN_STYLE_MID)
                .setFontSize(48)
                .setAntiAlias(true)
                .setFontStyle(null, Label.FONT_STYLE_BOLD)
                .setFontColor(Color.BLACK);
        lblAverageScore = new Label(new Rect(360, SCORE_BOARD_TOP, 720, SCORE_BOARD_TOP + 50));
        lblAverageScore.setAlign(8, 0, 8, 0)
                .setAlignStyle(Label.ALIGN_STYLE_RIGHT, Label.ALIGN_STYLE_MID)
                .setFontColor(Color.rgb(159, 0, 0))
                .setFontSize(30)
                .setAntiAlias(true);
        progressBar = new Rect(0, SCORE_BOARD_TOP + 50, 0, SCORE_BOARD_TOP + 120);
        paintPB = new Paint();
        paintPB.setColor(Color.argb(127, 0, 0, 255));
        paintLine = new Paint();
        paintLine.setStrokeWidth(5);
        paintLine.setColor(Color.argb(127, 255, 255, 255));
        paintLineAverage = new Paint();
        paintLineAverage.setStrokeWidth(5);
        paintLineAverage.setColor(Color.argb(127, 255, 0, 0));
        clearScore();
    }

    public int addScoreByRemove(final int count, final int combo) {
        int add = (count * count - 7 * count + 20) * combo;
        this.score += add;
        return add;
    }

    public void clearAllSavedData() {
        SharedPreferencesUtil.clear(mainScene.getContext());
    }

    public void updateHighestScore() {
        highestScore.clear();
        SharedPreferences sp = SharedPreferencesUtil.getSP(mainScene.getContext());
        for (int i = 0; i < 5; i++) {
            highestScore.add(new Score(
                    sp.getInt("HighestScore_" + String.valueOf(i), (5 - i) * 100),
                    sp.getString("HighestScoreDate_" + String.valueOf(i), "")
            ));
        }
        averageScore = sp.getFloat("AverageScore", (float) 0.0);
        gameCount = sp.getLong("GameCount", 0);
        Collections.sort(highestScore, comparator);
        highestScoreX.clear();
        lblHighestScore.clear();
        int x = 720;
        for (Score s : highestScore) {
            x -= 144;
            lblHighestScore.add(
                    new Label(new Rect(x, SCORE_BOARD_TOP + 150, x + 144, SCORE_BOARD_TOP + 200))
                            .setAlign(8)
                            .setAntiAlias(true)
                            .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
                            .setText(String.valueOf(s.value))
                            .setFontSize(24)
            );
        }
        lblAverageScore.setText("平均：" + String.valueOf((int) averageScore).trim());
        viewedScoreChanged();
    }

    private Comparator<Score> comparator = new Comparator<Score>() {
        @Override
        public int compare(Score o1, Score o2) {
            return Integer.compare(o2.value, o1.value);
        }
    };

    public void setScore(final int score) {
        this.score = score;
        viewedScoreChanged();
    }

    private boolean checkNewHighestScore() {
        for (Score s : highestScore) {
            if (score > s.value) {
                highestScore.remove(highestScore.size() - 1);
                highestScore.add(new Score(
                        score,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date())
                ));
                Collections.sort(highestScore, comparator);
                return true;
            }
        }
        return false;
    }

    /**
     * Called when game over.
     *
     * @return no. (-1: not on highest score)
     */
    public int getScoreNo() {
        if (gameCount < 150) {
            averageScore = averageScore * ((double) gameCount / (double) (gameCount + 1)) + (double) score / (double) (gameCount + 1);
        } else {
            averageScore = averageScore * (149.0 / 150.0) + (double) score / 150.0;
        }
        gameCount++;
        saveAverageScore();
        if (checkNewHighestScore()) {
            saveHighestScore();
            for (int i = highestScore.size() - 1; i >= 0; i--) {
                if (highestScore.get(i).value == score) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void saveHighestScore() {
        java.util.Map<String, Object> m = new HashMap<>();
        for (int i = 0; i < highestScore.size(); i++) {
            m.put("HighestScore_" + String.valueOf(i), highestScore.get(i).value);
            m.put("HighestScoreDate_" + String.valueOf(i), highestScore.get(i).date);
        }
        SharedPreferencesUtil.save(mainScene.getContext(), m);
    }

    private void saveAverageScore() {
        java.util.Map<String, Object> m = new HashMap<>();
        m.put("AverageScore", (float) averageScore);
        m.put("GameCount", gameCount);
        SharedPreferencesUtil.save(mainScene.getContext(), m);
    }

    public int getScore() {
        return score;
    }

    public void clearScore() {
        this.score = 0;
        this.viewedScore = 0f;
        updateHighestScore();
    }

    @Override
    public void drawSelf(Canvas canvas) {
        lblScoreLabel.drawSelf(canvas);
        canvas.drawRect(progressBar, paintPB);
        for (int i = 0; i < highestScoreX.size(); i++) {
            int x = highestScoreX.get(i);
            canvas.drawLine(x, SCORE_BOARD_TOP + 50, x, SCORE_BOARD_TOP + 120, paintLine);
            canvas.drawLine(x, SCORE_BOARD_TOP + 120, (4 - i) * 144 + 72, SCORE_BOARD_TOP + 150, paintLine);
        }
        canvas.drawLine(averageScoreX, SCORE_BOARD_TOP + 50, averageScoreX, SCORE_BOARD_TOP + 120, paintLineAverage);
        for (Label label : lblHighestScore) {
            label.drawSelf(canvas);
        }
        lblAverageScore.drawSelf(canvas);
        lblScore.drawSelf(canvas);
    }

    @Override
    public void logicSelf() {
        if (viewedScore != score) {
            viewedScoreIndex--;
            if (viewedScoreIndex <= 0) {
                viewedScoreIndex = VIEWED_SCORE_INTERVAL;
                if (Math.abs(viewedScore - (float) score) <= VIEWED_SCORE_SPEED_MIN) {
                    viewedScore = score;
                } else {
                    float addf = (float) score * VIEWED_SCORE_SPEED;
                    addf += viewedScore * (1f - VIEWED_SCORE_SPEED);
                    float add = addf - viewedScore;
                    if (viewedScore < score && add < VIEWED_SCORE_SPEED_MIN) {
                        add = VIEWED_SCORE_SPEED_MIN;
                    } else if (viewedScore > score && add > -VIEWED_SCORE_SPEED_MIN) {
                        add = -VIEWED_SCORE_SPEED_MIN;
                    }
                    viewedScore += add;
                }
                viewedScoreChanged();
            }
        }
    }

    private void viewedScoreChanged() {
        lblScore.setText(String.valueOf((int) viewedScore));
        if (viewedScore <= highestScore.get(0).value) {
            progressBar.right = (int) (viewedScore / (float) highestScore.get(0).value * 720f);
            if (viewedScore <= highestScore.get(0).value / 2) {
                lblScore.setAlignStyle(Label.ALIGN_STYLE_LEFT, Label.ALIGN_STYLE_MID)
                        .offsetTo(progressBar.right, SCORE_BOARD_TOP + 50)
                        .setFontColor(Color.BLACK);
            } else {
                lblScore.setAlignStyle(Label.ALIGN_STYLE_RIGHT, Label.ALIGN_STYLE_MID)
                        .offsetTo(progressBar.right - 720, SCORE_BOARD_TOP + 50)
                        .setFontColor(Color.WHITE);
            }
            if (highestScoreX.isEmpty()) {
                for (Score s : highestScore) {
                    highestScoreX.add((int) ((float) s.value / (float) highestScore.get(0).value * 720f));
                }
                averageScoreX = (int) ((float) averageScore / (float) highestScore.get(0).value * 720f);
            }
        } else {
            progressBar.right = 720;
            lblScore.setAlignStyle(Label.ALIGN_STYLE_RIGHT, Label.ALIGN_STYLE_MID)
                    .offsetTo(0, SCORE_BOARD_TOP + 50)
                    .setFontColor(Color.WHITE);
            highestScoreX.clear();
            for (Score s : highestScore) {
                highestScoreX.add((int) ((float) s.value / viewedScore * 720f));
            }
            averageScoreX = (int) ((float) averageScore / viewedScore * 720f);
        }
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    class Score {
        int value;
        String date;

        public Score() {
            this(-1, "");
        }

        public Score(int value, String date) {
            this.value = value;
            this.date = date;
        }
    }
}
