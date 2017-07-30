package ie.aomonitor.endpoints;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ie.aomonitor.ConfigLog;
import ie.aomonitor.ConfigMonitor;
import ie.aomonitor.DatabaseHandler;
import ie.aomonitor.DeviceLog;
import ie.aomonitor.Monitor;

import static ie.aomonitor.Constants.GAE_ENDPOINT;

/**
 * Created by Administrativo on 04/07/2017.
 */

public class GetConfigServletAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private Context context;
    public AsyncResponse delegate = null;

    String appId      = null;
    String device = null;

    public GetConfigServletAsyncTask(String device, AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor

        String[] array = device.split(",");
        appId = array[0];
        this.device = array[1];
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        URL url = null;
        try {
            url = new URL(GAE_ENDPOINT + "get_config");

            HttpURLConnection connection = null;

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            // Build name data request params
            Map<String, String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("appId", appId);
            nameValuePairs.put("device", device);
            //nameValuePairs.put("device", "ghost");

            String postParams = buildPostDataString(nameValuePairs);

            // Execute HTTP Post
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(postParams);
            writer.flush();
            writer.close();
            outputStream.close();
            connection.connect();
            // Read response
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            try {
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    //TODO decode the JSON array and save into the local database
                    JsonParser parser = new JsonParser();
                    JsonElement tradeElement = parser.parse(response.toString());
                    JsonArray trade = tradeElement.getAsJsonArray();
                    for (JsonElement el : trade) {
                        Config jobj = new Gson().fromJson(el, Config.class);
                        DatabaseHandler dh = Monitor.getDbInstance();
                        dh.addConfigInfo(jobj.getMethodName(), jobj.getEnvironment());
                    }

                    System.out.println(trade);

                    // dh.addConfigInfo();
                    return response.toString();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                return  null;
            }
            return "Error: " + responseCode + " " + connection.getResponseMessage();

        } catch (IOException e) {
            e.getMessage();
        }
        return "1";

    }

    private String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}


class Config {

    String device;
    String appId;
    String methodName;
    String environment;
    Long avgTime;

    public Config(){}
    public Config(String device, String appId, String methodName , String environment, Long avgTime ){
        this.device = device;
        this.environment = environment;
        this.appId = appId;
        this.methodName = methodName;
        this.avgTime = avgTime;
    }

    public String getDevice() {
        return device;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    public String getEnvironment() {
        return environment;
    }
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public Long getAvgTime() {
        return avgTime;
    }
    public void setAvgTime(Long avgTime) {
        this.avgTime = avgTime;
    }



}

