package com.ecs160.nittacraft.renderers;

import android.graphics.Canvas;

public class CMiniMapRenderer{
    protected CMapRenderer DMapRenderer;
    protected CAssetRenderer DAssetRenderer;
    protected CFogRenderer DFogRenderer;
    protected CViewportRenderer DViewportRenderer;
    protected int DVisibleWidth;
    protected int DVisibleHeight;

    public CMiniMapRenderer(CMapRenderer mapRenderer, CAssetRenderer assetRender, CFogRenderer
            fogRender, CViewportRenderer viewport) {
        DMapRenderer = mapRenderer;
        DAssetRenderer = assetRender;
        DFogRenderer = fogRender;
        DViewportRenderer = viewport;

        DVisibleWidth = DMapRenderer.MapWidth();
        DVisibleHeight = DMapRenderer.MapHeight();
    }

    public int VisibleWidth() {
        return DVisibleWidth;
    }

    public int VisibleHeight() {
        return DVisibleHeight;
    }

    public void DMapRenderer(CMapRenderer maprender) {
        DMapRenderer = maprender;
    }

    public void DAssetRenderer(CAssetRenderer assetrender) {
        DAssetRenderer = assetrender;
    }

    public void DFogRenderer(CFogRenderer fogrender) {
        DFogRenderer = fogrender;
    }

    public void DViewportRenderer(CViewportRenderer viewport) {
        DViewportRenderer = viewport;
    }

    public void DrawMiniMap(Canvas canvas) {
        int MiniMapWidth = canvas.getWidth();
        int MiniMapHeight = canvas.getHeight();
        int MMW_MH = MiniMapWidth * DMapRenderer.MapHeight();
        int MMH_MW = MiniMapHeight * DMapRenderer.MapWidth();

        if (MMH_MW > MMW_MH) {
            DVisibleWidth = MiniMapWidth;
            DVisibleHeight = (DMapRenderer.MapHeight() * MiniMapWidth) / DMapRenderer.MapWidth();
        } else if (MMH_MW < MMW_MH) {
            DVisibleWidth = (DMapRenderer.MapWidth() * MiniMapHeight) / DMapRenderer.MapHeight();
            DVisibleHeight = MiniMapHeight;
        } else {
            DVisibleWidth = MiniMapWidth;
            DVisibleHeight = MiniMapHeight;
        }

        DMapRenderer.DrawMiniMap(canvas);
        /*
        DAssetRenderer.DrawMiniAssets(canvas);
        if (DFogRenderer != null) {
            DFogRenderer.DrawMiniMap(canvas);
        }
        */
    }
}

