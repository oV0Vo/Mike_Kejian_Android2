package com.kejian.mike.mike_kejian_android.ui.course.detail;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kejian.mike.mike_kejian_android.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.campus.Post;
import model.course.CourseModel;
import com.kejian.mike.mike_kejian_android.dataType.course.CourseDetailInfo;
import util.TimeFormat;

public class CommentsAreaFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String TAG = "CommentsAreaFG";

    private CourseModel courseModel;

    private OnPostSelectedListener mListener;

    private AbsListView mListView;

    private CommentsArrayAdapter mAdapter;

    private ProgressBar progressBar;

    private TextView emptyText;

    private int taskCountDown;

    private boolean initFinish;

    public CommentsAreaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseModel = CourseModel.getInstance();

        CourseDetailInfo currentCourse = courseModel.getCurrentCourseDetail();
        if(currentCourse != null) {
            ArrayList<Post> posts = courseModel.getCurrentCoursePosts();
            if(posts.size() == 0) {
                taskCountDown += 1;
                new UpdateCommentsTask().execute();
            }
            mAdapter = new CommentsArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, posts);
        } else {
            mAdapter = null;
        }

    }

    public void initView() {
        //by default do nothing
    }

    public void refreshView() {
        if(!initFinish) //no need to refresh
            return;
        Log.i(TAG, "refreshView");
        taskCountDown++;
        new UpdateCommentsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_post, container, false);

        mListView = (AbsListView) view.findViewById(R.id.course_post_list_view);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        progressBar = (ProgressBar)view.findViewById(R.id.course_post_progress_bar);
        emptyText = (TextView)view.findViewById(R.id.empty_text);

        if(taskCountDown != 0) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            Post post = (Post)mAdapter.getItem(position);
            CourseModel.getInstance().setCurrentPost(post);
            mListener.onPostSelected(post.getPostId());
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    private void onUpdateTaskFinished() {
        taskCountDown -= 1;
        if(taskCountDown == 0) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private class CommentsArrayAdapter extends ArrayAdapter<Post> {

        public CommentsArrayAdapter(Context context, int resource, List<Post> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_post_brief, null);
            }

            Post post = getItem(position);

            TextView titleView = (TextView)convertView.findViewById(R.id.course_detail_post_brief_title);
            titleView.setText(post.getTitle());
            /**
             * 教师参与在这设置
             */
            TextView authorView = (TextView)convertView.findViewById(R.id.course_detail_post_brief_author_name);
            authorView.setText(post.getAuthorName());

            TextView timeView = (TextView)convertView.findViewById(R.id.course_detail_post_brief_time);
            //timeView.setText(TimeFormat.toMinute(post.getDate()));
            timeView.setText(TimeFormat.toMinute(new Date()));

            TextView viewNumView = (TextView)convertView.findViewById(R.id.course_detail_post_brief_view_num);
            viewNumView.setText(Integer.toString(post.getViewNum()));

            TextView replyNumView = (TextView)convertView.findViewById(R.id.course_detail_post_brief_reply_num);
            replyNumView.setText(Integer.toString(post.getReplyNum()));

            return convertView;
        }
    }

    public interface OnPostSelectedListener {
        void onPostSelected(String postId);
    }

    private class UpdateCommentsTask extends AsyncTask<Void, Integer, ArrayList<Post>> {

        @Override
        protected ArrayList<Post> doInBackground(Void... params) {
            ArrayList<Post> updatePosts = courseModel.updatePosts();
            return updatePosts;
        }

        protected void onPostExecute(ArrayList<Post> posts) {
            if(progressBar == null || getActivity() == null)
                return;

            if(!initFinish)
                initFinish = true;

            onUpdateTaskFinished();
            if(posts != null) {
                if(posts.size() == 0) {
                    mListView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    if(mListView.getVisibility() != View.INVISIBLE) {
                        mListView.setVisibility(View.VISIBLE);
                        emptyText.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), R.string.net_disconnet, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "net_disconnet");
            }
        }
    }

}
