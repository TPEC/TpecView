package pers.tpec.tpecview.widgets.particles;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.MotionEvent;

import pers.tpec.tpecview.SceneObject;

public abstract class Particles implements SceneObject {
    protected float countPerFrame, countPerFrameRange;
    protected int lifeTime, lifeTimeRange;
    protected PathMeasure startPathMeasure;
    protected float deltaStartPath;
    protected float velocity, velocityRange;
    protected float gravityX, gravityY;
    protected float angle, angleRange;
    protected float size, sizeRange;

    protected int timeLeft;
    protected boolean running;

    protected boolean timeoutSetNull;
    protected boolean isNull;

    public Particles(final float countPerFrame, final float countPerFrameRange,
                     final int lifeTime, final int lifeTimeRange,
                     final Path startPath, final float deltaStartPath,
                     final float velocity, final float velocityRange,
                     final float gravityX, final float gravityY,
                     final float angle, final float angleRange,
                     final float size, final float sizeRange) {
        this.countPerFrame = countPerFrame;
        this.countPerFrameRange = countPerFrameRange;
        this.lifeTime = lifeTime;
        this.lifeTimeRange = lifeTimeRange;
        setStartPath(startPath, deltaStartPath);
        this.velocity = velocity;
        this.velocityRange = velocityRange;
        this.gravityX = gravityX;
        this.gravityY = gravityY;
        this.angle = (float) (angle * Math.PI / 180f);
        this.angleRange = (float) (angleRange * Math.PI / 180f);
        this.size = size;
        this.sizeRange = sizeRange;
        timeLeft = 0;
        running = false;
        isNull = false;
    }

    protected void setStartPath(final Path path, final float deltaStartPath) {
        this.startPathMeasure = new PathMeasure(path, true);
        this.deltaStartPath = deltaStartPath;
        if (this.startPathMeasure.getLength() == 0) {
            this.deltaStartPath = 0;
        }
        while (this.deltaStartPath > 1) {
            this.deltaStartPath--;
        }
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    protected class ParticleUnit {
        public int lifeTime;
        public int timeLeft;
        public float x, y;
        public float vx, vy;
        public float size;

        public ParticleUnit(final int lifeTime,
                            final float x, final float y,
                            final float vx, final float vy,
                            final float size) {
            this.lifeTime = lifeTime;
            timeLeft = lifeTime;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.size = size;
        }

        public float getTimeSpent() {
            return lifeTime > 0f ? (float) (lifeTime - timeLeft) / (float) lifeTime : 0f;
        }
    }

    @Override
    public final boolean isNull() {
        return isNull;
    }
}
