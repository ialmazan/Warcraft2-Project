package com.ecs160.nittacraft.renderers;

import android.graphics.Canvas;

import com.ecs160.nittacraft.CGraphicTileset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.maps.CTerrainMap;

import java.util.ArrayList;

public class CResourceRenderer {
    protected CGraphicTileset DIconTileset;
    //protected CFontTileset DFont;
    protected CPlayerData DPlayer;
    protected ArrayList< Integer > DIconIndices = new ArrayList< Integer >();
    protected CTerrainMap DMap;
    //protected gint DTextHeight = new gint();
    protected int DForegroundColor;
    protected int DBackgroundColor;
    protected int DInsufficientColor;
    protected int DLastGoldDisplay;
    protected int DLastLumberDisplay;

    public CResourceRenderer(CGraphicTileset icons, CTerrainMap map/*, CFontTileset font, CPlayerData player*/) {
        DMap = map;
        DIconTileset = icons;

        DIconIndices.add(0, DIconTileset.FindTile("gold"));
        DIconIndices.add(1, DIconTileset.FindTile("lumber"));
        DIconIndices.add(2, DIconTileset.FindTile("food"));
        //DFont.MeasureText("0123456789", Width, DTextHeight);
    }

    public final void DrawResources(Canvas canvas)
    {
//        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "Kingthings_Exeter");

//        int DeltaGold = DPlayer.Gold() - DLastGoldDisplay;
//        int DeltaLumber = DPlayer.Lumber() - DLastLumberDisplay;
//
//        DeltaGold /= 5;
//        if ((-3 < DeltaGold) && (3 > DeltaGold))
//        {
//            DLastGoldDisplay = DPlayer.Gold();
//        }
//        else
//        {
//            DLastGoldDisplay += DeltaGold;
//        }
//        DeltaLumber /= 5;
//        if ((-3 < DeltaLumber) && (3 > DeltaLumber))
//        {
//            DLastLumberDisplay = DPlayer.Lumber();
//        }
//        else
//        {
//            DLastLumberDisplay += DeltaLumber;
//        }
        //gdk_pixmap_get_size(drawable, Width, Height);
        //TextYOffset = Height / 2 - DTextHeight / 2;
        //ImageYOffset = Height / 2 - DIconTileset.TileHeight() / 2;
//        float scale = (float) canvas.getWidth()/DMap.Width();
//        float resourceHeight = DMap.Height() * scale;
//        float resourceTextHeight = resourceHeight + DIconTileset.TileHalfHeight() * 3 + 5;
//        XOffset = 0;
//        WidthSeparation = DIconTileset.TileWidth() * DIconTileset.TileHalfWidth();
//        int textSize = DIconTileset.TileHeight() * 2;
//        Paint paint = new Paint();
//        paint.setARGB(255, 255, 221, 100);
//        paint.setTextSize(textSize);
//        //FIXME Should find player color, but they all seem to be red anyway
//
//        DIconTileset.DrawTile(canvas, 0,  (int) resourceHeight, DIconIndices.get(0));
//        canvas.drawText(String.valueOf(DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor).Gold()), XOffset + textSize, resourceTextHeight, paint);
//        //DFont.DrawTextWithShadow(drawable, gc, XOffset + DIconTileset.TileWidth(), TextYOffset, DForegroundColor, DBackgroundColor, 1, " " + CTextFormatter.IntegerToPrettyString(DLastGoldDisplay));
//        XOffset += WidthSeparation;
//
//        DIconTileset.DrawTile(canvas, XOffset, (int) resourceHeight, DIconIndices.get(1));
//        canvas.drawText(String.valueOf(DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor).Lumber()), XOffset + textSize, resourceTextHeight, paint);
//        //DFont.DrawTextWithShadow(drawable, gc, XOffset + DIconTileset.TileWidth(), TextYOffset, DForegroundColor, DBackgroundColor, 1, " " + CTextFormatter.IntegerToPrettyString(DLastLumberDisplay));
//        XOffset += WidthSeparation;
//
//        DIconTileset.DrawTile(canvas, XOffset, (int) resourceHeight, DIconIndices.get(2));
//        canvas.drawText(String.valueOf(DApplicationData.DGameModel.Player(CGameDataTypes
//                .EPlayerColor.pcRed).FoodConsumption()) + "/" + String.valueOf(
//                DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor)
//                        .FoodProduction()), XOffset + textSize, resourceTextHeight, paint);

        /*if (DPlayer.FoodConsumption() > DPlayer.FoodProduction())
        {
            int SecondTextWidth;
            int TotalTextWidth;
            int TextHeight;
            //DFont->MeasureText( std::string(" ") + std::to_string(DPlayer->FoodConsumption()), FirstTextWidth, TextHeight);
            DFont.MeasureText(" / " + String.valueOf(DPlayer.FoodProduction()), SecondTextWidth, TextHeight);
            DFont.MeasureText(" " + String.valueOf(DPlayer.FoodConsumption()) + " / " + String.valueOf(DPlayer.FoodProduction()), TotalTextWidth, TextHeight);
            DFont.DrawTextWithShadow(drawable, gc, XOffset + DIconTileset.TileWidth(), TextYOffset, DInsufficientColor, DBackgroundColor, 1, " " + String.valueOf(DPlayer.FoodConsumption()));
            DFont.DrawTextWithShadow(drawable, gc, XOffset + DIconTileset.TileWidth() + TotalTextWidth - SecondTextWidth, TextYOffset, DForegroundColor, DBackgroundColor, 1, " / " + String.valueOf(DPlayer.FoodProduction()));
        }
        else
        {
            DFont.DrawTextWithShadow(drawable, gc, XOffset + DIconTileset.TileWidth(), TextYOffset, DForegroundColor, DBackgroundColor, 1, " " + String.valueOf(DPlayer.FoodConsumption()) + " / " + String.valueOf(DPlayer.FoodProduction()));
        }*/
    }
}