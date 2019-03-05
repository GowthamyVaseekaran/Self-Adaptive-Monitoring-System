package monitoring.core.metrics;

import com.google.gson.Gson;
import com.sun.management.OperatingSystemMXBean;

import monitoring.core.bean.DiskLevelMetrics;
import monitoring.core.bean.ProcessLevelMetrics;
import monitoring.core.bean.SystemDetails;
import monitoring.core.bean.SystemLevelMetrics;
import monitoring.core.bean.ThreadLevelMetrics;
import monitoring.core.metrics.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;


/**
 * Implementation for metrics class.
 */
@SuppressWarnings("CheckStyle")
public final class Metrics {

    private static String pid = null;

    private static String operatingSystem = System.getProperty(Constants.SYSTEM_PROPERTY_TO_GET_OS_NAME).toLowerCase();

    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Metrics.class);
    private static MBeanServer mBeanServer;
    private static OperatingSystemMXBean operatingSystemMXBean;
    private static ThreadMXBean threadMXBean;


    public Metrics(String stringArgs) {
        parseArguments(stringArgs);
    }


    private static String collectProcessLevelMetrics() throws IOException {
        if (operatingSystem.equals(Constants.LINUX_OS)) {
            ProcessLevelMetrics processLevelMetrics = new ProcessLevelMetrics();
            String[] command = runCommandToGetProcessLevelMetrics(pid).split(Constants.SPLIT_BY_DOUBLE_SPACE);
            processLevelMetrics.setProcesscpuPercentage(command[0]);
            processLevelMetrics.setProcessMemoryPercentage(command[1]);
            String jsonString = new Gson().toJson(processLevelMetrics);
            LOGGER.info(jsonString);
            return jsonString;
        } else {
            return null;

        }
    }

    public SystemLevelMetrics getSystemLevelMetrics() throws MalformedObjectNameException, InstanceNotFoundException, ReflectionException {
        SystemLevelMetrics systemLevelMetrics = new SystemLevelMetrics();
        systemLevelMetrics.setCpuUsage(getSystemCpuUsage());
        systemLevelMetrics.setLoadAverage(getSystemLoadAverage());
        systemLevelMetrics.setTotalPhysicalMemory(getTotalPhysicalMemory());
        systemLevelMetrics.setCommittedVirtualMemory(getCommittedVirtualMemorySize());
        systemLevelMetrics.setFreePhycialMemory(getFreePhysicalMemory());
        systemLevelMetrics.setUsedMemoryPercentage(getUsedPhysicalMemory());
        systemLevelMetrics.setTotalSwapSize(getTotalSwapSpaceSize());
        systemLevelMetrics.setFreeSwapSize(getFreeSwapSize());
        systemLevelMetrics.setUsedSwapPercentage(getUsedSwapPercentage());
        return systemLevelMetrics;
    }

    public ThreadLevelMetrics getThreadLevelMetrics() {
        ThreadLevelMetrics threadLevelMetrics = new ThreadLevelMetrics();
        threadLevelMetrics.setRunningThreadCount(getRunningThreadCount());
        threadLevelMetrics.setTotalThreadCount(getTotalStartedThreadCount());
        threadLevelMetrics.setPeakThreadCount(getPeakThreadCount());
        threadLevelMetrics.setDaemonThreadCount(getDaemonThreadCount());
        return threadLevelMetrics;
    }

    public DiskLevelMetrics getDiskLevelMetrics() {
        DiskLevelMetrics diskLevelMetrics = new DiskLevelMetrics();
        for (String command : runCommandToGetDiskLevelMetrics()) {
            String[] elements = command.split(Constants.SPLIT_BY_MULTIPLE_SPACE);
            diskLevelMetrics.setDiskName(elements[0]);
            String[] diskUsageMetrics = elements[1].split("    ");
            diskLevelMetrics.setNoOfReadRequest(Double.parseDouble(diskUsageMetrics[0]));
            diskLevelMetrics.setNoOfWriteRequests(Double.parseDouble(diskUsageMetrics[1].split("   ")[0]));
            diskLevelMetrics.setNoOfReads(Double.parseDouble(diskUsageMetrics[2]));
            diskLevelMetrics.setNoOfWrites(Double.parseDouble(diskUsageMetrics[3]));
        }
        return diskLevelMetrics;
    }


    private static String runCommandToGetProcessLevelMetrics(String processId) throws IOException {
        String commandOutput = null;
        String output = null;

        try {
            //TO GET EACH CPU CORE CPU-USAGE "ps -p 12093 -L -o pid,tid,psr,pcpu,pmem"
            // run the Unix "ps -p <pid> -o %cpu,%mem" command
            // using the Runtime exec method:
            //doubt: sometimes cpu percentage returns greater than 100 cuz it runs on multiple cpu cores
            Process p = Runtime.getRuntime().exec("ps -p " + processId + " -o %cpu,%mem");

            BufferedReader metricsReader = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader error = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((output = metricsReader.readLine()) != null) {
                if (!(output.equals(Constants.IGNORE_COMMAND_TAG))) {
                    commandOutput = output;
                    LOGGER.info(commandOutput);
                }
            }

            // read any errors from the attempted command
            while ((output = error.readLine()) != null) {
                commandOutput = output;
                LOGGER.info(commandOutput);
            }
            return commandOutput;
        } catch (IOException e) {
            commandOutput = "exception happened " + e.getMessage();
            LOGGER.error("exception occurred " + e.getMessage());
            System.exit(-1);
            return commandOutput;
        }
    }


    private static double getSystemCpuUsage() throws MalformedObjectNameException,
            ReflectionException, InstanceNotFoundException {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = ObjectName.getInstance(Constants.OPERATING_SYSTEM);
        AttributeList attributeList = mBeanServer.getAttributes(objectName, new String[]{Constants.SYSTEM_CPU_LOAD});
        if (attributeList.isEmpty()) {
            LOGGER.info("Empty");
            return Double.NaN;
        }
        Attribute attribute = (Attribute) attributeList.get(0);
        Double cpuValue = (Double) attribute.getValue();

        //usually it takes some seconds to get the real value.
        if (cpuValue == -1.0) {
            LOGGER.info("NONE");
            return Double.NaN;
        }
        // LOGGER.info("system cpu " + (int) (cpuValue * 1000) / 10.0);
        return ((int) (cpuValue * 1000) / 10.0);
    }

    private static double getProcessCpuUsage() throws
            MalformedObjectNameException, ReflectionException, InstanceNotFoundException {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList attributeList = mBeanServer.getAttributes(objectName, new String[]{"ProcessCpuLoad"});
        if (attributeList.isEmpty()) {
            LOGGER.info("Empty");
            return Double.NaN;
        }
        Attribute attribute = (Attribute) attributeList.get(0);
        Double cpuValue = (Double) attribute.getValue();

        //usually it takes some seconds to get the real value.
        if (cpuValue == -1.0) {
            LOGGER.info("NONE");
            return Double.NaN;
        }
        return ((int) (cpuValue * 1000) / 10.0);
    }

    private double getSystemLoadAverage() throws
            MalformedObjectNameException, ReflectionException, InstanceNotFoundException {
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = ObjectName.getInstance(Constants.OPERATING_SYSTEM);
        AttributeList attributeList = mBeanServer.getAttributes(objectName, new String[]{Constants.LOAD_AVERAGE});
        if (attributeList.isEmpty()) {
            LOGGER.error("There is no attribute");
            return Double.NaN;
        }

        Attribute attribute = (Attribute) attributeList.get(0);
        Double loadAverage = (Double) attribute.getValue();
        return loadAverage;
    }

    //    Host memory size in in mega bytes.
    private static double getTotalPhysicalMemory() {
        operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double totalMemory = (operatingSystemMXBean.getTotalPhysicalMemorySize()) / (1024 * 1024);
        if (totalMemory == 0.00d) {
            LOGGER.error("Something went wrong please check");
            return Double.NaN;
        }
        return totalMemory;
    }

    private static double getCommittedVirtualMemorySize() {
        operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double committedVirtualMemory = (operatingSystemMXBean.getCommittedVirtualMemorySize()) / (1024 * 1024);
        if (committedVirtualMemory == 0.00d) {
            LOGGER.error("Something went wrong please check");
            return Double.NaN;
        }
        return committedVirtualMemory;
    }

    private static double getFreePhysicalMemory() {
        operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double freePhysicalMemory = (operatingSystemMXBean.getFreePhysicalMemorySize()) / (1024 * 1024);
        if (freePhysicalMemory == 0.00d) {
            LOGGER.error("Memory out of bound");
        }
        return freePhysicalMemory;
    }

    //Get used physical memory percentage ASK ANNA
    private double getUsedPhysicalMemory() {
        operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double totalMemory = (operatingSystemMXBean.getTotalPhysicalMemorySize()) / (1024 * 1024);
        double freeMemory = (operatingSystemMXBean.getFreePhysicalMemorySize()) / (1024 * 1024);
        double usedMemory = (totalMemory - freeMemory);
        double usedMemoryPercentage = Double.parseDouble(
                String.format(Constants.TWO_DECIMAL, (usedMemory / totalMemory) * 100));
        return usedMemoryPercentage;
    }

    private static double getTotalSwapSpaceSize() {
        operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double totalSwapSpaceSize = (operatingSystemMXBean.getTotalSwapSpaceSize()) / (1024 * 1024);
        if (totalSwapSpaceSize == 0.00d) {
            LOGGER.error("Something went wrong please check");
            return Double.NaN;
        }
        return totalSwapSpaceSize;
    }

    private static double getFreeSwapSize() {
        operatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double freeSwapSize = (operatingSystemMXBean.getFreeSwapSpaceSize()) / (1024 * 1024);
        if (freeSwapSize == 0.00d) {
            LOGGER.error("Something went wrong please check");
            return Double.NaN;
        }
        return freeSwapSize;
    }

    private static double getUsedSwapPercentage() {
        double totalSwapSize = (operatingSystemMXBean.getTotalSwapSpaceSize()) / (1024 * 1024);
        double freeSwapSize = (operatingSystemMXBean.getFreeSwapSpaceSize()) / (1024 * 1024);
        double usedSwapSize = totalSwapSize - freeSwapSize;
        double usedSwapPercentage = Double.parseDouble(String.format(
                Constants.TWO_DECIMAL, (usedSwapSize / totalSwapSize) * 100));
        return usedSwapPercentage;
    }


    public SystemDetails getSystemStats() {
        SystemDetails systemDetails = new SystemDetails();
        systemDetails.setJavaVersion(System.getProperty(Constants.JAVA_VERSION));
        systemDetails.setOsName(System.getProperty(Constants.SYSTEM_PROPERTY_TO_GET_OS_NAME));
        systemDetails.setOsArchitecture(System.getProperty(Constants.OS_ARCHITECTURE));
        systemDetails.setOsVersion(System.getProperty(Constants.OS_VERSION));
        systemDetails.setNoOfCores(Long.parseLong(String.valueOf(Runtime.getRuntime().availableProcessors())));
        return systemDetails;
    }

    private static int getRunningThreadCount() {
        threadMXBean = ManagementFactory.getPlatformMXBean(ThreadMXBean.class);
        int runningThread = threadMXBean.getThreadCount();
        return runningThread;
    }

    private static long getTotalStartedThreadCount() {
        threadMXBean = ManagementFactory.getPlatformMXBean(ThreadMXBean.class);
        long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();
        return totalStartedThreadCount;
    }

    private static int getPeakThreadCount() {
        threadMXBean = ManagementFactory.getPlatformMXBean(ThreadMXBean.class);
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        return peakThreadCount;
    }

    private static int getDaemonThreadCount() {
        threadMXBean = ManagementFactory.getPlatformMXBean(ThreadMXBean.class);
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        return daemonThreadCount;
    }


    private void parseArguments(String stringArgs) {

    }

    private static ArrayList<String> runCommandToGetDiskLevelMetrics() {
        ArrayList<String> commandOutput = new ArrayList<>();
        // String[] commandOutput = null;
        String output = null;

        try {
            // run the Unix "iostat -dkx" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(Constants.LINUX_COMMAND_TO_GET_DISK_USAGE);

            BufferedReader metricsReader = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader error = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            //Skip first three lines : to ignore command header
            for (int i = 1; i <= 3; i++) {
                metricsReader.readLine();
            }

            // read the output from the command
            while ((output = metricsReader.readLine()) != null) {
                //To skip blank line
                if (output.length() > 0) {
                    commandOutput.add(output);
                }

            }


            // read any errors from the attempted command
            while ((output = error.readLine()) != null) {
                commandOutput.add(output);
                LOGGER.info(commandOutput);
            }

            return commandOutput;
        } catch (IOException e) {
            commandOutput.add("exception happened " + e.getMessage());
            LOGGER.error("exception occurred " + e.getMessage());
            System.exit(-1);
            return commandOutput;
        }
    }


//        stats.put("maxMemory", Runtime.getRuntime().maxMemory());
//        LOGGER.info("max memory " + Runtime.getRuntime().maxMemory());
//        stats.put("totalMemory", Runtime.getRuntime().totalMemory());
//        LOGGER.info("total Memory " + Runtime.getRuntime().totalMemory());
//        stats.put("freeMemory", Runtime.getRuntime().freeMemory());
//        LOGGER.info("free memory " + Runtime.getRuntime().freeMemory());
//        stats.put("cpuUsage", Math.min(round(cpuUsage), 100.0));
//        LOGGER.info("cpu Usage " + cpuUsage);


}
