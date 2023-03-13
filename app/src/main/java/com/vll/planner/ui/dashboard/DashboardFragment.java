package com.vll.planner.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.vll.planner.MainActivity;
import com.vll.planner.R;
import com.vll.planner.ui.login.Login;
import com.vll.planner.ui.register.database;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences myPref;
    private LinearLayout linearLayout;
    private ArrayList<TextView> arrlist = new ArrayList<TextView>();
    private ArrayList<String>todo = new ArrayList<String>();
    private ArrayList<String>desc = new ArrayList<String>();
    private ArrayList<String>date = new ArrayList<String>();
    private ArrayList<String>time = new ArrayList<String>();
    private ArrayList<Integer> whatkeyindex = new ArrayList<Integer>();
    private ArrayList<Boolean> torf = new ArrayList<Boolean>();
    private ArrayList<CheckBox> checkbox = new ArrayList<CheckBox>();
    private ArrayList<String>amorpm = new ArrayList<String>();
    private database db = new database();
    private String expiredornot = "";
    private CalendarView calendarView;
    private Button savetocloud, loadfromcloud;
    private String text = "";
    private  String know = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.dash, container, false);
        myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        linearLayout = view.findViewById(R.id.todoView);
        final Map<String, ?> allEntries = myPref.getAll();
        final ArrayList<String>[] arrtodo = new ArrayList[]{new ArrayList<String>()};
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().contains("events_date")){
                arrtodo[0].add(entry.getKey());
            }
        }
        know = "event";
        expiredornot = "events";
        final DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
        final Date datenow= new Date(System.currentTimeMillis());
        final TextView sched = view.findViewById(R.id.todaydate4);
        final TextView todaydate= view.findViewById(R.id.todaydate);
        calendarView = view.findViewById(R.id.calendarView);
        todaydate.setText(df2.format(datenow));
        text = df2.format(datenow);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                linearLayout.removeAllViews();
                String  curDate = String.valueOf(dayOfMonth);
                String  Year = String.valueOf(year);
                String  Month = String.valueOf(month+1);
                if(Month.length() == 1){
                    Month = "0"+Month;
                }
                final ArrayList<String>[] arrtodo2= new ArrayList[]{new ArrayList<String>()};
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    if(entry.getKey().contains("events_date")){
                        arrtodo2[0].add(entry.getKey());
                    }
                }
                String ars[] = df2.format(datenow).split("/");
                String dateyesterday = ars[0] + "/" + (Integer.parseInt(ars[1])-1) + "/" + ars[2];
                String datetomorrow = ars[0] + "/" + (Integer.parseInt(ars[1])+1) + "/" + ars[2];
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.ENGLISH);
                Date datetimes = null;
                try {
                    datetimes = format.parse(Month+"/"+curDate+"/"+Year + " 11:59 pm");
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if(datetimes.getTime()<datenow.getTime()){
                    expiredornot="expired";
                    know = "expired";
                    arrtodo2[0] = new ArrayList<String>();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        if(entry.getKey().contains("expired_date")){
                            arrtodo2[0].add(entry.getKey());
                        }
                    }
                }else {
                    expiredornot = "events";
                    know = "event";
                }


                if( df2.format(datenow).equals(Month+"/"+curDate+"/"+Year)){
                    sched.setText("Today's Schedule");
                }else if(datetomorrow.equals(Month+"/"+curDate+"/"+Year)) {
                    sched.setText("Tommorow's Schedule");
                }else if(dateyesterday.equals(Month+"/"+curDate+"/"+Year)) {
                    sched.setText("Yesterday's Schedule");
                }else{
                    sched.setText(Month + "/" + curDate + "/" + Year + "'s Schedule");
                }
                todaydate.setText(Month+"/"+curDate+"/"+Year);
                text = Month+"/"+curDate+"/"+Year;
                dateee(view, arrtodo2[0], expiredornot);
            }
        });
        dateee(view, arrtodo[0], expiredornot);
        SharedPreferences myAcc = getActivity().getSharedPreferences("Accounts", Context.MODE_PRIVATE);
        String users = myAcc.getString("username", "");
        savetocloud = view.findViewById(R.id.savetocloud);
        loadfromcloud = view.findViewById(R.id.loadfromcloud);
        if(!users.equals("")){
            savetocloud.setOnClickListener(this);
            loadfromcloud.setOnClickListener(this);
        }else{
            savetocloud.setVisibility(view.INVISIBLE);
            loadfromcloud.setVisibility(view.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        for (int i=0; i<arrlist.size(); i++){
            if(v.getId() == arrlist.get(i).getId()){
                Boolean ch = false;
                if(expiredornot.equals("events")){
                    ch = false;
                }else{
                    ch = true;
                }
                String ddo = todo.get(i);
                String tde= desc.get(i);
                String tda= date.get(i);
                String tti = time.get(i);
                String ampm = amorpm.get(i);
                Boolean tf = torf.get(i);
                showPopUp(ddo,tde,tda,tti,ampm, whatkeyindex.get(i), checkbox.get(i),ch, tf);

                break;
            }
        }

        switch (v.getId()){
            case R.id.savetocloud:
                final String users[]={null};
                try {
                    db.connect("https://serverdbvin.herokuapp.com");
                    SharedPreferences myAcc = getActivity().getSharedPreferences("Accounts", Context.MODE_PRIVATE);
                    users[0] = myAcc.getString("username", "");
                    db.nextChildParent(users[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setTitle("Save to Cloud???")
                        .setMessage("This will be saved to your cloud")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    db.deleteAllChild("[\"Files\"]");
                                    db.nextChildParent("Files");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                save(users[0]);
                                Toast.makeText(getActivity(),"Saved!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                break;
            case R.id.loadfromcloud:
                final String userss[]={null};
                try {
                    db.connect("https://serverdbvin.herokuapp.com");
                    SharedPreferences myAcc = getActivity().getSharedPreferences("Accounts", Context.MODE_PRIVATE);
                    userss[0] = myAcc.getString("username", "");
                    db.nextChildParent(userss[0]);
                    db.nextChildParent("Files");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Load From Cloud???")
                        .setMessage("This will replace everything in your TaskPlan App")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                loadva(userss[0]);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                break;
        }

    }
    public void showPopUp(String todo, String desc, String date, String time, String amorpm, final int keyindex, final CheckBox cb, Boolean ch, Boolean check) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getLayoutInflater();
        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup, null);
        // reference the textview of custom_popup_dialog
        Button done = customView.findViewById(R.id.done);
        Button notdone = customView.findViewById(R.id.notfinished);
        TextView tdo = (TextView) customView.findViewById(R.id.thngstodo);
        TextView tdesc = (TextView) customView.findViewById(R.id.tododesc);
        TextView tdate = (TextView) customView.findViewById(R.id.tododate);
        TextView ttime = (TextView) customView.findViewById(R.id.todotime);
        TextView tdone = (TextView) customView.findViewById(R.id.doneornotdone);
        LinearLayout linearLayout3 =  customView.findViewById(R.id.getparentlinear);

        if((know.equals("event") || know.equals("expired")) && check){
            tdone.setText("---FINISHED---");
        }else if(know.equals("event") && !check){
            tdone.setText("---NOT FINISHED---");
        }else if(know.equals("expired") && !check) {
            tdone.setText("---MISSED---");
        }
        if(ch){
            done.setVisibility(customView.INVISIBLE);
            notdone.setVisibility(customView.INVISIBLE);
            done.setEnabled(false);
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


    private void dateee(View view, ArrayList<String> arrtodo, String expiredornot){
        arrlist.removeAll(arrlist);
        todo.removeAll(todo);
        desc.removeAll(desc);
        date.removeAll(desc);
        time.removeAll(time);
        whatkeyindex.removeAll(whatkeyindex);
        checkbox.removeAll(checkbox);
        amorpm.removeAll(amorpm);
        torf.removeAll(torf);
        for(int g=0; g<arrtodo.size(); g++) {
            String spl[] = null;
            spl = arrtodo.get(g).split("_date");
            int eventindex = Integer.parseInt(spl[1]);
            if (myPref.getString(expiredornot+"_date" + eventindex, "").equals(text) ){
                String sub = myPref.getString(expiredornot+"_todo" + eventindex, "");
                CheckBox valueTV = new CheckBox(getActivity());
                LinearLayout linearLayout2 = new LinearLayout(getActivity());
                linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(getActivity());
                textView.setText(sub);

                textView.setId(R.id.my_id + eventindex);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(0, 10, 0, 10);
                textView.setTextAppearance(getActivity(), R.style.fontStyleforitems);
                textView.setLayoutParams(layoutParams2);
                valueTV.setChecked(myPref.getBoolean(expiredornot+"_checked" + eventindex, false));
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
                torf.add(myPref.getBoolean(expiredornot+"_checked" + eventindex, false));
                linearLayout.addView(linearLayout2);
                arrlist.add(textView);
                todo.add(sub);
                whatkeyindex.add(eventindex);
                checkbox.add(valueTV);
                desc.add(myPref.getString(expiredornot+"_desc" + eventindex, ""));
                date.add(myPref.getString(expiredornot+"_date" + eventindex, ""));
                time.add(myPref.getString(expiredornot+"_time" + eventindex, ""));
                amorpm.add(myPref.getString(expiredornot+"_amorpm" + eventindex, ""));
            }
        }

    }
    private void save(String users){
        if(!users.equals("")) {
            Map<String, ?> keys = myPref.getAll();
            ArrayList<String> arrayList = new ArrayList<String>();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {

                if(entry.getKey().contains("note") || entry.getKey().contains("events") || entry.getKey().contains("expired")){
                    arrayList.add("[\"" + entry.getKey() + "\",\"" + entry.getValue() + "\"]");
                }else if(!entry.getKey().contains("code")){
                    arrayList.add("[\"" + entry.getKey().replace(":", "z").replace("/","x")+"\",\"" + entry.getValue() + "\"]");
                }

            }
            String val = "";
            for (int h = 0; h < arrayList.size(); h++) {
                if (h != arrayList.size() - 1) {
                    val += arrayList.get(h) + ",";
                } else {
                    val += arrayList.get(h);
                }
            }
            database database = db;
            try {
                database.addChild(val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Fragment fragment = new Login();
            replaceFragment(fragment);
        }
    }
    private void loadva(String users){
        if(!users.equals("")) {
            Map<String, ?> keys = myPref.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                myPref.edit().remove(entry.getKey()).apply();
            }
            database database = db;
            try {
                ArrayList<String[]> array = database.getChildandValue();
                for (int i = 0; i < array.size(); i++) {
                    if(array.get(i)[0].contains("events") ||array.get(i)[0].contains("expired") || array.get(i)[0].contains("notes")){
                        if (array.get(i)[1].equals("true")) {
                            myPref.edit().putBoolean(array.get(i)[0], true).apply();
                        } else if (array.get(i)[1].equals("false")) {
                            myPref.edit().putBoolean(array.get(i)[0], false).apply();
                        } else {
                            myPref.edit().putString(array.get(i)[0], array.get(i)[1]).apply();
                        }
                    }else{
                        DateFormat dateFormat = new SimpleDateFormat(
                                "MM/dd/yyyy hh:mm aa", Locale.ENGLISH);
                        Date date = dateFormat.parse(array.get(i)[0].substring(0, array.get(i)[0].length()-1).replace("z", ":").replace("x","/"));
                        Calendar c = Calendar.getInstance();
                        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.ENGLISH);
                        c.setTime(date);
                        if(Integer.parseInt(String.valueOf(array.get(i)[0].charAt(array.get(i)[0].length()-1))) == 0){
                            myPref.edit().putString(format.format(date)+"0", array.get(i)[1]).apply();
                        }else if(Integer.parseInt(String.valueOf(array.get(i)[0].charAt(array.get(i)[0].length()-1))) == 1) {
                            myPref.edit().putString(format.format(date) + "1", array.get(i)[1]).apply();
                        }

                        ((MainActivity) getActivity()).startAlarm(c);
                    }
                }
                Toast.makeText(getActivity(), "Done!!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Fragment fragment = new Login();
            replaceFragment(fragment);
        }
    }
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}