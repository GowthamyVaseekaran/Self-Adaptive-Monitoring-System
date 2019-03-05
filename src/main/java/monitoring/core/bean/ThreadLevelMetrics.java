package monitoring.core.bean;

/**
 * Bean class for thread level metrics.
 */
public class ThreadLevelMetrics {
    private int runningThreadCount;
    private long totalThreadCount;
    private int peakThreadCount;
    private int daemonThreadCount;

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
}
