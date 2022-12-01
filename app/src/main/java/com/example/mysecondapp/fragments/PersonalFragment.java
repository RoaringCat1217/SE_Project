package com.example.mysecondapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mysecondapp.BackendUtils;
import com.example.mysecondapp.R;
import com.example.mysecondapp.UserInfo;
import com.example.mysecondapp.activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private ImageView avatar;
    private TextView user_name;
    private TextView true_gender;
    private TextView gender_edit;
    private TextView true_phone;
    private TextView phone_edit;
    private TextView true_age;
    private TextView age_edit;

    private LinearLayout gender;
    private LinearLayout phone;
    private LinearLayout age;

    private final String content;
    public PersonalFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,container,false);
        user_name = (TextView) view.findViewById(R.id.usenameText);
        avatar = (ImageView) view.findViewById(R.id.imageView2);
        true_gender = (TextView) view.findViewById(R.id.true_gender);
        gender_edit = (TextView) view.findViewById(R.id.gender_edit);
        true_phone = (TextView) view.findViewById(R.id.true_phone);
        phone_edit = (TextView) view.findViewById(R.id.phone_edit);
        true_age = (TextView) view.findViewById(R.id.true_age);
        age_edit = (TextView) view.findViewById(R.id.age_edit);

        gender = (LinearLayout) view.findViewById(R.id.gender_entry);
        phone = (LinearLayout) view.findViewById(R.id.phone_entry);
        age = (LinearLayout) view.findViewById(R.id.age_entry);

        avatar.setOnClickListener(this);
        gender.setOnClickListener(this);
        phone.setOnClickListener(this);
        age.setOnClickListener(this);
        return view;
    }

    public void showInfo(String id){
        user_name.setText(id);
        Map<String, String> query = new HashMap<>();
        query.put("name", id);
        BackendUtils.get(this.getActivity(), "getuserinfo", query, (JSONObject json)->{
            try {
                for(int i = 0; i < json.length(); ++i) {
                    System.out.println(json.names().get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                long retCode = json.getLong("code");
                System.out.println(retCode);
                if (retCode == 1 || retCode == 0) {
                    String gender = json.getString("gender");
                    String phone = json.getString("phone_number");
                    int age = json.getInt("age");
                    String head = json.getString("avatar");

                    gender_edit.setText(gender + " >  ");
                    phone_edit.setText(phone + " >  ");
                    age_edit.setText(age + " >  ");

                    if(retCode == 1) {
                        Base64.Decoder decoder = Base64.getDecoder();
                        head = head.replace(" ", "+");
                        byte[] decoded = decoder.decode(head.split(",")[1]);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                        avatar.setImageBitmap(bitmap);
                    }
                }
                else
                    Toast.makeText(this.getActivity(), "获取个人信息失败!", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView2:
                showTypeDialog();
                break;
            case R.id.gender_entry:
                showGenderDialog();
                break;
            case R.id.phone_entry:
                showPhoneDialog();
                break;
            case R.id.age_entry:
                showAgeDialog();
                break;
            default:
                break;
        }
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
