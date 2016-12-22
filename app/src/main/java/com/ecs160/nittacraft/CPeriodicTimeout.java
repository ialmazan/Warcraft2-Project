package com.ecs160.nittacraft;

public class CPeriodicTimeout {
    protected int DTimeoutInterval;
    protected long DNextExpectedTimeout;

    /**
     * Functions similar to the POSIX gettimeofday() function.
     * Sets the DNextExpectedTimeout data member to the current time in ms.
     */
    private long gettimeofday() {
        return System.currentTimeMillis();
    }

    public CPeriodicTimeout(int periodms) {
        DNextExpectedTimeout = gettimeofday();
        if (0 >= periodms) {
            DTimeoutInterval = 1000;
        }
        else {
            DTimeoutInterval = periodms;
        }
    }

    public int MiliSecondPeriod() {
        return DTimeoutInterval;
    }

    public int Frequency() {
        return 1000 / DTimeoutInterval;
    }

    public long MiliSecondsUntilDeadline() {
        long CurrentTime;
        long TimeDelta;

        CurrentTime = gettimeofday();
        TimeDelta = DNextExpectedTimeout - CurrentTime;
        while (0 >= TimeDelta) {
            DNextExpectedTimeout += DTimeoutInterval;
            TimeDelta = DNextExpectedTimeout - CurrentTime;
        }
        return TimeDelta;
    }
}
