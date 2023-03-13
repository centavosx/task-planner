package com.vll.planner.ui.time;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vll.planner.R;
import com.vll.planner.SaveText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class TimeFragments extends Fragment implements View.OnClickListener{
    private SharedPreferences myPref;
    private SaveText saveText = new SaveText();
    private String text = saveText.d;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        View view =inflater.inflate(R.layout.time, container, false);
        Calendar cal = Calendar.getInstance();
        myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = myPref.getAll();
        ArrayList<String> arrtodo = new ArrayList<String>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().contains("events_date")){
                arrtodo.add(entry.getKey());
            }
        }
        Calendar c = Calendar.getInstance();

        TextView datetoday = view.findViewById(R.id.todaydate);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        ArrayList<String>array = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            array.add(df.format(c.getTime()));
            c.add(Calendar.DATE, 1);
        }
        DateFormat df2 = new SimpleDateFormat("EEE MM/dd/yyyy");
        Date datenow= new Date(System.currentTimeMillis());
        datetoday.setText(df2.format(datenow));
        DateFormat dateFormat = new SimpleDateFormat("a");
        TextView textView = view.findViewById(R.id.todaydate2);
        textView.append("\n"+array.get(0) + " to "+array.get(array.size()-1) + " (" + (text.toUpperCase()) + ") ");

        for(int g=0; g<arrtodo.size(); g++) {
            String sDate1 = myPref.getString(arrtodo.get(g), "");

            String spl[] = arrtodo.get(g).split("events_date");
            if(array.contains(sDate1)){
            for(int i=0; i<12; i++) {
                int eventindex = Integer.parseInt(spl[spl.length - 1]);
                String[] a = {"thurswe","tueswe","monwe","wedwe","friwe","satwe","timewe","sunwe"};

                TextView textViewx;
                String s = String.valueOf(i);
                textViewx = (TextView) view.findViewWithTag(a[0]+s);
                String[] v = textViewx.getText().toString().split(":");
                String[] arrsplit = myPref.getString("events_time" + eventindex, "").split(":");

                if ( arrsplit[0].equals(v[0]) && myPref.getString("events_amorpm" + eventindex, "").equals(text)) {

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        Date date1 = format.parse(sDate1);
                        cal.setTime(date1);
                        checkdate(cal, view, eventindex, i, a);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        }
        Button buttonam = view.findViewById(R.id.btnam);
        Button buttonpm = view.findViewById(R.id.btnpm);
        buttonam.setOnClickListener(this);
        buttonpm.setOnClickListener(this);
        return view;
    }
    private void checkdate(Calendar cal, View view, int eventindex, int val, String a[]){
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            TextView textView = view.findViewWithTag(a[1]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            TextView textView = view.findViewWithTag(a[2]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            TextView textView = view.findViewWithTag(a[3]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            TextView textView = view.findViewWithTag(a[4]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            TextView textView = view.findViewWithTag(a[5]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            TextView textView = view.findViewWithTag(a[6]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            TextView textView = view.findViewWithTag(a[7]+val);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            if(textView.getText().toString().equals("----")){
                textView.setText(myPref.getString("events_todo"+eventindex, ""));
            }else {
                textView.append("\n" + myPref.getString("events_todo" + eventindex, ""));
            }
        }
    }
    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btnam:

               saveText.d ="am";
               Fragment fragment1 = new TimeFragments();
               replaceFragment(fragment1);
               break;

           case R.id.btnpm:
               saveText.d ="pm";
               Fragment fragment2 = new TimeFragments();
               replaceFragment(fragment2);
               break;
       }
    }
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}