/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package ie.ncirl;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import java.util.List;

import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "syncDeviceAPI",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "ncirl.ie",
                ownerName = "ncirl.ie",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    /*@ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }*/
    @ApiMethod(name = "sync_device")
    public MyBean sync_device(@Named("appId") String appId, @Named("device") String device ) {
        MyBean response = new MyBean();

        Config conf = null;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter propertyFilter =
                new Query.FilterPredicate("device", Query.FilterOperator.GREATER_THAN_OR_EQUAL, device);

        Query q =
                new com.google.appengine.api.datastore.Query("Person").setFilter(propertyFilter);
        /*Query<Entity> query = Query.newEntityQueryBuilder().setKind("configs")
                .setFilter(propertyFilter)
                //.setOrderBy(StructuredQuery.OrderBy.asc("timeElapsed"))
                //.setLimit(10)
                .build();
        QueryResults<Entity> results = datastore.run(query);
*/
        List<Entity> results =
                datastore.prepare(q.setKeysOnly()).asList(FetchOptions.Builder.withDefaults());

        //TODO here call a already processed table for configs
        Long smallerTimeElapsed = null;
        for (Entity entity : results) {

            if (smallerTimeElapsed == null) {
                smallerTimeElapsed = Long.valueOf(entity.getProperty("timeElapsed").toString());
                conf = new Config(
                        entity.getProperty("device").toString(),
                        entity.getProperty("appId").toString(),
                        entity.getProperty("methodName").toString(),
                        entity.getProperty("environment").toString(),
                        Long.valueOf(entity.getProperty("timeElapsed").toString())
                );
            } else if (smallerTimeElapsed > Long.valueOf(entity.getProperty("timeElapsed").toString())) {
                smallerTimeElapsed = Long.valueOf(entity.getProperty("timeElapsed").toString());
                conf = new Config(
                        entity.getProperty("device").toString(),
                        entity.getProperty("appId").toString(),
                        entity.getProperty("methodName").toString(),
                        entity.getProperty("environment").toString(),
                        Long.valueOf(entity.getProperty("timeElapsed").toString())
                );
            }
        }

        response.setConfig(conf);
        response.setData("Hi, " + conf.getEnvironment());

        return response;
    }

    @ApiMethod(name = "processing_data")
    public void processing_data( ) {
        //TODO implement this method
        //retrieve all configs
        //get the mean for each device / method/ app
        //save into another table

    }
}
