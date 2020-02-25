package pers.tpec.tpecview.utils;

public class ColorUtil {
    public static int getColorBetween(final int color1, final int color2, final float f) {
        int colorA = (int) ((float) ((color1 >> 24) & 0xff) * (1 - f) + (float) ((color2 >> 24) & 0xff) * f);
        int colorR = (int) ((float) ((color1 >> 16) & 0xff) * (1 - f) + (float) ((color2 >> 16) & 0xff) * f);
        int colorG = (int) ((float) ((color1 >> 8) & 0xff) * (1 - f) + (float) ((color2 >> 8) & 0xff) * f);
        int colorB = (int) ((float) (color1 & 0xff) * (1 - f) + (float) (color2 & 0xff) * f);

        return (colorA << 24) | (colorR << 16) | (colorG << 8) | colorB;
    }
}
