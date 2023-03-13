package com.vll.planner.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.vll.planner.MainActivity;
import com.vll.planner.R;
import com.vll.planner.encdecr;
import com.vll.planner.ui.register.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Login extends Fragment implements View.OnClickListener{
    private EditText user, pass;
    private Button save;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.login, container, false);
        SharedPreferences myAcc = getActivity().getSharedPreferences("Accounts", Context.MODE_PRIVATE);
        String users = myAcc.getString("username", "");
        if(!users.equals("")) {
            Toast.makeText(getActivity(), "Already Logged In!", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        user = view.findViewById(R.id.us);
        pass = view.findViewById(R.id.pw);
        save = view.findViewById(R.id.willsave);
        save.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.willsave:
                String u = user.getText().toString();
                String p = pass.getText().toString();
                if (TextUtils.isEmpty(u)){
                    user.setError("Field is Empty!");
                    return;
                }

                if (TextUtils.isEmpty(p)){
                    pass.setError("Empty!");
                    return;
                }

                getdata(v, u,p);

                break;

        }
    }
    public void getdata(View v, String u, String p){
        String pass2= encdecr.encrypt(p, "hel", 1).replace("\\","+")
                .replace("<", "q").replace(">","w")
                .replace("/","e").replace(":","r").replace("\"","t")
                .replace("?","y").replace("*","u").replace(".","i").replace("{","o").replace("}","p")
                .replace("|","a");

        String parent =encdecr.encrypt(u, "hel", 1).replace("\\","+")
                .replace("<", "q").replace(">","w")
                .replace("/","e").replace(":","r").replace("\"","t")
                .replace("?","y").replace("*","u").replace(".","i").replace("{","o").replace("}","p")
                .replace("|","a");
        try {
            final database db = new database();
            db.connect("https://serverdbvin.herokuapp.com");
            db.nextChildParent(parent);
            if(db.checkChild("password").equals(pass2)){
                SharedPreferences myacc = getActivity().getSharedPreferences("Accounts", Context.MODE_PRIVATE);
                myacc.edit().putString("username", parent).apply();
                myacc.edit().putString("Name", db.checkChild("Name")).apply();
                db.addChildParent("[\"Files\"]");
                db.nextChildParent("Files");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Save App Data To Cloud???")
                            .setMessage("Will replace the current cloud data")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences myPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                                    Map<String, ?> keys = myPref.getAll();
                                    ArrayList<String> arrayList = new ArrayList<String>();
                                    for (Map.Entry<String, ?> entry : keys.entrySet()) {

                                        if(entry.getKey().contains("note") || entry.getKey().contains("events") || entry.getKey().contains("expired")){
                                            arrayList.add("[\"" + entry.getKey() + "\",\"" + entry.getValue() + "\"]");
                                        }else if(!entry.getKey().contains("code")){
                                            arrayList.add("[\"" + entry.getKey().replace(":", "z").replace("/","x")+"\",\"" + entry.getValue() + "\"]");
                                        }

                                    }
                                    try {
                                        db.deleteAllChild("[\"Files\"]");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String val = "";
                                    for (int h = 0; h < arrayList.size(); h++) {
                                        if (h != arrayList.size() - 1) {
                                            val += arrayList.get(h) + ",";
                                        } else {
                                            val += arrayList.get(h);
                                        }
                                    }

                                    try {
                                        db.addChild(val);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .show();
            }else{
                Toast.makeText(getActivity(), "Incorrect username or password!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}