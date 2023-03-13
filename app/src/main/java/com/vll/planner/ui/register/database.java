package com.vll.planner.ui.register;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class database {
    private String urls = "";
    private String dbloc = "";

    public database() {
        this.urls = new String();
        this.dbloc = new String();
    }

    public database connect(String host) throws Exception{
        this.urls = host;
        String query_url = this.urls+"/getDb";
        String json = "{ \"path\" : \"daw\"}";
        JSONObject js = getJSON(query_url,json);
        String value = js.getString("root");
        this.dbloc = value.replace("\\", "\\\\");
        return this;
    }

    public String checkChild(String child) throws Exception {
        String value = "";

        String query_url = this.urls+"/checkChild";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"path\" : \""+child+"\"}";

        JSONObject js = getJSON(query_url,json);
        value = js.getString("data");

        return value;
    }
    public Boolean addChildParent(String parent) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/addParent";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+parent+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public Boolean addChild(String child) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/addChild";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+child+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public Boolean updateChild(String childandval) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/updateChild";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+childandval+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public Boolean deleteChild(String child) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/deleteChild";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+child+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public Boolean deleteAllChild(String child) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/deleteAllChild";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+child+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public Boolean deleteParent(String parent) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/deleteParent";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+parent+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public Boolean renameChildorChildParent(String parentorchild) throws Exception {
        Boolean value = null;
        String query_url = this.urls+"/rename";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"data\" : ["+parentorchild+"]}";

        JSONObject js = getJSON(query_url,json);
        value = js.getBoolean("data");

        return value;
    }
    public database nextChildParent(String childParent) throws Exception {
        this.dbloc = this.dbloc+"/"+childParent;
        return this;
    }
    public JSONArray getDB() throws Exception {
        JSONArray value = null;
        String query_url = this.urls+"/getDIR";
        String json = "{ \"currpath\" : \""+this.dbloc+"\"}";

        JSONObject js = getJSON(query_url,json);
        value = js.getJSONArray("data");

        return value;
    }
    public ArrayList<String[]> getChildandValue() throws Exception {
        JSONArray value = null;
        String query_url = this.urls+"/getDIRArrandVal";
        String json = "{ \"currpath\" : \""+this.dbloc+"\"}";

        JSONObject js = getJSON(query_url,json);
        value = js.getJSONArray("data");
        JSONArray value2 =  js.getJSONArray("data2");
        ArrayList<String[]>stringArrayList = new ArrayList<String[]>();
        for (int i = 0; i < value.length(); i++) {
            String arr[] = {value.getString(i), value2.getString(i)};
            stringArrayList.add(arr);

        }
        return stringArrayList;
    }
    public JSONArray getAllChildValues(String arrinstring) throws Exception {
        JSONArray value = null;
        String query_url = this.urls+"/checkAllChild";
        String json = "{ \"currpath\" : \""+this.dbloc+"\", \"path\":"+arrinstring+"}";

        JSONObject js = getJSON(query_url,json);
        value = js.getJSONArray("data");

        return value;
    }
    public JSONObject getJSON(String query_url,String json) throws Exception {
        URL url = new URL(query_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.close();
        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = IOUtils.toString(in, "UTF-8");

        JSONObject myResponse = new JSONObject(result);
        in.close();
        conn.disconnect();
        return myResponse;
    }

}


