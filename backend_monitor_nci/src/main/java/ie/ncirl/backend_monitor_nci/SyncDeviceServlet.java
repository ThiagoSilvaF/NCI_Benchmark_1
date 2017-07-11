/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package ie.ncirl.backend_monitor_nci;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

public class SyncDeviceServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response)
            throws IOException {

        String appId = req.getParameter("appId");
        String device = req.getParameter("device");

        Config conf = null;
        if (appId == null) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        try {
            //Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
          com.google.appengine.api.datastore.Query.Filter propertyFilter =
                    new com.google.appengine.api.datastore.Query.FilterPredicate("device", com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, device);
 /*
            com.google.appengine.api.datastore.Query q =
                    new com.google.appengine.api.datastore.Query("configs").setFilter(propertyFilter);


            List<com.google.appengine.api.datastore.Entity> results =
                    datastore.prepare(q.setDistinct(true)).asList(FetchOptions.Builder.withDefaults());

*/

            FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();

            // If this servlet is passed a cursor parameter, let's use it.


            com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("configs")
                    .addSort("device", com.google.appengine.api.datastore.Query.SortDirection.ASCENDING)
                    .setFilter(propertyFilter);
            PreparedQuery pq = datastore.prepare(q);

            QueryResultList<com.google.appengine.api.datastore.Entity> results = null;
            try {
                results = pq.asQueryResultList(fetchOptions);
            } catch (IllegalArgumentException e) {
                // IllegalArgumentException happens when an invalid cursor is used.
                // A user could have manually entered a bad cursor in the URL or there
                // may have been an internal implementation detail change in App Engine.
                // Redirect to the page without the cursor parameter to show something
                // rather than an error.
               // resp.sendRedirect("/people");
               // return;
            }

            Long smallerTimeElapsed = null;
            for (com.google.appengine.api.datastore.Entity entity : results) {
                if (smallerTimeElapsed == null) {
                    smallerTimeElapsed = (Long)entity.getProperty("timeElapsed");
                    conf = new Config(
                            entity.getProperty("device").toString(),
                            entity.getProperty("appId").toString(),
                            entity.getProperty("methodName").toString(),
                            entity.getProperty("environment").toString(),
                            smallerTimeElapsed
                    );
                } else if (smallerTimeElapsed > Long.valueOf(entity.getProperty("timeElapsed").toString())) {
                    smallerTimeElapsed = (Long) entity.getProperty("timeElapsed");
                    conf = new Config(
                            entity.getProperty("device").toString(),
                            entity.getProperty("appId").toString(),
                            entity.getProperty("methodName").toString(),
                            entity.getProperty("environment").toString(),
                            smallerTimeElapsed
                    );
                }
            }


        }
        catch (Exception e){
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(500);
        }

        String json = new com.google.gson.Gson().toJson(conf, Config.class);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
        response.setStatus(200);

    }
}
