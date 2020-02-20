package org.cacticouncil.sensorsurvey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mSensorProximity;
    private Sensor mSensorLight;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        TextView sensorTextView = (TextView)findViewById(R.id.sensor_list);
        mImage = (ImageView)findViewById(R.id.image_android);

        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensorList)
            sensorText.append(currentSensor.getName()).append(System.getProperty("line.separator"));

        sensorTextView.setText(sensorText);

        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    protected void onStart()
    {
        super.onStart();

        if (mSensorProximity != null)
            mSensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        if (mSensorLight != null)
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onStop()
    {
        super.onStop();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        int sensorType = sensorEvent.sensor.getType();

        // light and proximity only use first value
        float currentValue = sensorEvent.values[0];

        switch (sensorType)
        {
            case Sensor.TYPE_LIGHT:
                Log.d("Sensor", "Light: " + currentValue);
                //mImage.getLayoutParams().width = (int)(currentValue * (float)mImage.getMaxWidth());
                //mTextSensorLight.setText(getResources().getString(R.string.label_light, currentValue));
                break;

            case Sensor.TYPE_PROXIMITY:
                Log.d("Sensor", "Proxy: " + currentValue);

                if (currentValue < 0.5)
                {
                    //ConstraintLayout.LayoutParams parms = new ConstraintLayout.LayoutParams(50, 50);
                    //mImage.setLayoutParams(parms);
                    mImage.setImageAlpha(50);
                }
                else
                {
                    mImage.setImageAlpha(255);
                }
                //mTextSensorProximity.setText(getResources().getString(R.string.label_proximity, currentValue));
                break;

            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
        // do nothing for now
    }
}
