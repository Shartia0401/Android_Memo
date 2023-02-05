package com.example.memo_android;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;


public class WriteFragment extends Fragment{

    private EditText et_title, et_content;
    private  MainActivity act;
    private SearchView searchView;
    private boolean first;
    private boolean isBold;

    SpannableString spannableString;
    private SeekBar sizeBar;
    private Switch isBoldSw;
    private String defaultTxT;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write, container, false);


        fnClear(v);
        getFile();
        return v;
    }

    private void getFile(){
        if(getArguments() != null){
            first = false;
            String name = getArguments().getString("path");
            act.openFile(name);
            setText(act.listPass(), act.namePass());
        }
    }



    private void setSw(){
        isBoldSw.setOnCheckedChangeListener((compoundButton, b) -> {
            spannableString = new SpannableString(et_content.getText().toString());
            if(b){
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),0, et_content.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                et_content.setText(spannableString);
                isBold = true;
            }else{
                et_content.setText(defaultTxT);
                isBold = false;
            }
        });
    }

    private  void setSizeBar(){
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("test" , Integer.toString(seekBar.getProgress()));
            }
        });

    }


    private void fnClear(View v){
        act = new MainActivity();
        searchView = v.findViewById(R.id.SearchView);
        et_title = v.findViewById(R.id.TitleText);
        et_content = v.findViewById(R.id.MainText);
        sizeBar = v.findViewById(R.id.SizeBar);
        isBoldSw = v.findViewById(R.id.IsBoldSw);
        setSw();
        setSizeBar();
        first = true;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {return false;}
        });
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
            if(defaultTxT == null){
                defaultTxT = et_content.getText().toString().trim();
            }
        }catch (NullPointerException e){
            e.getStackTrace();
            defaultTxT = null;
        }
        content = defaultTxT;
        return content;
    }

    public String content(ArrayList<String> text_list){
        String content = "";

        for(int i = 0; i < text_list.size(); i++){
            content += text_list.get(i);
        }
        defaultTxT = content;
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
        String A = et_content.getText().toString();
    }

    public void setFont(String source, ArrayList<Integer> index, String word){
        spannableString = new SpannableString(source);
        for(int idx : index){
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), idx, idx + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new RelativeSizeSpan(1.3f),idx, idx + word.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        et_content.setText(spannableString);
    }

    public void search(String word){
        boolean asd = true;

        String source = et_content.getText().toString();
        ArrayList<Integer> index = new ArrayList<Integer>();
        int fromindex = 0;
        int i = 0;
        while(asd){
            if(source.indexOf(word, fromindex) != -1){
                index.add(source.indexOf(word, fromindex));
                fromindex = index.get(i);
                ++fromindex;
                ++i;
            }else{
                asd = false;
            }
        }
        setFont(source, index, word);
    }
}