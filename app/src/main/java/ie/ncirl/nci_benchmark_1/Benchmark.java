package ie.ncirl.nci_benchmark_1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Benchmark extends AppCompatActivity {

    private TextView result;
    private Button compute;
    private String teststring;
    private String HashValue;
    private String tt;
    private String output;
    private long tsLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);

        compute=(Button)findViewById(R.id.btn1);
        result= (TextView)findViewById(R.id.textView2);
        teststring = generateSessionKey(999999);///getResources().getString(R.string.teststring);
    }

    public void onBeginClick (View view) {
        tsLong = System.nanoTime();
        for (Integer i = 0; i<20000; i++) {
            computeSHAHash(teststring);
        }
        Long ttLong = System.nanoTime() - tsLong;
        tt = ttLong.toString();
        Integer floor = Math.round(ttLong / 100000000);
        String score =  floor.toString();
        output = "SHA-1 hash: " + " " + HashValue + "\n Time Taken (nanoseconds): " + tt + "\n Score: " + score;
        result.setText(output);
    }

    public void computeSHAHash(String password)
    {

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);
        HashValue=sb.toString();

    }

    public static String generateSessionKey(int length){
        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11


        for (int i=0; i<length; i++) {
            System.out.println(i);
            result = result + alphabet.charAt(r.nextInt(n));
        }

        return result;
    }


}