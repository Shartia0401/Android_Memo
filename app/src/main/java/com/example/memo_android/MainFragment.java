package com.example.memo_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    ListView listView;
    private ArrayList<String> listItem;
    MainActivity act;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        act = new MainActivity();
        listAdd(v);
        return v;
    }

    ArrayAdapter<String> adapter;
    public void listAdd(View v){
        listItem = new ArrayList<>();

        File[] list = act.isFileExistsCheck();
        for(File file : list){
            listItem.add(file.getName().toString());
        }
        listView = v.findViewById(R.id.fileList);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listItem);
        listView.setAdapter(adapter);

    }


}