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

    private String javaHome;
    private String javaRunTimeName;
    private String repoLocation;
    private String userName;
    private String userHome;

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

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public String getJavaRunTimeName() {
        return javaRunTimeName;
    }

    public void setJavaRunTimeName(String javaRunTimeName) {
        this.javaRunTimeName = javaRunTimeName;
    }

    public String getRepoLocation() {
        return repoLocation;
    }

    public void setRepoLocation(String repoLocation) {
        this.repoLocation = repoLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHome() {
        return userHome;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }
}
