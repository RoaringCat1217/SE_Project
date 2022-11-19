package com.example.mysecondapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.*;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup rgMain;
    private RadioButton rbMain;
    private HitsFragment hitsFragment;
    private FavoriteFragment favoriteFragment;
    private EditFragment editFragment;
    private GroupFragment groupFragment;
    private PersonalFragment personalFragment;
    private FragmentManager fragmentManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        rgMain = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rgMain.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) this);
        //获取第一个单选按钮，并设置其为选中状态
        rbMain = (RadioButton) findViewById(R.id.rb_hits);
        rbMain.setChecked(true);

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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fTransaction);
        if(checkedId == R.id.rb_hits){
            if(hitsFragment == null){
                hitsFragment = new HitsFragment();
                fTransaction.add(R.id.ly_content,hitsFragment);
            }else
                fTransaction.show(hitsFragment);
        }else if(checkedId == R.id.rb_favorite){
            if(favoriteFragment == null){
                favoriteFragment = new FavoriteFragment();
                fTransaction.add(R.id.ly_content,favoriteFragment);
            }else
                fTransaction.show(favoriteFragment);
        }else if(checkedId == R.id.rb_edit){
            if(editFragment == null){
                editFragment = new EditFragment();
                fTransaction.add(R.id.ly_content,editFragment);
            }else
                fTransaction.show(editFragment);
        }else if(checkedId == R.id.rb_group){
            if(groupFragment == null){
                groupFragment = new GroupFragment();
                fTransaction.add(R.id.ly_content,groupFragment);
            }else
                fTransaction.show(groupFragment);
        }else if(checkedId == R.id.rb_personal){
            if(personalFragment == null){
                personalFragment = new PersonalFragment("个人");
                fTransaction.add(R.id.ly_content,personalFragment);
            }else
                fTransaction.show(personalFragment);
        }
        fTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(hitsFragment != null)fragmentTransaction.hide(hitsFragment);
        if(favoriteFragment != null)fragmentTransaction.hide(favoriteFragment);
        if(editFragment != null)fragmentTransaction.hide(editFragment);
        if(groupFragment != null)fragmentTransaction.hide(groupFragment);
        if(personalFragment != null)fragmentTransaction.hide(personalFragment);
    }

    public void go_back(View view) {
    }

    public void settings(View view) {
    }
}