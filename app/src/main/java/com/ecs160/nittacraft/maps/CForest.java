package com.ecs160.nittacraft.maps;

import com.ecs160.nittacraft.CPosition;

public class CForest {
    protected CPosition DCurrentPos;
    protected int DCurrentTime;
    protected int DGrowthPeriod;
    protected int DCurrentType;

    public CForest(CPosition DCurrentPosition, int DCurrentTimeStep, int DGrowthTimePeriod, int
            DType) {
        DCurrentPos = DCurrentPosition;
        DCurrentTime = DCurrentTimeStep;
        DGrowthPeriod = DGrowthTimePeriod;
        DCurrentType = DType;
    }

    public CPosition GetCurrentPos()
    {
        return DCurrentPos;
    }
    public int GetForestTime()
    {
        return DCurrentTime;
    }
    public void IncrementTime()
    {
        DCurrentTime++;
    }
    public void SetCurrentTime(int DTime)
    {
        DCurrentTime = DTime;
    }

    public int GetGrowthPeriod()
    {
        return DGrowthPeriod;
    }
    public int GetCurrentType()
    {
        return DCurrentType;
    }

    public void IncrementType()
    {
        DCurrentType++;
    }
    public void SetCurrentType(int DType) {
        DCurrentType = DType;
    }

    public void UpdateGrowthPeriod(int DNewGrowthPeriod)
    {
        DGrowthPeriod = DNewGrowthPeriod;
    }
}
