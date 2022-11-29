package com.example.mysecondapp.fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mysecondapp.BackendUtils;
import com.example.mysecondapp.LoginUtils;
import com.example.mysecondapp.R;
import com.example.mysecondapp.UserInfo;
import com.example.mysecondapp.activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditFragment extends Fragment {
    private Spinner spinner;
    private EditText etTitle, etContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit,container,false);
        etTitle = view.findViewById(R.id.et_title);
        etContent = view.findViewById(R.id.et_content);

        // 这里用来设置Spinner的监听事件
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String info = (String) spinner.getSelectedItem();
                Toast.makeText(getContext(),info,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public void send(View view) {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        // 后端交互
        // 板块的信息可以设类变量 从上面的onItemSelected()里拿
        // 成功发帖后跳转到对应板块界面？还是停留在当然界面+加入自己的收藏帖子（这种情况需要和后端商量）？

//        Map<String, String> query = new HashMap<>();
//        query.put("username", id);
//        query.put("password", pwd);
//        BackendUtils.get(this, "register", query, this::signupCallback);
    }

    private void sendCallback(JSONObject json) {
//        try {
//            long retCode = json.getLong("code");
//            if (retCode == 1) {
//                Toast.makeText(LoginActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
//                loginSuccess();
//            }
//            else
//                Toast.makeText(LoginActivity.this, "注册失败! 账号已存在.", Toast.LENGTH_SHORT).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void sendSuccess() {
//        LoginUtils.login = true;
//        UserInfo.userID = etUserID.getText().toString().trim();
//        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LoginUtils.callback != null)
            LoginUtils.callback.run();
    }
}
