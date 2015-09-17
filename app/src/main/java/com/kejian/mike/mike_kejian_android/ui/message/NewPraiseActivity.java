package com.kejian.mike.mike_kejian_android.ui.message;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kejian.mike.mike_kejian_android.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import bl.MessageBLService;
import model.message.Praise;
import util.DensityUtil;

public class NewPraiseActivity extends Activity implements View.OnClickListener{
    private View layout_title;
    private ArrayList<Praise> praises = new ArrayList<>();
    private int praiseNum = 0;
    private LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_praise);
        this.layout_title = findViewById(R.id.layout_bar);
        this.container = (LinearLayout)findViewById(R.id.praise_container);
        initData();
        initViews();
    }
    private void initData(){
        this.praises = MessageBLService.getPraiseList();
        this.praiseNum = this.praises.size();

    }
    private void initViews(){
        ImageView iv = (ImageView)this.layout_title.findViewById(R.id.image_title);
        iv.setImageResource(R.drawable.left);
        iv.setOnClickListener(this);
        TextView tv = (TextView)this.layout_title.findViewById(R.id.txt_title);
        tv.setText("收到的赞");
        TextView praise_num_text = (TextView)this.findViewById(R.id.praise_num);
        praise_num_text.setText("共 "+this.praiseNum+ " 条");
        for(int i = 0;i<this.praiseNum;i++){
            this.container.addView(genPraiseLayout(this.praises.get(i)));
            this.container.addView(genLineSplitView());
        }

    }
    private View genLineSplitView(){
        View lineView = new View(this);
        LinearLayout.LayoutParams layout_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2);
        lineView.setBackgroundResource(R.color.black2);
        lineView.setLayoutParams(layout_line);
        return lineView;
    }
    private LinearLayout genPraiseLayout(Praise praise){
        int dp15 = DensityUtil.dip2px(this, 15);
        int dp5 = DensityUtil.dip2px(this,5);
        int dp40 = DensityUtil.dip2px(this,40);
        int dp10 = DensityUtil.dip2px(this,10);

        LinearLayout praise_layout = new LinearLayout(this);
        praise_layout.setOrientation(LinearLayout.HORIZONTAL);
        praise_layout.setBackgroundResource(R.drawable.setting_item_selector);
        LinearLayout.LayoutParams layout_praise = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        praise_layout.setLayoutParams(layout_praise);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layout_image = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_image.gravity = Gravity.CENTER;
        layout_image.setMargins(dp15, dp10, 0, dp10);
        imageView.setImageResource(R.drawable.mail);
        imageView.setMaxHeight(dp40);
        imageView.setMaxWidth(dp40);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(layout_image);

        LinearLayout text = new LinearLayout(this);
        LinearLayout.LayoutParams layout_text = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_text.gravity = Gravity.RIGHT;
        layout_text.setMargins(dp15, 0, 0, dp10);
        text.setOrientation(LinearLayout.VERTICAL);
        text.setLayoutParams(layout_text);

        LinearLayout firstLineStr = new LinearLayout(this);
        LinearLayout.LayoutParams layout_firstLine = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        firstLineStr.setOrientation(LinearLayout.HORIZONTAL);
        layout_firstLine.setMargins(0, dp10, 0, 0);
        firstLineStr.setLayoutParams(layout_firstLine);

        TextView replyer_view = new TextView(this);
        LinearLayout.LayoutParams layout_replyer = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        replyer_view.setText(praise.getReplyer());
        replyer_view.setTextColor(getResources().getColor(R.color.green));
        replyer_view.setLayoutParams(layout_replyer);

        TextView reply_view = new TextView(this);
        LinearLayout.LayoutParams layout_reply = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_reply.setMargins(dp10, 0, 0, 0);
        reply_view.setText("赞了你的帖子");
        reply_view.setLayoutParams(layout_reply);

        LinearLayout secondLineStr = new LinearLayout(this);
        LinearLayout.LayoutParams layout_secondLine = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_secondLine.setMargins(0, dp10, 0, 0);
        secondLineStr.setOrientation(LinearLayout.HORIZONTAL);
        secondLineStr.setLayoutParams(layout_secondLine);

        TextView toYourPost = new TextView(this);
        LinearLayout.LayoutParams layout_toPost = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toYourPost.setText("对我的帖子：");
        toYourPost.setLayoutParams(layout_toPost);

        TextView postContent = new TextView(this);
        LinearLayout.LayoutParams layout_post = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_post.setMargins(dp10, 0, 0, 0);
        postContent.setText(praise.getPost());
        postContent.setTextColor(getResources().getColor(R.color.green));
        postContent.setLayoutParams(layout_post);

        TextView time_view = new TextView(this);
        LinearLayout.LayoutParams layout_time = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_time.gravity = Gravity.RIGHT;
        layout_time.setMargins(0,dp10,dp15,0);
        time_view.setText(praise.getReplyTime());
        time_view.setLayoutParams(layout_time);
        time_view.setGravity(Gravity.RIGHT);

        firstLineStr.addView(replyer_view);
        firstLineStr.addView(reply_view);

        secondLineStr.addView(toYourPost);
        secondLineStr.addView(postContent);

        text.addView(firstLineStr);
        text.addView(secondLineStr);
        text.addView(time_view);

        praise_layout.addView(imageView);
        praise_layout.addView(text);
        return praise_layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_praise, menu);
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

    @Override
    public void onClick(View v) {
        MessageBLService.unreadPraiseNum = 0;
        NewPraiseActivity.this.finish();

    }
}
