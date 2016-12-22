package com.ecs160.nittacraft.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ecs160.nittacraft.CApplicationData;
import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.activities.BaseActivity;

public class GameView extends SurfaceView {
    private static GameView sGameView;
    public static CApplicationData DApplicationData = BaseActivity.MainData;

    public float movX = 0;
    public float movY = 0;

    int fingerCount;
    public float firstX = -1;
    public float firstY = -1;
    public float secX = -1;
    public float secY = -1;
    float touchThreshold = 64;
    float touchDistance = 0;
    public boolean pinching = false;

    public static synchronized GameView getInstance(Context context) {
        if (sGameView == null) {
            sGameView = new GameView(context);
        }

        return sGameView;
    }

    public static GameView getInstance() throws Exception {
        if (sGameView == null) {
            throw new Exception("sGameView is null!");
        }

        return sGameView;
    }

    public GameView(Context context) {
        super(context);
        this.setWillNotDraw(false);

        SurfaceHolder holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                /*
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
                */
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                DApplicationData.InitializeMapView();
                /*
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                */
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pinching = false;
        int action = (event.getAction() & MotionEvent.ACTION_MASK);

        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                fingerCount++;
                if (fingerCount == 1) {
                    firstX = event.getX(0);
                    firstY = event.getY(0);
//                    DApplicationData.ProcessInputGameMode(this);
                    DApplicationData.ProcessInput(this);
                    //DApplicationData.CalculateGameMode();
                }
                if (fingerCount == 2) {
                    secX = event.getX(1);
                    secY = event.getY(1);

                    touchDistance = distance(event, 0, 1);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                fingerCount--;
                if (fingerCount < 2) {
                    secX = -1;
                    secY = -1;
                }
                if (fingerCount < 1) {
                    firstX = -1;
                    firstY = -1;

                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("GameView", "I'm panning!");
                boolean isScrolling = isScrollGesture(event, 0, firstX, firstY);

                if (fingerCount > 1) { //) && isPinchGesture(event)) {
                    pinching = true;
                    firstX = event.getX(0);
                    firstY = event.getY(0);
                    secX = event.getX(1);
                    secY = event.getY(1);
                    DApplicationData.ProcessInput(this);
                } else if (fingerCount == 1 && isScrolling) {
                    isScrollDist(event, 0, firstX, firstY);
//                    textView.setText("scrolling");
                }
                break;
        }
        return true;
    }

    public float distance(MotionEvent event, int first, int second) {
        if (event.getPointerCount() >= 2) {
            final float x = event.getX(first) - event.getX(second);
            final float y = event.getY(first) - event.getY(second);

            return (float)Math.sqrt(x*x + y*y);
        } else {
            return 0;
        }
    }

    private void isScrollDist(MotionEvent event, int ptrIndex, float originalX, float originalY) {

        float distX = (event.getX(ptrIndex) - originalX);
        float distY = (event.getY(ptrIndex) - originalY);

        if (distX > 45) {
            distX = 45;
        }
        if (distX < -45) {
            distX = -45;
        }
        movX -= distX;
        if (distY > 45) {
            distY = 45;
        }
        if (distY < -45) {
            distY = -45;
        }
        movY -= distY;

        if (movX < 0) {
            movX = 0;
        }

        if (movY < 0) {
            movY = 0;
        }

        if (movX > (96 * CPosition.TileWidth() - getWidth())) {
            movX = 96 * CPosition.TileWidth() - getWidth();
        }

        if (movY > (64 * CPosition.TileHeight() - getHeight())) {
            movY = 64 * CPosition.TileHeight() - getHeight();
        }
    }

    private boolean isScrollGesture(MotionEvent event, int ptrIndex, float originalX, float
            originalY) {
        float moveX = Math.abs(event.getX(ptrIndex) - originalX);
        float moveY = Math.abs(event.getY(ptrIndex) - originalY);
        return moveX > touchThreshold || moveY > touchThreshold;
    }

    private boolean isPinchGesture(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            final float currDistance = distance(event, 0, 1);
            final float diffFirstX = firstX - event.getX(0);
            final float diffFirstY = firstY - event.getY(0);
            final float diffSecX = secX - event.getX(1);
            final float diffSecY = secY - event.getY(1);

            if (Math.abs(currDistance - touchDistance) > touchThreshold && (diffFirstY * diffSecY
                    <= 0) && (diffFirstX*diffSecX <= 0)) {
                return true;
            }
        }

        return false;
    }

    private void drawPinchRect(float x1, float y1, float x2, float y2, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0, 150, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(x1,y1,x2,y2,paint);
    }
//        float x = motionEvent.getX();
//        float y = motionEvent.getY();
//
//        int ix = (int)x;
//        int iy = (int)y;
//
//        //1-9 tile clicked in tictactoe grid
//        int tile;
//
//        tile = ix / (this.getWidth() / 3) + 1;
//
//        tile = tile + (iy / (this.getHeight() / 3)) * 3;
//
//        //Updates set of frames sprite is using for animation based on touch location
//        switch (tile) {
//            case 1:
//                sprite.startFrame = 35;
//                sprite.endFrame = 39;
//                break;
//            case 2:
//                sprite.startFrame = 0;
//                sprite.endFrame = 4;
//                break;
//            case 3:
//                sprite.startFrame = 5;
//                sprite.endFrame = 9;
//                break;
//            case 4:
//                sprite.startFrame = 30;
//                sprite.endFrame = 34;
//                break;
//            case 5:
//                sprite.startFrame = sprite.endFrame;
//                break;
//            case 6:
//                sprite.startFrame = 10;
//                sprite.endFrame = 14;
//                break;
//            case 7:
//                sprite.startFrame = 25;
//                sprite.endFrame = 29;
//                break;
//            case 8:
//                sprite.startFrame = 20;
//                sprite.endFrame = 24;
//                break;
//            case 9:
//                sprite.startFrame = 15;
//                sprite.endFrame = 19;
//                break;
//        }
//        return true;

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        DApplicationData.RenderGameMap(canvas);
        if (pinching) { // draw rectangle for selecting multiple units
//            Log.d("multiple", "drawing rectangle " + firstX + " " + firstY + " " + secX + " " + secY);
            drawPinchRect(firstX, firstY, secX, secY, canvas);
        }

    }
}