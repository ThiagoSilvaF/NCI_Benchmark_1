package ie.aomonitor.endpoints;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;


import com.google.api.client.util.Data;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.aomonitor.DatabaseHandler;
import ie.aomonitor.DeviceLog;
import ie.aomonitor.Monitor;

import static ie.aomonitor.Constants.GAE_ENDPOINT;

/**
 * Created by Administrativo on 03/07/2017.
 */

public class PersistLogsServletAsyncTask  extends AsyncTask<Pair<Context, String>, Void, String> {
    private Context context;
    public AsyncResponse delegate = null;

    public PersistLogsServletAsyncTask(AsyncResponse asyncResponse, Context context) {
        this.context = context;
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        context = params[0].first;
        DatabaseHandler dh = Monitor.getDbInstance();
        List<DeviceLog> deviceLogs = dh.getAllProcessInfo();

        for(DeviceLog d : deviceLogs) {
            String param = new Gson().toJson(d);
            try {
                // Set up the request
                URL url = new URL(GAE_ENDPOINT + "save_device");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                // Build name data request params
                Map<String, String> nameValuePairs = new HashMap<>();
                nameValuePairs.put("param", param);
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
                DatabaseHandler dbh = Monitor.getDbInstance();
                dbh.deleteAll();
            } catch (IOException e) {
                e.getMessage();
            }
        }

        Monitor.getDbInstance().deleteAll();
        return "0";

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
