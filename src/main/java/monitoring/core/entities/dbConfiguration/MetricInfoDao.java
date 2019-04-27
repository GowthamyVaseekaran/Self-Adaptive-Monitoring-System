package monitoring.core.entities.dbConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DB Configuration class.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricInfoDao {
    private String cpu;
    private String memory;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }
}
