package com.kejian.mike.mike_kejian_android.ui.course.detail.question;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;
import com.kejian.mike.mike_kejian_android.ui.util.ColorBarFragment;

import net.course.CourseQuestionNetService;

import java.util.ArrayList;
import java.util.List;

import dataType.course.question.BasicQuestion;
import dataType.course.question.ChoiceQuestion;
import dataType.course.question.QuestionShowAnswer;
import dataType.course.question.QuestionStats;
import model.course.CourseModel;
import util.UnImplementedAnnotation;

public class QuesitionStatsActivity extends AppCompatActivity {

    public static final String ARG_QUESTION_ID = "quesitonId";

    private BasicQuestion question;

    private ArrayList<QuestionShowAnswer> answers;

    private CourseModel courseModel;

    private static final int ANSWER_INIT_NUM = 50;
    private static final int ANSWER_UPDATE_NUM = 10;

    private ProgressBar progressBar;

    private ViewGroup mainLayout;

    private ViewGroup statsTitleLayout;
    private ViewGroup statsContentLayout;
    private static final int[] choiceColors = new int[8];

    private ViewGroup answerListTitleLayout;
    private ViewGroup answerContentListLayout;

    private ListView answerListView;

    private QuestionAnswerAdapter answerListAdapter;

    private int taskCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_stats);

        answers = new ArrayList();
        courseModel = CourseModel.getInstance();
        question = courseModel.getFocusQuestion();

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        mainLayout = (ViewGroup)findViewById(R.id.main_layout);

        initQuestionContentView();

        initStatsLayout();

        initAnswerLayout();
    }

    private void initAttrs() {
        choiceColors[0] = getResources().getColor(R.color.orange);
        choiceColors[1] = getResources().getColor(R.color.green2);
        choiceColors[2] = getResources().getColor(R.color.blue);
        choiceColors[3] = getResources().getColor(R.color.pink);
        choiceColors[4] = getResources().getColor(R.color.yellow);
        choiceColors[5] = getResources().getColor(R.color.dark);
        choiceColors[6] = getResources().getColor(R.color.purple);
        choiceColors[7] = getResources().getColor(R.color.brown);
    }

    private void initQuestionContentView() {
        TextView questionContentText = (TextView)findViewById(R.id.question_content_text);
        questionContentText.setText(question.getContent());

        if(question instanceof ChoiceQuestion)
            setChoiceContentView();

        initContentZhankaiButton();
    }

    private void setChoiceContentView() {
        ViewGroup choiceContainer = (ViewGroup)findViewById(R.id.choice_container);

        ChoiceQuestion choiceQuestion = (ChoiceQuestion)question;
        ArrayList<String> choiceContents = choiceQuestion.getChoiceContents();
        for(int i=0; i<choiceContents.size(); ++i){
            ViewGroup choiceContentLayout = (ViewGroup)getLayoutInflater().inflate(R.layout.
                    layout_quesiton_choice_content, null);

            TextView choiceIndexText = (TextView)choiceContentLayout.findViewById(
                    R.id.choice_index_text);
            choiceIndexText.setText(Character.toString((char)('A' + i)));

            TextView choiceContentText = (TextView)choiceContentLayout.findViewById
                    (R.id.choice_content_text);
            choiceContentText.setText(choiceContents.get(i));

            choiceContainer.addView(choiceContentLayout);
        }

        choiceContainer.setVisibility(View.VISIBLE);
    }

    @UnImplementedAnnotation
    private void initContentZhankaiButton() {
        Button zhankaiButton = (Button)findViewById(R.id.question_answer_question_detail);
        zhankaiButton.setVisibility(View.GONE);
    }

    private void initStatsLayout() {
        statsContentLayout = (ViewGroup)findViewById(R.id.question_stats_container);
        statsTitleLayout = (ViewGroup)findViewById(R.id.question_answer_stats_title);
        final ImageView actionImageView = (ImageView)findViewById(R.id.stats_zhankai_image);
        statsTitleLayout.setOnClickListener(new View.OnClickListener() {

            private boolean isShow = false;

            @Override
            public void onClick(View v) {
                if (isShow) {
                    statsContentLayout.setVisibility(View.GONE);
                    actionImageView.setImageDrawable(getResources().getDrawable(R.drawable.down));
                } else {
                    statsContentLayout.setVisibility(View.VISIBLE);
                    actionImageView.setImageDrawable(getResources().getDrawable(R.drawable.up1));
                }
            }
        });

        new GetQuestionStatsTask().execute();
        taskCountDown++;
    }

    private void initAnswerLayout() {
        answerContentListLayout = (ViewGroup)findViewById(R.id.question_answer_all_question_container);
        answerListTitleLayout = (ViewGroup)findViewById(R.id.all_answer_title_layout);
        final ImageView actionImageView = (ImageView)findViewById(R.id.all_answer_zhankai_image);
        answerListTitleLayout.setOnClickListener(new View.OnClickListener() {

            private boolean isShow = false;

            @Override
            public void onClick(View v) {
                if (isShow) {
                    answerContentListLayout.setVisibility(View.GONE);
                    actionImageView.setImageDrawable(getResources().getDrawable(R.drawable.down));
                } else {
                    answerContentListLayout.setVisibility(View.VISIBLE);
                    actionImageView.setImageDrawable(getResources().getDrawable(R.drawable.up1));
                }
            }
        });

        answerListView = (ListView)findViewById(R.id.question_answer_answer_list);
        answerListAdapter = new QuestionAnswerAdapter(this, android.R.layout.simple_list_item_1,
                answers);
        answerListView.setAdapter(answerListAdapter);

        new InitQuestionAnswerTask().execute();
        taskCountDown++;
    }

    private void updateViewOnGetInitAnswers() {
        answerListAdapter.notifyDataSetChanged();
        showViewIfInitTaskFinish();
    }

    private void updateViewOnGetQuestionStats(QuestionStats stats) {
        int joinNum = stats.getTotalAnswerNum();
        int totalNum = courseModel.getCurrentCourseDetail().getCurrentStudents();
        String joinNumStr = Integer.toString(joinNum) + "/" + Integer.toString(totalNum);
        TextView joinNumText = (TextView)findViewById(R.id.join_num_text);
        joinNumText.setText(joinNumStr);

        int colorBarWidth = (int)getResources().getDimension(R.dimen.color_bar_width);
        int colorBarHeight = (int)getResources().getDimension(R.dimen.color_bar_height);
        int redColor = getResources().getColor(R.color.my_red);
        int greenColor = getResources().getColor(R.color.green);
        double joinRate = ((double)joinNum) / totalNum;
        ColorBarFragment joinRateColorBar = ColorBarFragment.getInstance(redColor, greenColor, joinRate,
                colorBarWidth, colorBarHeight);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.join_rate_color_bar, joinRateColorBar).commit();

        TextView joinRateText = (TextView)findViewById(R.id.join_rate_text);
        joinRateText.setText("93.2%");
        setTextColorAccordingToRate(joinRateText, joinRate);

        int correctNum = stats.getCorrectAnswerNum();
        String correctNumStr = Integer.toString(correctNum) + "/" + Integer.toString(joinNum);
        TextView correctNumText = (TextView)findViewById(R.id.correct_num_text);
        correctNumText.setText(correctNumStr);

        double correctRate = ((double)correctNum) / joinNum;
        ColorBarFragment correctRateColorBar = ColorBarFragment.getInstance(redColor, greenColor, correctRate,
                colorBarWidth, colorBarHeight);
        fm.beginTransaction().add(R.id.correct_rate_color_bar, correctRateColorBar).commit();

        TextView correctRateText = (TextView)findViewById(R.id.correct_rate_text);
        correctRateText.setText("93.2%");
        setTextColorAccordingToRate(correctRateText, correctRate);

        initChoiceDistributeView(stats.getChoiceDistribute());

        showViewIfInitTaskFinish();
    }

    private void initChoiceDistributeView(List<Integer> distributes) {
        ViewGroup distributeContainer = (ViewGroup)findViewById(R.id.distribute_container);
        int sum = caculateSum(distributes);

        for(int i=0; i<distributes.size(); ++i) {
            ViewGroup choiceLayout = (ViewGroup)getLayoutInflater().inflate(
                    R.layout.layout_choice_distirbute, null);

            TextView choiceIndexText = (TextView)choiceLayout.findViewById(R.id.choice_index_text);
            choiceIndexText.setText(Character.toString((char)('A' + i)));

            TextView choiceBarText = (TextView)choiceLayout.findViewById(R.id.choice_bar_text);
            int choiceColor = (i < choiceColors.length)? choiceColors[i]: choiceColors[0];
            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{choiceColor, choiceColor, choiceColor});
            int barWidth = (int)getResources().getDimension(R.dimen.choice_distribute_bar_width);
            double barMaxHeight = getResources().getDimension(R.dimen.choice_distribute_bar_max_height);
            int distribute = distributes.get(i);
            int barHeight = (int)(barMaxHeight * ((double)distribute) / sum );
            drawable.setSize(barWidth, barHeight);
            choiceBarText.setBackgroundDrawable(drawable);

            TextView distributeNumText = (TextView)choiceLayout.findViewById(
                    R.id.choice_distribute_num_text);
            distributeNumText.setText(Integer.toString(distribute));

            distributeContainer.addView(choiceLayout);
        }
    }

    private int caculateSum(List<Integer> nums) {
        int sum = 0;
        for(Integer num: nums)
            sum += num;
        return sum;
    }

    private void setTextColorAccordingToRate(TextView rateText, double rate) {
        if (rate < 0.6)
            rateText.setTextColor(getResources().getColor(R.color.my_red));
        else
            rateText.setTextColor(getResources().getColor(R.color.green));
    }

    private void showViewIfInitTaskFinish() {
        if(taskCountDown == 0) {
            progressBar.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }

    private class GetQuestionStatsTask extends AsyncTask<Void, Void, QuestionStats> {

        @Override
        protected QuestionStats doInBackground(Void... params) {
            return CourseQuestionNetService.getQuestionStats(question.getQuestionId());
        }

        @Override
        protected void onPostExecute(QuestionStats stats) {
            taskCountDown--;
            updateViewOnGetQuestionStats(stats);
        }
    }

    private class InitQuestionAnswerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            int beginPos = 0;
            int num = ANSWER_INIT_NUM;
            ArrayList<QuestionShowAnswer> initAnswers = CourseQuestionNetService.getQuestionAnswer
                    (question.getQuestionId(), beginPos, num);
            answers.addAll(initAnswers);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            taskCountDown--;
            updateViewOnGetInitAnswers();
        }
    }

    private class QuestionAnswerAdapter extends ArrayAdapter<QuestionShowAnswer> {

        public QuestionAnswerAdapter(Context context, int resource, List<QuestionShowAnswer> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(convertView != null)
                return convertView;

            convertView = View.inflate(QuesitionStatsActivity.this, R.layout.
                    layout_question_answer_brief, null);//@null?
            QuestionShowAnswer answer = getItem(position);

            ImageView userImageView = (ImageView)convertView.findViewById(R.id.user_image);
            userImageView.setImageDrawable(null);

            TextView userNameText = (TextView)convertView.findViewById(R.id.user_name);
            userNameText.setText(answer.getStudentName());

            TextView answerContentText = (TextView)convertView.findViewById(R.id.answer_content);
            answerContentText.setText(answer.getAnswer());

            return convertView;
        }
    }
}
