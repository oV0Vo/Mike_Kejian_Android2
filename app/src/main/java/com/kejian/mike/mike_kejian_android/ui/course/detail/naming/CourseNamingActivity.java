package com.kejian.mike.mike_kejian_android.ui.course.detail.naming;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;

import net.CourseNamingNetService;
import net.CourseNetService;

import bl.course.NamingBLService;
import model.course.CourseModel;
import model.course.data.CourseNamingRecord;
import util.TimeFormat;
import util.TimerThread;

public class CourseNamingActivity extends AppCompatActivity {

    private NamingBLService namingBL;

    private ViewGroup mainLayout;
    private ProgressBar progressBar;
    private ListView historyListView;
    private ArrayAdapter historyNamingAdapter;

    private TextView namingTimeText;
    private TextView leftTimeClock;
    private TextView namingActionText;

    private TimerThread timerThread;

    private int taskCountDown;

    private String teacherIdMock = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_naming);
        namingBL = NamingBLService.getInstance();

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mainLayout = (ViewGroup)findViewById(R.id.course_naming_main_layout);
        mainLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        taskCountDown++;
        new GetHistoryNamingTask().execute();
        taskCountDown++;
        new GetCurrentNamingTask().execute();
    }

    private void updateViewOnGetHistoryNaming(ArrayList<CourseNamingRecord> namingRecords) {
        if(mainLayout == null) {
            return;
        }

        historyListView = (ListView)findViewById(R.id.course_naming_history_list);
        historyNamingAdapter = new HistoryNamingAdapter(this, android.R.layout.simple_list_item_1,
                namingRecords);
        historyListView.setAdapter(historyNamingAdapter);

        updateViewIfAllTaskFinish();
    }

    private void updateViewOnGetCurrentNaming(CourseNamingRecord currentNaming) {
        namingTimeText = (TextView)findViewById(R.id.course_naming_time_text);
        leftTimeClock = (TextView)currentNamingLayout.findViewById(R.id.left_time_text);
        namingActionText = (TextView)findViewById(R.id.naming_action_text);

        if(currentNaming != null) {
            setViewOnNaming(currentNaming);
        } else {
            setViewOnNamingNotStart();
        }
    }

    private void setViewOnNaming(CourseNamingRecord currentNaming) {
        Date beginTime = currentNaming.getBeginTime();
        Date endTime = currentNaming.getEndTime();
        String timeStr = TimeFormat.convertDateInterval(beginTime, endTime);
        namingTimeText.setText(timeStr);

        long leftTime = endTime.getTime() - beginTime.getTime();
        startLeftTimeClock(leftTime);
    }

    private void startLeftTimeClock(long leftTime) {
        CountDownTimer timer = new CountDownTimer(leftTime, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(leftTimeClock != null)
                    leftTimeClock.setText(TimeFormat.toSeconds(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                if(leftTimeClock != null)
                    leftTimeClock.setTextColor(getResources().getColor(R.color.my_red));
                setViewOnNamingFinish();
            }
        };
        timerThread = new TimerThread(timer);
        timerThread.start();
    }

    private void setViewOnNamingNotStart() {
        namingActionText.setText(R.string.course_naming_naming_action);
        //namingActionText.setBackgroundColor();
        namingTimeText.setText(R.string.course_naming_not_start);
        leftTimeClock.setText(R.string.course_naming_not_start_clock_text);
    }

    private void setViewOnNamingFinish() {
        namingActionText.setText(R.string.course_naming_finish);
        namingActionText.setEnabled(false);
        progressBar.setText(R.string.on_getting_naming_result);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setViewOnBeginNamingSuccess(CourseNamingRecord namingRecord) {
        progressBar.setVisibility(View.GONE);
        namingActionText.setText(R.string.course_naming_on_naming);
        namingActionText.setEnabled(false);
        setViewOnNaming(namingRecord);
    }

    private void setViewOnBeginNamingFailure() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, R.string.net_disconnet, Toast.LENGTH_LONG).show();
    }

    private void setViewOnGetNamingResult(CourseNamingRecord namingResult) {
        progressBar.setVisibilit(View.GONE);

        View resultView = getLayoutInflater().inflate(R.layout.layout_course_naming_result, null);
        TextView statsText = (TextView)resultView.findViewById(R.id.stats_text);
        int signInNum = namingResult.getSignInNum();
        int totalNum = namingResult.getTotalNum();
        String statsStr = Integer.toString(signInNum) + "/" + Integer.toString(totalNum);
        statsText.setText(statsStr);

        int red = getResources().getColor(R.color.my_red);
        int green = getResources().getColor(R.color.green);
        double signInPercent = (double)signInNum / totalNum;
        ColorBarFragment colorBarFragment = ColorBarFragment.getInstance(red, green,
                signInPercent);
        getSupportFragmentManager().beginTransaction().add(R.id.percent_image, colorBarFragment).
                commit();

        TextView percentText = (TextView)resultView.findViewById(R.id.percent_text);
        percentText.setText("93.2%");

        TextView absentListText = (TextView)resultView.findViewById(R.id.absent_list_text);
        String absentListStr = convertToString(namingResult.getAbsentNames(),
                namingResult.getAbsentIds());
        absentListText.setText(absentListStr);

        initZhankaiButton();
    }

    private void initZhankaiButton() {

    }

    private String convertToString(ArrayList<String> absentNames, ArrayList<String> ids) {
        StringBuilder strBuilder = new StringBuilder();
        for(int i=0; i<absentNames.size(); ++i) {
            String name = absentNames.get(i);
            String sid = ids.get(i);
            strBuilder.append(name);
            strBuilder.append("(");
            strBuilder.append(sid);
            strBuilder.append(")");
            if(i != absentNames.size())
                strBuilder.append("、");
        }
        return strBuilder.toString();
    }

    private void updateViewIfAllTaskFinish() {
        if(taskCountDown == 0 && mainLayout != null) {
            mainLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setNotOnNamingView() {
        TextView timeText = (TextView)findViewById(R.id.course_naming_time_text);
        timeText.setText(R.string.course_naming_not_start);
        TextView clockText = (TextView)findViewById(R.id.course_naming_not_start_clock_text);
        colorText.setText(R.string.course_naming_not_start_clock_text);
        TextView namingText = (TextView)findViewById(R.id.naming);
        namingText.setText(R.string.course_naming_naming_action);

        namingText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setText(R.string.on_send_naming_request);
                progressBar.setVisibility(View.VISIBLE);
                new BeginNamingTask().execute();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timerThread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class HistoryNamingAdapter extends ArrayAdapter<CourseNamingRecord> {

        public HistoryNamingAdapter(Context context, int resource, CourseNamingRecord[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_history_naming, null);
            }

            CourseNamingRecord r = getItem(position);
            Date beginTime = r.getBeginTime();
            Date endTime = r.getEndTime();
            String timeStr = TimeFormat.convertDateInterval(beginTime, endTime);
            TextView textView = (TextView)convertView.findViewById(R.id.history_naming_time);
            textView.setText(timeStr);

            TextView statsText = (TextView)convertView.findViewById(R.id.stats_text);
            int signInNum = r.getSignInNum();
            int totalNum = r.getTotalNum();
            String statsStr = Integer.toString(signInNum) + "/" + Integer.toString(totalNum);
            statsText.setText(statsStr);

            int red = getResources().getColor(R.color.my_red);
            int green = getResources().getColor(R.color.green);
            double signInPercent = (double)signInNum / totalNum;
            ColorBarFragment colorBarFragment = ColorBarFragment.getInstance(red, green,
                    signInPercent);
            getSupportFragmentManager().beginTransaction().add(R.id.percent_image, colorBarFragment).
                    commit();

            TextView percentText = (TextView)convertView.findViewById(R.id.percent_text);
            percentText.setText("93.2%");
            return convertView;
        }
    }

    private class GetHistoryNamingTask extends AsyncTask<Void, Void, ArrayList<CourseNamingRecord>> {

        @Override
        protected ArrayList<CourseNamingRecord> doInBackground(Void... params) {
            String courseId = CourseModel.getInstance().getCurrentCourseId();
            return CourseNetService.getHistoryNamingRecords(courseId);
        }

        @Override
        protected void onPostExecute(ArrayList<CourseNamingRecord> namingRecords) {
            updateView(namingRecords);
        }
    }

    private class GetCurrentNamingTask extends AsyncTassk<Void, Void, CourseNamingRecord> {

        @Override
        protected CourseNamingRecord doInBackground(Void... params) {
            CourseModel courseModel = CourseModel.getInstance();
            String courseId = courseModel.getCurrentCourseId();
            return CourseNamingNetService.getCurrentNamingRecord(courseId);
        }

        @Override
        protected void onPostExecute(CourseNamingRecord currentNaming) {
            updateViewOnGetCurrentNaming(currentNaming);
        }
    }

    private class BeginNamingTask extends AsyncTask<Void, Void, CourseNamingRecord> {

        @Override
        protected CourseNamingRecord doInBackground(Void... params) {
            CourseModel courseModel = CourseModel.getInstance();
            String courseId = courseModel.getCurrentCourseId();
            return CourseNamingNetService.beginNaming(courseId, teacherIdMock);
        }

        @Override
        protected void onPostExecute(CourseNamingRecord namingResult) {
            if(namingResult != null)
                setViewOnBeginNamingSuccess(namingResult);
            else
                setViewOnBeginNamingFailure();
        }
    }

    private class GetNamingResultTask extends AsyncTask<Void, Void, CourseNamingRecord> {

        @Override
        protected CourseNamingRecord doInBackground(Void... params) {
            CourseModel courseModel = CourseModel.getInstance();
            String courseId = courseModel.getCurrentCourseId();
            return CourseNamingNetService.getCurrentNamingRecord(courseId);
        }

        @Override
        protected void onPostExecute(CourseNamingRecord currentNaming) {
            setViewOnGetNamingResult(currentNaming);
        }
    }

}
