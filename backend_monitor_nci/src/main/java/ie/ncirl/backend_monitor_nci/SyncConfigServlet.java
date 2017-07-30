package ie.ncirl.backend_monitor_nci;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrativo on 13/07/2017.
 */

public class SyncConfigServlet extends HttpServlet {

    MathUtil m = MathUtil.getInstance();


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }



    @Override
    public  void doPost(HttpServletRequest req, HttpServletResponse response)
            throws IOException {

        List<Config> listConfig = new ArrayList<Config>();
        String LOG = "*********** INIT LOG ********* \n";

        Config conf = null;

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();

        com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("devices")
                .addSort("device", com.google.appengine.api.datastore.Query.SortDirection.ASCENDING);
        PreparedQuery pq = datastore.prepare(q);


        LOG += "-- datastore initiated \n";
        QueryResultList<Entity> results = null;
            try {
            results = pq.asQueryResultList(fetchOptions);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOG += e.getMessage();
        }

        LOG += "-- query created , result ---> "+ results.size() +" \n";
        //THIS LIST HAS A LIST WITH THE DEVICES
        // BY APP ID WHICH CONTAINS LISTS BY METHOD NAME THAT CONTAINS
        // LISTS OF DEVICES THAT WILL BE PROCESSED!

        List<List<Entity>> listByAppId = new ArrayList<>();

        List<Integer> listOfAppIdIndex = new ArrayList<Integer>();
        List<String> listOffAppIdDescription = new ArrayList<String>();

        Long smallerTimeElapsed = null;
        LOG += "-- looping appId \n";
            try {
            out:
            for (Entity entity : results) {
                //GET THE APP ID
                String appId = entity.getProperty("appId").toString();

                //CHECK IF IS THERE ANY LIST WITH THE APP IP
                for (String name : listOffAppIdDescription) {
                    if (appId.equals(name)) {
                        for (Integer i : listOfAppIdIndex) {
                            if (listByAppId.get(i) != null)
                                listByAppId.get(i).add(entity);
                            continue out;
                        }
                    }
                }

                //IF NOT, CREATE A NEW LIST AND LOOP CONTINUE THE LOOP
                List<Entity> newList = new ArrayList<Entity>();
                newList.add(entity);
                listByAppId.add(newList);
                listOfAppIdIndex.add(listByAppId.size() - 1);
                listOffAppIdDescription.add(appId);

            }
            LOG += "-- size of the list appID ---> "+ listByAppId.size() +" \n";
            LOG += "-- finished looping appId \n";

            LOG += "-- looping appId to get the method names ---> "+ listByAppId.size()+"\n";
            for (List<Entity> list : listByAppId) {
                //NOW WE HAVE ALL THE CONFIGS SORTED BY APP ID
                //THEN WE NEED TO SORT AGAIN BY METHOD NAME

                List<List<Entity>> listByMethodName = new ArrayList<>();
                List<Integer> listOfMethodNameIndex = new ArrayList<Integer>();
                List<String> listOfMethodNameDescription = new ArrayList<String>();

                out:
                for (Entity entity : list) {
                    //GET THE METHOD NAME
                    String methodName = entity.getProperty("methodName").toString();

                    //CHECK IF IS THERE ANY LIST WITH THE METHOD NAME
                    for (String name : listOfMethodNameDescription) {
                        if (methodName.equals(name)) {
                            for (Integer i : listOfMethodNameIndex) {
                                if (listByMethodName.get(i) != null)
                                    listByMethodName.get(i).add(entity);
                                continue out;
                            }
                        }
                    }

                    //IF NOT, CREATE A NEW LIST AND LOOP CONTINUE THE LOOP
                    List<Entity> newList = new ArrayList<Entity>();
                    newList.add(entity);
                    listByMethodName.add(newList);
                    listOfMethodNameIndex.add(listByMethodName.size() - 1);
                    listOfMethodNameDescription.add(methodName);

                }


                LOG += "-- starting looping into methodnames ----> "+ listByMethodName.size() +"\n";
                for (List<Entity> listMethodName : listByMethodName) {
                    LOG += "-- into the loop \n";
                    List<List<Entity>> listByDevice = new ArrayList<>();
                    List<Integer> listOfDeviceIndex = new ArrayList<Integer>();
                    List<String> listOfDeviceDescription = new ArrayList<String>();

                    out:
                    for (Entity entity : listMethodName) {
                        //GET THE METHOD NAME
                        String device = entity.getProperty("device").toString();
                        LOG += "--- DEVICE HERE IS ---> "+device+" \n";

                        //CHECK IF IS THERE ANY LIST WITH THE METHOD NAME
                        int count = 0;
                        for (String name : listOfDeviceDescription) {
                            LOG += "--- testing if "+name+"="+device+"\n";

                            if (device.equals(name)) {
                               // for (Integer i : listOfDeviceIndex) {
                                    if (listByDevice.get(count) != null) {
                                        listByDevice.get(count).add(entity);
                                        continue out;
                                    }
                                //}//
                            }
                            count++;
                        }

                        //IF NOT, CREATE A NEW LIST AND LOOP CONTINUE THE LOOP
                        LOG += " --- creating list by device: "+device+"\n";
                        List<Entity> newList = new ArrayList<Entity>();
                        newList.add(entity);
                        listByDevice.add(newList);
                        listOfDeviceIndex.add(listByDevice.size() - 1);
                        listOfDeviceDescription.add(device);

                    }
                    LOG += "-- starting to read the devices ----> "+listByDevice.size()+" \n";
                    for (List<Entity> listDevice : listByDevice) {
                        LOG += "-- This is the device loop ----> "+listDevice.size()+" \n";

                        Config config = null;
                        List<Long> listLocal = new ArrayList<Long>();
                        List<Long> listRemote = new ArrayList<Long>();
                        String deviceName = "";
                        String appId = "";
                        String methodName = "";

                        for (Entity entity : listDevice) {
                            LOG += "--  "+entity.getProperty("device").toString()+" \n";

                            deviceName = entity.getProperty("device").toString();
                            appId = entity.getProperty("appId").toString();
                            methodName = entity.getProperty("methodName").toString();

                            Long startTime = (Long) entity.getProperty("startTime");
                            Long endTime = (Long) entity.getProperty("endTime");

                            if (entity.getProperty("environment").equals("LOCAL")) {
                                listLocal.add(endTime - startTime);
                            } else if (entity.getProperty("environment").equals("REMOTE")) {
                                listRemote.add(endTime - startTime);
                            }

                        }
                        LOG += "-- finished by one device and now will do the avg \n";
                        Double avgLocal = m.avg(removeOutliers(listLocal));
                        Double avgRemote = m.avg(removeOutliers(listRemote));

                        if (avgLocal > avgRemote) {
                            config = new Config(deviceName, appId, methodName, "REMOTE", avgRemote.longValue());
                        } else {
                            config = new Config(deviceName, appId, methodName, "LOCAL", avgLocal.longValue());
                        }
                        LOG += "-- listConfig adding --->" + config.getDevice() + "\n";
                        listConfig.add(config);
                        LOG += "-- listConfig = " + listConfig.size() + "\n";
                    }
                }
            }
        }catch (Exception ex){
            LOG += " caiu no primeiro ex";
            LOG += ex.getMessage();
            ex.printStackTrace();

        }

        LOG += "************** EVERYTHING DONE ***************** \n";
        try {
            for (Config c : listConfig) {
                GAEClient.addConfigToTheCloud(c);
            }
        }catch (Exception ex){
            LOG += " caiu no segundo ex";
            LOG += ex.getMessage();
            ex.printStackTrace();
        }

        String json = new com.google.gson.Gson().toJson(conf, Config.class);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(LOG);
        response.setStatus(200);
    }

    private List<Long> removeOutliers(List<Long> longList) {
        int size = longList.size();
        boolean b = true;

        while (b){
            Long d6 = m.getOutlier(longList, 0.05);
            longList.remove(d6);
            if(longList.size() == 0){
                break;
            }else if(!m.hasOutlier(longList)){
                break;
            }
        }

        return longList;
    }
}
