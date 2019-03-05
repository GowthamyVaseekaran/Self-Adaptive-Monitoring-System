package monitoring.core.bean;

import org.springframework.stereotype.Component;

/**
 * Bean class for system status.
 */
@Component
public class SystemStatus {
    private String systemStatus;
    private HealthDeterminer healthDeterminer;
    private  DiskLevelMetrics diskLevelMetrics;
    private ThreadLevelMetrics threadLevelMetrics;
    private SystemLevelMetrics systemLevelMetrics;


    public HealthDeterminer getHealthDeterminer() {
        return healthDeterminer;
    }

    public void setHealthDeterminer(HealthDeterminer healthDeterminer) {
        this.healthDeterminer = healthDeterminer;
    }

    public DiskLevelMetrics getDiskLevelMetrics() {
        return diskLevelMetrics;
    }

    public void setDiskLevelMetrics(DiskLevelMetrics diskLevelMetrics) {
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
}
