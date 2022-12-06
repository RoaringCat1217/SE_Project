package com.example.mysecondapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mysecondapp.fragments.GroupListFragment;
import com.example.mysecondapp.utils.Constants;
import com.example.mysecondapp.utils.LoginUtils;
import com.example.mysecondapp.fragments.EditFragment;
import com.example.mysecondapp.fragments.FavoriteFragment;
import com.example.mysecondapp.fragments.GroupFragment;
import com.example.mysecondapp.fragments.HitsFragment;
import com.example.mysecondapp.fragments.PersonalFragment;
import com.example.mysecondapp.R;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private HitsFragment hitsFragment;
    private FavoriteFragment favoriteFragment;
    private EditFragment editFragment;
    private GroupFragment groupFragment;
    private PersonalFragment personalFragment;
    private FragmentManager fragmentManager;
    private GroupListFragment groupListFragment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        RadioGroup rgMain = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rgMain.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) this);
        //获取第一个单选按钮，并设置其为选中状态
        RadioButton rbMain = (RadioButton) findViewById(R.id.rb_hits);
        rbMain.setChecked(true);

        // 搜索
        ImageButton searchButton = findViewById(R.id.search_bt);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchKey = v.findViewById(R.id.search_key);
                String key = searchKey.getText().toString().trim();
                // 发给后端搜索
                // 跳到SearchFragment（用类似Group->GroupList的逻辑）
                // ……
            }
        });

        // 回退（不知道能不能成功运行）
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        } catch (Exception e) {
                            Log.e("Exception when sendKeyDownUpSync",
                                    e.toString());
                        }
                    }
                }.start();
            }
        });
    }

    public void showGroupList(String group) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fTransaction);
        groupListFragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.GROUP_NAME, group);
        groupListFragment.setArguments(bundle);
        fTransaction.add(R.id.ly_content, groupListFragment);
        fTransaction.commit();
    }

    public void showSearchList(String search) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fTransaction);
        if (checkedId == R.id.rb_hits) {
            if (hitsFragment == null) {
                hitsFragment = new HitsFragment();
                fTransaction.add(R.id.ly_content,hitsFragment);
            } else {
                // 切换回来时更新下热榜
                hitsFragment.fetchHotList();
                fTransaction.show(hitsFragment);
            }
            fTransaction.commit();
        } else if (checkedId == R.id.rb_favorite){
            // 收藏帖子要求登录
            LoginUtils.checkLogin(this, () -> {
                if (favoriteFragment == null){
                    favoriteFragment = new FavoriteFragment();
                    fTransaction.add(R.id.ly_content,favoriteFragment);
                } else
                    fTransaction.show(favoriteFragment);
                fTransaction.commit();
            });
        } else if(checkedId == R.id.rb_edit) {
            // 发帖要求登录
            LoginUtils.checkLogin(this, () -> {
                if (editFragment == null) {
                    editFragment = new EditFragment();
                    fTransaction.add(R.id.ly_content, editFragment);
                }
                else
                    fTransaction.show(editFragment);
                fTransaction.commit();
            });
        } else if (checkedId == R.id.rb_group) {
            if (groupFragment == null) {
                groupFragment = new GroupFragment();
                fTransaction.add(R.id.ly_content,groupFragment);
            } else
                fTransaction.show(groupFragment);
            fTransaction.commit();
        } else if(checkedId == R.id.rb_personal) {
            // 查看个人信息要求登录
            LoginUtils.checkLogin(this, ()->{
                if (personalFragment == null) {
                    personalFragment = new PersonalFragment("个人");
                    fTransaction.add(R.id.ly_content, personalFragment);
                } else
                    fTransaction.show(personalFragment);
                fTransaction.commit();
            });
        }
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(hitsFragment != null)fragmentTransaction.hide(hitsFragment);
        if(favoriteFragment != null)fragmentTransaction.hide(favoriteFragment);
        if(editFragment != null)fragmentTransaction.hide(editFragment);
        if(groupFragment != null)fragmentTransaction.hide(groupFragment);
        if(personalFragment != null)fragmentTransaction.hide(personalFragment);
        if (groupListFragment != null) fragmentTransaction.hide(groupListFragment);
    }
}