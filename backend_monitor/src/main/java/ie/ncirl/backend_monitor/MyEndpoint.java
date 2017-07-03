/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package ie.ncirl.backend_monitor;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import org.json.JSONObject;

import java.util.Random;


import jdk.nashorn.internal.parser.JSONParser;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend_monitor.ncirl.ie",
                ownerName = "backend_monitor.ncirl.ie",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     *
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }
    */

    @ApiMethod(name = "synchronizeInfo")
    public MyBean synchronizeInfo(@Named("param") String param){
        MyBean response = new MyBean();

        String[] params = param.split(",");
        String appId = params[0];
        String device = params[1];
        /*

        IMPLEMENTAR AQUI A RESPOSTA DA CONFIGURACAO PRA CADA METODO DE CADA APP PARA O RESPECTIVO DISPOSITIVO



        String alphabet =
                new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i=0; i<length; i++) {
            result = result + alphabet.charAt(r.nextInt(n));
        }


        */
        response.setData("testing!!!!11!");
        return response;

    }
}

