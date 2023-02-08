package com.example.memo_android;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    ListView listView;
    FileSystem fileSystem;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        fileSystem = new FileSystem();
        listAdd(v);

        return v;
    }

    public void listAdd(View v){
        Bundle bundle = new Bundle();

        ArrayList<String> listItem = new ArrayList<>();
        ArrayAdapter<String> adapter;
        File[] list = fileSystem.isFileExistsCheck();
        for(File file : list){
            listItem.add(file.getName());
        }
        listView = v.findViewById(R.id.fileList);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            WriteFragment wr = new WriteFragment();
            String path = (String) adapterView.getItemAtPosition(i);
            bundle.putString("path", path);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            wr.setArguments(bundle);
            transaction.replace(R.id.frameLayout, wr);
            transaction.commit();
        });
    }
}