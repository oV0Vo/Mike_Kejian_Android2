package com.kejian.mike.mike_kejian_android.ui.course.detail.naming;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;

public class ColorBarFragment extends Fragment {
    private static final String ARG_BEGIN_COLOR = "begin_color";
    private static final String ARG_END_COLOR = "end_color";
    private static final String ARG_REST_COLOR = "rest_color";
    private static final String ARG_COLOR_BAR_PERCENT = "color_bar_percent";


    private int beginColor;
    private int endColor;
    private int restColor;
    private double colorBarPercent;

    public ColorBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            Bundle args = getArguments();
            beginColor = args.getInt(ARG_BEGIN_COLOR);
            endColor = args.getInt(ARG_END_COLOR);
            if(args.containsKey(ARG_REST_COLOR))
                restColor = args.getInt(ARG_REST_COLOR);
            else
                restColor = getResources().getColor(R.color.dark);
            colorBarPercent = args.getDouble(ARG_COLOR_BAR_PERCENT);
        }
    }

    public static ColorBarFragment getInstance(int beginColor, int endColor, double colorBarPercent) {
        Bundle args = new Bundle();
        args.putInt(ARG_BEGIN_COLOR, beginColor);
        args.putInt(ARG_END_COLOR, endColor);
        args.putDouble(ARG_COLOR_BAR_PERCENT, colorBarPercent);
        ColorBarFragment fg = new ColorBarFragment();
        fg.setArguments(args);
        return fg;
    }

    public static ColorBarFragment getInstance(int beginColor, int endColor, int restColor,
                                               double colorBarPercent) {
        Bundle args = new Bundle();
        args.putInt(ARG_BEGIN_COLOR, beginColor);
        args.putInt(ARG_END_COLOR, endColor);
        args.putInt(ARG_REST_COLOR, restColor);
        args.putDouble(ARG_COLOR_BAR_PERCENT, colorBarPercent);
        ColorBarFragment fg = new ColorBarFragment();
        fg.setArguments(args);
        return fg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color_bar, container, false);

        TextView colorView = (TextView)v.findViewById(R.id.color_text);
        int centerColor = (beginColor + endColor) / 2;
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{beginColor, centerColor, endColor});
        colorView.setBackgroundDrawable(drawable);
        colorView.setText("                   ");
        TextView restView = (TextView)v.findViewById(R.id.rest_text);
        restView.setBackgroundColor(restColor);
        restView.setText("     ");
        return v;
    }

}