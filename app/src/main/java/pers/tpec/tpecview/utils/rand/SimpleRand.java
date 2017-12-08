package pers.tpec.tpecview.utils.rand;

import java.util.Random;

public class SimpleRand extends Rand {
    private Random rand = new Random();

    @Override
    public int i(int min, int max) {
        return Math.abs(rand.nextInt()) % (max - min) + min;
    }

    @Override
    public long l(long min, long max) {
        return Math.abs(rand.nextLong()) % (max - min) + min;
    }

    @Override
    public float f(float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }

    @Override
    public double d(double min, double max) {
        return rand.nextDouble() * (max - min) + min;
    }

    @Override
    public void setSeed(long seed) {
        rand.setSeed(seed);
    }
}
