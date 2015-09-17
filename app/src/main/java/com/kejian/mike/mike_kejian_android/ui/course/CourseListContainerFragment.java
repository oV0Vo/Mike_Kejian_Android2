package com.kejian.mike.mike_kejian_android.ui.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;

import bl.UserInfoService;

/**
 *
 */
public class CourseListContainerFragment extends Fragment {

    private RadioButton myCourseButton;
    private TextView allCourseButton;
    private CourseListFragment courseListFg;

    private boolean isShowMyCourse;

    public CourseListContainerFragment() {
        // Required empty public constructor
    }

    private void createListFragment() {
        FragmentManager fm = getChildFragmentManager();
        CourseListFragment fg= (CourseListFragment)fm.findFragmentById(R.id.main_course_course_list);
        if(fg == null) {
            String studentId = UserInfoService.getInstance().getSid();
            fg = new CourseListFragment();
            fm.beginTransaction().add(R.id.main_course_course_list, fg).commit();
        }
        courseListFg = fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_list_container, container, false);
        allCourseButton = (RadioButton)v.findViewById(R.id.main_course_all_course_button);
        myCourseButton = (RadioButton)v.findViewById(R.id.main_course_my_course_button);
        initCourseButtonListner();
        myCourseButton.callOnClick();
        return v;
    }

    private void initCourseButtonListner() {
        allCourseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isShowMyCourse) {
                    isShowMyCourse = false;
                    courseListFg.showAllCourse();
                }
            }
        });

        myCourseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!isShowMyCourse) {
                    isShowMyCourse = true;
                    courseListFg.showMyCourse();
                }
            }
        });
    }




}
