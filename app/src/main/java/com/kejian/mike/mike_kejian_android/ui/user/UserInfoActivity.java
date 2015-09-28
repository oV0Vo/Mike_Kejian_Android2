package com.kejian.mike.mike_kejian_android.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.kejian.mike.mike_kejian_android.R;

import model.user.user;

/**
 * Created by kisstheraik on 15/9/18.
 */


public class UserInfoActivity extends Activity{

    private user user;
    private LinearLayout userInfoLayout;
    private TableLayout userBaseInfoView;
    private TableLayout userSchoolInfoView;
    private TextView baseInfoName;
    private TextView baseInfoGender;
    private TextView baseInfoGrade;
    private TextView baseInfoIdentify;
    private TextView baseInfoSign;






    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_info);
        initViews();

    }

    public void initViews(){

        user=(user)getIntent().getSerializableExtra(UserActivityComm.USER_INFO.name());
        /*userBaseInfoView=(TableLayout)findViewById(R.id.user_base_info_view);
        baseInfoName=(TextView)findViewById(R.id.base_info_name);
        baseInfoGender=(TextView)findViewById(R.id.base_info_gender);
        baseInfoGrade=(TextView)findViewById(R.id.user_school_info_grade);
        baseInfoIdentify=(TextView)findViewById(R.id.user_school_info_identify);
        baseInfoSign=(TextView)findViewById(R.id.base_info_sign);*/
        setUserInfo();

        assert user==null:"do not get user";


    }

    public void setUserInfo(){



        if(user==null){

            System.out.println("no user");



            return ;
        }

        else{

            System.out.println("get user " + user.getName());



            String name="姓名："+user.getName();
            String gender="性别："+user.getGender();
            String grade="年级："+user.getGrade();
            String sign="个性签名："+user.getSign();
            String identify="身份："+user.getIdentify();

            baseInfoGender.setText(gender);
            baseInfoGrade.setText(grade);
            baseInfoIdentify.setText(identify);
            baseInfoSign.setText(sign);
            baseInfoName.setText(name);

        }

    }


}
