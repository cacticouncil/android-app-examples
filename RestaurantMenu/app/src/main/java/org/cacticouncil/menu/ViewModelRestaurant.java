package org.cacticouncil.menu;

import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


class ViewModelRestaurant extends ViewModel
{
    class MenuItem
    {
        public String name;
        public float price;
        public String desc;
    }

    String mName;
    String mStory;
    String mAddress;
    String mPhone;
    ArrayList<MenuItem> mMenuItems;
    ArrayList<MenuItem> mFood;
    ArrayList<MenuItem> mDrinks;

    public ViewModelRestaurant(String json)
    {
        loadRestaurantData(json);
    }

    private void loadRestaurantData(String json)
    {
        // lists reset
        mMenuItems = new ArrayList<MenuItem>();
        mFood = new ArrayList<MenuItem>();
        mDrinks = new ArrayList<MenuItem>();

        try
        {
            JSONObject root = new JSONObject(json);

            // about
            JSONObject jsAbout = root.getJSONObject("about");
            mName = jsAbout.getString("name");
            mStory = jsAbout.getString("story");
            mAddress = jsAbout.getString("address");
            mPhone = jsAbout.getString("phone");

            // menu items
            JSONArray jsMenuItems = root.getJSONArray("menu");
            JSONObject jso;
            MenuItem item;
            int type;
            for (int i = 0; i < jsMenuItems.length(); i++)
            {
                jso = jsMenuItems.getJSONObject(i);
                type = jso.getInt("type");

                item = new MenuItem();
                item.name = jso.getString("name");
                item.desc = jso.getString("description");
                item.price = (float)jso.getDouble("price");

                mMenuItems.add(item);
                if (type == 1)
                    mFood.add(item);
                else if (type == 2)
                    mDrinks.add(item);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}