package pers.tpec.games.colorbeans2.scenes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import pers.tpec.tpecview.Scene;
import pers.tpec.tpecview.TpecView;
import pers.tpec.tpecview.controller.Border;
import pers.tpec.tpecview.controller.ControllerClassifier;
import pers.tpec.tpecview.widgets.particles.ParticleFactory;

public class MenuScene extends Scene implements ControllerClassifier.OnClickListener {
    private Paint paint;
    private ControllerClassifier controllerClassifier;

    private EditText et;

    public MenuScene(@NonNull TpecView tpecView) {
        super(tpecView);

        paint = new Paint();
        controllerClassifier = new ControllerClassifier().setBorder(new Border() {
            @Override
            public boolean inside(int x, int y) {
                return true;
            }
        }).setOnClickListener(this);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        super.draw(canvas);
        et.draw(canvas);
    }

    @Override
    public void logic() {
        super.logic();
        try {
            switchScene(MainScene.class);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        et = new EditText(tpecView.getContext());
        et.setSingleLine(true);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20,20,20,30);
        et.setLayoutParams(lp);
        et.setLeft(200);
        et.setTop(200);
        et.setRight(600);
        et.setBottom(400);
        et.setText("asdf");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return et.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        if (et.onTouchEvent(event)) {
            return true;
        }
        if (super.onTouch(event)) {
            return true;
        }
        return controllerClassifier.onTouch(event);
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

    @Override
    public boolean click(int x, int y) {
        addSceneObject(ParticleFactory.createFireworkEffects(64f, x, y, 4f, 2.5f, Color.GREEN).playSetNull(2));
        return true;
    }
}
