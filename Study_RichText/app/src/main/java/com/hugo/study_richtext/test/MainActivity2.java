package com.hugo.study_richtext.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.bobomee.android.mentions.edit.MentionEditText;
import com.bobomee.android.mentions.edit.listener.InsertData;
import com.bobomee.android.mentions.model.Range;
import com.bobomee.android.mentions.text.MentionTextView;
import com.hugo.study_richtext.R;
import com.hugo.study_richtext.databinding.ActivityMain2Binding;
import com.hugo.study_richtext.test.edit.MentionData;
import com.hugo.study_richtext.test.edit.Tag;
import com.hugo.study_richtext.test.edit.TagListActivity;
import com.hugo.study_richtext.test.parser.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * tag半角转全角不行，太难看了
 */
public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    ActivityMain2Binding binding;
    MentionEditText mMentionedittext;

    Button mBtnCovert;

    TextView mCovertedString;

    Button mBtnClear;

    Button mAtUser;

    Button mTopic;

    Button mInsert;

    Button mBtnShow;
    MentionTextView mMentiontextview;
    private MainActivity2 mMainActivity;

    public static final int REQUEST_USER_APPEND = 1 << 2;
    public static final int REQUEST_TAG_APPEND = 1 << 3;

    private Parser mTagParser = new Parser();
    private JSONArray tagArr;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mMainActivity = this;

        initViews();
        initListener();
    }

    private void initViews() {
        mMentionedittext = binding.mentionedittext;
        mBtnCovert = binding.btnCovert;
        mCovertedString = binding.covertedString;
        mBtnClear = binding.btnClear;
        mAtUser = binding.atUser;
        mTopic = binding.topic;
        mInsert = binding.insert;
        mBtnShow = binding.btnShow;
        mMentiontextview = binding.mentiontextview;


        mCovertedString.setMovementMethod(new ScrollingMovementMethod());
        mMentiontextview.setMovementMethod(new LinkMovementMethod());
        mMentiontextview.setParserConverter(mTagParser);

        mMentiontextview.setText(str2);
        // 成功获取无格式的字符串
        Log.e("-->>", "获取剥去格式后的字符串 " + mMentiontextview.getText());

        // 编辑帖子时，有话题的场景，将格式化的tag，解析为有颜色的字符串
        // 目前MentionEditText是不带格式的string，携带格式后，计算tag的起始、接受位置会出问题
        // 暂时看是不是将 带格式的string先转化为不带格式的string，然后设置给MentionEditText
        binding.mentionedittext.setText(str2);
    }

    private void initListener() {

        binding.btnCovert.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
        binding.atUser.setOnClickListener(this);
        binding.topic.setOnClickListener(this);
        binding.insert.setOnClickListener(this);
        binding.btnShow.setOnClickListener(this);
        binding.restore.setOnClickListener(this);


        mMentionedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && !TextUtils.isEmpty(s)) {
                    char mentionChar = s.toString().charAt(start);
                    int selectionStart = mMentionedittext.getSelectionStart();
                    if (mentionChar == '@') {
//                        startActivityForResult(UserList.getIntent(mMainActivity), REQUEST_USER_APPEND);
//                        mMentionedittext.getText().delete(selectionStart - 1, selectionStart);
                    } else if (mentionChar == '#') {
                        startActivityForResult(TagListActivity.getIntent(mMainActivity), REQUEST_TAG_APPEND);
                        mMentionedittext.getText().delete(selectionStart - 1, selectionStart);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && null != data) {
            switch (requestCode) {
                case REQUEST_USER_APPEND:
//                    User user = (User) data.getSerializableExtra(UserList.RESULT_USER);
//                    mMentionedittext.insert(user);
                    break;
                case REQUEST_TAG_APPEND:
                    Tag tag = (Tag) data.getSerializableExtra(TagListActivity.RESULT_TAG);
                    mMentionedittext.insert(tag);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_covert:
                CharSequence convertMetionString = mMentionedittext.getFormatCharSequence();
                mCovertedString.setText(convertMetionString);
                break;
            case R.id.btn_clear:
                mMentionedittext.clear();
                mCovertedString.setText("");
                mMentiontextview.setText("");
                break;
            case R.id.at_user:
//                startActivityForResult(UserList.getIntent(mMainActivity), REQUEST_USER_APPEND);
                break;
            case R.id.topic:
                startActivityForResult(TagListActivity.getIntent(mMainActivity), REQUEST_TAG_APPEND);
                break;
            case R.id.insert:
                mMentionedittext.insert("http://www.baidu.com/");
                break;
            case R.id.btn_show:
                CharSequence convertMetionString1 = mMentionedittext.getFormatCharSequence();
                mMentiontextview.setText(convertMetionString1);

//                getTagArr();
                break;
            case R.id.restore:
                try {
                    tagArr = getTagArr();
                    content = binding.mentionedittext.getText().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.mentionedittext.clear();
                Handler handler = new Handler(getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Range> ranges = MentionData.convertRanges(tagArr);
                        binding.mentionedittext.restore(content, ranges);
                    }
                }, 2000);
                break;
        }
    }

    public JSONArray getTagArr() throws JSONException {
        ArrayList<? extends Range> ranges = binding.mentionedittext.getRangeManager().get();

        JSONArray tagJsonArr = new JSONArray();
        for (Range range : ranges) {
            InsertData insertData = range.getInsertData();
            if (insertData instanceof Tag) {
                /*JSONObject jsonObject = new JSONObject();
                jsonObject.put("topic_id", ((Tag) insertData).getTagId());
                jsonObject.put("topic_name", ((Tag) insertData).getTagLable());
                jsonObject.put("start", range.getFrom());
                jsonObject.put("end", range.getTo());

                tagJsonArr.put(jsonObject);*/

//                MentionData mentionData = MentionData.convertMention(range, (Tag) insertData);
//                JSONObject object = new JSONObject();
                JSONObject jsonObject = MentionData.convertJsonObject(range, (Tag) insertData);
                tagJsonArr.put(jsonObject);
            }
        }

        return tagJsonArr;
    }

    private void recoverMentions(JSONArray jsonArray) {

    }


    String str2 =
            "myfont效果：<tag id='100'>导演：轻口味</tag>副导演:重口味  再来一次<tag id='100'>导演：轻口味</tag>";


}
