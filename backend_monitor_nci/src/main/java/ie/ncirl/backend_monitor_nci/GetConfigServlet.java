package ie.ncirl.backend_monitor_nci;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrativo on 13/07/2017.
 */

public class GetConfigServlet extends HttpServlet {
    List<Entity> results = null;
    List<Config> listResult = new ArrayList<>();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String appId = req.getParameter("appId");
        //String methodName = req.getParameter("methodName");
        String device = req.getParameter("device");

        String LOG = "---- init log ---- \n";

        try {
            Config conf = null;

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();

            com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("configs")
                    .addSort("device", com.google.appengine.api.datastore.Query.SortDirection.ASCENDING);
            PreparedQuery pq = datastore.prepare(q);

            LOG += "-- datastore initiated \n";
            QueryResultList<Entity> results = null;
            try {
                results = pq.asQueryResultList(fetchOptions);
                LOG += "-- results ---> " +results.size();
                LOG += "-- results² ---> " +results.get(0).getKind();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                LOG += e.getMessage();
            }

            if(results != null){
                for(Entity e : results){
                    LOG += "-- comparing ---> " +e.getProperty("device").toString() +" == "+device;
                    if(e.getProperty("device").toString().equals(device)){
                        LOG += "-- comparing ---> " +e.getProperty("appId").toString() +" == "+appId;
                        if(e.getProperty("appId").toString().equals(appId)){
                            Config c = new Config();
                            c.setEnvironment(e.getProperty("environment").toString());
                            c.setAppId(appId);
                            c.setMethodName(e.getProperty("methodName").toString());
                            listResult.add(c);

                        }
                    }
                }
            }
            //THIS LIST HAS A LIST WITH THE DEVICES
        }catch (Exception ex){
            ex.printStackTrace();
            LOG += ex.getMessage();
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(LOG);
            resp.setStatus(200);
            return;
        }

        String json = new Gson().toJson( listResult );
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(json);
        resp.setStatus(200);
    }
}
