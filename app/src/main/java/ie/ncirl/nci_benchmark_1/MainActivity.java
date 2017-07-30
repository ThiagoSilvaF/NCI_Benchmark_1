package ie.ncirl.nci_benchmark_1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import ie.aomonitor.Monitor;
import ie.aomonitor.endpoints.AsyncResponse;
import ie.ncirl.nci_benchmark_1.endpoints.EndpointsAsyncTask;
import ie.ncirl.nci_benchmark_1.endpoints.RunConfigAsyncTask;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private Button compute;

    private Button btnRunConfig;

    private String teststring;
    private String HashValue;
    private String tt;
    private String output;
    private long tsLong;

    private String testeEnv = "def";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunConfigAsyncTask eat = new RunConfigAsyncTask(new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        System.out.print("process finished!");
                        //Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                    }
                });

                eat.execute();
            }
        });
        result= (TextView)findViewById(R.id.textView2);
        teststring = generateString(9999);///getResources().getString(R.string.teststring);

        Monitor.initMonitor(getApplicationContext(),"1");

    }

    public void onBeginClick (View view) {
        computeSHAHash(teststring);
        Toast.makeText(getApplicationContext(), testeEnv, Toast.LENGTH_LONG).show();
    }


    public void computeSHAHash(final String password)
    {

        new Monitor("1","computeSHAHash",getApplicationContext()) {

            @Override
            public void taskLocal() {

                MessageDigest mdSha1 = null;
                try
                {
                    mdSha1 = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e1) {
                    Log.e("Benchmark", "Error initializing SHA1");
                }
                try {
                    mdSha1.update(password.getBytes("ASCII"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] data = mdSha1.digest();
                StringBuffer sb = new StringBuffer();
                String hex=null;

                hex = Base64.encodeToString(data, 0, data.length, 0);

                sb.append(hex);
                HashValue=sb.toString();
                testeEnv = "LOCAL";
            }

            @Override
            public void taskRemote() {


                EndpointsAsyncTask eat = new EndpointsAsyncTask(new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {

                        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                    }
                }, getApplicationContext());


                eat.execute();
                testeEnv = "REMOTE";
            }
        }.start();


    }


    public static String generateString(int length){
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11


        for (int i=0; i<length; i++) {
            result = result + alphabet.charAt(r.nextInt(n));
        }

        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
