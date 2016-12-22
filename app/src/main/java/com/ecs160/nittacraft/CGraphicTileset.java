package com.ecs160.nittacraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static com.ecs160.nittacraft.CApplicationData.DTileScaleFactor;

public class CGraphicTileset {
    /// A pixel buffer that holds the tileset image data.
    protected ArrayList<Bitmap> mTiles;
    /// A vector of bitmap images that define the clipping area of each tile.
    protected ArrayList<Bitmap> DClippingMasks;
    /// A map between tile names and tile indices.
    protected HashMap<String, Integer> DMapping;
    /// The number of tiles in the tileset.
    public int DTileCount;
    public int DTileWidth;
    public int DTileHeight;
    protected int DTileHalfWidth;
    protected int DTileHalfHeight;
    /// The number of pixel colors.
    protected int DPixelCount;
    /// A vector->ArrayList of pixel colors.
    protected ArrayList<String> DPixelColors;
    /// A map between pixel names and pixel color indices.
    protected HashMap<String, Integer> DPixelMapping;
    protected Context DContext;

    public CGraphicTileset() {
        DTileCount = 0;
        DTileWidth = 0;
        DTileHeight = 0;
        DTileHalfWidth = 0;
        DTileHalfHeight = 0;
        DPixelCount = 0;
        DMapping = new HashMap<String, Integer>();
        DPixelMapping = new HashMap<String, Integer>();
        DPixelColors = new ArrayList<>();
        mTiles = new ArrayList<>();
    }

    public CGraphicTileset(Context context) {
        DContext = context;
        DTileCount = 0;
        DTileWidth = 0;
        DTileHeight = 0;
        DTileHalfWidth = 0;
        DTileHalfHeight = 0;
        DPixelCount = 0;
        DMapping = new HashMap<String, Integer>();
        DPixelMapping = new HashMap<String, Integer>();
        DPixelColors = new ArrayList<>();
        mTiles = new ArrayList<>();
    }

    public int TileWidth() {
        return DTileWidth*DTileScaleFactor;
    };

    /// \return CGraphicTileset#DTileHeight
    public int TileHeight() {
        return DTileHeight*DTileScaleFactor;
    };

    /// \return CGraphicTileset#DTileHalfWidth
    public int TileHalfWidth() {
        return DTileHalfWidth*DTileScaleFactor;
    };

    /// \return CGraphicTileset#DTileHalfHeight
    public int TileHalfHeight() {
        return DTileHalfHeight*DTileScaleFactor;
    };

    /// \return CGraphicTileset#DPixelCount
    public int PixelCount() {
        return DPixelCount;
    };

    public boolean TileCount(int count) {
/*        GdkPixbuf TempPixbufTileset;
        char Pixels;
        int PixelLength;

        if (0 > count) {
            return false;
        }
        if (!DTileWidth || !DTileHeight) {
            return false;
        }
        if (count < DTileCount) {
            auto Iterator = DMapping.begin();
            DTileCount = count;

            while (DMapping.end() != Iterator) {
                if (Iterator->second >= DTileCount) {
                    Iterator = DMapping.erase(Iterator);
                }
                else {
                    Iterator++;
                }
            }
            return true;
        }
        TempPixbufTileset = gdk_pixbuf_new(GDK_COLORSPACE_RGB, true, 8, DTileWidth, count * DTileHeight);
        if (null == TempPixbufTileset) {
            return false;
        }
        Pixels = gdk_pixbuf_get_pixels_with_length(TempPixbufTileset, PixelLength);
        memset(Pixels, 0, PixelLength);
        if (null != DPixbufTileset) {
            int NumberChannels;
            NumberChannels = gdk_pixbuf_get_n_channels(DPixbufTileset);

            memcpy(Pixels, gdk_pixbuf_get_pixels(DPixbufTileset), NumberChannels * DTileHeight * DTileWidth * DTileCount);
            g_object_unref(G_OBJECT(DPixbufTileset));
        }
        DPixbufTileset = TempPixbufTileset;*/
        DTileCount = count;
        return true;
    }

