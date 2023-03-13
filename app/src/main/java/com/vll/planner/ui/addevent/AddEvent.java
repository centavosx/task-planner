package com.vll.planner.ui.addevent;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vll.planner.AlertReceiver;
import com.vll.planner.MainActivity;
import com.vll.planner.R;
import com.vll.planner.ui.events.EventsFragments;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class AddEvent extends Fragment implements View.OnClickListener{
    private EditText event_date, event_time, event_todo, event_description;
    private Button cancel, save;
    private DatePickerDialog.OnDateSetListener date;
    final Calendar myCalendar = Calendar.getInstance();
    private RadioButton r1, r2;
    private String text = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.addevent, container, false);
        event_date= view.findViewById(R.id.event_date);
        event_time = view.findViewById(R.id.event_time);
        event_todo = view.findViewById(R.id.todo);
        event_description = view.findViewById(R.id.description);
        cancel = view.findViewById(R.id.wllcancel);
        save = view.findViewById(R.id.willsave);
        r1 = view.findViewById(R.id.radioam);
        r2 = view.findViewById(R.id.radiopm);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        event_date.setOnClickListener(this);

        return view;
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        event_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_date:
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.willsave:
                String todotext = event_todo.getText().toString();
                String tododesc = event_description.getText().toString();
                String todotime = event_time.getText().toString();
                String tododate = event_date.getText().toString();
                if(todotext.length() != 0 && tododesc.length() != 0 && todotime.length()!=0 && tododate.length()!=0 && text != "") {
                    SharedPreferences myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    Map<String, ?> allEntries = myPref.getAll();
                    ArrayList<String> arrtodo = new ArrayList<String>();
                    ArrayList<Integer> arrint = new ArrayList<Integer>();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        if (entry.getKey().contains("events_todo")) {
                            arrtodo.add(entry.getKey());
                        }
                    }
                    int num = 0;
                    if (arrtodo.size() != 0) {
                        for (int i = 0; i < arrtodo.size(); i++) {
                            String spl[] = arrtodo.get(i).split("events_todo");
                            int eventindex = Integer.parseInt(spl[spl.length - 1]);
                            arrint.add(eventindex);
                        }
                        Collections.sort(arrint);
                        int lastint = arrint.get(arrint.size() - 1) + 1;
                        myPref.edit().putString("events_todo" + lastint, todotext).apply();
                        myPref.edit().putString("events_date" + lastint, tododate).apply();
                        myPref.edit().putString("events_desc" + lastint, tododesc).apply();
                        myPref.edit().putString("events_time" + lastint, todotime).apply();
                        myPref.edit().putString("events_amorpm" + lastint, text).apply();
                        myPref.edit().putBoolean("events_checked" + lastint, false).apply();
                    } else {
                        myPref.edit().putString("events_todo" + 0, todotext).apply();
                        myPref.edit().putString("events_date" + 0, tododate).apply();
                        myPref.edit().putString("events_desc" + 0, tododesc).apply();
                        myPref.edit().putString("events_time" + 0, todotime).apply();
                        myPref.edit().putString("events_amorpm" + 0, text).apply();
                        myPref.edit().putBoolean("events_checked" + 0, false).apply();
                    }

                    String datestring = tododate + " " + todotime + " " + text;
                    DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.ENGLISH);
                    try {
                        Calendar c = Calendar.getInstance();
                        Date date = format.parse(datestring);
                        c.setTime(date);
                        myPref.edit().putString(format.format(date) + "0", todotext).apply();
                        myPref.edit().putString(format.format(date) + "1", tododesc).apply();
                        ((MainActivity) getActivity()).startAlarm(c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                    Fragment fragment2 = new EventsFragments();
                    replaceFragment(fragment2);
                }else{
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.wllcancel:
                Fragment fragment = new EventsFragments();
                replaceFragment(fragment);
                break;

            case R.id.radioam:
                text = "am";
                break;

            case R.id.radiopm:
                text = "pm";
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