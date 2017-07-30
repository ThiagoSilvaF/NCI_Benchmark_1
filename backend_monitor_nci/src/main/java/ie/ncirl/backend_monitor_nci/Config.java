package ie.ncirl.backend_monitor_nci;

class Config {

	String device;
	String appId;
	String methodName;
	String environment;
	Long avgTime;

	public Config(){}
	public Config(String device, String appId, String methodName , String environment, Long avgTime ){
		this.device = device;
		this.environment = environment;
		this.appId = appId;
		this.methodName = methodName;
		this.avgTime = avgTime;
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
	public Long getAvgTime() {
		return avgTime;
	}
	public void setAvgTime(Long avgTime) {
		this.avgTime = avgTime;
	}



}