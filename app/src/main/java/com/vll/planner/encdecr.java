package com.vll.planner;

import java.util.ArrayList;

public class encdecr {
    public static String getencrypt(String encrypt, String key, int countnum){
        int[] tom = {1,2,0,6,2,0};
        String encryptedstring = "";
        int lengthofstring = encrypt.length();

        for(int cou=0; cou<lengthofstring; cou++){
            int counter = 0;
            boolean ch = true;
            int index = key.indexOf(encrypt.charAt(cou));
            while(ch) {
                for (int i = index; i < key.length(); i++) {
                    if(countnum == counter){
                        String copy = "";
                        if (i <= key.length() - 7) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[0]) + key.charAt(i + tom[1]) + key.charAt(i + tom[2]) + key.charAt(i + tom[3]) + key.charAt(i + tom[4]) + key.charAt(i + tom[5]);
                        }
                        else if(i <= key.length() - 6) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[0]) + key.charAt(i + tom[1]) + key.charAt(i + tom[2]) + key.charAt(0) + key.charAt(i + tom[4]) + key.charAt(i + tom[5]);
                        }
                        else if(i <= key.length() - 5) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[0]) + key.charAt(i + tom[1]) + key.charAt(i + tom[2]) + key.charAt(1) + key.charAt(i + tom[4]) + key.charAt(i + tom[5]);
                        }
                        else if(i <= key.length() - 4) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[0]) + key.charAt(i + tom[1]) + key.charAt(i + tom[2]) + key.charAt(2) + key.charAt(i + tom[4]) + key.charAt(i + tom[5]);
                        }
                        else if(i <= key.length() - 3) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[0]) + key.charAt(i + tom[1]) + key.charAt(i + tom[2]) + key.charAt(3) + key.charAt(i + tom[4]) + key.charAt(i + tom[5]);
                        }
                        else if(i <= key.length() - 2) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[0]) + key.charAt(0) + key.charAt(i + tom[2]) + key.charAt(4) + key.charAt(0) + key.charAt(i + tom[5]);
                        }
                        else if(i <= key.length() - 1) {
                            copy = ""+key.charAt(i) + key.charAt(i + tom[5]) + key.charAt(1) + key.charAt(i + tom[2]) + key.charAt(5) + key.charAt(1) + key.charAt(i + tom[5]);
                        }
                        encryptedstring+=copy;
                        ch = false;
                        break;
                    }
                    index = 0;
                    counter++;
                }
            }
        }
        return encryptedstring;
    }
    public static String encrypt (String string, String passw, int id){
        String key = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ ";
        int ans = 0;
        for(int count=0; count<passw.length(); count++){
            ans += key.indexOf(passw.charAt(count));
        }
        ans += passw.length();
        String encryptstring = "";
        int countnum = ans * (id + key.length());
        for (int i=0; i<string.length(); i++){
            encryptstring+=getencrypt(""+string.charAt(i), key, countnum);
        }
        return encryptstring;
    }
    public static String decrypt (String string, String passw, int id){
        ArrayList<String> arr = new ArrayList<String>();
        for(int i = 0; i<string.length(); i+=7){
            arr.add(""+string.charAt(i)+string.charAt(i+1)+string.charAt(i+2)+string.charAt(i+3)+string.charAt(i+4)+string.charAt(i+5)+string.charAt(i+6));
        }
        String key = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ ";
        int ans = 0;
        for(int count=0; count<passw.length(); count++){
            ans += key.indexOf(passw.charAt(count));
        }
        ans += passw.length();
        int countnum = ans * (id + key.length());
        ArrayList<String> check = new ArrayList<String>();
        for(int i=0; i<key.length(); i++){
            check.add(""+getencrypt(""+key.charAt(i), key, countnum));
        }

        String decryptstring = "";

        for (int i=0; i<arr.size(); i++){
            String look = arr.get(i);
            decryptstring += key.charAt(check.indexOf(look));
        }
        return decryptstring;
    }
}
