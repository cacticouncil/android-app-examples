package org.cacticouncil.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class MenuActivity extends Activity implements View.OnClickListener
{
    private TextView tvMenuPvP;
    private TextView tvMenuPvC;
    private TextView tvMenuCvC;
    private TextView tvMenuDifficulty;
    private TextView tvMenuCredits;
    private MainActivity.GameDiff mDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvMenuPvP        = (TextView)findViewById(R.id.tvMenuPvP);
        tvMenuPvC        = (TextView)findViewById(R.id.tvMenuPvC);
        tvMenuCvC        = (TextView)findViewById(R.id.tvMenuCvC);
        tvMenuDifficulty = (TextView)findViewById(R.id.tvMenuDifficulty);
        tvMenuCredits    = (TextView)findViewById(R.id.tvMenuCredits);

        tvMenuPvP.setOnClickListener(this);
        tvMenuPvC.setOnClickListener(this);
        tvMenuCvC.setOnClickListener(this);
        tvMenuDifficulty.setOnClickListener(this);
        tvMenuCredits.setOnClickListener(this);

        mDiff = MainActivity.GameDiff.HARD;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v)
    {
        MainActivity.GameMode mode = null;

        if (v == tvMenuPvP)
            mode = MainActivity.GameMode.PVP;
        else if (v == tvMenuPvC)
            mode = MainActivity.GameMode.PVC;
        else if (v == tvMenuCvC)
            mode = MainActivity.GameMode.CVC;
        else if (v == tvMenuDifficulty)
        {
            if (tvMenuDifficulty.getText().toString().endsWith("Hard"))
            {
                tvMenuDifficulty.setText("Difficulty: Easy");
                mDiff = MainActivity.GameDiff.EASY;
            }
            else
            {
                tvMenuDifficulty.setText("Difficulty: Hard");
                mDiff = MainActivity.GameDiff.HARD;
            }
        }
        else if (v == tvMenuCredits)
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

        if (mode != null)
        {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("mode", mode.ordinal());
            i.putExtra("diff", mDiff.ordinal());
            startActivity(i);
            finish();
        }
    }
}
