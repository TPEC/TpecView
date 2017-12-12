package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import pers.tpec.games.colorbeans2.GameScenes;
import pers.tpec.tpecview.SceneDialog;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.widgets.Button;
import pers.tpec.tpecview.widgets.Label;

public class GameOverScene extends SceneDialog {
    private int btnNewGame;
    private int lblScore;

    public GameOverScene(@NonNull TpecView tpecView) {
        super(tpecView);
        GameScenes.getInstance().setGameOverScene(this);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.translate(getBorder().left, getBorder().top);
        super.draw(canvas);
        canvas.translate(-getBorder().left, -getBorder().top);
    }

    public Label getLblScore() {
        return (Label) getSceneObject(lblScore);
    }

    @Override
    public SceneDialog show() {
        getLblScore().setText(String.valueOf(GameScenes.getInstance().getMainScene().getScoreBoard().getScore()));
        // TODO: 2017/12/12 scoreboard
        return super.show();
    }

    @Override
    public void load() {
        lblScore = addSceneObject(
                new Label(new Rect(0, 0, 100, 100))
                        .setAntiAlias(true)
        );
        btnNewGame = addSceneObject(
                new Button(new Rect(0, 0, 80, 80))
                        .setOnClickListener(new ControllerClassifier.OnClickListener() {
                            @Override
                            public boolean click(int x, int y) {
                                GameScenes.getInstance().getMainScene().getMap().newGame();
                                hide();
                                return true;
                            }
                        })
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
