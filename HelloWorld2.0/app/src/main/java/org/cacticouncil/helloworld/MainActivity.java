package org.cacticouncil.helloworld;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    private static final String LIFECYCLE = "Lifecycle Event";
    private static final int RC_ADD_EVENT = 1;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(LIFECYCLE, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivityForResult(intent, RC_ADD_EVENT);
            }
        });
    }

    @Override
    protected void onStart()
    {
        Log.d(LIFECYCLE, "onStart");

        super.onStart();
    }

    @Override
    protected void onResume()
    {
        Log.d(LIFECYCLE, "onResume");

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Log.d(LIFECYCLE, "onPause");

        super.onPause();
    }

    @Override
    protected void onStop()
    {
        Log.d(LIFECYCLE, "onStop");

        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.d(LIFECYCLE, "onDestroy");

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_CANCELED)
            return;

        if (requestCode == RC_ADD_EVENT)
        {
            String event = data.getStringExtra("event");
            Date date = new Date(data.getLongExtra("date", -1));

            String message = "Added: " + event + " on " + date.toString();
            Snackbar.make(fab, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