    public int TileCount() {
        return this.DTileCount;
    }

    /**
     * Clears the tile with the specified index.
     *
     * \param index The index of the tile.
     * \return True if the tile was cleared. False if the tile doesn't exist or a tileset is not loaded.
     */
    public boolean ClearTile(int index) {
//        int NumberChannels, RowStride;
//
//        if ((0 > index) || (index >= DTileCount)) {
//            return false;
//        }
//        if (null == DPixbufTileset) {
//            return false;
//        }
//        NumberChannels = gdk_pixbuf_get_n_channels(DPixbufTileset);
//        RowStride = gdk_pixbuf_get_rowstride(DPixbufTileset);
//
//        memset(gdk_pixbuf_get_pixels(DPixbufTileset) + (index * DTileHeight) * RowStride, 0, NumberChannels * DTileHeight * DTileWidth);

        return true;
    }

    /**
     * Overwrites the tile at index 'destindex' with the tile at index 'srcindex'.
     *
     * \param destindex The index of the tile to copy.
     * \param tilename Unused in the source code.
     * \param srcindex The index of the tile to overwrite.
     * \return True if the copy was successful. False if either index is out of range.
     */
    public boolean DuplicateTile(int destindex, String tilename, int srcindex) {
//        char Pixels;
//        int NumberChannels, RowStride;
//
//        if ((0 > srcindex) || (0 > destindex) || (srcindex >= DTileCount) || (destindex >= DTileCount)) {
//            return false;
//        }
//        if (tilename.empty()) {
//            return false;
//        }
//        ClearTile(destindex);
//
//        NumberChannels = gdk_pixbuf_get_n_channels(DPixbufTileset);
//        RowStride = gdk_pixbuf_get_rowstride(DPixbufTileset);
//        Pixels = gdk_pixbuf_get_pixels(DPixbufTileset);
//
//        memcpy(Pixels + (destindex * DTileHeight) * RowStride, Pixels + (srcindex * DTileHeight) * RowStride, NumberChannels * DTileHeight * DTileWidth);

        return true;
    }

    public boolean OrAlphaTile(int destindex, int srcindex) {
        //Pixel manipulation based on a char array?
        //Using pointer math to access pixel locations?
//
//        char PixelSrc, PixelDest;
//        int NumberChannels, RowStride, TilePixels;
//
//        if ((0 > srcindex) || (0 > destindex) || (srcindex >= DTileCount) || (destindex >= DTileCount)) {
//            return false;
//        }
//
//        NumberChannels = gdk_pixbuf_get_n_channels(DPixbufTileset);
//        RowStride = gdk_pixbuf_get_rowstride(DPixbufTileset);
//        PixelSrc = PixelDest = gdk_pixbuf_get_pixels(DPixbufTileset);
//
//        PixelSrc += (srcindex * DTileHeight) * RowStride;
//        PixelDest += (destindex * DTileHeight) * RowStride;
//        TilePixels = DTileHeight * DTileWidth;
//        for (int Index = 0; Index < TilePixels; Index++) {
//            if (0 == PixelSrc[3]) {
//                PixelDest[3] = 0;
//            }
//
//            PixelSrc += NumberChannels;
//            PixelDest += NumberChannels;
//        }
        return true;
    }

    /**
     * Returns the index for the tile named 'tilename', if that tile exists. Otherwise, returns -1.
     *
     * \param tilename The name of the tile.
     * \return If the tile named 'tilename' exists, returns its index. Otherwise -1.
     */
    public int FindTile(String tilename) {
        //Log.d("CGraphicTileset", "before dmapping.get " + tilename);
        Integer tileindex = DMapping.get(tilename);
        //Log.d("CGraphicTileset", "after dmapping.get " + tileindex);
        if (tileindex != null) {

            return tileindex;
        }
        return -1;
    }

