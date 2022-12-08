package com.example.mysecondapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.example.mysecondapp.activities.PostDisplayActivity;
import com.example.mysecondapp.models.CommentItem;
import com.example.mysecondapp.utils.BackendUtils;
import com.example.mysecondapp.R;
import com.example.mysecondapp.utils.UserInfo;
import com.example.mysecondapp.activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private ImageView avatar;
    private TextView user_name;
    private TextView true_phone;
    private TextView phone_edit;
    private TextView true_age;
    private TextView age_edit;


    private RadioGroup rg;
    private Button saveBt;
    private CircleImageView usrPortrait;

    private final String content;
    public PersonalFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,container,false);
        user_name = view.findViewById(R.id.usenameText);

        rg = view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                // 性别信息
                // ……
                int ifMale = R.id.male;

            }
        });

        saveBt = (Button) view.findViewById(R.id.save_bt);
        saveBt.setOnClickListener(view1 -> {
            // 点击了“保存”按钮
            // 告诉后端：updateuserinfo
        });

        usrPortrait = (CircleImageView) view.findViewById(R.id.usr_portrait);
        usrPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 上传照片

            }
        });

        true_phone = (TextView) view.findViewById(R.id.true_phone);
        phone_edit = (TextView) view.findViewById(R.id.phone_edit);
        true_age = (TextView) view.findViewById(R.id.true_age);
        age_edit = (TextView) view.findViewById(R.id.age_edit);

        fetchInfo();

        return view;
    }

    public void fetchInfo(){
        user_name.setText(UserInfo.userID);
        Map<String, String> query = new HashMap<>();
        query.put("username", UserInfo.userID);
        BackendUtils.get(getActivity(), "getuserinfo", query, this::fetchInfoCallback);
        BackendUtils.getAvatar(getActivity(), UserInfo.userID, img -> usrPortrait.setImageBitmap(img));
    }

    public void fetchInfoCallback(JSONObject json){
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                String gender = json.getString("gender");
                if (gender.equals("female"))
                    rg.check(R.id.female);
                else
                    rg.check(R.id.male);
                String phone = json.getString("phone_number");
                true_phone.setText(phone);
                int age = json.getInt("age");
                true_age.setText(Integer.valueOf(age).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.imageView2:
//                showTypeDialog();
//                break;
//            case R.id.gender_entry:
//                showGenderDialog();
//                break;
//            case R.id.phone_entry:
//                showPhoneDialog();
//                break;
//            case R.id.age_entry:
//                showAgeDialog();
//                break;
//            default:
//                break;
//        }
    }

    private void showAgeDialog() {
    }

    private void showPhoneDialog() {
    }

    private void showGenderDialog() {
    }

    private void showTypeDialog() {
    }
}
