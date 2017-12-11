package pers.tpec.tpecview.widgets.particles;

import android.graphics.Color;
import android.graphics.Path;

public final class ParticleFactory {
    public static ColorParticles createFireEffects(final float lifeTime, final float x, final float y, final float v, final float size) {
        int[] color = new int[]{
                Color.argb(0, 255, 191, 0),
                Color.argb(255, 255, 191, 0),
                Color.argb(191, 255, 0, 0),
                Color.argb(0, 255, 0, 0)
        };
        float[] colorAtTime = new float[]{
                0, 0.2f, 0.8f, 1
        };
        return new ColorParticles(2, 0,
                (int) (lifeTime * 0.8f), (int) (lifeTime * 0.45f),
                x, y,
                v * 0.5f, v * 0.5f,
                0f, 0f,
                -60f, -60f,
                6f * size, 0.5f * size,
                color, colorAtTime, 8f * size);
    }

    public static ColorParticles createFireworkEffects(final float lifeTime, final float x, final float y, final float v, final float size, final int mainColor) {
        int[] color = new int[]{
                mainColor,
                Color.argb(127, 255, 255, 255),
                Color.argb(0, 255, 255, 255)
        };
        float[] colorAtTime = new float[]{
                0, 0.7f, 1
        };
        return new ColorParticles(7f, 3f,
                (int) (lifeTime * 0.8f), (int) (lifeTime * 0.45f),
                x, y,
                v * 0.5f, v * 0.5f,
                0, 0.1f,
                0, 360,
                6f * size, 0.5f * size,
                color, colorAtTime, 8f * size);
    }

    public static Path getOnePointPath(final float x, final float y) {
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x + 1, y);
        return path;
    }

    public static Path getPointCirclePath(final float x, final float y, final float r) {
        Path path = new Path();
        path.addCircle(x, y, r, Path.Direction.CW);
        return path;
    }

    public static int getColorBetween(final int color1, final int color2, final float f) {
        int colorA = (int) ((float) ((color1 >> 24) & 0xff) * (1 - f) + (float) ((color2 >> 24) & 0xff) * f);
        int colorR = (int) ((float) ((color1 >> 16) & 0xff) * (1 - f) + (float) ((color2 >> 16) & 0xff) * f);
        int colorG = (int) ((float) ((color1 >> 8) & 0xff) * (1 - f) + (float) ((color2 >> 8) & 0xff) * f);
        int colorB = (int) ((float) (color1 & 0xff) * (1 - f) + (float) (color2 & 0xff) * f);

        return (colorA << 24) | (colorR << 16) | (colorG << 8) | colorB;
    }
}
