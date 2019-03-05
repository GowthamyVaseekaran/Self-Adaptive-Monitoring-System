package monitoring.core.bean;

/**
 * Bean class for system level metrics.
 */
public class SystemLevelMetrics {
    private double cpuUsage;
    private double loadAverage;
    private double totalPhysicalMemory;
    private double committedVirtualMemory;
    private double freePhycialMemory;
    private double usedMemoryPercentage;
    private double totalSwapSize;
    private double freeSwapSize;
    private double usedSwapPercentage;

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

    public double getUsedMemoryPercentage() {
        return usedMemoryPercentage;
    }

    public void setUsedMemoryPercentage(double usedMemoryPercentage) {
        this.usedMemoryPercentage = usedMemoryPercentage;
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
}

