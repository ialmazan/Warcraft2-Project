package com.ecs160.nittacraft.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.activities.BaseActivity;

public class SideBarView extends SurfaceView {
    private static SideBarView sSideBarView;
    public float firstX = -1;
    public float firstY = -1;

    private SideBarView(Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    public static synchronized SideBarView getInstance(Context context) {
        if (sSideBarView == null) {
            sSideBarView = new SideBarView(context);
            BaseActivity.MainData.InitializeSideView();
        }

        return sSideBarView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = (event.getAction() & MotionEvent.ACTION_MASK);
        float xScale = 0;
        float yScale = 0;
        float newX = 0;
        float newY = 0;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                    firstX = event.getX(0);
                    firstY = event.getY(0);
                    try {
                        xScale = (96 * 64 - GameView.getInstance().getWidth()) / sSideBarView
                                .getWidth();
                        yScale =  (64 * 64 - GameView.getInstance().getHeight()) /sSideBarView.getHeight();
                        newX = firstX * xScale;
                        newY = firstY * yScale;
                        if (newX > 96 * CPosition.TileWidth() - GameView.getInstance().getWidth()
                                - 96 && newY > (64 * CPosition.TileHeight()) / 2 - 64) {
                            GameView.getInstance().movX = (96 * CPosition.TileWidth() - GameView.getInstance().getWidth());
                            GameView.getInstance().movY = ((64 * CPosition.TileHeight()) -
                                    GameView.getInstance().getHeight());
                        } else {
                            GameView.getInstance().movX = (newX);
                            GameView.getInstance().movY = (newY);
                        }
                    }
                    catch (Exception e) {
                        Log.e("Jump From Minimap", e.getMessage());
                    }
                break;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        BaseActivity.MainData.RenderSideBar(canvas);
    }
}