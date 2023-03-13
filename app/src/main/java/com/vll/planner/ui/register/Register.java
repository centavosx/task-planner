package com.vll.planner.ui.register;
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

import com.vll.planner.R;
import com.vll.planner.encdecr;
import com.vll.planner.ui.login.Login;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Calendar;

public class Register extends Fragment implements View.OnClickListener{

    private Button save, log;
    EditText name, username, password, confirmpass;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.register, container, false);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        log = view.findViewById(R.id.willsave3);
        password = view.findViewById(R.id.pwreg);
        confirmpass = view.findViewById(R.id.pwreg2);
        save = view.findViewById(R.id.willsave);
        save.setOnClickListener(this);
        log.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.willsave:
                String n = name.getText().toString();
                String u = username.getText().toString();
                String p = password.getText().toString();
                String p2 = confirmpass.getText().toString();
                if (TextUtils.isEmpty(n)){
                    name.setError("Field is Empty!");
                    return;
                }
                if (TextUtils.isEmpty(u)){
                    username.setError("Field is Empty!");
                    return;
                }
                if (TextUtils.isEmpty(p)){
                    password.setError("Field is Empty!");
                    return;
                }
                if (TextUtils.isEmpty(p2)){
                    confirmpass.setError("Field is Empty!");
                    return;
                }
                if (!p.equals(p2)){
                    password.setError("Not matched!");
                    confirmpass.setError("Not matched!");
                    return;
                }else{
                    adddata(u,p,n);

                }

                break;

            case R.id.willsave3:
                Fragment fragment2 = new Login();
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
    public void adddata(String u, String p, String nameofuser){
        String arra2 = "[\"password\",\""+encdecr.encrypt(p, "hel", 1).replace("\\","+")
                .replace("<", "q").replace(">","w")
                .replace("/","e").replace(":","r").replace("\"","t")
                .replace("?","y").replace("*","u").replace(".","i").replace("{","o").replace("}","p")
                .replace("|","a")+"\"]";

        String parent =encdecr.encrypt(u, "hel", 1).replace("\\","+")
                .replace("<", "q").replace(">","w")
                .replace("/","e").replace(":","r").replace("\"","t")
                .replace("?","y").replace("*","u").replace(".","i").replace("{","o").replace("}","p")
                .replace("|","a");

        try {
            database db = new database();
            db.connect("https://serverdbvin.herokuapp.com");
                Boolean c = db.addChildParent("[\"" + parent + "\"]");
                Boolean c1 = false;
                if (c) {
                    db.nextChildParent(parent);
                    c1 = db.addChild(arra2);
                    db.addChild("[\"Name\",\"" + nameofuser + "\"]");
                }
                if (c && c1) {
                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new Login();
                    replaceFragment(fragment);
                } else {
                    username.setError("Username already taken!");
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