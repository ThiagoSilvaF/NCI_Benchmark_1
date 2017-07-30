package ie.ncirl.backend_monitor_nci;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * Created by Administrativo on 01/07/2017.
 */

public class GAEClient {

    private static DatastoreService datastore;

    public void  addProcessInfoToTheCloud(DeviceLog device) {
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
            task.setProperty("environment", device.getEnvironment());

            Key bookKey = datastore.put(task); // Save the Entity
    }


        public static void addConfigToTheCloud(Config c) {
                datastore = DatastoreServiceFactory.getDatastoreService(); // Authorized Datastore service
                Entity task =  new Entity("configs");
                task.setProperty("appId", c.getAppId());
                task.setProperty("device", c.getDevice());
                task.setProperty("methodName", c.getMethodName());
                task.setProperty("avgTime", c.getAvgTime());
                task.setProperty("environment", c.getEnvironment());

                Key bookKey = datastore.put(task); // Save the Entity
        }
}
