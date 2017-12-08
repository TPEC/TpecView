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

    public float f() {
        return f(0f, 1f);
    }

    public float f(float max) {
        return f(0f, max);
    }

    public abstract float f(float min, float max);

    public double d() {
        return d(0d, 1d);
    }

    public double d(double max) {
        return d(0d, max);
    }

    public abstract double d(double min, double max);

    public abstract void setSeed(long seed);

}
