package org.cacticouncil.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class XOView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
    private MainActivity  mGame;
    private SurfaceHolder mHolder;
    private byte          mValue;
    private Paint         mPaintToken;
    private Paint         mPaintText;
    private Rect          mFieldDim;
    private int           mLocation;

    public XOView(Context context)
    {
        super(context);
        initialize(context);
    }

    public XOView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize(context);
    }

    public XOView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context)
    {
        getHolder().addCallback(this);
        setOnTouchListener(this);

        mGame = (MainActivity)context;

        mValue = 0;
        mFieldDim = new Rect();

        mPaintToken = new Paint();
        mPaintToken.setStyle(Paint.Style.STROKE);
        mPaintToken.setARGB(255,32,32,32);
        mPaintToken.setStrokeWidth(10);

        mPaintText = new Paint();
        mPaintText.setARGB(255,32,32,32);
        mPaintText.setStrokeWidth(4f);
        mPaintText.setTextSize(25f);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mHolder = holder;

        // capture view dimensions and do initial draw
        Canvas c = mHolder.lockCanvas();
        mFieldDim.set(0,0,c.getWidth(),c.getHeight());
        myDraw(c);
        mHolder.unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }

    @Override
    public void onDraw(Canvas c)
    {
        myDraw(c);
    }

    private void myDraw(Canvas c)
    {
        // clear cell
        c.drawColor(Color.WHITE);

        // draw player token
        //mValue = (byte)'x';
        if (mValue != 0)
        {
            float radius = mFieldDim.width()/4f;

            switch (mValue)
            {
                case 'x':
                    float left   = mFieldDim.centerX() - radius;
                    float right  = mFieldDim.centerX() + radius;
                    float top    = mFieldDim.centerY() - radius;
                    float bottom = mFieldDim.centerY() + radius;
                    c.drawLine(left,top,right,bottom,mPaintToken);
                    c.drawLine(right,top,left,bottom,mPaintToken);
                    break;

                case 'o':
                    c.drawCircle(mFieldDim.centerX(), mFieldDim.centerY(), radius, mPaintToken);
                    break;

                default:
                    break;
            }
        }

        // draw cell location
        c.drawText("" + mLocation, 5, 27, mPaintText);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // ignore touch if already has a value
        if (mValue != 0) return true;

        // ignore touch input on computer turn
        if ((mGame.getGameMode() == MainActivity.GameMode.CVC) ||
            (mGame.getGameMode() == MainActivity.GameMode.PVC) && !mGame.isPlayerX())
        {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            select();
        }

        return true;
    }

    public void setLocation(int loc)
    {
        mLocation = loc;
    }

    public void select()
    {
        mValue = (byte)(mGame.isPlayerX() ? 'x' : 'o');

        Canvas c = mHolder.lockCanvas();
        myDraw(c);
        mHolder.unlockCanvasAndPost(c);

        mGame.endTurn(mLocation, mValue);

//        dispatchTouchEvent(MotionEvent.obtain(
//                SystemClock.uptimeMillis(),
//                SystemClock.uptimeMillis(),
//                MotionEvent.ACTION_DOWN,
//                0,0,0));
    }
}
