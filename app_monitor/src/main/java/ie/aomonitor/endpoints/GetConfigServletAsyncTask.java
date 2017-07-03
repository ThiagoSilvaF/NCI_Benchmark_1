package ie.aomonitor.endpoints;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

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
 * Created by Administrativo on 04/07/2017.
 */

public class GetConfigServletAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private Context context;
    public AsyncResponse delegate = null;

    public GetConfigServletAsyncTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        context = params[0].first;
        String param = params[1].second;

        //TODO implement here
        return "Testing";

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

