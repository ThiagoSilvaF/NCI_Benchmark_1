package ie.aomonitor;

/**
 * Created by Administrativo on 11/07/2017.
 */

public class ConfigMonitor {


    String device;
    String appId;
    String methodName;
    String environment;
    Long timeElapsed;

    ConfigMonitor(){}
    ConfigMonitor(String device, String appId, String methodName , String environment, Long timeElapsed ){
        this.device = device;
        this.environment = environment;
        this.appId = appId;
        this.methodName = methodName;
        this.timeElapsed = timeElapsed;
    }

    public String getDevice() {
        return device;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    public String getEnvironment() {
        return environment;
    }
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public Long getTimeElapsed() {
        return timeElapsed;
    }
    public void setTimeElapsed(Long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }



}
