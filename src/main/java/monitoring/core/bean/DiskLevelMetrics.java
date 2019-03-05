package monitoring.core.bean;

/**
 * Bean class for disk level metrics.
 */
public class DiskLevelMetrics {

    private String diskName;
    //The number of read requests that were issued to the device per second.
    private double noOfReadRequest;
    //The number of write requests that were issued to the device per second.
    private double noOfWriteRequests;
    //number of kilobytes read per second
    private double noOfReads;
    //number of kilobytes written per second
    private double noOfWrites;

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public double getNoOfReadRequest() {
        return noOfReadRequest;
    }

    public void setNoOfReadRequest(double noOfReadRequest) {
        this.noOfReadRequest = noOfReadRequest;
    }

    public double getNoOfWriteRequests() {
        return noOfWriteRequests;
    }

    public void setNoOfWriteRequests(double noOfWriteRequests) {
        this.noOfWriteRequests = noOfWriteRequests;
    }

    public double getNoOfReads() {
        return noOfReads;
    }

    public void setNoOfReads(double noOfReads) {
        this.noOfReads = noOfReads;
    }

    public double getNoOfWrites() {
        return noOfWrites;
    }

    public void setNoOfWrites(double noOfWrites) {
        this.noOfWrites = noOfWrites;
    }
}
