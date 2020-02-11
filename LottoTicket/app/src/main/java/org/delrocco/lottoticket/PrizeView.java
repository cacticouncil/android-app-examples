package org.delrocco.lottoticket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class PrizeView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
    public enum PrizeValue
    {
        WIN,
        LOSE,
        DRAW
    }

    private MainActivity  mActivity;
    private SurfaceHolder mHolder;
    private Rect          mDimensions;
    private Rect          mPlacement;
    private Point         mCentroid;
    private Bitmap        mBMPPrize;
    private Bitmap        mBMPResin;
    private Paint         mAlphaPaint;
    private Paint         mTextPaint;
    private PrizeValue    mPrizeValue;
    private int           mTouchRadius;
    private int           mResinPixels;
    private int[]         mPixelBuffer;

    public PrizeView(Context context)
    {
        super(context);
        initialize(context);
    }

    public PrizeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize(context);
    }

    public PrizeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void initialize(Context context)
    {
        getHolder().addCallback(this);
        setOnTouchListener(this);
        setZOrderOnTop(true);

        mActivity = (MainActivity)context;
        mDimensions = new Rect();
        mPlacement = new Rect();
        mCentroid = new Point();
        mTouchRadius = 16;
        mPrizeValue = PrizeValue.LOSE;
        mResinPixels = 0;

        mAlphaPaint = new Paint();
        mAlphaPaint.setStyle(Paint.Style.FILL);
        mAlphaPaint.setARGB(255, 0, 0, 0);
        mAlphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        mTextPaint = new Paint();
        mTextPaint.setARGB(255,64,64,64);
        mTextPaint.setTextSize(10f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mHolder = holder;
        mHolder.setFormat(PixelFormat.TRANSPARENT);

        mBMPPrize = BitmapFactory.decodeResource(getResources(), R.drawable.scratchoff_prize);

        Bitmap bmpResinOrig = BitmapFactory.decodeResource(getResources(), R.drawable.scratchoff_resin);
        mBMPResin = Bitmap.createBitmap(bmpResinOrig.getWidth(), bmpResinOrig.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(mBMPResin);
        can.drawBitmap(bmpResinOrig,null,new Rect(0,0,bmpResinOrig.getWidth(),bmpResinOrig.getHeight()),null);

        Canvas c = mHolder.lockCanvas();

        mDimensions.set(0,0,c.getWidth(),c.getHeight());
        mCentroid.set(mDimensions.width()/2, mDimensions.height()/2);

        mBMPPrize = scaleBitmap(mBMPPrize, ((float)mDimensions.width())/(float)mBMPPrize.getWidth());
        mBMPResin = scaleBitmap(mBMPResin, ((float)mDimensions.width())/(float)mBMPResin.getWidth());

        mPlacement.set(0,0,mBMPPrize.getWidth(),mBMPPrize.getHeight());
        mPlacement.offset(mCentroid.x-(mBMPPrize.getWidth()/2), mCentroid.y-(mBMPPrize.getHeight()/2));

        // scalers
        mTouchRadius = mBMPResin.getWidth()/10;
        mTextPaint.setTextSize(((float)mBMPResin.getHeight())/4f);

        mPixelBuffer = new int[mBMPResin.getWidth()*mBMPResin.getHeight()];
        mResinPixels = 0;
        mBMPResin.getPixels(mPixelBuffer, 0, mBMPResin.getWidth(), 0,0, mBMPResin.getWidth(),mBMPResin.getHeight());
        for (int i=0; i<mPixelBuffer.length; i++)
            if (((mPixelBuffer[i] >> 24) & 0xFF) > 0)
                mResinPixels++;

        draw(c);
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
    protected void onDraw(Canvas c)
    {
        c.drawBitmap(mBMPPrize, null, mPlacement, null);

        switch (mPrizeValue)
        {
            case LOSE:
                c.drawText("Abort!", mCentroid.x, mCentroid.y, mTextPaint);
                break;

            case WIN:
                c.drawText("$1000", mCentroid.x, mCentroid.y, mTextPaint);
                break;

            case DRAW:
                c.drawText("$2", mCentroid.x, mCentroid.y, mTextPaint);
                break;

            default: break;
        }

        c.drawBitmap(mBMPResin, null, mPlacement, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // ignore input under these conditions, set selected prize if unset
        if (mActivity.isGameOver())
            return true;
        else if (!mActivity.hasPrize())
            mActivity.setPrize(this);
        else if (mActivity.getPrize() != this)
            return true;

        // we only care about up, down, and move events
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
            event.getAction() == MotionEvent.ACTION_UP   ||
            event.getAction() == MotionEvent.ACTION_MOVE )
        {
            Point p = new Point((int)event.getX(), (int)event.getY());

            // if inside bounds of resin image
            if (p.x >= mPlacement.left  &&
                p.x <= mPlacement.right &&
                p.y >= mPlacement.top   &&
                p.y <= mPlacement.bottom)
            {
                //Log.i("PrizeView", "get("+event.getX()+","+event.getY()+") raw("+event.getRawX()+","+event.getRawY()+") place("+mPlacement.left+","+mPlacement.top+")");
                Canvas can = new Canvas(mBMPResin);
                can.drawCircle(event.getX()-mPlacement.left, event.getY()-mPlacement.top, mTouchRadius, mAlphaPaint);

                Canvas c = mHolder.lockCanvas();
                draw(c);
                mHolder.unlockCanvasAndPost(c);
            }

            // if finished move or move outside bounds of view
            //if (event.getAction() == MotionEvent.ACTION_UP ||
            //   (event.getAction() == MotionEvent.ACTION_MOVE &&
            //   (p.x <= getLeft()  ||
            //    p.x >= getRight() ||
            //    p.y <= getTop()   ||
            //    p.y >= getBottom())))
            //{
                int numResinPixels = 0;
                mBMPResin.getPixels(mPixelBuffer, 0, mPlacement.width(), 0,0, mPlacement.width(), mPlacement.height());
                for (int i=0; i<mPixelBuffer.length; i++)
                    if (((mPixelBuffer[i] >> 24) & 0xFF) > 0)
                        numResinPixels++;

                // check for game over condition
                if ((float)numResinPixels/(float)mResinPixels <= 0.4f)
                    mActivity.setGameOver();
            //}
        }

        // consume these events, so Android will send MOVE events
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
            event.getAction() == MotionEvent.ACTION_UP   ||
            event.getAction() == MotionEvent.ACTION_CANCEL)
        {
            return true;
        }

        return false;
    }

    public void setPrizeValue(PrizeValue val)
    {
        mPrizeValue = val;
    }

    public PrizeValue getPrizeValue()
    {
        return mPrizeValue;
    }

    private Bitmap scaleBitmap(Bitmap bm, float scale)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
        return newbm;
    }
}
