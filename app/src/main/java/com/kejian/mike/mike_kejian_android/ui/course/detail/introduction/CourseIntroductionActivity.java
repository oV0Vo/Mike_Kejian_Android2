package com.kejian.mike.mike_kejian_android.ui.course.detail.introduction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;

import model.course.CourseBriefInfo;
import model.course.CourseDetailInfo;
import model.course.CourseModel;

public class CourseIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_introduction);


    }

    private void setContent() {
        CourseDetailInfo courseDetail = CourseModel.getInstance().getCurrentCourseDetail();
        if(courseDetail == null) {
            Log.e("CourseIntro", "current CourseDetail null!");
            return;
        }

        CourseBriefInfo courseBrief = CourseModel.getInstance().getCurrentCourseBrief();
        if(courseBrief == null) {
            Log.e("CourseIntro", "current CourseBrief null!");
            return;
        }

        /*
        ImageView courseImage = (ImageView)findViewById(R.id.course_intro_course_image);
        courseImage.setBackground();
         */

        TextView courseTitleView = (TextView)findViewById(R.id.course_intro_course_name);
        courseTitleView.setText(courseBrief.getCourseName());

        TextView courseAcademyView = (TextView)findViewById(R.id.course_intro_course_academy);
        courseAcademyView.setText(courseBrief.getAcademyName());

        TextView courseTypeView = (TextView)findViewById(R.id.course_intro_course_type);
        String courseTypeText = courseDetail.getCourseType().toString();
        courseTypeView.setText(courseTypeText);

        TextView studentNumView = (TextView)findViewById(R.id.course_intro_student_num);
        String studentNumText = new Integer(courseDetail.getCurrentStudents()).toString();
        studentNumView.setText(studentNumText);

        GridLayout layout = (GridLayout)findViewById(R.id.course_intro_teacher_name);
        TextView teacherNameView = (TextView)findViewById(R.id.course_intro_teacher_name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_introduction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
