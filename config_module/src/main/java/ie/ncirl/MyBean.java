package ie.ncirl;

/**
 * The object model for the data we are sending through endpoints
 */
public class MyBean {

    private String myData;
    private Config config;



    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}