package ie.ncirl.nci_benchmark_1.endpoints;


import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ie.aomonitor.ConfigMonitor;
import ie.aomonitor.endpoints.AsyncResponse;

import static ie.aomonitor.Constants.GAE_ENDPOINT;

public class EndpointsAsyncTask extends AsyncTask<android.util.Pair<Context, String>, Void, String> {

    private Context context;
    public AsyncResponse delegate = null;

    public EndpointsAsyncTask(AsyncResponse asyncResponse, Context context) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        this.context = context;
    }

    @Override
    protected String doInBackground(android.util.Pair<Context, String>... params) {

        try {
            // Set up the request
            URL url = new URL(GAE_ENDPOINT+"sync_config");
            //URL url = new URL("http://10.0.2.2:8080/_ah/api/sync_device");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Build name data request params
            //Map<String, String> nameValuePairs = new HashMap<>();
            //nameValuePairs.put("appId", appId);
            //nameValuePairs.put("device", device);
            //String postParams = buildPostDataString(nameValuePairs);

            // Execute HTTP Post
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            //writer.write(postParams);
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
            //ConfigMonitor conf = new Gson().fromJson(response.toString(), ConfigMonitor.class);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return response.toString();
            }
            return "Error: " + responseCode + " " + connection.getResponseMessage();

        } catch (IOException e) {
            e.getMessage();
        }

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