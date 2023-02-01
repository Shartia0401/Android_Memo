package com.example.memo_android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


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
        String title;
        try{
            title = et_title.getText().toString().trim();
        }catch (NullPointerException e){
            e.getStackTrace();
            title = null;
        }

        return title;
    }
    public String title(String name){
        String title;
        try{
            title = name;
        }catch (NullPointerException e){
            e.getStackTrace();
            title = null;
        }
        return title;
    }

    public String content(){
        String content;
        try{
            content = et_content.getText().toString().trim();
        }catch (NullPointerException e){
            e.getStackTrace();
            content = null;
        }
        return content;
    }

    public String content(ArrayList<String> text_list){
        String content = null;

        for(int i = 0; i < text_list.size(); i++){
            content += text_list.get(i);
            Log.d("test", content);
        }

        return content;
    }
    public void newText(){
        if(first){
            if(et_content.getText().toString().length() != 0 || et_title.getText().toString().length() != 0){
                et_title.setText("");
                et_content.setText("");
            }
        }
    }
    public void setText(ArrayList<String> txtlist, String fileName){
        et_title.setText(title(fileName));
        et_content.setText(content(txtlist));
    }


}