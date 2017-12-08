package pers.tpec.tpecview.widgets.gif;

import android.view.MotionEvent;

public class SimpleGif extends Gif {
    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
