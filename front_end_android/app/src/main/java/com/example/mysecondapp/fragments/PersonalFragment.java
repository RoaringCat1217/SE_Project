package com.example.mysecondapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.mysecondapp.utils.Constants;
import com.example.mysecondapp.utils.UserInfo;
import com.example.mysecondapp.activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment{
    private TextView user_name;
    private TextView true_phone;
    private EditText phone_edit;
    private TextView true_age;
    private EditText age_edit;


    private RadioGroup rg;
    private Button saveBt;
    private CircleImageView usrPortrait;
    private boolean portraitUpdated = false;
    private boolean genderUpdated = false;
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,container,false);
        user_name = view.findViewById(R.id.usenameText);

        rg = view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            genderUpdated = true;
            if (checkedId == R.id.male)
                gender = "male";
            else
                gender = "female";
        });

        saveBt = view.findViewById(R.id.save_bt);
        saveBt.setOnClickListener(view1 -> updateInfo());

        usrPortrait = view.findViewById(R.id.usr_portrait);
        usrPortrait.setOnClickListener(v -> selectPhoto());

        true_phone = view.findViewById(R.id.true_phone);
        phone_edit = view.findViewById(R.id.phone_edit);
        true_age = view.findViewById(R.id.true_age);
        age_edit = view.findViewById(R.id.age_edit);


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
                if (phone.equals("-1"))
                    true_phone.setText("暂无电话号码");
                else
                    true_phone.setText(phone);
                int age = json.getInt("age");
                if (age == -1)
                    true_age.setText("暂无年龄");
                else
                    true_age.setText(Integer.valueOf(age).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Constants.SELECT_PHOTO);
    }

    public void selectPhotoCallback() {
        usrPortrait.setImageURI(UserInfo.uri);
        portraitUpdated = true;
    }

    private void updateInfo() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", UserInfo.userID);
            json.put("nickname", "-1");
            if (genderUpdated)
                json.put("gender", gender);
            if (phone_edit.getText().toString().length() > 0)
                json.put("phone_number", phone_edit.getText().toString());
            else
                json.put("phone_number", -1);
            if (age_edit.getText().toString().length() > 0)
                json.put("age", age_edit.getText().toString());
            else
                json.put("age", "-1");
            if (portraitUpdated)
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), UserInfo.uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                json.put("avatar", encoded);
            } else
                json.put("avatar", "-1");
            BackendUtils.post(getActivity(), "updateuserinfo", json, this::updateInfoCallback);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateInfoCallback(JSONObject json) {
        try {
            long retCode = json.getLong("code");
            if (retCode == 1) {
                portraitUpdated = false;
                genderUpdated = false;
                if (phone_edit.getText().toString().length() > 0) {
                    true_phone.setText(phone_edit.getText().toString());
                    phone_edit.setText(null);
                }
                if (age_edit.getText().toString().length() > 0) {
                    true_age.setText(age_edit.getText().toString());
                    age_edit.setText(null);
                }
                Toast.makeText(getActivity(), "更新成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "更新失败!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
