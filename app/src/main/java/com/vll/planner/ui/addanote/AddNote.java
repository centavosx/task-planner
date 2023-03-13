package com.vll.planner.ui.addanote;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vll.planner.R;
import com.vll.planner.SaveText;
import com.vll.planner.ui.notes.NotesFragments;

import java.util.ArrayList;
import java.util.Map;

public class AddNote extends Fragment implements View.OnClickListener{

    private SharedPreferences myPref;
    private EditText subject, message;
    private int id = 0;
    private String sub;
    private Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view =inflater.inflate(R.layout.addnote, container, false);
        myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        subject = view.findViewById(R.id.subject);
        message = view.findViewById(R.id.messages);
        SaveText saveText = new SaveText();
        if(saveText.prefkey != 0 ){
            subject.setText(saveText.subject);
            message.setText(saveText.text);
            id = saveText.prefkey;
        }else{
            Map<String, ?> allEntries = myPref.getAll();
            ArrayList<String>arrlistsubject = new ArrayList<String>();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if(entry.getKey().contains("notes_subject")){
                    arrlistsubject.add(entry.getKey());
                }
            }
            if(arrlistsubject.size()!=0) {
                String[] spl = arrlistsubject.get(arrlistsubject.size() - 1).split("notes_subject");
                id = Integer.parseInt(spl[spl.length - 1]) + 1;
            }else{
                id= 1;
            }
        }
        button = view.findViewById(R.id.save);
        Button button3 = view.findViewWithTag("cancels");
        button3.setOnClickListener(this);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                sub = subject.getText().toString();
                String mes = message.getText().toString();
                myPref.edit().putString("notes_subject"+id, sub).apply();
                myPref.edit().putString("notes_text"+id, mes).apply();
                Toast.makeText(getActivity(), "Done!", Toast.LENGTH_SHORT).show();
                break;
        }
        if ("cancels".equals(view.getTag())) {
            Fragment fragment1 = new NotesFragments();
            replaceFragment(fragment1);
        }
    }
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}