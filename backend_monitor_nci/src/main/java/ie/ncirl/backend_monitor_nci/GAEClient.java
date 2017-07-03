package ie.ncirl.backend_monitor_nci;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.Timestamp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrativo on 01/07/2017.
 */

public class GAEClient {

    private DatastoreService datastore;

    public void  addProcessInfoToTheCloud(DeviceLog device) {
        //Type listType = new TypeToken<ArrayList<DeviceLog>>(){}.getType();
        //List<DeviceLog> listDL = new Gson().fromJson(jsonParam, listType);

        //for (DeviceLog device : listDL) {
            datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
            Entity task =  new Entity("devices");
            task.setProperty("brand", device.getBrand());
            task.setProperty("device", device.getDevice());
            task.setProperty("hardware", device.getHardware());
            task.setProperty("model", device.getModel());
            task.setProperty("product", device.getProduct());
            task.setProperty("manufacturer", device.getManufacturer());
            task.setProperty("board", device.getBoard());
            task.setProperty("memory", device.getMemory());
            task.setProperty("processor", device.getProcessor());
            task.setProperty("startTime", device.getStartTime());
            task.setProperty("endTime", device.getEndTime());
            task.setProperty("methodName", device.getMethodName());
            task.setProperty("appId", device.getAppKey());

            Key bookKey = datastore.put(task); // Save the Entity
        //}
    }
}
