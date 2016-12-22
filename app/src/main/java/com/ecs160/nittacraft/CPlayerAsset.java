package com.ecs160.nittacraft;

import android.util.Log;

import com.ecs160.nittacraft.CGameDataTypes.EAssetAction;
import com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType;
import com.ecs160.nittacraft.CGameDataTypes.EAssetType;
import com.ecs160.nittacraft.CGameDataTypes.EDirection;
import com.ecs160.nittacraft.CGameDataTypes.EPlayerColor;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dSouth;

//typedef struct{
//        EAssetAction DAction;
//        EAssetCapabilityType DCapability;
//        std::shared_ptr< CPlayerAsset > DAssetTarget;
//        std::shared_ptr< CActivatedPlayerCapability > DActivatedCapability;
//        } SAssetCommand, *SAssetCommandRef;

public class CPlayerAsset {

    private int DId; // give each asset an unique
    private int DAge;
    private int DCreationCycle;
    private int DHitPoints;
    private int DGold;
    private int DLumber;
    private int DStep;
    private int DMoveRemainderX;
    private int DMoveRemainderY;
    private CPosition DTilePosition;
    private CPosition DPosition;
    private EDirection DDirection;
    private ArrayList<SAssetCommand> DCommands = new ArrayList<>();
    protected CPlayerAssetType DType;
    protected static int DUpdateFrequency = 1;
    protected static int DUpdateDivisor = 32;

    public CPlayerAsset() { }

    public CPlayerAsset(CPlayerAsset type) {
        DTilePosition = new CPosition(0, 0);
        DPosition = new CPosition(0, 0);

        DCreationCycle = 0;
        DType = type.DType;
        DHitPoints = type.DHitPoints;
        DGold = 0;
        DLumber = 0;
        DStep = 0;
        DMoveRemainderX = 0;
        DMoveRemainderY = 0;
        DDirection = dSouth;
        TilePosition(new CPosition());
    }

    public CPlayerAsset(CPlayerAssetType type) {
        DCreationCycle = 0;
        DType = type;
        DHitPoints = type.HitPoints();
        DGold = 0;
        DLumber = 0;
        DStep = 0;
        DMoveRemainderX = 0;
        DMoveRemainderY = 0;
        DAge = 0;
        DDirection = dSouth;
        //TilePosition(new CPosition());

        DTilePosition = new CPosition(0,0);
        DPosition = new CPosition(0,0);
    }

    public int ID() {
        return DId;
    }
    public int ID(int ID) {
        return DId = ID;
    }
    public int IncrementAge() {
        return DAge++;
    }
    public CPlayerAssetType DType() { return DType; }

    public void ChangeDirection() {
        CRandomNumberGenerator p = new CRandomNumberGenerator();

        if (DAge % 100 == 0) {
            int x = Direction().ordinal() + ((p.Random() % 2 ==  1) ? 1 : -1 );
            if (x == -1)
                x = 8;
            if (x == 9)
                x = 0;
            Direction(EDirection.values()[(x%dMax.ordinal())]);
        }
    }

    public static int UpdateFrequency() {
        return DUpdateFrequency;
    }

    public static int UpdateFrequency(int freq) {
        if (0 < freq) {
            DUpdateFrequency = freq;
            DUpdateDivisor = 32 * DUpdateFrequency;
        }
        return DUpdateFrequency;
    }

    public boolean Alive() {
        return 0 < DHitPoints;
    }

    public int CreationCycle() {
        return DCreationCycle;
    }

    public int CreationCycle(int cycle) {
        return DCreationCycle = cycle;
    }

    public int HitPoints() {
        return DHitPoints;
    }

    public int HitPoints(int hitpts) {
        return DHitPoints = hitpts;
    }

    public int IncrementHitPoints(int hitpts) {
        DHitPoints += hitpts;
        if (MaxHitPoints() < DHitPoints) {
            DHitPoints = MaxHitPoints();
        }
        return DHitPoints;
    }

    public int DecrementHitPoints(int hitpts) {
        DHitPoints -= hitpts;
        if (0 > DHitPoints) {
            DHitPoints = 0;
        }
        return DHitPoints;
    }

    public int Gold() {
        return DGold;
    }

    public int Gold(int gold) {
        return DGold = gold;
    }

    public int IncrementGold(int gold) {
        DGold += gold;
        return DGold;
    }

    public int DecrementGold(int gold) {
        DGold -= gold;
        return DGold;
    }

    public int Lumber()  {
        return DLumber;
    }

    public int Lumber(int lumber) {
        return DLumber = lumber;
    }

    public int IncrementLumber(int lumber) {
        DLumber += lumber;
        return DLumber;
    };

    public int DecrementLumber(int lumber) {
        DLumber -= lumber;
        return DLumber;
    }

    public int Step() {
        return DStep;
    }

    public int Step(int step) {
        return DStep = step;
    }

    public void ResetStep() {
        DStep = 0;
    }

    public void IncrementStep() {
        DStep++;
    }

