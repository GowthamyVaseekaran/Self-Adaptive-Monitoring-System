package monitoring.core.bean;

/**
 * Bean class for metrics.
 */
public class HealthDeterminer {
    private double cpuUsage;
    private double memoryUsage;
    private int systemCurrentStatus;

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getSystemCurrentStatus() {
        return systemCurrentStatus;
    }

    public void setSystemCurrentStatus(int systemCurrentStatus) {
        this.systemCurrentStatus = systemCurrentStatus;
    }
}
