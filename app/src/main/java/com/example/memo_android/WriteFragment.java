package com.example.memo_android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class WriteFragment extends Fragment {

    private EditText et_title, et_content;
    private boolean first;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(this.getContext(), "asd", Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_write, container, false);

        et_title = v.findViewById(R.id.TitleText);
        et_content = v.findViewById(R.id.MainText);
        first = true;
        return v;
    }
    public String title() {
        String title = et_title.getText().toString().trim();
        return title;
    }
    public String content(){
        String content = et_content.getText().toString().trim();
        return content;
    }
    public void newtext(){
        if(first){
            if(et_content.getText().toString().length() != 0 || et_title.getText().toString().length() != 0){
                et_title.setText("");
                et_content.setText("");
            }
        }
    }





}