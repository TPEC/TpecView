package pers.tpec.tpecview.utils.rand;

public abstract class Rand {
    public int i(int max) {
        return i(0, max);
    }

    public abstract int i(int min, int max);

    public long l(long max) {
        return l(0, max);
    }

    public abstract long l(long min, long max);

    public abstract float f();

    public float f(float max) {
        return f() * max;
    }

    public float f(float min, float max) {
        return f() * (max - min) + min;
    }

    public abstract double d();

    public double d(double max) {
        return d() * max;
    }

    public double d(double min, double max) {
        return d() * (max - min) + min;
    }

    public abstract boolean bln();

    public abstract Rand setSeed(long seed);

}
