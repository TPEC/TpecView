package pers.tpec.tpecview.controller;

import android.view.MotionEvent;

import java.util.HashSet;
import java.util.Set;

public class ControllerClassifier implements Controller {
    private Border border;
    private OnClickListener onClickListener;
    private OnDragListener onDragListener;

    private Set<Integer> pointID;
    protected boolean _click, _scale;
    private float x0, y0, x1, y1, xd, yd;

    private boolean enabled;

    public ControllerClassifier() {
        enabled = true;
        pointID=new HashSet<>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ControllerClassifier setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ControllerClassifier setOnClickListener(final OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public ControllerClassifier setOnDragListener(final OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
        return this;
    }

    public ControllerClassifier setBorder(Border border) {
        this.border = border;
        return this;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        if (enabled) {
            int action = event.getActionMasked();
            int index = event.getActionIndex();
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                if (border != null && border.inside((int) event.getX(index), (int) event.getY(index))) {
                    pointID.add(event.getPointerId(index));
                    _click = pointID.size() == 1;
                    _scale = pointID.size() == 2;
                    if (_click) {
                        x0 = event.getX(index);
                        y0 = event.getY(index);
                        xd = x0;
                        yd = y0;
                    } else if (_scale) {
                        x1 = event.getX(index);
                        y1 = event.getY(index);
                    }
                    return true;
                }
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
                if (pointID.contains(event.getPointerId(index))) {
                    pointID.remove(event.getPointerId(index));
                    if (_click && border != null && border.inside((int) event.getX(index), (int) event.getY(index))) {
                        _click = false;
                        double dd = Math.sqrt(Math.pow(event.getX(index) - x0, 2) + Math.pow(event.getY(index) - y0, 2));
                        if (dd < 32) {
                            if (onClickListener != null) {
                                return onClickListener.click((int) x0, (int) y0);
                            }
                        }
                    }
                    _scale = false;
                }
            } else {
                if (_click) {//drag
                    if (onDragListener != null) {
                        boolean f = onDragListener.drag((int)event.getX(index),(int) event.getY(index),
                                (int)x0,(int)y0,
                                (int)xd,(int)yd);
                        xd = event.getX(index);
                        yd = event.getY(index);
                        return f;
                    }
                } else if (_scale) {//scale

                }
            }
            return false;
        }
        return false;
    }

    public interface OnClickListener {
        boolean click(int x, int y);
    }

    public interface OnDragListener {
        /**
         * @param x0 start x
         * @param y0 start y
         * @param lx last x
         * @param ly last y
         */
        boolean drag(int x, int y, int x0, int y0, int lx, int ly);
    }
}
