package com.vll.planner.ui.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vll.planner.R;
import com.vll.planner.SaveText;
import com.vll.planner.ui.addanote.AddNote;

import java.util.ArrayList;
import java.util.Map;

public class NotesFragments extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private ImageButton button;
    private SharedPreferences myPref;
    private LinearLayout linearLayout;
    private ArrayList<EditText>arrlist = new ArrayList<EditText>();
    private ArrayList<String>subject = new ArrayList<String>();
    private ArrayList<String>text = new ArrayList<String>();
    private ArrayList<Integer>index = new ArrayList<Integer>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View root = inflater.inflate(R.layout.notes, container, false);
        myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = myPref.getAll();
        ArrayList<String>arrlistsubject = new ArrayList<String>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().contains("notes_subject")){
                arrlistsubject.add(entry.getKey());
            }
        }

        linearLayout = root.findViewById(R.id.allnotes);
        for(int g=0; g<arrlistsubject.size(); g++){
           String[] spl = arrlistsubject.get(g).split("notes_subject");

           int noteindex = Integer.parseInt(spl[spl.length-1]);
                   EditText valueTV = new EditText(getActivity());
                   String sub = myPref.getString(arrlistsubject.get(g), "");
                   valueTV.setText(sub);
                   valueTV.setId(noteindex);

                    valueTV.setFocusable(false);
                    valueTV.setFocusableInTouchMode(false);
                    valueTV.setClickable(false);
                    valueTV.setTextAppearance(getActivity(), R.style.fontStyleforitems);
                   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                           LinearLayout.LayoutParams.MATCH_PARENT,
                           LinearLayout.LayoutParams.WRAP_CONTENT);
                   layoutParams.setMargins(0,10,0,10);
                   valueTV.setLayoutParams(layoutParams);
                   valueTV.setTextSize(18);
                   linearLayout.addView(valueTV);
                   valueTV.setOnClickListener(this);
                   valueTV.setOnLongClickListener(this);
                   arrlist.add(valueTV);
                   index.add(noteindex);
                   subject.add(sub);
                   text.add(myPref.getString("notes_text"+noteindex, ""));
        }

        button = (ImageButton) root.findViewById(R.id.addanotes);
        button.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View view) {
        Fragment fragment;
        for (int i=0; i<arrlist.size(); i++){
            EditText ed = arrlist.get(i);
            if(view.getId() == ed.getId()){
                SaveText saveText = new SaveText();
                saveText.prefkey = arrlist.get(i).getId();
                saveText.subject = subject.get(i);
                saveText.text = text.get(i);
                fragment = new AddNote();
                replaceFragment(fragment);
                break;
            }
        }
        if(view.getId() == R.id.addanotes){
            SaveText saveText = new SaveText();
            saveText.prefkey = 0;
            saveText.subject = "";
            saveText.text = "";
            fragment = new AddNote();
            replaceFragment(fragment);
        };
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onLongClick(View view) {
        int i= 0;
        for (i=0; i<arrlist.size(); i++) {
            EditText ed = arrlist.get(i);
            if (view.getId() == ed.getId()) {

                showPopUp(ed, index.get(i));

                break;
            }
        }
        return true;
    }
    public void showPopUp(final EditText ed, final int indexx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.popupdelete, null);
        Button done = customView.findViewById(R.id.done);
        Button notdone = customView.findViewById(R.id.notfinished);



        builder.setView(customView);
        final AlertDialog dialog = builder.create();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPref.edit().remove("notes_text"+indexx).apply();
                myPref.edit().remove("notes_subject"+indexx).apply();
                arrlist.remove(ed);
                linearLayout.removeView(ed);
                dialog.dismiss();
            }
        });
        notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}