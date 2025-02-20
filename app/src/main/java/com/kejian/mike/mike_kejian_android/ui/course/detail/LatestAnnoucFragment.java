package com.kejian.mike.mike_kejian_android.ui.course.detail;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;

import com.kejian.mike.mike_kejian_android.dataType.course.CourseAnnoucement;

import net.course.CourseAnnoucNetService;

import java.util.ArrayList;

import model.course.CourseModel;
import util.TimeFormat;


public class LatestAnnoucFragment extends Fragment {

    private static final String TAG = "LatestAnnoucFg";

    private OnAnnoucementClickListener mListener;

    private ProgressBar progressBar;

    private TextView errorMessageText;

    private TextView emptyText;

    private ViewGroup contentLayout;

    private CourseAnnoucement annouc;

    private boolean initFinish;

    public LatestAnnoucFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_annoucement, container, false);
        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        errorMessageText = (TextView)v.findViewById(R.id.error_message_text);
        emptyText = (TextView)v.findViewById(R.id.empty_text);
        contentLayout = (ViewGroup)v.findViewById(R.id.annouc_content_layout);
        v.setOnClickListener(new OnAnnoucClickListener());

        String courseId = CourseModel.getInstance().getCurrentCourseId();
        new GetAnnoucsTask().execute(courseId);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnAnnoucementClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshView() {
        if(!initFinish)  //no need to refresh
            return;
        String courseId = CourseModel.getInstance().getCurrentCourseId();
        new GetAnnoucsTask(true).execute(courseId);
    }

    private void updateViewOnGetAnnouc() {
        if(getActivity() == null)
            return;

        initFinish = true;
        contentLayout.setVisibility(View.VISIBLE);

        TextView contentView = (TextView) contentLayout.findViewById(R.id.
                course_detail_annoucement_content);
        if(annouc != null) {
            contentView.setText(annouc.getContent());
            TextView authorView = (TextView) contentLayout.findViewById(R.id.
                    course_detail_annoucement_author_name);
            authorView.setText(annouc.getPersonName());
            TextView dateView = (TextView) contentLayout.findViewById(R.id.
                    course_detail_annoucement_date);
            dateView.setText(TimeFormat.toMinute(annouc.getDate()));
            emptyText.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
        }
    }

    public interface OnAnnoucementClickListener {
        void onAnnoucementClick();
    }

    private class GetAnnoucsTask extends AsyncTask<String, Void, Boolean> {

        private boolean isOnRefresh;

        public GetAnnoucsTask() {

        }

        public GetAnnoucsTask(boolean isOnRefresh) {
            this.isOnRefresh = isOnRefresh;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            CourseModel courseModel = CourseModel.getInstance();
            boolean updateSuccess = courseModel.updateAnnoucs();
            if(updateSuccess)
                annouc = courseModel.getLatestAnnouc();
            return updateSuccess;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(progressBar == null)
                return;

            progressBar.setVisibility(View.GONE);
            if(success) {
                updateViewOnGetAnnouc();
            } else {
                if(!isOnRefresh)
                    errorMessageText.setVisibility(View.VISIBLE);
            }
        }
    }

    private class OnAnnoucClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(mListener != null)
                mListener.onAnnoucementClick();
        }
    }

}
