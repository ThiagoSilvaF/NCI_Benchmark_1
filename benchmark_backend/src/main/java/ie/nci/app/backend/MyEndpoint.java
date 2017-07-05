/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package ie.nci.app.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.repackaged.org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.app.nci.ie",
                ownerName = "backend.app.nci.ie",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back

    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }
     */
    @ApiMethod(name = "generateString")
    public MyBean generateString(@Named("param") String param){
        MyBean response = new MyBean();

       /* String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i=0; i<length; i++) {
            result = result + alphabet.charAt(r.nextInt(n));
        }

        response.setData(result);
        return response;
*/




       // String param = req.getParameter("text_param");
       // resp.setContentType("text/plain");

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            e1.getMessage();
        }
        try {
            mdSha1.update(param.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.encodeBase64String(data);

        sb.append(hex);
        //HashValue=sb.toString();
        response.setData(sb.toString());
        return response;


    }


}
