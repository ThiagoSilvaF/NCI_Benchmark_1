package ie.ncirl.nci_benchmark_1.endpoints;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ie.aomonitor.endpoints.AsyncResponse;
import ie.nci.app.backend.myApi.MyApi;

/**public EndpointsAsyncTask(AsyncResponse ) {
    }
 * Created by Administrativo on 07/06/2017.
 */

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private String param;


    public AsyncResponse delegate = null;

    public EndpointsAsyncTask(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    public EndpointsAsyncTask(AsyncResponse asyncResponse, String password) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        this.param = password;
    }


    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null).setRootUrl("https://triking-scout-114623.appspot.com/_ah/api/");

            myApiService = builder.build();

        }

        //context = params[0].first;
        //String param = params[0].second;

        //long stringLenght = 1000;

        try {
            return myApiService.generateString(param).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
       // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}