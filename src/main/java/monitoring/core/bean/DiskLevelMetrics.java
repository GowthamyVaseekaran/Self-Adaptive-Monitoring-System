package monitoring.core.bean;

/**
 * Bean class for disk level Metrics.
 */
public class DiskLevelMetrics {

    private String diskName;
    //The number of read requests that were issued to the device per second.
    private double diskReadBytes;
    //The number of write requests that were issued to the device per second.
    private double diskWriteBytes;
    //number of kilobytes read per second
    private double noOfReads;
    //number of kilobytes written per second
    private double noOfWrites;

    private double totalSpace;

    private double usedSpace;

    private double freeSpace;

    private double fileCount;

    private double usedPercentage;

    public double getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(double usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public double getDiskReadBytes() {
        return diskReadBytes;
    }

    public void setDiskReadBytes(double diskReadBytes) {
        this.diskReadBytes = diskReadBytes;
    }

    public double getDiskWriteBytes() {
        return diskWriteBytes;
    }

    public void setDiskWriteBytes(double diskWriteBytes) {
        this.diskWriteBytes = diskWriteBytes;
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

    public double getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(double totalSpace) {
        this.totalSpace = totalSpace;
    }

    public double getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(double usedSpace) {
        this.usedSpace = usedSpace;
    }

    public double getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(double freeSpace) {
        this.freeSpace = freeSpace;
    }

    public double getFileCount() {
        return fileCount;
    }

    public void setFileCount(double fileCount) {
        this.fileCount = fileCount;
    }
}
