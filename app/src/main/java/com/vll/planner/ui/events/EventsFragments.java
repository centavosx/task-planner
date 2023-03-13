package com.vll.planner.ui.events;

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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vll.planner.R;
import com.vll.planner.ui.addevent.AddEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EventsFragments extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private SharedPreferences myPref;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout5;
    private  ArrayList<TextView>arrlist = new ArrayList<TextView>();
    private ArrayList<LinearLayout>linearlayoutsecond = new ArrayList<LinearLayout>();
    private ArrayList<String>todo = new ArrayList<String>();
    private ArrayList<String>desc = new ArrayList<String>();
    private ArrayList<String>date = new ArrayList<String>();
    private ArrayList<String>time = new ArrayList<String>();
    private ArrayList<String>amorpm = new ArrayList<String>();
    private ArrayList<TextView>arrlistexp = new ArrayList<TextView>();
    private ArrayList<Boolean> arraybool = new ArrayList<Boolean>();
    private ArrayList<Boolean> arrayboolexp = new ArrayList<Boolean>();
    private ArrayList<String>todoexp = new ArrayList<String>();
    private ArrayList<String>descexp = new ArrayList<String>();
    private ArrayList<String>dateexp = new ArrayList<String>();
    private ArrayList<String>timeexp = new ArrayList<String>();
    private ArrayList<String>amorpmexp = new ArrayList<String>();
    private ArrayList<Integer> whatkeyindex = new ArrayList<Integer>();
    private ArrayList<Integer> whatkeyindexexp = new ArrayList<Integer>();
    private ArrayList<CheckBox>checkBoxes = new ArrayList<CheckBox>();
    private  ImageButton addevent;
    private String know = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.events, container, false);
        addevent = view.findViewById(R.id.addevents);
        myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = myPref.getAll();
        ArrayList<String> arrtodo = new ArrayList<String>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().contains("events_date")){
                arrtodo.add(entry.getKey());
            }
        }

        linearLayout = view.findViewById(R.id.todoView);
        for(int g=0; g<arrtodo.size(); g++) {
            String[] spl = arrtodo.get(g).split("events_date");
            int eventindex = Integer.parseInt(spl[spl.length - 1]);
            String sDate1 = myPref.getString(arrtodo.get(g), "");
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = null;
            try {
                date1 = format.parse(sDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            Date datenow = new Date(System.currentTimeMillis());
            Date datetimes = new Date(sDate1);

            if ((!date1.before(datenow)) || (datenow.getDate() == datetimes.getDate() && datenow.getMonth() == datetimes.getMonth() && datenow.getYear() == datetimes.getYear())) {
                String sub = myPref.getString("events_todo" + eventindex, "");
                CheckBox valueTV = new CheckBox(getActivity());
                LinearLayout linearLayout2 = new LinearLayout(getActivity());
                linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(getActivity());
                textView.setText(sub);
                textView.setId(R.id.my_id + eventindex);
                textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(0, 10, 0, 10);
                textView.setLayoutParams(layoutParams2);
                valueTV.setChecked(myPref.getBoolean("events_checked" + eventindex, false));
                valueTV.setClickable(false);
                valueTV.setEnabled(false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 10);
                valueTV.setLayoutParams(layoutParams);
                valueTV.setTextSize(18);
                linearLayout2.addView(valueTV);
                linearLayout2.addView(textView);
                textView.setOnClickListener(this);
                textView.setOnLongClickListener(this);
                linearlayoutsecond.add(linearLayout2);
                linearLayout.addView(linearLayout2);
                arrlist.add(textView);
                checkBoxes.add(valueTV);
                arraybool.add(myPref.getBoolean("events_checked" + eventindex, false));
                todo.add(sub);
                desc.add(myPref.getString("events_desc" + eventindex, ""));
                date.add(myPref.getString("events_date" + eventindex, ""));
                time.add(myPref.getString("events_time" + eventindex, ""));
                amorpm.add(myPref.getString("events_amorpm" + eventindex, ""));
                whatkeyindex.add(eventindex);
            }else{
                int x = eventindex;
                int temp = eventindex;
                String subj = myPref.getString("events_todo" + eventindex, "");
                String d = myPref.getString("events_desc" + eventindex, "");
                String da = myPref.getString("events_date" + eventindex, "");
                String ti = myPref.getString("events_time" + eventindex, "");
                String am = myPref.getString("events_amorpm" + eventindex, "");
                Boolean ch = myPref.getBoolean("events_checked" + eventindex, false);
                Boolean trylng = true;
                while(true) {
                    if (myPref.contains("expired_todo" + x)){
                           if(!(myPref.getString("expired_todo" + x, "").equals(subj) &&
                            myPref.getString("expired_desc" + x, "").equals(d) &&
                            myPref.getString("expired_date" + x, "").equals(da) &&
                            myPref.getString("expired_time" + x, "").equals(ti) &&
                            myPref.getString("expired_amorpm" + x, "").equals(am) &&
                            myPref.getString("expired_checked" + x, "").equals(ch))) {
                               x+=1;
                           }else{
                               trylng = false;
                               break;
                           }
                    }else{
                       break;
                    }
                }
                if(trylng) {
                    myPref.edit().putString("expired_todo" + x, myPref.getString("events_todo" + eventindex, "")).apply();
                    myPref.edit().putString("expired_desc" + x, myPref.getString("events_desc" + eventindex, "")).apply();
                    myPref.edit().putString("expired_date" + x, myPref.getString("events_date" + eventindex, "")).apply();
                    myPref.edit().putString("expired_time" + x, myPref.getString("events_time" + eventindex, "")).apply();
                    myPref.edit().putString("expired_amorpm" + x, myPref.getString("events_amorpm" + eventindex, "")).apply();
                    myPref.edit().putBoolean("expired_checked" + x, myPref.getBoolean("events_checked"+eventindex, false)).apply();

                }
                SharedPreferences.Editor editor = myPref.edit();
                editor.remove("events_todo" + eventindex);
                editor.remove("events_desc" + eventindex);
                editor.remove("events_time" + eventindex);
                editor.remove("events_date" + eventindex);
                editor.remove("events_amorpm" + eventindex);
                editor.remove("events_checked" + eventindex);
                editor.apply();
            }
        }
        ArrayList<String> arrtoexpired = new ArrayList<String>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().contains("expired_date")){
                arrtoexpired.add(entry.getKey());
            }
        }
        linearLayout5 = view.findViewById(R.id.expiredView);
        for(int g=0; g<arrtoexpired.size(); g++) {
            String[] spl= arrtoexpired.get(g).split("expired_date");
            int eventindex = Integer.parseInt(spl[spl.length - 1]);
            TextView textView = new TextView(getActivity());
            String sub = myPref.getString("expired_todo"+eventindex, "");
            String sDesc = myPref.getString("expired_desc"+eventindex, "");
            String sDate1 = myPref.getString(arrtoexpired.get(g), "");
            String sTIme2 = myPref.getString("expired_time"+eventindex, "");
            String sTIme3= myPref.getString("expired_amorpm"+eventindex, "");

            textView.setText(sub);
            textView.setId(12345 + eventindex);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(20, 10, 20, 10);
            textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
            textView.setLayoutParams(layoutParams2);
            linearLayout5.addView(textView);
            arrayboolexp.add(myPref.getBoolean("expired_checked" + eventindex, false));
            arrlistexp.add(textView);
            todoexp.add(sub);
            descexp.add(sDesc);
            dateexp.add(sDate1);
            timeexp.add(sTIme2);
            amorpmexp.add(sTIme3);
            whatkeyindexexp.add(eventindex);
        }


        addevent.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        for (int i=0; i<arrlist.size(); i++){

            if(v.getId() == arrlist.get(i).getId()){
                Boolean chlng = false;
                String ddo = todo.get(i);
                String tde= desc.get(i);
                String tda= date.get(i);
                String tti = time.get(i);
                String ampm = amorpm.get(i);
                Boolean bool = arraybool.get(i);
                know = "event";
                showPopUp(ddo,tde,tda,tti,ampm, whatkeyindex.get(i), checkBoxes.get(i), chlng, bool);
                break;
            }
        }
        for (int i=0; i<arrlistexp.size(); i++){
            if(v.getId() == arrlistexp.get(i).getId()){
                Boolean chlng = true;
                String ddo = todoexp.get(i);
                String tde= descexp.get(i);
                String tda= dateexp.get(i);
                String tti = timeexp.get(i);
                String ampm= amorpmexp.get(i);
                Boolean bool = arrayboolexp.get(i);
                know = "expired";
                showPopUp(ddo,tde,tda,tti,ampm, 0, new CheckBox(getActivity()), chlng, bool);
                break;
            }
        }
        switch (v.getId()){
            case R.id.addevents:
                fragment = new AddEvent();
                replaceFragment(fragment);
                break;
        }
    }
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showPopUp(String todo, String desc, String date, String time, String amorpm, final int keyindex, final CheckBox cb, Boolean ch, Boolean check) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.popup, null);
        Button done = customView.findViewById(R.id.done);
        Button notdone = customView.findViewById(R.id.notfinished);
        TextView tdo = (TextView) customView.findViewById(R.id.thngstodo);
        TextView tdesc = (TextView) customView.findViewById(R.id.tododesc);
        TextView tdate = (TextView) customView.findViewById(R.id.tododate);
        TextView ttime = (TextView) customView.findViewById(R.id.todotime);
        TextView tdone = (TextView) customView.findViewById(R.id.doneornotdone);
        if((know.equals("event") || know.equals("expired")) && check){
            tdone.setText("---FINISHED---");
        }else if(know.equals("event") && !check){
            tdone.setText("---NOT FINISHED---");
        }else if(know.equals("expired") && !check) {
            tdone.setText("---MISSED---");
        }
        if(ch){
            done.setEnabled(false);
            done.setVisibility(customView.INVISIBLE);
            notdone.setVisibility(customView.INVISIBLE);
            notdone.setEnabled(false);
        }
        tdo.setText(todo);
        tdesc.setText(desc);
        tdate.setText(date);
        ttime.setText(time + " " + amorpm);
        builder.setView(customView);
        final AlertDialog dialog = builder.create();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPref.edit().putBoolean("events_checked" + keyindex, true).apply();
                cb.setChecked(true);
                dialog.dismiss();
            }
        });
        notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPref.edit().putBoolean("events_checked" + keyindex, false).apply();
                cb.setChecked(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void delete(final View ed, final int indexx, final LinearLayout linearLayouts, final String evorexp) {
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
                String tododate  = myPref.getString(evorexp+"_date" + indexx, "");
                String todotime  = myPref.getString(evorexp+"_time" + indexx, "");
                String text = myPref.getString(evorexp+"_amorpm" + indexx, "");
                String datestring = tododate + " " + todotime + " " + text;
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.ENGLISH);
                try {
                    Date date3 = format.parse(datestring);
                    myPref.edit().remove(evorexp+"_todo"+indexx).apply();
                    myPref.edit().remove(evorexp+"_desc"+indexx).apply();
                    myPref.edit().remove(evorexp+"_date"+indexx).apply();
                    myPref.edit().remove(evorexp+"_time"+indexx).apply();
                    myPref.edit().remove(evorexp+"_checked"+indexx).apply();
                    myPref.edit().remove(evorexp+"_amorpm"+indexx).apply();
                    myPref.edit().remove(date3+""+indexx).apply();
                    linearLayouts.removeView(ed);

                    dialog.dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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

    @Override
    public boolean onLongClick(View v) {
        for (int i=0; i<arrlist.size(); i++){

            if(v.getId() == arrlist.get(i).getId()){
                delete(linearlayoutsecond.get(i), whatkeyindex.get(i), linearLayout, "events");
                break;
            }
        }
        for (int i=0; i<arrlistexp.size(); i++){
            if(v.getId() == arrlistexp.get(i).getId()){
                delete(arrlistexp.get(i), whatkeyindexexp.get(i), linearLayout5, "expired");

                break;
            }
        }
        return true;
    }
}