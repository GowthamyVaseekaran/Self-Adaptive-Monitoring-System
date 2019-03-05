package monitoring.core.bean;

/**
 * Bean class for system details.
 */
public class SystemDetails {
    private String osName;
    private String javaVersion;
    private String osArchitecture;
    private String osVersion;
    private long noOfCores;

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getOsArchitecture() {
        return osArchitecture;
    }

    public void setOsArchitecture(String osArchitecture) {
        this.osArchitecture = osArchitecture;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public long getNoOfCores() {
        return noOfCores;
    }

    public void setNoOfCores(long noOfCores) {
        this.noOfCores = noOfCores;
    }
}
