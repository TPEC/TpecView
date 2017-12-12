package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.widgets.Label;

public class TestScene extends Scene {
    public TestScene(@NonNull TpecView tpecView) {
        super(tpecView);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        super.draw(canvas);
    }

    @Override
    public void load() {
        Label label;
        label = (Label) getSceneObject(addSceneObject(new Label(new Rect(200, 200, 600, 400))));
        label.setAlign(10)
                .setAlignStyle(Label.ALIGN_STYLE_LEFT, Label.ALIGN_STYLE_LEFT)
                .setFontSize(50)
                .setFontColor(Color.RED)
                .setText("ASDF\nFF")
                .setAntiAlias(true);
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
