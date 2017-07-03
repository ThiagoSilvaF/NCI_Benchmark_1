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

import static ie.aomonitor.Constants.GAE_ENDPOINT;

public class SyncDeviceServletAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    private Context context;
    public AsyncResponse delegate = null;

    public SyncDeviceServletAsyncTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        context = params[0].first;
        String param = params[0].second;

        try {
            // Set up the request
            URL url = new URL(GAE_ENDPOINT+"sync_device");
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
            if(responseCode == 200){
                return "1";
            }else{
                return "0";
            }
            /*StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return response.toString();
            }
            return "Error: " + responseCode + " " + connection.getResponseMessage(); */

        } catch (IOException e) {
            e.getMessage();
        }

        return "0";

        /*if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setRootUrl("https://maximal-sphere-169913.appspot.com/_ah/api/");
            /**
              MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
             .setRootUrl("https://android-app-backend.appspot.com/_ah/api/");

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                // options for running against local devappserver
                // - 10.0.2.2 is localhost's IP address in Android emulator
                // - turn off compression when running against local devappserver
                .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
                // end options for devappserver



            myApiService = builder.build();
        }

        context = params[0].first;
        String param = params[0].second;

        try {
            return myApiService.synchronizeInfo(param).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }*/
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