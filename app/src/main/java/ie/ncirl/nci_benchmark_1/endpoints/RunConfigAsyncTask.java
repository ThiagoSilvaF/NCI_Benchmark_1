package ie.ncirl.nci_benchmark_1.endpoints;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ie.aomonitor.ConfigMonitor;
import ie.aomonitor.endpoints.AsyncResponse;

import static ie.aomonitor.Constants.GAE_ENDPOINT;

/**
 * Created by Administrativo on 12/07/2017.
 */

public class RunConfigAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    //private static SyncDeviceAPI myApiService = null;
    private Context context;
    private String param;


    public AsyncResponse delegate = null;

    public RunConfigAsyncTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    public RunConfigAsyncTask(AsyncResponse asyncResponse, String password) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        this.param = password;
    }


    @Override
    protected String doInBackground(Pair<Context, String>... params) {
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
           // ConfigMonitor conf = new Gson().fromJson(response.toString(), ConfigMonitor.class);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return response.toString();
            }
            return "Error: " + responseCode + " " + connection.getResponseMessage();

        } catch (IOException e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}