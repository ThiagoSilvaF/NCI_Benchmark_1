package ie.aomonitor.endpoints;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ie.aomonitor.DeviceLog;

import static ie.aomonitor.Constants.GAE_ENDPOINT;

/**
 * Created by Administrativo on 04/07/2017.
 */

public class GetConfigServletAsyncTask extends AsyncTask<Pair<String, String>, Void, String> {
    private Context context;
    public AsyncResponse delegate = null;
    private DeviceLog device;

    public GetConfigServletAsyncTask(DeviceLog device, AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        this.device = device;
    }

    @Override
    protected String doInBackground(Pair<String, String>... params) {

        String appId      = params[0].first;
        String methodName = params[0].second;


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
            nameValuePairs.put("methodName", methodName);
            nameValuePairs.put("device", device.getDevice());

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
            System.out.println(responseCode);

        } catch (IOException e) {
            e.getMessage();
            return "0";
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

