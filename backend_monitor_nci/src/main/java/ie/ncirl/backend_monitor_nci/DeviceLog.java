package ie.ncirl.backend_monitor_nci;

/**
 * Created by silvat on 19/05/2017.
 */

public class DeviceLog {

    private String brand;
    private String device;
    private String hardware;
    private String model;
    private String product;
    private String manufacturer;
    private String board;

    private String memory;
    private String processor;

    private Long startTime;
    private Long endTime;

    private String appKey;
    private String methodName;

    public DeviceLog(){}

    public DeviceLog(String brand, String device, String hardware, String model, String product, String manufacturer, String board, String memory, String processor) {
        this.brand = brand;
        this.device = device;
        this.hardware = hardware;
        this.model = model;
        this.product = product;
        this.manufacturer = manufacturer;
        this.board = board;
        this.memory = memory;
        this.processor = processor;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public void setStartTime(Long startTime){ this.startTime = startTime; }

    public Long getStartTime(){ return startTime; }

    public void setEndTime(Long endTime ){ this.endTime = endTime;}

    public Long getEndTime(){return endTime;}

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}