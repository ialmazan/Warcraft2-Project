package com.ecs160.nittacraft;

import java.util.ArrayList;

public class CSoundEventRenderer {
    float DVolume;
    CSoundLibraryMixer DSoundMixer;
    CPlayerData DPlayer;
    protected CRandomNumberGenerator DRandomNumberGenerator = new CRandomNumberGenerator();

    protected ArrayList<CGameModel.SGameEvent> DDelayedEvents = new ArrayList<>();
    protected int DPlaceIndex;
    protected int DTickIndex;
    protected int DTockIndex;
    protected ArrayList<Integer> DDelayedSelectionIndices = new ArrayList<>();
    protected ArrayList<Integer> DDelayedAcknowledgeIndices = new ArrayList<>();
    protected ArrayList<Integer> DConstructIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DWorkCompleteIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DSelectionIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DAcknowledgeIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DReadyIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DDeathIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DAttackedIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DMissleFireIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DMissleHitIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DHarvestIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DMeleeHitIndices = new ArrayList<>();

    public CSoundEventRenderer(CSoundLibraryMixer mixer, CPlayerData player) {
        ArrayList<String> Names = new ArrayList<>();
        Names.add(CGameDataTypes.EAssetType.atNone.ordinal(), "basic");
        Names.add(CGameDataTypes.EAssetType.atPeasant.ordinal(), "peasant");
        Names.add(CGameDataTypes.EAssetType.atFootman.ordinal(), "footman");
        Names.add(CGameDataTypes.EAssetType.atArcher.ordinal(), "archer");
        Names.add(CGameDataTypes.EAssetType.atRanger.ordinal(), "archer");
        Names.add(CGameDataTypes.EAssetType.atGoldMine.ordinal(), "gold-mine");
        Names.add(CGameDataTypes.EAssetType.atGoldVein.ordinal(), "goldvein");
        Names.add(CGameDataTypes.EAssetType.atTownHall.ordinal(), "town-hall");
        Names.add(CGameDataTypes.EAssetType.atKeep.ordinal(), "keep");
        Names.add(CGameDataTypes.EAssetType.atCastle.ordinal(), "castle");
        Names.add(CGameDataTypes.EAssetType.atFarm.ordinal(), "farm");
        Names.add(CGameDataTypes.EAssetType.atBarracks.ordinal(), "barracks");
        Names.add(CGameDataTypes.EAssetType.atLumberMill.ordinal(), "lumber-mill");
        Names.add(CGameDataTypes.EAssetType.atBlacksmith.ordinal(), "blacksmith");
        Names.add(CGameDataTypes.EAssetType.atScoutTower.ordinal(), "scout-tower");
        Names.add(CGameDataTypes.EAssetType.atGuardTower.ordinal(), "guard-tower");
        Names.add(CGameDataTypes.EAssetType.atCannonTower.ordinal(), "cannon-tower");
        Names.add(CGameDataTypes.EAssetType.atGold.ordinal(), "gold");
        Names.add(CGameDataTypes.EAssetType.atLumber.ordinal(), "lumber");
        Names.add(CGameDataTypes.EAssetType.atWall.ordinal(), "wall");
        Names.add(CGameDataTypes.EAssetType.atKnight.ordinal(), "knight");

        DSoundMixer = mixer;
        DPlayer = player;
        DVolume = 1.0f;

        for (int Index = 0; Index < CGameDataTypes.EAssetType.atMax.ordinal(); ++Index) {
            DDelayedSelectionIndices.add(0);
            DDelayedAcknowledgeIndices.add(0);
            DConstructIndices.add(0);
        }

        for (int i = 0; i < CGameDataTypes.EAssetType.atMax.ordinal(); ++i) {
            DWorkCompleteIndices.add(new ArrayList<Integer>());
            DSelectionIndices.add(new ArrayList<Integer>());
            DAcknowledgeIndices.add(new ArrayList<Integer>());
            DReadyIndices.add(new ArrayList<Integer>());
            DDeathIndices.add(new ArrayList<Integer>());
            DAttackedIndices.add(new ArrayList<Integer>());
            DMissleFireIndices.add(new ArrayList<Integer>());
            DMissleHitIndices.add(new ArrayList<Integer>());
            DHarvestIndices.add(new ArrayList<Integer>());
            DMeleeHitIndices.add(new ArrayList<Integer>());
        }

        {
            int ClipIndex = DSoundMixer.FindClip("construct");
            if (0 <= ClipIndex) {
                DConstructIndices.add(ClipIndex);
            } else {
                int SoundIndex = 1;
                while (true) {
                    ClipIndex = DSoundMixer.FindClip("construct" + Integer.toString(SoundIndex));
                    if (0 > ClipIndex) {
                        break;
                    }
                    DConstructIndices.add(ClipIndex);
                    SoundIndex++;
                }
            }
        }

        for (int TypeIndex = 0; TypeIndex < CGameDataTypes.EAssetType.atMax.ordinal(); TypeIndex++) {
            CPlayerAssetType AssetType = DPlayer.AssetTypes().get(CPlayerAssetType.TypeToName
                    (CGameDataTypes.EAssetType.values()[TypeIndex]));

            int ClipIndex;
            ClipIndex = DSoundMixer.FindClip(Names.get(TypeIndex) + "-work-completed");
            if (0 <= ClipIndex) {
                DWorkCompleteIndices.get(TypeIndex).add(ClipIndex);
            } else if (DWorkCompleteIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).size() > 0) {
                DWorkCompleteIndices.get(TypeIndex).add(DWorkCompleteIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).get(0));
            }
            ClipIndex = DSoundMixer.FindClip(Names.get(TypeIndex) + "-selected");
            if (0 <= ClipIndex) {
                DSelectionIndices.get(TypeIndex).add(ClipIndex);
            } else {
                int SoundIndex = 1;
                while (true) {
                    ClipIndex = DSoundMixer.FindClip(Names.get(TypeIndex) + "-selected" + String.valueOf(SoundIndex));
                    if (0 > ClipIndex) {
                        break;
                    }
                    DSelectionIndices.get(TypeIndex).add(ClipIndex);
                    SoundIndex++;
                }
                if (DSelectionIndices.get(TypeIndex).size() == 0 && AssetType.Speed() > 0 &&
                        DSelectionIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).size()
                                > 0) {
                    DSelectionIndices.set(TypeIndex, DSelectionIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()));
                }
            }
            ClipIndex = DSoundMixer.FindClip(Names.get(TypeIndex) + "-acknowledge");
            if (0 <= ClipIndex) {
                DAcknowledgeIndices.get(TypeIndex).add(ClipIndex);
            } else {
                int SoundIndex = 1;
                while (true) {
                    ClipIndex = DSoundMixer.FindClip(Names.get(TypeIndex) + "-acknowledge" + String.valueOf(SoundIndex));
                    if (0 > ClipIndex) {
                        break;
                    }
                    DAcknowledgeIndices.get(TypeIndex).add(ClipIndex);
                    SoundIndex++;
                }
                if (DAcknowledgeIndices.get(TypeIndex).size() == 0 && DAcknowledgeIndices.get
                        (CGameDataTypes.EAssetType.atNone.ordinal()).size() > 0) {
                    DAcknowledgeIndices.set(TypeIndex, DAcknowledgeIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()));
                }
            }
            ClipIndex = DSoundMixer.FindClip(Names.get(TypeIndex) + "-ready");
            if (0 <= ClipIndex) {
                DReadyIndices.get(TypeIndex).add(ClipIndex);
            } else if (CGameDataTypes.EAssetType.atFootman.ordinal() == TypeIndex) {
                ClipIndex = DSoundMixer.FindClip(Names.get(CGameDataTypes.EAssetType.atNone.ordinal()) + "-ready");
                if (0 <= ClipIndex) {
                    DReadyIndices.get(TypeIndex).add(ClipIndex);
                }
            }
            String UnitBuildingName = AssetType.Speed() > 0 ? "unit" : "building";
            ClipIndex = DSoundMixer.FindClip(UnitBuildingName + "-death");
            if (0 <= ClipIndex) {
                DDeathIndices.get(TypeIndex).add(ClipIndex);
            } else {
                int SoundIndex = 1;
                while (true) {
                    ClipIndex = DSoundMixer.FindClip(UnitBuildingName + "-death" + String.valueOf(SoundIndex));
                    if (0 > ClipIndex) {
                        break;
                    }
                    DDeathIndices.get(TypeIndex).add(ClipIndex);
                    SoundIndex++;
                }
                if (DDeathIndices.get(TypeIndex).size() > 0 && DDeathIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).size() > 0) {
                    DDeathIndices.set(TypeIndex, DDeathIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()));
                }
            }

            ClipIndex = DSoundMixer.FindClip(UnitBuildingName + "-help");
            if (0 <= ClipIndex) {
                DAttackedIndices.get(TypeIndex).add(ClipIndex);
            } else {
                int SoundIndex = 1;
                while (true) {
                    ClipIndex = DSoundMixer.FindClip(UnitBuildingName + "-help" + String.valueOf(SoundIndex));
                    if (0 > ClipIndex) {
                        break;
                    }
                    DAttackedIndices.get(TypeIndex).add(ClipIndex);
                    SoundIndex++;
                }
                if (DAttackedIndices.get(TypeIndex).size() > 0 && DAttackedIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).size() > 0) {
                    DAttackedIndices.set(TypeIndex, DAttackedIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()));
                }
            }
            if ((CGameDataTypes.EAssetType.atArcher.ordinal() == TypeIndex) || (CGameDataTypes.EAssetType.atRanger.ordinal() == TypeIndex) || (CGameDataTypes.EAssetType.atGuardTower.ordinal() == TypeIndex)) {
                DMissleFireIndices.get(TypeIndex).add(DSoundMixer.FindClip("bowfire"));
                DMissleHitIndices.get(TypeIndex).add(DSoundMixer.FindClip("bowhit"));
            } else if (CGameDataTypes.EAssetType.atCannonTower.ordinal() == TypeIndex) {
                DMissleFireIndices.get(TypeIndex).add(DSoundMixer.FindClip("cannonfire"));
                DMissleHitIndices.get(TypeIndex).add(DSoundMixer.FindClip("cannonhit"));
            }
            ClipIndex = DSoundMixer.FindClip("harvest");
            if (0 <= ClipIndex) {
                DAttackedIndices.get(TypeIndex).add(ClipIndex);
            } else {
                int SoundIndex = 1;
                while (true) {
                    ClipIndex = DSoundMixer.FindClip("harvest" + String.valueOf(SoundIndex));
                    if (0 > ClipIndex) {
                        break;
                    }
                    DHarvestIndices.get(TypeIndex).add(ClipIndex);
                    SoundIndex++;
                }
                if (DHarvestIndices.get(TypeIndex).size() == 0 && DHarvestIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).size() > 0) {
                    DHarvestIndices.set(TypeIndex, DHarvestIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()));
                }
            }
            if (1 == AssetType.Range()) {
                ClipIndex = DSoundMixer.FindClip("melee-hit");
                if (0 <= ClipIndex) {
                    DMeleeHitIndices.get(TypeIndex).add(ClipIndex);
                } else {
                    int SoundIndex = 1;
                    while (true) {
                        ClipIndex = DSoundMixer.FindClip("melee-hit" + String.valueOf(SoundIndex));
                        if (0 > ClipIndex) {
                            break;
                        }
                        DMeleeHitIndices.get(TypeIndex).add(ClipIndex);
                        SoundIndex++;
                    }

                    if (DMeleeHitIndices.get(TypeIndex).size() == 0 && DMeleeHitIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()).size() > 0) {
                        DMeleeHitIndices.set(TypeIndex, DMeleeHitIndices.get(CGameDataTypes.EAssetType.atNone.ordinal()));
                    }
                }
            }
        }

    }

    float Volume() {
        return DVolume;
    }

    float Volume(int vol) {
        if ((0.0 <= vol) && (1.0 >= vol)) {
            DVolume = vol;
        }
        return DVolume;
    }

    protected static float RightBias(SRectangle viewportrect, CPosition position) {
        int LeftX;
        int RightX;
        int CenterX;

        LeftX = viewportrect.DXPosition;
        RightX = viewportrect.DXPosition + viewportrect.DWidth - 1;
        CenterX = (LeftX + RightX) / 2;

        if (position.X() <= LeftX) {
            return -1.0f;
        }
        if (position.X() >= RightX) {
            return 1.0f;
        }
        if (LeftX == CenterX) {
            return 0.0f;
        }
        if (position.X() < CenterX) {
            return 1.0f - ((float) (position.X() - LeftX)) / (CenterX - LeftX);
        }
        return ((float) (position.X() - CenterX)) / (RightX - CenterX);
    }

    protected static boolean OnScreen(SRectangle viewportrect, CPosition position) {
        int LeftX;
        int RightX;
        int TopY;
        int BottomY;

        LeftX = viewportrect.DXPosition - CPosition.TileWidth();
        if (position.X() < LeftX) {
            return false;
        }
        RightX = viewportrect.DXPosition + viewportrect.DWidth + CPosition.TileWidth() - 1;
        if (position.X() > RightX) {
            return false;
        }
        TopY = viewportrect.DYPosition - CPosition.TileHeight();
        if (position.Y() < TopY) {
            return false;
        }
        BottomY = viewportrect.DYPosition + viewportrect.DHeight + CPosition.TileHeight() - 1;
        if (position.Y() > BottomY) {
            return false;
        }
        return true;
    }

    public void RenderEvents(SRectangle viewportrect) {
        int MainRandomNumber = DRandomNumberGenerator.Random();
        ArrayList<CGameModel.SGameEvent> AllEvents = new ArrayList<>(DDelayedEvents);
        ArrayList<Integer> Selections = new ArrayList<>();
        ArrayList<Integer> Acknowledges = new ArrayList<>();

        // Initialize arrays with empty values
        DDelayedEvents.clear();
        for (int i = 0; i < CGameDataTypes.EAssetType.atMax.ordinal(); ++i) {
            Selections.add(0);
            Acknowledges.add(0);
        }

        AllEvents.addAll(DPlayer.GameEvents());
        for (CGameModel.SGameEvent Event : AllEvents) {
            if (CGameModel.EEventType.etSelection == Event.DType) {
                if (Event.DAsset != null) {
                    if ((CGameDataTypes.EPlayerColor.pcNone == Event.DAsset.Color()) || (DPlayer.Color() == Event.DAsset.Color())) {
                        if (CGameDataTypes.EAssetAction.aaConstruct == Event.DAsset.Action()) {
                            if (DConstructIndices.size() > 0) {
                                int RandomClip = MainRandomNumber % DConstructIndices.size();

                                DSoundMixer.PlayClip(DConstructIndices.get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                            }
                        } else if (DSelectionIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            if (1 > Selections.get(Event.DAsset.Type().ordinal())) {
                                int RandomClip = DRandomNumberGenerator.Random() % DSelectionIndices.get(Event.DAsset.Type().ordinal()).size();
                                if (0 > DDelayedSelectionIndices.get(Event.DAsset.Type().ordinal())) {
                                    DDelayedSelectionIndices.set(Event.DAsset.Type().ordinal(), RandomClip);
                                } else {
                                    RandomClip = DDelayedSelectionIndices.get(Event.DAsset.Type().ordinal());
                                }
                                DSoundMixer.PlayClip(DSelectionIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                                Selections.set(Event.DAsset.Type().ordinal(), Event.DAsset.Type().ordinal() + 1);
                            } else if (0 == (DRandomNumberGenerator.Random() & 0x3)) {
                                DDelayedEvents.add(Event);
                            }
                        }
                    }
                }
            } else if (CGameModel.EEventType.etAcknowledge == Event.DType) {
                if (Event.DAsset != null) {
                    if ((CGameDataTypes.EPlayerColor.pcNone == Event.DAsset.Color()) || (DPlayer.Color() == Event.DAsset.Color())) {
                        if (DAcknowledgeIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            if (1 > Acknowledges.get(Event.DAsset.Type().ordinal())) {
                                int RandomClip = MainRandomNumber % DAcknowledgeIndices.get(Event.DAsset.Type().ordinal()).size();

                                if (0 > DDelayedAcknowledgeIndices.get(Event.DAsset.Type().ordinal())) {
                                    DDelayedAcknowledgeIndices.set(Event.DAsset.Type().ordinal(), RandomClip);
                                } else {
                                    RandomClip = DDelayedAcknowledgeIndices.get(Event.DAsset.Type().ordinal());
                                }
                                DSoundMixer.PlayClip(DAcknowledgeIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                                Acknowledges.set(Event.DAsset.Type().ordinal(), Acknowledges.get(Event.DAsset.Type().ordinal()));
                            } else if (0 == (DRandomNumberGenerator.Random() & 0x3)) {
                                DDelayedEvents.add(Event);
                            }
                        }
                    }
                }
            } else if (CGameModel.EEventType.etWorkComplete == Event.DType) {
                if (Event.DAsset != null) {
                    if (DPlayer.Color() == Event.DAsset.Color()) {
                        if (DWorkCompleteIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DWorkCompleteIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DWorkCompleteIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etReady == Event.DType) {
                if (Event.DAsset != null) {
                    if ((CGameDataTypes.EPlayerColor.pcNone == Event.DAsset.Color()) || (DPlayer.Color() == Event.DAsset.Color())) {
                        if (DReadyIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DReadyIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DReadyIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etDeath == Event.DType) {
                if (Event.DAsset != null) {
                    if (OnScreen(viewportrect, Event.DAsset.Position())) {
                        if (DDeathIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DDeathIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DDeathIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etAttacked == Event.DType) {
                if (Event.DAsset != null) {
                    if (!OnScreen(viewportrect, Event.DAsset.Position())) {
                        if (DAttackedIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DAttackedIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DAttackedIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etMissleFire == Event.DType) {
                if (Event.DAsset != null) {
                    if (OnScreen(viewportrect, Event.DAsset.Position())) {
                        if (DMissleFireIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DMissleFireIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DMissleFireIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etMissleHit == Event.DType) {
                if (Event.DAsset != null) {
                    if (OnScreen(viewportrect, Event.DAsset.Position())) {
                        SAssetCommand CreationCommand = Event.DAsset.NextCommand(); // Find out type of missle

                        if ((CGameDataTypes.EAssetAction.aaConstruct == CreationCommand.DAction) && CreationCommand.DAssetTarget != null) {
                            if (DMissleHitIndices.get(CreationCommand.DAssetTarget.Type().ordinal()).size() > 0) {
                                int RandomClip = DRandomNumberGenerator.Random() % DMissleHitIndices.get(CreationCommand.DAssetTarget.Type().ordinal()).size();

                                DSoundMixer.PlayClip(DMissleHitIndices.get(CreationCommand.DAssetTarget.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                            }
                        }
                    }
                }
            } else if (CGameModel.EEventType.etHarvest == Event.DType) {
                if (Event.DAsset != null) {
                    if (OnScreen(viewportrect, Event.DAsset.Position()) && (Event.DAsset.AttackSteps() - 1 == (Event.DAsset.Step() % Event.DAsset.AttackSteps()))) {
                        if (DHarvestIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DHarvestIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DHarvestIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etMeleeHit == Event.DType) {
                if (Event.DAsset != null) {
                    if (OnScreen(viewportrect, Event.DAsset.Position())) {
                        if (DMeleeHitIndices.get(Event.DAsset.Type().ordinal()).size() > 0) {
                            int RandomClip = DRandomNumberGenerator.Random() % DMeleeHitIndices.get(Event.DAsset.Type().ordinal()).size();

                            DSoundMixer.PlayClip(DMeleeHitIndices.get(Event.DAsset.Type().ordinal()).get(RandomClip), DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                        }
                    }
                }
            } else if (CGameModel.EEventType.etPlaceAction == Event.DType) {
                if (Event.DAsset != null) {
                    if (0 <= DPlaceIndex) {
                        DSoundMixer.PlayClip(DPlaceIndex, DVolume, RightBias(viewportrect, Event.DAsset.Position()));
                    }
                }

            } else if (CGameModel.EEventType.etButtonTick == Event.DType) {
                if (0 <= DTickIndex) {
                    DSoundMixer.PlayClip(DTickIndex, DVolume, 0.0f);
                }
            }
        }

        for (int Index = 0; Index < CGameDataTypes.EAssetType.atMax.ordinal(); Index++) {
            if (0 == Selections.get(Index)) {
                DDelayedSelectionIndices.set(Index, -1);
            }
            if (0 == Acknowledges.get(Index)) {
                DDelayedAcknowledgeIndices.set(Index, -1);
            }
        }

    }
}
