package com.ecs160.nittacraft;

import com.ecs160.nittacraft.CGameDataTypes.*;
import com.ecs160.nittacraft.maps.CTerrainMap;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atWall;
import static com.ecs160.nittacraft.CPixelType.EAssetTerrainType.attGold;
import static com.ecs160.nittacraft.CPixelType.EAssetTerrainType.attKnight;
import static com.ecs160.nittacraft.CPixelType.EAssetTerrainType.attLumber;
import static com.ecs160.nittacraft.CPixelType.EAssetTerrainType.attWall;

public class CPixelType{
    public enum EAssetTerrainType{
        attNone,
        attGrass,
        attDirt,
        attRock,
        attTree,
        attStump,
        attWater,
        attWall,
        attWallDamaged,
        attRubble,
        attPeasant,
        attFootman,
        attArcher,
        attRanger,
        attGoldMine,
        attTownHall,
        attKeep,
        attCastle,
        attFarm,
        attBarracks,
        attLumberMill,
        attBlacksmith,
        attScoutTower,
        attGuardTower,
        attCannonTower,
        attGoldVein,
        attGold,
        attLumber,
        attKnight,
        attSeedling,
        attAdolescent,
        attMax
    };

//    public enum EAssetTerrainTypeRef = EAssetTerrainType;

    static protected EAssetTerrainType DType;
    protected EPlayerColor DColor;
//    protected static GdkPixbuf DPixbufTranslater;

    public CPixelType(int red, int green, int blue) {
        DColor = EPlayerColor.values()[red]; //(EPlayerColor)red;
        DType = EAssetTerrainType.values()[green]; //(EAssetTerrainType)green;
    }
    public CPixelType(CTerrainMap.ETileType type) {
        DColor = EPlayerColor.pcNone;

        switch(type) {
            case ttGrass:
                DType = EAssetTerrainType.attGrass;
                break;
            case ttDirt:
                DType = EAssetTerrainType.attDirt;
                break;
            case ttRock:
                DType = EAssetTerrainType.attRock;
                break;
            case ttTree:
                DType = EAssetTerrainType.attTree;
                break;
            case ttStump:
                DType = EAssetTerrainType.attStump;
                break;
            case ttWater:
                DType = EAssetTerrainType.attWater;
                break;
            case ttWall:
                DType = attWall;
                break;
            case ttWallDamaged:
                DType = EAssetTerrainType.attWallDamaged;
                break;
            case ttRubble:
                DType = EAssetTerrainType.attRubble;
                break;
            case ttSeedling:
                DType = EAssetTerrainType.attSeedling;
                break;
            case ttAdolescent:
                DType = EAssetTerrainType.attAdolescent;
                break;
            default:
                DType = EAssetTerrainType.attNone;
                break;
        }
    }
    public CPixelType(CPlayerAsset asset) {
        switch(asset.Type()) {
            case atPeasant:
                DType = EAssetTerrainType.attPeasant;
                break;
            case atFootman:
                DType = EAssetTerrainType.attFootman;
                break;
            case atArcher:
                DType = EAssetTerrainType.attArcher;
                break;
            case atRanger:
                DType = EAssetTerrainType.attRanger;
                break;
            case atGoldMine:
                DType = EAssetTerrainType.attGoldMine;
                break;
            case atTownHall:
                DType = EAssetTerrainType.attTownHall;
                break;
            case atKeep:
                DType = EAssetTerrainType.attKeep;
                break;
            case atCastle:
                DType = EAssetTerrainType.attCastle;
                break;
            case atFarm:
                DType = EAssetTerrainType.attFarm;
                break;
            case atBarracks:
                DType = EAssetTerrainType.attBarracks;
                break;
            case atLumberMill:
                DType = EAssetTerrainType.attLumberMill;
                break;
            case atBlacksmith:
                DType = EAssetTerrainType.attBlacksmith;
                break;
            case atScoutTower:
                DType = EAssetTerrainType.attScoutTower;
                break;
            case atGuardTower:
                DType = EAssetTerrainType.attGuardTower;
                break;
            case atCannonTower:
                DType = EAssetTerrainType.attCannonTower;
                break;
            case atGoldVein:
                DType = EAssetTerrainType.attGoldVein;
                break;
            case atGold:
                DType = attGold;
                break;
            case atLumber:
                DType = attLumber;
                break;
            case atWall:
                DType = attWall;
                break;
            case atKnight:
                DType = attKnight;
                break;
            default:
                DType = EAssetTerrainType.attNone;
                break;
        }
        DColor = asset.Color();
    }
    public CPixelType(CPixelType pixeltype) {
        DType = pixeltype.DType;
        DColor = pixeltype.DColor;
    }


    public CPixelType isEqual(CPixelType pixeltype) {
        if (this != pixeltype) {
            DType = pixeltype.DType;
            DColor = pixeltype.DColor;
        }
        return this;
    }

    public EAssetTerrainType Type() {
        return DType;
    }

    public EPlayerColor Color() {
        return DColor;
    }

    public int ToPixelColor() {
        int RetVal = DColor.ordinal();

        RetVal <<= 16;
        RetVal |= (DType.ordinal())<<8;
        return RetVal;
    }

    static public EAssetType AssetType() {
        switch(DType) {
            case attPeasant:
                return EAssetType.atPeasant;
            case attFootman:
                return EAssetType.atFootman;
            case attArcher:
                return EAssetType.atArcher;
            case attRanger:
                return EAssetType.atRanger;
            case attGoldMine:
                return EAssetType.atGoldMine;
            case attTownHall:
                return EAssetType.atTownHall;
            case attKeep:
                return EAssetType.atKeep;
            case attCastle:
                return EAssetType.atCastle;
            case attFarm:
                return EAssetType.atFarm;
            case attBarracks:
                return EAssetType.atBarracks;
            case attLumberMill:
                return EAssetType.atLumberMill;
            case attBlacksmith:
                return EAssetType.atBlacksmith;
            case attScoutTower:
                return EAssetType.atScoutTower;
            case attGuardTower:
                return EAssetType.atGuardTower;
            case attCannonTower:
                return EAssetType.atCannonTower;
            case attGoldVein:
                return EAssetType.atGoldVein;
            case attWall:
                return EAssetType.atWall;
            case attGold:
                return EAssetType.atGold;
            case attLumber:
                return EAssetType.atLumber;
            case attKnight:
                return EAssetType.atKnight;
            default:
                return EAssetType.atNone;
        }
    }

//    public static CPixelType GetPixelType(GdkDrawable drawable, CPosition pos) {
//        return GetPixelType(drawable, pos.X(), pos.Y());
//    }
//    public static CPixelType GetPixelType(GdkDrawable drawable, int xpos, int ypos) {
//        char Pixel;
//
//        DPixbufTranslater = gdk_pixbuf_get_from_drawable(DPixbufTranslater, drawable, nullptr, xpos, ypos, 0, 0, 1, 1);
//        Pixel = gdk_pixbuf_get_pixels(DPixbufTranslater);
//
//        return CPixelType(Pixel[0], Pixel[1], Pixel[2]);
//    }
}