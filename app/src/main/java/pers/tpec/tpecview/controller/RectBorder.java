package pers.tpec.tpecview.controller;

import android.graphics.Rect;

public class RectBorder implements Border {
    private Rect rect;

    public RectBorder(Rect rect) {
        this.rect = rect;
    }

    public RectBorder(int left, int top, int width, int height) {
        this.rect = new Rect(left, top, left + width, top + height);
    }

    public Rect getRect() {
        return rect;
    }

    public RectBorder setRect(Rect rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public boolean inside(int x, int y) {
        return rect != null && rect.contains(x, y);
    }
}
