package monitoring.core.bean;

/**
 * Bean class for process level Metrics.
 */
public class ProcessLevelMetrics {
    private String  processcpuPercentage;
    private String processMemoryPercentage;
    //tps and latency


    public String getProcesscpuPercentage() {
        return processcpuPercentage;
    }

    public void setProcesscpuPercentage(String processcpuPercentage) {
        this.processcpuPercentage = processcpuPercentage;
    }

    public String getProcessMemoryPercentage() {
        return processMemoryPercentage;
    }

    public void setProcessMemoryPercentage(String processMemoryPercentage) {
        this.processMemoryPercentage = processMemoryPercentage;
    }
}
