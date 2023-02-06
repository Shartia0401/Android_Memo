package com.example.Memo_android;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;


public class WriteFragment extends Fragment{

    static private EditText et_title, et_content;
    private  MainActivity act;
    private SearchView searchView;
    private boolean first;
    public boolean isBold;
    public int currentSize;

    SpannableString spannableString;
    private SeekBar sizeBar;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch isBoldSw;
    private String defaultTxT;
    private String defaultTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write, container, false);
        defaultTxT = "";
        fnClear(v);
        getFile();

        spannableString = new SpannableString(defaultTxT);
        return v;
    }

    private void getFile(){
        if(getArguments() != null){
            first = false;
            String name = getArguments().getString("path");
            act.openFile(name);
            act.openContent(act.openFile(name));
            setText(act.listPass(), act.namePass());
        }
    }

    private void setSw(){
        isBoldSw.setOnCheckedChangeListener((compoundButton, b) -> {
            isBold = b;
            setFontSize();
        });
    }
    private  void setSizeBar(){
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentSize = seekBar.getProgress();
                setFontSize();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }



    private void textAct(){

        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                defaultTitle = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력난 변화

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                defaultTxT = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력하기전에 조치
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
        currentSize = 10;
        sizeBar.setProgress(10);


        textAct();
        setSw();
        setSizeBar();
        first = true;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }
        });
    }
    public String title() {
        String title = null;
        try{
            title = et_title.getText().toString().trim();
        }catch (NullPointerException e){
            e.getStackTrace();
            title = null;
        }

        return title;
    }
    public String title(String name){
        String title = null;
        try{
            title = name;
        }catch (NullPointerException e){
            e.getStackTrace();
            title = null;
        }
        return title;
    }

    public String content(){
        String content = null;

        try{
            defaultTxT = et_content.getText().toString().trim();
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
                defaultTitle = "";
                defaultTxT = "";
                isBold = false;
                currentSize = 10;
                first = false;
                et_title.setText(defaultTitle);
                et_content.setText(defaultTxT);
            }
        }
    }
    public void setText(ArrayList<String> txtlist, String fileName){
        et_title.setText(title(fileName));
        et_content.setText(content(txtlist));

        setFontSize();
    }

    public void setFont(ArrayList<Integer> index, String word){
        setFontSize();
        for(int idx : index){
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), idx, idx + word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new RelativeSizeSpan(1.3f),idx, idx + word.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        et_content.setText(spannableString);
    }

    private void setFontSize(){
        spannableString = new SpannableString(defaultTxT);

        spannableString.setSpan(new RelativeSizeSpan((float)currentSize/10),0,defaultTxT.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_content.setText(spannableString);

        if(isBold){
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),0, et_content.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            et_content.setText(spannableString);
        }else{
            et_content.setText(spannableString);
        }
    }

    public void search(String word){
        boolean asd = true;
        ArrayList<Integer> index = new ArrayList<Integer>();
        int fromindex = 0;
        int i = 0;
        while(asd){
            if(defaultTxT.indexOf(word, fromindex) != -1){
                if(word.length() == 0){
                    asd = false;
                }
                index.add(defaultTxT.indexOf(word, fromindex));
                fromindex = index.get(i);
                ++fromindex;
                ++i;
            }else{
                asd = false;
            }
        }
        setFont(index, word);
    }
}