package pers.tpec.tpecview.widgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import pers.tpec.tpecview.SceneObject;

public class Label implements SceneObject {
    public static final int ALIGN_STYLE_LEFT = 0;
    public static final int ALIGN_STYLE_MID = 1;
    public static final int ALIGN_STYLE_RIGHT = 2;

    public static final int FONT_STYLE_UNCHANGED = -1;
    public static final int FONT_STYLE_NORMAL = 0;
    public static final int FONT_STYLE_BOLD = 1;
    public static final int FONT_STYLE_ITALIC = 2;
    public static final int FONT_STYLE_BOLD_ITALIC = 3;


    private Rect rectDst;
    private Paint paint;

    private String text = "";
    private final List<String> textInLines;

    private int alignStyleHorizon = 0;
    private int alignStyleVertical = 0;
    private int fontStyle = 0;

    private float multiSpacing = 1f;
    private float spacing;  //行距

    private int alignLeft, alignTop, alignRight, alignBottom;

    private boolean visible;

    public Label(@NonNull Rect rectDst) {
        paint = new Paint();
        this.rectDst = rectDst;
        textInLines = new ArrayList<>();
        setAlign(0);
        visible = true;
    }

    public Label setVisible(final boolean visible) {
        this.visible = visible;
        return this;
    }

    public Label setMultiSpacing(final float multiSpacing) {
        this.multiSpacing = multiSpacing;
        return this;
    }

    public Label setText(final String text) {
        this.text = text;
        getTextInLines();
        return this;
    }

    public Label setAlign(final int alignLeft, final int alignTop, final int alignRight, final int alignBottom) {
        this.alignLeft = alignLeft;
        this.alignTop = alignTop;
        this.alignRight = alignRight;
        this.alignBottom = alignBottom;
        getTextInLines();
        return this;
    }

    public Label setAlign(final int align) {
        return setAlign(align, align, align, align);
    }

    public Label setAlignStyle(final int alignStyleHorizon, final int alignStyleVertical) {
        if (alignStyleHorizon != FONT_STYLE_UNCHANGED) {
            this.alignStyleHorizon = alignStyleHorizon;
            switch (alignStyleHorizon) {
                case ALIGN_STYLE_LEFT:
                    paint.setTextAlign(Paint.Align.LEFT);
                    break;
                case ALIGN_STYLE_MID:
                    paint.setTextAlign(Paint.Align.CENTER);
                    break;
                case ALIGN_STYLE_RIGHT:
                    paint.setTextAlign(Paint.Align.RIGHT);
                    break;
            }
        }
        if (alignStyleVertical != FONT_STYLE_UNCHANGED) {
            this.alignStyleVertical = alignStyleVertical;
        }
        return this;
    }

    public Label setFontStyle(Typeface font, final int fontStyle) {
        this.fontStyle = fontStyle;
        if (font == null) {
            font = Typeface.DEFAULT;
        }
        Typeface typeface;
        switch (fontStyle) {
            case FONT_STYLE_NORMAL:
                typeface = Typeface.create(font, Typeface.NORMAL);
                break;
            case FONT_STYLE_BOLD:
                typeface = Typeface.create(font, Typeface.BOLD);
                break;
            case FONT_STYLE_ITALIC:
                typeface = Typeface.create(font, Typeface.ITALIC);
                break;
            case FONT_STYLE_BOLD_ITALIC:
                typeface = Typeface.create(font, Typeface.BOLD_ITALIC);
                break;
            default:
                typeface = font;
                break;
        }
        paint.setTypeface(typeface);
        getTextInLines();
        return this;
    }

    public Label setFontSize(final float fontSize) {
        paint.setTextSize(fontSize);
        getTextInLines();
        return this;
    }

    public Label setFontColor(final int color) {
        paint.setColor(color);
        return this;
    }

    public Label setRectDst(@NonNull final Rect rectDst) {
        this.rectDst = rectDst;
        getTextInLines();
        return this;
    }

    public Label setAntiAlias(final boolean antiAlias) {
        paint.setAntiAlias(antiAlias);
        return this;
    }

    private void getTextInLines() {
        synchronized (textInLines) {
            textInLines.clear();
            if (text != null && !text.isEmpty() && rectDst != null) {
                StringBuilder til = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == '\n') {
                        textInLines.add(til.toString());
                        til = new StringBuilder();
                    } else {
                        if (paint.measureText(til.toString() + text.charAt(i)) > rectDst.width() - alignLeft - alignRight && (til.length() > 0)) {
                            textInLines.add(til.toString());
                            til = new StringBuilder();
                        }
                        til.append(text.charAt(i));
                    }
                }
                if (til.length() > 0) {
                    textInLines.add(til.toString());
                } else {
                    textInLines.add("");
                }
            }
        }
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (visible) {
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            canvas.drawRect(rectDst, p);
            int x, y;
            Paint.FontMetrics fm = paint.getFontMetrics();
            spacing = multiSpacing * (fm.bottom - fm.top);
            synchronized (textInLines) {
                if (alignStyleVertical == ALIGN_STYLE_LEFT) {
                    y = (int) (rectDst.top + alignTop - fm.ascent);
                } else if (alignStyleVertical == ALIGN_STYLE_RIGHT) {
                    y = (int) (rectDst.bottom - alignBottom - textInLines.size() * spacing - fm.ascent);
                } else {
                    y = (int) (rectDst.centerY() + (alignTop - alignBottom - (textInLines.size() - 1) * spacing - fm.top - fm.bottom) / 2);
                }
                for (String s : textInLines) {
                    if (alignStyleHorizon == ALIGN_STYLE_LEFT) {
                        x = rectDst.left + alignLeft;
                    } else if (alignStyleHorizon == ALIGN_STYLE_RIGHT) {
                        x = rectDst.right - alignRight;
                    } else {
                        x = rectDst.centerX() + (alignLeft - alignRight) / 2;
                    }
                    canvas.drawText(s, x, y, paint);
                    y += spacing;
                }
            }
        }
    }

    @Override
    public void logicSelf() {

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
