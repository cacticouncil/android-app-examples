package org.cacticouncil.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class FragmentAbout extends Fragment
{
    private ViewModelRestaurant mModel;

    @Override
    public void onCreate(Bundle saved)
    {
        super.onCreate(saved);

        mModel = ((MainActivity)getActivity()).getRestaurantModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        TextView tv;
        tv = root.findViewById(R.id.txtStory);
        tv.setText(mModel.mStory);
        tv = root.findViewById(R.id.txtPhone);
        tv.setText(mModel.mPhone);
        tv = root.findViewById(R.id.txtAddress);
        tv.setText(mModel.mAddress);

        return root;
    }
}