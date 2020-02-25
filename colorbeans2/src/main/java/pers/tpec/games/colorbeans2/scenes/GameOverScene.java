package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;

import pers.tpec.game.colorbeans2.R;
import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.tpecview.ResManager;
import pers.tpec.tpecview.SceneDialog;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.widgets.Button;
import pers.tpec.tpecview.widgets.Label;

public class GameOverScene extends SceneDialog {
    private Bitmap bmpBg;
    private Paint paintBg;
    private Rect rectSrc;

    private int btnNewGame;
    private int lblScore;
    private int lblNo;

    public GameOverScene(@NonNull TpecView tpecView) {
        super(tpecView);
        GameScenes.getInstance().setGameOverScene(this);

        paintBg = new Paint();
        paintBg.setAlpha(191);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bmpBg, rectSrc, getBorderRect_offset(), paintBg);
        super.draw(canvas);
    }

    public Label getLblScore() {
        return (Label) getSceneObject(lblScore);
    }

    public Label getLblNo() {
        return (Label) getSceneObject(lblNo);
    }

    @Override
    public void show() {
        super.show();
        getLblScore().setText("得分：" + String.valueOf(GameScenes.getInstance().getMainScene().getScoreBoard().getScore()));
        int no = GameScenes.getInstance().getMainScene().getScoreBoard().getScoreNo();
        if (no >= 0) {
            getLblNo().setText("恭喜获得第" + String.valueOf(no + 1) + "名！");
        } else {
            getLblNo().setText("继续努力！");
        }
        // TODO: 2017/12/12 scoreboard
    }

    @Override
    public void load() {
        bmpBg = ResManager.getInstance().decodeResource(R.mipmap.bgl);
        rectSrc = new Rect(0, 0, bmpBg.getWidth(), bmpBg.getHeight());

        lblScore = addSceneObject(
                new Label(new Rect(0, 240, getBorderRect().width(), 360))
                        .setAntiAlias(true)
                        .setFontSize(50)
                        .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
        );
        lblNo = addSceneObject(
                new Label(new Rect(0, 360, getBorderRect().width(), 480))
                        .setAntiAlias(true)
                        .setFontSize(40)
                        .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
        );
        btnNewGame = addSceneObject(
                new Button(new Rect(0, 480, getBorderRect().width(), 720))
                        .setOnClickListener(new ControllerClassifier.OnClickListener() {
                            @Override
                            public boolean click(int x, int y) {
                                GameScenes.getInstance().getMainScene().getMap().newGame();
                                hide();
                                return true;
                            }
                        })
                        .setBmp(ResManager.getInstance().decodeResource(R.mipmap.beans2))
        );

        addSceneObject(
                new Label(((Button) getSceneObject(btnNewGame)).getRectDst())
                        .setAntiAlias(true)
                        .setFontSize(50)
                        .setFontColor(Color.WHITE)
                        .setAlignStyle(Label.ALIGN_STYLE_MID, Label.ALIGN_STYLE_MID)
                        .setText("再来一局")
        );
    }

    @Override
    public void unload() {
        clearSceneObject();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
