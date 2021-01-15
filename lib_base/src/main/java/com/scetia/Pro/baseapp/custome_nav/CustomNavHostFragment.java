package com.scetia.Pro.baseapp.custome_nav;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.scetia.Pro.baseapp.R;

/**
 * created by ljs
 * on 2021/1/8
 */
public class CustomNavHostFragment extends NavHostFragment {
    @NonNull
    @Override
    protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
        return new HideSwitchFragmentNavigator(requireContext(),getChildFragmentManager(),getContainerId());
    }

    private int getContainerId(){
        final int id = getId();
        return id!=0&&id!= View.NO_ID? id: R.id.nav_host_fragment_container;
    }
}
