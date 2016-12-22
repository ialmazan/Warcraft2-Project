package com.ecs160.nittacraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Decode bitmap strips to images
 */

public class Tileset {
    private static Tileset sInstance;

    private Tileset() {}

    public static Tileset getInstance() {
        if (sInstance == null) {
            sInstance = new Tileset();
        }

        return sInstance;
    }

    private ArrayList<Bitmap> getTiles(int resource, Context context) {
        ArrayList<Bitmap> tiles = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(resource);
        try {
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            for (int i = 0; i < decoder.getHeight() / decoder.getWidth(); i++) {
                Bitmap tile = decoder.decodeRegion(new Rect(0, decoder.getWidth() * i, decoder
                        .getWidth(), decoder.getWidth() * (i + 1) ), options);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
