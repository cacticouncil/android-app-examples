package org.cacticouncil.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity
{
    private static final String RESTAURANT_FILE = "restaurant.json";

    private ViewModelRestaurant mViewModel;

    @Override
    protected void onCreate(Bundle saved)
    {
        super.onCreate(saved);
        setContentView(R.layout.activity_main);

        String json = loadJSON2String(RESTAURANT_FILE);
        mViewModel = new ViewModelRestaurant(json);

        TextView tv = findViewById(R.id.title);
        tv.setText(mViewModel.mName);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.tabPager);
        viewPager.setAdapter(tabPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String number = mViewModel.mPhone.replace("-", "");

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
    }

    public ViewModelRestaurant getRestaurantModel()
    {
        return mViewModel;
    }

    @Nullable
    private String loadJSON2String(String assetFile)
    {
        String json = null;
        try
        {
            InputStream input = getAssets().open(assetFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}