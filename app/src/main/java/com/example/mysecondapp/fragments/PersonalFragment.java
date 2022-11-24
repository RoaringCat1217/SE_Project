package com.example.mysecondapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.mysecondapp.R;

public class PersonalFragment extends Fragment {

    private final String content;
    public PersonalFragment(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.fragment_content);
        txt_content.setText(content);
        return view;
    }
}
