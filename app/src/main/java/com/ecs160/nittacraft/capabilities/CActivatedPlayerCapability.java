package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;

public abstract class CActivatedPlayerCapability{

    protected CPlayerAsset DActor;
    protected CPlayerData DPlayerData;
    protected CPlayerAsset DTarget;

    public CActivatedPlayerCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
            target) {
        DActor = actor;
        DPlayerData = playerData;
        DTarget = target;
    }

    abstract public int PercentComplete(int max);
    abstract public boolean IncrementStep();
    abstract public void Cancel();
}