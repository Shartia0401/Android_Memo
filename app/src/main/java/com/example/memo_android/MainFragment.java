package com.example.memo_android;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class MainFragment extends Fragment {
    ListView listView;
    FileSystem fileSystem;

    SendPath sendPath;

    DB_Helper dbHelper;

    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);



        try{
            sendPath = (SendPath) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context + " must");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        fileSystem = new FileSystem();
        listAdd(v);
        dbHelper = new DB_Helper(getActivity());
        return v;
    }

    public void listAdd(View v){
        listItem = new ArrayList<>();

        File[] list = fileSystem.isFileExistsCheck();
        for(File file : list){
            listItem.add(file.getName());
        }
        listView = v.findViewById(R.id.fileList);
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String path = (String) adapterView.getItemAtPosition(i);

            boolean isModify = false;
            if(getArguments() != null ){
                isModify = getArguments().getBoolean("isModify");
            }
            if(isModify){
                onClick_setting_costume_save(v, path, adapterView, i);


            }else{
                sendPath.sendPath(path);
            }
        });
    }

    public void onClick_setting_costume_save(View view, String path, AdapterView<?> adapterView, int i){
        new AlertDialog.Builder(getActivity())
                .setTitle("파일 삭제")
                .setMessage("파일을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.no, (dialog, whichButton) -> {})
                .setNegativeButton(android.R.string.yes, (dialog, whichButton) -> {
                    // 확인시 처리 로직
                    fileSystem.fileDel(path);
                    dbHelper.delete(path.substring(0, path.length()-4));
                    Toast.makeText(getActivity(), "삭제를 완료했습니다.", Toast.LENGTH_SHORT).show();

                    listItem.remove(adapterView.getItemAtPosition(i));
                    adapter.notifyDataSetChanged();
                })
                .show();
    }
}