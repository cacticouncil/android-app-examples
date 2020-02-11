package org.cacticouncil.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Date;
import java.util.GregorianCalendar;

public class EventActivity extends AppCompatActivity
{
    private EditText txtEventName;
    private CalendarView calEventDate;
    private Date dateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        txtEventName = findViewById(R.id.txtEvent);
        calEventDate = findViewById(R.id.calEvent);
        calEventDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                dateEvent = new GregorianCalendar(year, month, dayOfMonth).getTime();
                //dateEvent.setTime();
                //LocalDate.of(year, month, dayOfMonth);
            }
        });
    }

    public void onClickOK(View view)
    {
        Intent data = new Intent();
        data.putExtra("event", txtEventName.getText().toString());
        data.putExtra("date", dateEvent.getTime());
        setResult(RESULT_OK, data);
        finish();
    }

    public void onClickCancel(View view)
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}
