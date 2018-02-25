package pers.tpec.tpecview.utils.rand;

import java.util.Random;

public class SimpleRand extends Rand {
    private Random rand = new Random();

    @Override
    public int i(int min, int max) {
        if (max == min) {
            return min;
        }
        return Math.abs(rand.nextInt()) % (max - min) + min;
    }

    @Override
    public long l(long min, long max) {
        if (max == min) {
            return min;
        }
        return Math.abs(rand.nextLong()) % (max - min) + min;
    }

    @Override
    public float f() {
        return rand.nextFloat();
    }

    @Override
    public double d() {
        return rand.nextDouble();
    }

    @Override
    public boolean bln() {
        return rand.nextBoolean();
    }

    @Override
    public Rand setSeed(long seed) {
        rand.setSeed(seed);
        return this;
    }
}
