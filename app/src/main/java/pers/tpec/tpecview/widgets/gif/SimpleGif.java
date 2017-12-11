package pers.tpec.tpecview.widgets.gif;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class SimpleGif extends Gif {
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
