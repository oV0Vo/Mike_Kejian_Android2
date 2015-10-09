package com.kejian.mike.mike_kejian_android.ui.campus;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kejian.mike.mike_kejian_android.R;

/**
 * Created by showjoy on 15/9/16.
 */
public class PostListContainerFragment extends Fragment {
    private View view;
    private FragmentTabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            Activity ctx = this.getActivity();
            view = ctx.getLayoutInflater().inflate(R.layout.fragment_post_list_container,
                    null);
            mTabHost = (FragmentTabHost)view.findViewById(android.R.id.tabhost);
            mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
            mTabHost.addTab(mTabHost.newTabSpec("latest").setIndicator("最新"),
                    LatestPostListFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("hottest").setIndicator("热门"),
                    HottestPostListFragment.class, null);

            return view;

        }else{
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

}
