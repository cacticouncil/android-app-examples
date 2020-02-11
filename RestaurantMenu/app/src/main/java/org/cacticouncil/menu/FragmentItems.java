package org.cacticouncil.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FragmentItems extends Fragment
{
    private ArrayList<ViewModelRestaurant.MenuItem> mItems;
    private RecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle saved)
    {
        super.onCreate(saved);

        TabPagerAdapter.Tab tab = null;
        if (getArguments() != null)
            tab = (TabPagerAdapter.Tab)getArguments().getSerializable(TabPagerAdapter.ARG_TAB_INDEX);
        switch (tab)
        {
            case Noms:
                mItems = ((MainActivity)getActivity()).getRestaurantModel().mFood;
                break;
            case Sips:
                mItems = ((MainActivity)getActivity()).getRestaurantModel().mDrinks;
                break;
            default:
                mItems = new ArrayList<ViewModelRestaurant.MenuItem>();
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saved)
    {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle saved)
    {
        RecyclerView rcview = view.findViewById(R.id.rvwItems);
        rcview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerViewAdapter(getContext(), mItems);
        rcview.setAdapter(mAdapter);
    }
}