    public CPosition TilePosition() {
        return DTilePosition;
    }

    public CPosition TilePosition(CPosition pos) {
        DPosition.SetFromTile(pos);
        return DTilePosition = pos;
    }

    public int TilePositionX() {
        return DTilePosition.X();
    }

    public int TilePositionX(int x) {
        DPosition.SetXFromTile(x);
        return DTilePosition.X(x);
    }

    public int TilePositionY() {
        return DTilePosition.Y();
    }

    public int TilePositionY(int y) {
        DPosition.SetYFromTile(y);
        return DTilePosition.Y(y);
    }

    public CPosition Position() {
        return DPosition;
    }

    public CPosition Position(CPosition pos) {
        DTilePosition.SetToTile(pos);
        return DPosition = pos;
    }

    public boolean TileAligned() {
        return DPosition.TileAligned();
    }

    public int PositionX() {
        return DPosition.X();
    }

    public int PositionX(int x) {
        DTilePosition.SetXToTile(x);
        return DPosition.X(x);
    }

    public int PositionY() {
        return DPosition.Y();
    }

    public int PositionY(int y) {
        DTilePosition.SetYToTile(y);
        return DPosition.Y(y);
    }

    public CPosition ClosestPosition(CPosition pos) {
//        Log.d("CPA", "Size is " + Size());
//        Log.d("CPA", "This was a " + DType());
        return pos.ClosestPosition(DPosition, Size());
    }

    public int CommandCount() {
        return DCommands.size();
    }

    public void ClearCommand() {
        DCommands.clear();
    }

    public void PushCommand(SAssetCommand command) {
        DCommands.add(command);
    }

    public void EnqueueCommand(SAssetCommand command) {
//        DCommands.insert(DCommands.begin(),command);
        DCommands.add(0, command);
    }

    public void PopCommand() {
        if (DCommands.size() != 0) {
//            DCommands.pop_back();
            DCommands.remove(DCommands.size() - 1);
        }
    }

    public SAssetCommand CurrentCommand() {
        if (DCommands.size() != 0) {
            return DCommands.get(DCommands.size() - 1);
        }
        SAssetCommand RetVal = new SAssetCommand();
        RetVal.DAction = aaNone;
        return RetVal;
    }

    public SAssetCommand NextCommand() {
        if (1 < DCommands.size()) {
            return DCommands.get(DCommands.size() - 2);
        }
        SAssetCommand RetVal = new SAssetCommand();
        RetVal.DAction = aaNone;
        return RetVal;
    }

    public EAssetAction Action() {
        if (DCommands.size() != 0) {
            return DCommands.get(DCommands.size() - 1).DAction;
        }
        return aaNone;
    }

    public boolean HasAction(EAssetAction action) {
        for (SAssetCommand Command : DCommands) {
            if (action == Command.DAction) {
                return true;
            }
        }
        return false;
    }

