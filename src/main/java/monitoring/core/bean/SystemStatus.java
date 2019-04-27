package monitoring.core.bean;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Bean class for system status.
 */
@Component
public class SystemStatus {
    private String currentTime;
    private String systemStatus;
    private HealthDeterminer healthDeterminer;
    private  List<List<DiskLevelMetrics>> diskLevelMetrics;
    private ThreadLevelMetrics threadLevelMetrics;
    private SystemLevelMetrics systemLevelMetrics;
    private NetworkStats networkStats;

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

    private String address;
    private String name;
    private String rxbytes;
    private String rxdropped;
    private String rxerrors;
    private String rxFrame;
    private String rxOverRuns;
    private String rxpackets;
    private String speed;
    private String txbytes;
    private String txcarrier;
    private String txcollisions;
    private String txdropped;
    private String txerrors;
    private String txoverruns;
    private String txpackets;

    private int runningThreadCount;
    private long totalThreadCount;
    private int peakThreadCount;
    private int daemonThreadCount;


    private double cpuUsage;
    private double loadAverage = 0;
    private double cpuIdle = 0;
    private double cpuNice = 0;
    private double cpuUser = 0;
    private double cpuWait = 0;
    private double totalPhysicalMemory = 0;
    private double committedVirtualMemory = 0;
    private double freePhycialMemory = 0;
    private double ramUsage = 0;
    private double totalSwapSize = 0;
    private double freeSwapSize = 0;
    private double usedSwapPercentage = 0;

    private double heapInitUsage;
    private double heapCommitted;
    private double heapUsed;
    private double heapMax;
    private double nonHeapInitUsage;
    private double nonHeapCommitted;
    private double nonHeapUsed;
    private double nonHeapMax;

    private double currentlyLoadedClass;
    private double totalLoadedClass;


    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getLoadAverage() {
        return loadAverage;
    }

    public void setLoadAverage(double loadAverage) {
        this.loadAverage = loadAverage;
    }

    public double getCpuIdle() {
        return cpuIdle;
    }

    public void setCpuIdle(double cpuIdle) {
        this.cpuIdle = cpuIdle;
    }

    public double getCpuNice() {
        return cpuNice;
    }

    public void setCpuNice(double cpuNice) {
        this.cpuNice = cpuNice;
    }

    public double getCpuUser() {
        return cpuUser;
    }

    public void setCpuUser(double cpuUser) {
        this.cpuUser = cpuUser;
    }

    public double getCpuWait() {
        return cpuWait;
    }

    public void setCpuWait(double cpuWait) {
        this.cpuWait = cpuWait;
    }

    public double getTotalPhysicalMemory() {
        return totalPhysicalMemory;
    }

    public void setTotalPhysicalMemory(double totalPhysicalMemory) {
        this.totalPhysicalMemory = totalPhysicalMemory;
    }

    public double getCommittedVirtualMemory() {
        return committedVirtualMemory;
    }

    public void setCommittedVirtualMemory(double committedVirtualMemory) {
        this.committedVirtualMemory = committedVirtualMemory;
    }

    public double getFreePhycialMemory() {
        return freePhycialMemory;
    }

    public void setFreePhycialMemory(double freePhycialMemory) {
        this.freePhycialMemory = freePhycialMemory;
    }

//    public double getUsedMemoryPercentage() {
//        return usedMemoryPercentage;
//    }
//
//    public void setUsedMemoryPercentage(double usedMemoryPercentage) {
//        this.usedMemoryPercentage = usedMemoryPercentage;
//    }

    public double getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(double ramUsage) {
        this.ramUsage = ramUsage;
    }

    public double getTotalSwapSize() {
        return totalSwapSize;
    }

    public void setTotalSwapSize(double totalSwapSize) {
        this.totalSwapSize = totalSwapSize;
    }

    public double getFreeSwapSize() {
        return freeSwapSize;
    }

    public void setFreeSwapSize(double freeSwapSize) {
        this.freeSwapSize = freeSwapSize;
    }

    public double getUsedSwapPercentage() {
        return usedSwapPercentage;
    }

