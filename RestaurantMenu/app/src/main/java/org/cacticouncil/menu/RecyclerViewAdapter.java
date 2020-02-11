package org.cacticouncil.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemName;
        TextView itemPrice;
        TextView itemDesc;

        ViewHolder(View itemView)
        {
            super(itemView);
            itemName = itemView.findViewById(R.id.txtItemName);
            itemPrice = itemView.findViewById(R.id.txtItemPrice);
            itemDesc = itemView.findViewById(R.id.txtItemDesc);
        }
    }

    private ArrayList<ViewModelRestaurant.MenuItem> mItems;
    private LayoutInflater mInflater;

    RecyclerViewAdapter(Context context, ArrayList<ViewModelRestaurant.MenuItem> items)
    {
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // inflates row layout per row as needed
        View view = mInflater.inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // binds data to view
        ViewModelRestaurant.MenuItem item = mItems.get(position);
        holder.itemName.setText(item.name);
        holder.itemPrice.setText(Float.toString(item.price));
        holder.itemDesc.setText(item.desc);
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }
}