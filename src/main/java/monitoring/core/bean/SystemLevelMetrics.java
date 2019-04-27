package monitoring.core.bean;

/**
 * Bean class for system level Metrics.
 */
public class SystemLevelMetrics {
    private double cpuUsage;
    private double usedMemoryPercentage;

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getUsedMemoryPercentage() {
        return usedMemoryPercentage;
    }

    public void setUsedMemoryPercentage(double usedMemoryPercentage) {
        this.usedMemoryPercentage = usedMemoryPercentage;
    }
}