    public void setUsedSwapPercentage(double usedSwapPercentage) {
        this.usedSwapPercentage = usedSwapPercentage;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRxbytes() {
        return rxbytes;
    }

    public void setRxbytes(String rxbytes) {
        this.rxbytes = rxbytes;
    }

    public String getRxdropped() {
        return rxdropped;
    }

    public void setRxdropped(String rxdropped) {
        this.rxdropped = rxdropped;
    }

    public String getRxerrors() {
        return rxerrors;
    }

    public void setRxerrors(String rxerrors) {
        this.rxerrors = rxerrors;
    }

    public String getRxFrame() {
        return rxFrame;
    }

    public void setRxFrame(String rxFrame) {
        this.rxFrame = rxFrame;
    }

    public String getRxOverRuns() {
        return rxOverRuns;
    }

    public void setRxOverRuns(String rxOverRuns) {
        this.rxOverRuns = rxOverRuns;
    }

    public String getRxpackets() {
        return rxpackets;
    }

    public void setRxpackets(String rxpackets) {
        this.rxpackets = rxpackets;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTxbytes() {
        return txbytes;
    }

    public void setTxbytes(String txbytes) {
        this.txbytes = txbytes;
    }

    public String getTxcarrier() {
        return txcarrier;
    }

    public void setTxcarrier(String txcarrier) {
        this.txcarrier = txcarrier;
    }

    public String getTxcollisions() {
        return txcollisions;
    }

    public void setTxcollisions(String txcollisions) {
        this.txcollisions = txcollisions;
    }

    public String getTxdropped() {
        return txdropped;
    }

    public void setTxdropped(String txdropped) {
        this.txdropped = txdropped;
    }

    public String getTxerrors() {
        return txerrors;
    }

    public void setTxerrors(String txerrors) {
        this.txerrors = txerrors;
    }

    public String getTxoverruns() {
        return txoverruns;
    }

    public void setTxoverruns(String txoverruns) {
        this.txoverruns = txoverruns;
    }

    public String getTxpackets() {
        return txpackets;
    }

    public void setTxpackets(String txpackets) {
        this.txpackets = txpackets;
    }

    public int getRunningThreadCount() {
        return runningThreadCount;
    }

    public void setRunningThreadCount(int runningThreadCount) {
        this.runningThreadCount = runningThreadCount;
    }

    public long getTotalThreadCount() {
        return totalThreadCount;
    }

    public void setTotalThreadCount(long totalThreadCount) {
        this.totalThreadCount = totalThreadCount;
    }

    public int getPeakThreadCount() {
        return peakThreadCount;
    }

    public void setPeakThreadCount(int peakThreadCount) {
        this.peakThreadCount = peakThreadCount;
    }

    public int getDaemonThreadCount() {
        return daemonThreadCount;
    }

    public void setDaemonThreadCount(int daemonThreadCount) {
        this.daemonThreadCount = daemonThreadCount;
    }

    public HealthDeterminer getHealthDeterminer() {
        return healthDeterminer;
    }

    public void setHealthDeterminer(HealthDeterminer healthDeterminer) {
        this.healthDeterminer = healthDeterminer;
    }

    public List<List<DiskLevelMetrics>> getDiskLevelMetrics() {
        return diskLevelMetrics;
    }

    public void setDiskLevelMetrics(List<List<DiskLevelMetrics>> diskLevelMetrics) {
        this.diskLevelMetrics = diskLevelMetrics;
    }

    public ThreadLevelMetrics getThreadLevelMetrics() {
        return threadLevelMetrics;
    }

    public void setThreadLevelMetrics(ThreadLevelMetrics threadLevelMetrics) {
        this.threadLevelMetrics = threadLevelMetrics;
    }

    public SystemLevelMetrics getSystemLevelMetrics() {
        return systemLevelMetrics;
    }

    public void setSystemLevelMetrics(SystemLevelMetrics systemLevelMetrics) {
        this.systemLevelMetrics = systemLevelMetrics;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }

    public NetworkStats getNetworkStats() {
        return networkStats;
    }

    public void setNetworkStats(NetworkStats networkStats) {
        this.networkStats = networkStats;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public double getHeapInitUsage() {
        return heapInitUsage;
    }

    public void setHeapInitUsage(double heapInitUsage) {
        this.heapInitUsage = heapInitUsage;
    }

    public double getHeapCommitted() {
        return heapCommitted;
    }

    public void setHeapCommitted(double heapCommitted) {
        this.heapCommitted = heapCommitted;
    }

    public double getHeapUsed() {
        return heapUsed;
    }

    public void setHeapUsed(double heapUsed) {
        this.heapUsed = heapUsed;
    }

    public double getHeapMax() {
        return heapMax;
    }

    public void setHeapMax(double heapMax) {
        this.heapMax = heapMax;
    }

    public double getNonHeapInitUsage() {
        return nonHeapInitUsage;
    }

    public void setNonHeapInitUsage(double nonHeapInitUsage) {
        this.nonHeapInitUsage = nonHeapInitUsage;
    }

    public double getNonHeapCommitted() {
        return nonHeapCommitted;
    }

    public void setNonHeapCommitted(double nonHeapCommitted) {
        this.nonHeapCommitted = nonHeapCommitted;
    }

    public double getNonHeapUsed() {
        return nonHeapUsed;
    }

    public void setNonHeapUsed(double nonHeapUsed) {
        this.nonHeapUsed = nonHeapUsed;
    }

    public double getNonHeapMax() {
        return nonHeapMax;
    }

    public void setNonHeapMax(double nonHeapMax) {
        this.nonHeapMax = nonHeapMax;
    }

    public double getCurrentlyLoadedClass() {
        return currentlyLoadedClass;
    }

    public void setCurrentlyLoadedClass(double currentlyLoadedClass) {
        this.currentlyLoadedClass = currentlyLoadedClass;
    }

    public double getTotalLoadedClass() {
        return totalLoadedClass;
    }

    public void setTotalLoadedClass(double totalLoadedClass) {
        this.totalLoadedClass = totalLoadedClass;
    }
}
