package com.ecs160.nittacraft;

import java.util.Random;

public class CRandomNumberGenerator {
    protected Random DRandomGenerator;
    protected int DRandomSeedHigh;

    public CRandomNumberGenerator() {
        DRandomGenerator = new Random();
        DRandomSeedHigh = 0x01234567;
    }

    public void Seed(int seed) {
        DRandomGenerator.setSeed(seed);
    }

    /* FIXME This function may be unneeded
    public void Seed(int high, int low) {
        if ((high != low) && low > 0 && high > 0) {
            DRandomSeedHigh = high;
            DRandomSeedLow = low;
        }
    }
    */

    public int Random() {
        return DRandomGenerator.nextInt(DRandomSeedHigh);
    }
}