    public boolean HasActiveCapability(EAssetCapabilityType capability) {
        for (SAssetCommand Command : DCommands) {
            if (aaCapability == Command.DAction) {
                if (capability == Command.DCapability) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean Interruptible() {
        SAssetCommand Command = CurrentCommand();

        switch(Command.DAction) {
            case aaConstruct:
            case aaBuild:
            case aaMineGold:
            case aaConveyLumber:
            case aaConveyGold:
            case aaDeath:
            case aaDecay:       return false;
            case aaCapability:  if (Command.DAssetTarget != null) {
                return aaConstruct != Command.DAssetTarget.Action();
            }
            default:            return true;
        }
    }

    public EDirection Direction() {
        return DDirection;
    }

    public EDirection Direction(EDirection direction) {
        return DDirection = direction;
    }

    public int MaxHitPoints() {
        return DType.HitPoints();
    }

    public EAssetType Type() {
        return DType.Type();
    }

    public CPlayerAssetType AssetType() {
        return DType;
    }

    public void ChangeType(CPlayerAssetType type) {
        DType = type;
    }

    public EPlayerColor Color() {
        return DType.Color();
    }

    public int Armor() {
        return DType.Armor();
    }

    public int Sight() {
        return aaConstruct == Action() ? DType.ConstructionSight() : DType.Sight();
    }

    public int Size() {
        return DType.Size();
    }

    public int Speed() {
        return DType.Speed();
    }

    public int GoldCost() {
        return DType.GoldCost();
    }

    public int LumberCost() {
        return DType.LumberCost();
    }

    public int FoodConsumption() {
        return DType.FoodConsumption();
    }

    public int BuildTime() {
        return DType.BuildTime();
    }

    public int AttackSteps() {
        return DType.AttackSteps();
    }

    public int ReloadSteps() {
        return DType.ReloadSteps();
    }

    public int BasicDamage() {
        return DType.BasicDamage();
    }

    public int PiercingDamage() {
        return DType.PiercingDamage();
    }

    public int Range() {
        return DType.Range();
    }

    public int ArmorUpgrade() {
        return DType.ArmorUpgrade();
    }

    public int SightUpgrade() {
        return DType.SightUpgrade();
    }

    public int SpeedUpgrade() {
        return DType.SpeedUpgrade();
    }

    public int BasicDamageUpgrade() {
        return DType.BasicDamageUpgrade();
    }

    public int PiercingDamageUpgrade() {
        return DType.PiercingDamageUpgrade();
    }

    public int RangeUpgrade() {
        return DType.RangeUpgrade();
    }

    public int EffectiveArmor() {
        return Armor() + ArmorUpgrade();
    }

    public int EffectiveSight() {
        return Sight() + SightUpgrade();
    }

    public int EffectiveSpeed() {
        return Speed() + SpeedUpgrade();
    }

    public int EffectiveBasicDamage() {
        return BasicDamage() + BasicDamageUpgrade();
    }

    public int EffectivePiercingDamage() {
        return PiercingDamage() + PiercingDamageUpgrade();
    }

    public int EffectiveRange() {
        return Range() + RangeUpgrade();
    }

    public boolean HasCapability(EAssetCapabilityType capability) {
        return DType.HasCapability(capability);
    }

    public ArrayList <EAssetCapabilityType> Capabilities() {
        return DType.Capabilities();
    }

    public boolean MoveStep(ArrayList<ArrayList<CPlayerAsset>> occupancyMap,
                            ArrayList<ArrayList<Boolean>> diagonals) {
        EDirection CurrentOctant = DPosition.TileOctant();
        int DeltaX[] = {0, 5, 7, 5, 0, -5, -7, -5};
        int DeltaY[] = {-7, -5, 0, 5, 7, 5, 0, -5};
        CPosition CurrentTile = new CPosition(DTilePosition), CurrentPosition = new CPosition
                (DPosition);

        if ((dMax == CurrentOctant) || (CurrentOctant == DDirection)) {// Aligned just move
            int NewX = Speed() * DeltaX[DDirection.ordinal()] * CPosition.TileWidth() +
                    DMoveRemainderX;
            int NewY = Speed() * DeltaY[DDirection.ordinal()] * CPosition.TileHeight() +
                    DMoveRemainderY;
            DMoveRemainderX = NewX % DUpdateDivisor;
            DMoveRemainderY = NewY % DUpdateDivisor;
            DPosition.IncrementX(NewX / DUpdateDivisor);
            DPosition.IncrementY(NewY / DUpdateDivisor);
        } else { // Entering
            int NewX = Speed() * DeltaX[DDirection.ordinal()] * CPosition.TileWidth() +
                    DMoveRemainderX;
            int NewY = Speed() * DeltaY[DDirection.ordinal()] * CPosition.TileHeight() +
                    DMoveRemainderY;
            int TempMoveRemainderX = NewX % DUpdateDivisor;
            int TempMoveRemainderY = NewY % DUpdateDivisor;
            CPosition NewPosition = new CPosition(DPosition.X() + NewX / DUpdateDivisor,
                    DPosition.Y() + NewY / DUpdateDivisor);

            if (NewPosition.TileOctant() == DDirection) {
                NewPosition.SetToTile(NewPosition);
                NewPosition.SetFromTile(NewPosition);
                TempMoveRemainderX = TempMoveRemainderY = 0;
            }
            DPosition = NewPosition;
            DMoveRemainderX = TempMoveRemainderX;
            DMoveRemainderY = TempMoveRemainderY;
        }
        DTilePosition.SetToTile(DPosition);
        if (!CurrentTile.equals(DTilePosition)) {
            boolean Diagonal = (CurrentTile.X() != DTilePosition.X()) && (CurrentTile.Y() !=
                    DTilePosition.Y());
            int DiagonalX = Math.min(CurrentTile.X(), DTilePosition.X());
            int DiagonalY = Math.min(CurrentTile.Y(), DTilePosition.Y());

            if (occupancyMap.get(DTilePosition.Y()).get(DTilePosition.X()) != null || (Diagonal &&
                    diagonals.get(DiagonalY).get(DiagonalX))) {
                boolean ReturnValue = false;
                    if (aaWalk == occupancyMap.get(DTilePosition.Y()).get(DTilePosition.X())
                            .Action()) {
                        ReturnValue = occupancyMap.get(DTilePosition.Y()).get(DTilePosition.X())
                                .Direction() == CurrentPosition.TileOctant();
                    }
                DTilePosition = CurrentTile;
                DPosition = CurrentPosition;
                Log.d("CPA", "Someone just got reset diagonally?");
                return ReturnValue;
            }
            if (Diagonal) {
                diagonals.get(DiagonalY).set(DiagonalX, true);
            }
            occupancyMap.get(DTilePosition.Y()).set(DTilePosition.X(), occupancyMap.get
                    (CurrentTile.Y()).get(CurrentTile.X()));
            occupancyMap.get(CurrentTile.Y()).set(CurrentTile.X(), null);
        }

        IncrementStep();
        return true;
    }
}