    /**
     * Returns the index for the pixel named 'pixelname', if that pixel exists. Otherwise, returns -1.
     *
     * \param pixelname The name of the pixel.
     * \return If the pixel named 'pixelname' exists, returns its index. Otherwise -1.
     */
    public int FindPixel(String pixelname) {
        Integer iterator = DPixelMapping.get(pixelname);
//        Log.d("CGraphicTileset", "aft dmapping.get " + iterator);
        if (iterator != null) {
            return iterator;
        }
//        auto Iterator = DPixelMapping.find(pixelname);
//        if (DPixelMapping.end() != Iterator) {
//            return Iterator -> second;
//        }
        return -1;
    }


    // Function responsible for loading in the tileset.
    // Have left most of the code as is since this seems to do the core of loading from files and
    // may require significant alterations.  Receives only a CDataSource and will have to know
    // how to do everything required with that source before it returns.
    // Uses a few public functions of classes so did not remove prefixes for future reference.

    //Changed Function Parameter!!
    //Turned into CDataSource into an InputStream
    public boolean LoadTileset(CDataSource source) {
        CLineDataSource LineSource = new CLineDataSource(source);
        StringBuffer PNGPath = new StringBuffer("");
        StringBuffer TempString  = new StringBuffer("");

        ArrayList<String> Tokens = new ArrayList<>();

        if (null == source) {
            return false;
        }

        if (!LineSource.Read(PNGPath)) {
            System.out.println("Failed to get path.\n");
            return false;
        }


        if (!LineSource.Read(TempString)) {
            return false;
        }

        try {
            InputStream is = DContext.getResources().openRawResource(DContext.getResources()
                    .getIdentifier(PNGPath.toString(), "drawable", DContext.getPackageName()));
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            BitmapFactory.Options o = new Options();

            o.inScaled = false;

            DTileWidth = decoder.getWidth();
            DTileHeight = decoder.getHeight();

            DTileCount = Integer.parseInt(TempString.toString());
            DTileHeight /= DTileCount;

            DTileHalfWidth = DTileWidth / 2;
            DTileHalfHeight = DTileHeight / 2;
            for (int i = 0; i < DTileCount; i++) {
                mTiles.add(decoder.decodeRegion(new Rect(0, (DTileHeight) * i, DTileWidth,
                        (DTileHeight) * (i + 1)), o));
            }
        } catch (IOException e) {
            Log.d("TD", "IOException ");
            e.printStackTrace();
        }

        for (int Index = 0; Index < DTileCount; Index++) {
            if (!LineSource.Read(TempString)) {
                return false;
            }

            DMapping.put(TempString.toString(), Index);
//            Log.d("CGraphicTileset", TempString.toString() + " " + Index);

        }

        //Clipping Masks, currently unsure of purpose
//        for (int Index = 0; Index < DTileCount; Index++) {
//            GdkBitmap *NewClipMask;
//
//            NewClipMask = gdk_pixmap_new(NULL, DTileWidth, DTileHeight, 1);
//            gdk_pixbuf_render_threshold_alpha(DPixbufTileset, NewClipMask, 0, Index * DTileHeight, 0, 0, DTileWidth, DTileHeight, 1);
//
//            DClippingMasks.push_back(NewClipMask);
//        }
//        DPixelColors.clear();
//        DPixelMapping.clear();
//        PrintDebug(DEBUG_LOW, "Tile Dims %d x %d\n", DTileWidth, DTileHeight);

        //Exit if end of file
        if (!LineSource.Read(TempString)) {
            return true;
        }

        //Otherwise there is special pixel information at the end.  Unsure of function.
        //try{
            DPixelCount = Integer.parseInt(TempString.toString());
//            DPixelColors.add(0,"");
            for (int Index = 0; Index < DPixelCount; Index++) {
//                GdkColor TempColor;
//                std::size_t NextPos;
                if (!LineSource.Read(TempString)) {
                    Log.d("CGraphicTileset", "Failed to read pixel color");
                    return false;
//                    PrintError("Failed to read %d pixel color.\n", Index);
//                    goto LoadTilesetExit;
                }
                String[] tokens = TempString.toString().split(" " );
                //                CTokenizer::Tokenize(Tokens, TempString);
                if (tokens.length != 2) {
                    Log.d("CGraphicTileset", "Failed pixel color does not have 2 tokens)");
                    return false;
//                    PrintError("Failed pixel color %d does not have 2 tokens.\n", Index);
//                    goto LoadTilesetExit;
                }
                String[] hex = tokens[1].split("x");
                DPixelColors.add(Index, "#" + hex[1]);
//                TempColor.pixel = std::stoi(Tokens[1], &NextPos, 16);
//                TempColor.red = (TempColor.pixel>>8) & 0xFF00;
//                TempColor.green = TempColor.pixel & 0xFF00;
//                TempColor.blue = (TempColor.pixel<<8) & 0xFF00;
//                Log.d("potato", "token " + tokens[0] + " dpm " + DPixelColors.size());
                DPixelMapping.put(tokens[0],DPixelColors.size());
//                DPixelColors.push_back(TempColor);
            }
        //}

        //Need java analogue for exception handling
//        catch(Exception E) {
//            Log.d("CGraphicTileset", "can't load pixel");
////            PrintError("%s\n",E.what());
//        }

        return true;
    }