/*
public class CMiniMapRenderer{

    //#include "ViewportRenderer.h"

    //data members
    protected CMapRenderer DMapRenderer;
    protected CAssetRenderer DAssetRenderer;
    protected CFogRenderer DFogRenderer;
    protected CViewportRenderer DViewportRenderer;

    protected Bitmap.Config config; //FIXME
    protected Canvas DWorkingPixBuf;
    protected Bitmap DWorkingPixMap;
    //protected GdkPixmap DWorkingPixmap;
    //protected GdkPixbuf DWorkingPixbuf; //can't draw on
    //protected GdkColor DViewportColor;
    protected int red,green,blue;

    protected int DVisibleWidth;
    protected int DVisibleHeight;
    //methods
    public CMiniMapRenderer(CMapRenderer maprender, CAssetRenderer assetrender, CFogRenderer fogrender, CViewportRenderer viewport, int depth)
    {
        //GdkColor ColorBlack = {0,0,0,0};
        //GdkGC TempGC;
        DMapRenderer = maprender;
        DAssetRenderer = assetrender;
        DFogRenderer = fogrender;
        DViewportRenderer = viewport;
        //DWorkingPixbuf = null;
        // TODO: Create DCViewportColor class that extends Paint with the following public var
        //DViewportColor.pixel = 0xFFFFFF;
        //DViewportColor.red = 0xFFFF;
        //DViewportColor.green = 0xFFFF;
        //DViewportColor.blue = 0xFFFF;
        red = 255;
        green = 255;
        blue = 255;

        DVisibleWidth = DMapRenderer.MapWidth();
        DVisibleHeight = DMapRenderer.MapHeight();

        //DWorkingPixmap = gdk_pixmap_new(null, DMapRenderer.MapWidth(), DMapRenderer.MapHeight(), depth);
        DWorkingPixMap = DWorkingPixMap.createBitmap(DMapRenderer.MapWidth(),DMapRenderer.MapHeight(),config);

        //TempGC = gdk_gc_new(DWorkingPixmap);

        //FIXME  canvas does not have foreground vs background specific RGB setter
        //gdk_gc_set_rgb_fg_color(TempGC, ColorBlack);
        // gdk_gc_set_rgb_bg_color(TempGC, ColorBlack);
        //gdk_draw_rectangle(DWorkingPixmap, TempGC, true, 0, 0, DMapRenderer.MapWidth(), DMapRenderer.MapHeight());
        Paint myPaint = new Paint();
        myPaint.setColor(Color.BLACK);
        myPaint.setStrokeWidth(1);
        DWorkingPixBuf.drawRect(DMapRenderer.MapWidth(),DMapRenderer.MapWidth(),DMapRenderer.MapHeight(),DMapRenderer.MapHeight(),myPaint);
        //g_object_unref(TempGC);
    }

    //public int ViewportColor()
    //{
    //    return DViewportColor.pixel;
    //}

    //public int ViewportColor(int color)
    //{
    //    return DViewportColor.pixel = color;
    //}


    public int VisibleWidth()
    {
        return DVisibleWidth;
    }

    public int VisibleHeight()
    {
        return DVisibleHeight;
    }


    void DrawMiniMap(Canvas drawable) {
//        GdkPixbuf *ScaledPixbuf;
        Paint TempGC = new Paint();
        int MiniMapViewportX, MiniMapViewportY;
        int MiniMapViewportWidth, MiniMapViewportHeight;
        int MiniMapWidth, MiniMapHeight;
        int MMW_MH, MMH_MW;

        MiniMapWidth = drawable.getWidth();
        MiniMapHeight = drawable.getHeight();

        MMW_MH = MiniMapWidth * DMapRenderer.MapHeight();
        MMH_MW = MiniMapHeight * DMapRenderer.MapWidth();

        if (MMH_MW > MMW_MH) {
            DVisibleWidth = MiniMapWidth;
            DVisibleHeight = (DMapRenderer.MapHeight() * MiniMapWidth) / DMapRenderer.MapWidth();
        }
        else if (MMH_MW < MMW_MH) {
            DVisibleWidth = (DMapRenderer.MapWidth() * MiniMapHeight) / DMapRenderer.MapHeight();
            DVisibleHeight = MiniMapHeight;
        }
        else {
            DVisibleWidth = MiniMapWidth;
            DVisibleHeight = MiniMapHeight;
        }

        DMapRenderer.DrawMiniMap(drawable);
        DAssetRenderer.DrawMiniAssets(drawable, DMapRenderer.getScale());
        if (null != DFogRenderer) {
            DFogRenderer.DrawMiniMap(drawable);
        }

//        DWorkingPixbuf = gdk_pixbuf_get_from_drawable(DWorkingPixbuf, DWorkingPixmap, nullptr, 0, 0, 0, 0, -1, -1);

//        ScaledPixbuf = gdk_pixbuf_scale_simple(DWorkingPixbuf, DVisibleWidth, DVisibleHeight, GDK_INTERP_BILINEAR);
//        gdk_draw_pixbuf(drawable, TempGC, ScaledPixbuf, 0, 0, 0, 0, -1, -1, GDK_RGB_DITHER_NONE, 0, 0);
//        g_object_unref(ScaledPixbuf);

        if (null != DViewportRenderer) {
            // DViewportColor - sets the color in original LINUX
            TempGC.setARGB(1, red, green, blue);
            MiniMapViewportX = (DViewportRenderer.ViewportX() * DVisibleWidth) / DMapRenderer.DetailedMapWidth();
            MiniMapViewportY = (DViewportRenderer.ViewportY() * DVisibleHeight) / DMapRenderer.DetailedMapHeight();
            MiniMapViewportWidth = (DViewportRenderer.LastViewportWidth() * DVisibleWidth) / DMapRenderer.DetailedMapWidth();
            MiniMapViewportHeight = (DViewportRenderer.LastViewportHeight() * DVisibleHeight) / DMapRenderer.DetailedMapHeight();
            drawable.drawRect(MiniMapViewportX, MiniMapViewportY, MiniMapViewportWidth, MiniMapViewportHeight, TempGC);

        }
    }
}*/
