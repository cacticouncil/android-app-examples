package org.cacticouncil.lottoticket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;
import java.util.Vector;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Vector<PrizeView> mPrizes;
    private PrizeView         mPrize;
    private boolean           mGameOver;
    private MediaPlayer       mMusic;
    private Button            mBtnReset;
    private Button            mBtnCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnReset = (Button)findViewById(R.id.btnReset);
        mBtnReset.setOnClickListener(this);
        mBtnCredits = (Button)findViewById(R.id.btnCredits);
        mBtnCredits.setOnClickListener(this);

        getSupportActionBar().hide();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mPrize = null;
        mPrizes = new Vector<PrizeView>();
        findPrizeViews((ViewGroup)getWindow().getDecorView().getRootView(), mPrizes);
        for (int i=0; i<10; i++)
        {
            int idx = (new Random()).nextInt(mPrizes.size());
            PrizeView p = mPrizes.get(0);
            mPrizes.set(0, mPrizes.get(idx));
            mPrizes.set(idx, p);
        }
        for (int i=0; i<mPrizes.size(); i++)
        {
            mPrizes.get(i).setPrizeValue(PrizeView.PrizeValue.values()[i%3]);
        }

        mGameOver = false;
        mMusic = MediaPlayer.create(this, R.raw.sfx_win);
    }

    @Override
    protected void onStop()
    {
        mMusic.stop();
        mMusic.release();

        super.onStop();
    }

    @Override
    public void onClick(View v)
    {
        if (v == mBtnCredits)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Brought to you by:");
            dialog.setMessage("Cacti Council\nwww.cacticouncil.org");
            dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else if (v == mBtnReset)
        {
            restartActivity();
        }
    }

    public boolean hasPrize()
    {
        return (mPrize != null);
    }

    public PrizeView getPrize()
    {
        return mPrize;
    }

    public void setPrize(PrizeView p)
    {
        mPrize = p;
    }

    public void setGameOver()
    {
        mGameOver = true;

        switch (mPrize.getPrizeValue())
        {
            case WIN:
                mMusic = MediaPlayer.create(this, R.raw.sfx_win);
                break;

            case LOSE:
                mMusic = MediaPlayer.create(this, R.raw.sfx_lose);
                break;

            default:
            case DRAW:
                mMusic = MediaPlayer.create(this, R.raw.sfx_draw);
                break;
        }

        mMusic.start();
    }

    public boolean isGameOver()
    {
        return mGameOver;
    }

    public void findPrizeViews(ViewGroup parent, Vector<PrizeView> list)
    {
        View child = null;
        int count = parent.getChildCount();
        for (int i=0; i<count; i++)
        {
            child = parent.getChildAt(i);
            if (child instanceof PrizeView)
                list.add((PrizeView) child);
            else if (child instanceof ViewGroup)
                findPrizeViews((ViewGroup)child, list);
        }
    }

    public void restartActivity()
    {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
