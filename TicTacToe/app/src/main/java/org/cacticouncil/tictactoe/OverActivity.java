package org.cacticouncil.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


public class OverActivity extends Activity implements View.OnClickListener
{
    private TextView            tvWinner;
    private TextView            tvPlayAgain;
    private MainActivity.Winner mWinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        // read startup params
        Bundle b = getIntent().getExtras();
        mWinner = MainActivity.Winner.values()[b.getInt("winner")];

        tvWinner    = (TextView)findViewById(R.id.tvWinner);
        tvPlayAgain = (TextView)findViewById(R.id.tvPlayAgain);
        tvPlayAgain.setOnClickListener(this);

        if (mWinner == MainActivity.Winner.DRAW)
            tvWinner.setText("Draw!");
        else
            tvWinner.setText("Player " + ((mWinner == MainActivity.Winner.X) ? "X" : "O") + " wins!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.over, menu);
        return true;
    }

    @Override
    public void onClick(View v)
    {
        if (v == tvPlayAgain)
        {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            finish();
        }
    }
}