    public Bitmap mTile(int index) {
        if (index < 0) {
            return null;
        } else {
            return mTiles.get(index);
        }

    }

    /**
     * Draws a tile to the drawing surface at the specified position.
     *
     * \param drawable The drawing surface.
     * \param gc The graphics context.
     * \param xPos The x-coordinate to draw the sprite at.
     * \param yPos The y-coordinate to draw the sprite at.
     * \param tileIndex The index of the tile to draw.
     */
    //Original signature modified to take only the canvas object and coordinates
    //(GdkDrawable drawable, GdkGC gc, int xPos, int yPos, int tileIndex)
    public void DrawTile(Canvas canvas, int xPos, int yPos, int tileIndex) {
        if ((0 > tileIndex) || (tileIndex >= DTileCount)) {
            return;
        }

        Rect src = new Rect(0, 0, DTileWidth, DTileHeight);
        Rect dst = new Rect(xPos, yPos, xPos + DTileWidth * DTileScaleFactor, yPos + DTileHeight *
                DTileScaleFactor);

//        canvas.drawBitmap(DPixbufTileset, src, dst, null);
        canvas.drawBitmap(mTiles.get(tileIndex), src, dst, null);

//        gdk_draw_pixbuf(drawable, gc, DPixbufTileset, 0, tileIndex * DTileHeight, xPos, yPos, DTileWidth, DTileHeight, GDK_RGB_DITHER_NONE, 0, 0);
    }
//
//    /**
//     * Draws the top-left pixel of a tile to the drawing surface at the specified position.
//     *
//     * \param drawable The drawing surface.
//     * \param gc The graphics context.
//     * \param xpos The x-coordinate to draw the sprite at.
//     * \param ypos The y-coordinate to draw the sprite at.
//     * \param tileindex The index of the tile to draw.
//     */
    public void DrawTileCorner(Canvas canvas, int xPos, int yPos, int tileIndex) {
        if ((0 > tileIndex) || (tileIndex >= DTileCount)) {
            return;
        }
        Rect src = new Rect(0, 0, DTileWidth, DTileHeight);
        Rect dst = new Rect(xPos, yPos, xPos + DTileWidth * DTileScaleFactor, yPos + DTileHeight *
                DTileScaleFactor);

//        canvas.drawBitmap(DPixbufTileset, src, dst, null);
        canvas.drawBitmap(mTiles.get(tileIndex), src, dst, null);
    }
//
//    /**
//     * Fills a rectangular area of the drawing surface using with a tile.
//     *
//     * \param drawable The drawing surface.
//     * \param gc The graphics context.
//     * \param xpos The x-coordinate of the top-left corner of the rectangle.
//     * \param ypos The y-coordinate of the top-left corner of the rectangle.
//     * \param width The width of the rectangular area.
//     * \param height The height of the rectangular area.
//     * \param tileindex The index of the tile to draw.
//     */
//    public void DrawTileRectangle(GdkDrawable drawable, int xpos, int ypos, int width, int height, int tileindex) {
//        if ((0 > tileindex) || (tileindex >= DTileCount)) {
//            return;
//        }
//        GdkGC TempGC = gdk_gc_new(drawable);
//        GdkRectangle TempRect({xpos, ypos, width, height});
//        int MaxXPos = xpos + width;
//        int MaxYPos = ypos + height;
//        int TileOffset = tileindex * DTileHeight;
//
//        gdk_gc_set_clip_rectangle(TempGC, &TempRect);
//        for (int YPos = ypos; YPos < MaxYPos; YPos += DTileHeight) {
//            for (int XPos = xpos; XPos < MaxXPos; XPos += DTileWidth) {
//                gdk_draw_pixbuf(drawable, TempGC, DPixbufTileset, 0, TileOffset, XPos, YPos, DTileWidth, DTileHeight, GDK_RGB_DITHER_NONE, 0, 0);
//            }
//        }
//        g_object_unref(TempGC);
//    }
//
//    /**
//     * Draws a solid color to the drawing surface that is masked by a tile's clipping mask.
//     *
//     * \param drawable The drawing surface.
//     * \param gc The graphics context.
//     * \param xpos The x-coordinate to draw the sprite at.
//     * \param ypos The y-coordinate to draw the sprite at.
//     * \param tileindex The index of the tile to use as a clipping mask.
//     * \param color The color to fill the masked region.
//     */
//    public void DrawClipped(GdkDrawable drawable, GdkGC gc, gint xpos, gint ypos, int tileindex, int color) {
//        GdkColor ClipColor;
//
//        ClipColor.pixel = color;
//        ClipColor.red = (color>>8) & 0xFF00;
//        ClipColor.green = color & 0xFF00;
//        ClipColor.blue = (color<<8) & 0xFF00;
//
//        if ((0 > tileindex) || (tileindex >= DTileCount)) {
//            return;
//        }
//        gdk_gc_set_clip_mask(gc, DClippingMasks[tileindex]);
//        gdk_gc_set_clip_origin(gc, xpos, ypos);
//        gdk_gc_set_rgb_fg_color(gc, ClipColor);
//        gdk_gc_set_rgb_bg_color(gc, ClipColor);
//
//        gdk_draw_rectangle(drawable, gc, true, xpos, ypos, DTileWidth, DTileHeight);
//    }
//
//    /**
//     * Draws a square to the drawing surface using a pixel color.
//     *
//     * \param drawable The drawing surface.
//     * \param gc The graphics context.
//     * \param xpos The x-coordinate of the top-left corner of the square.
//     * \param ypos The y-coordinate of the top-left corner of the square.
//     * \param size The side length of the square.
//     * \param pixelindex The index of the pixel color to use.
//     */
    public void DrawPixel(Canvas canvas, float xpos, float ypos, float size, int pixelindex) {
        Paint paint = new Paint();
        if ((0 > pixelindex) || (pixelindex >= DPixelCount)) {
            return;
        }
        if (0 >= size) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(DPixelColors.get(pixelindex)));

        canvas.drawRect(xpos, ypos, (size + xpos), (size + ypos), paint);
//        gdk_gc_set_rgb_fg_color(gc, DPixelColors[pixelindex]);
//        gdk_gc_set_rgb_bg_color(gc, DPixelColors[pixelindex]);
//
//        gdk_draw_rectangle(drawable, gc, true, xpos, ypos, size, size);
    }
}
