package monitoring.core.executor.impl;

import monitoring.core.bean.DiskLevelMetrics;
import monitoring.core.bean.SystemStatus;
import monitoring.core.executor.impl.utils.Constants;
import monitoring.core.monitor.Metrics;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

/**
 * Metrics changer class.
 */
@Component
public class MetricsChanger {

    public void changeMetrics(String metrics, SystemStatus systemStatus, Metrics metricsAgent)
            throws MalformedObjectNameException, ReflectionException, SigarException, InstanceNotFoundException {
        switch (metrics) {
            case Constants.CPU_IDLE:
                double cpuIdle = metricsAgent.getCPUIdle();
                systemStatus.setCpuIdle(cpuIdle);
                break;
            case Constants.CPU_NICE:
                double cpuNice = metricsAgent.getCPUNice();
                systemStatus.setCpuNice(cpuNice);
                break;
            case Constants.CPU_USER:
                double cpuUser = metricsAgent.getCPUUser();
                systemStatus.setCpuUser(cpuUser);
                break;
            case Constants.CPU_WAIT:
                double cpuWait = metricsAgent.getCPUWait();
                systemStatus.setCpuWait(cpuWait);
                break;
            case Constants.COMMITTED_VM:
                double committedVM = metricsAgent.getCommittedVirtualMemorySize();
                systemStatus.setCommittedVirtualMemory(committedVM);
                double ramUsage = metricsAgent.getRAMUsage();
                systemStatus.setRamUsage(ramUsage);
                break;
            case Constants.FREE_PHYSICAL_MEMORY:
                double freePhysicalMemory = metricsAgent.getFreePhysicalMemory();
                double totalPhysicalMemory = metricsAgent.getTotalPhysicalMemory();
                systemStatus.setFreePhycialMemory(freePhysicalMemory);
                systemStatus.setTotalPhysicalMemory(totalPhysicalMemory);
                break;
            case Constants.RAM_USAGE:
                ramUsage = metricsAgent.getRAMUsage();
                systemStatus.setRamUsage(ramUsage);
                break;
            case Constants.FREE_SWAP_USAGE:
                double freeSwapUsage = metricsAgent.getFreeSwapSize();
                systemStatus.setFreeSwapSize(freeSwapUsage);
                double totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
                systemStatus.setTotalSwapSize(totalSwapSize);
                break;
            case Constants.TOTAL_SWAP_SIZE:
                totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
                systemStatus.setTotalSwapSize(totalSwapSize);
                freeSwapUsage = metricsAgent.getFreeSwapSize();
                systemStatus.setFreeSwapSize(freeSwapUsage);
                break;
            case Constants.USED_SWAP_SIZE:
                double usedSwapSize = metricsAgent.getUsedSwapPercentage();
                totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
                systemStatus.setTotalSwapSize(totalSwapSize);
                freeSwapUsage = metricsAgent.getFreeSwapSize();
                systemStatus.setFreeSwapSize(freeSwapUsage);
                systemStatus.setUsedSwapPercentage(usedSwapSize);
                break;
            case Constants.NO_OF_READS:
            case Constants.READ_BYTES:
                for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
                    for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
                        String diskName = diskLevelMetrics.getDiskName();
                        double noOfReads = diskLevelMetrics.getNoOfReads();
                        double readBytes = diskLevelMetrics.getDiskReadBytes();
                        systemStatus.setDiskName(diskName);
                        systemStatus.setNoOfReads(noOfReads);
                        systemStatus.setDiskReadBytes(readBytes);
                    }
                }

                break;
            case Constants.NO_OF_WRITES:
            case Constants.WRITE_BYTES:
                for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
                    for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
                        String diskName = diskLevelMetrics.getDiskName();
                        double noOfWrites = diskLevelMetrics.getNoOfWrites();
                        double writeRequests = diskLevelMetrics.getDiskWriteBytes();
                        systemStatus.setDiskName(diskName);
                        systemStatus.setNoOfWrites(noOfWrites);
                        systemStatus.setDiskWriteBytes(writeRequests);
                    }
                }
                break;
            case Constants.FILE_COUNT:
                for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
                    for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
                        double fileCount = diskLevelMetrics.getFileCount();
                        systemStatus.setFileCount(fileCount);
                    }
                }

                break;
            case Constants.TOTAL_THREAD_COUNT:
                long threadCount = metricsAgent.getTotalStartedThreadCount();
                systemStatus.setTotalThreadCount(threadCount);
                break;
            case Constants.DAEMON_THREAD_COUNT:
                int daemonThreadCount = metricsAgent.getDaemonThreadCount();
                systemStatus.setDaemonThreadCount(daemonThreadCount);
                break;
            case Constants.PEAK_THREAD_COUNT:
                int peakThreadCount = metricsAgent.getPeakThreadCount();
                systemStatus.setPeakThreadCount(peakThreadCount);
                break;
            case Constants.RUNNING_THREAD_COUNT:
                int runningThreadCount = metricsAgent.getRunningThreadCount();
                systemStatus.setRunningThreadCount(runningThreadCount);
                break;
            case Constants.RX_BYTES:
            case Constants.RX_DROPPED:
            case Constants.RX_ERROR:
            case Constants.RX_FRAMES:
            case Constants.RX_OVERRUNS:
            case Constants.RX_PACKETS:
                String networkAddress = metricsAgent.getIfstats().getAddress();
                String name = metricsAgent.getIfstats().getName();
                String rxDropped = metricsAgent.getIfstats().getRxdropped();
                String rxError = metricsAgent.getIfstats().getRxerrors();
                String rxFrame = metricsAgent.getIfstats().getRxFrame();
                String rxOverrun = metricsAgent.getIfstats().getRxOverRuns();
                String rxPackets = metricsAgent.getIfstats().getRxpackets();
                systemStatus.setAddress(networkAddress);
                systemStatus.setName(name);
                systemStatus.setRxdropped(rxDropped);
                systemStatus.setRxerrors(rxError);
                systemStatus.setRxFrame(rxFrame);
                systemStatus.setRxOverRuns(rxOverrun);
                systemStatus.setRxpackets(rxPackets);
                break;
            case Constants.SPEED:
                networkAddress = metricsAgent.getIfstats().getAddress();
                name = metricsAgent.getIfstats().getName();
                String speed = metricsAgent.getIfstats().getSpeed();
                systemStatus.setAddress(networkAddress);
                systemStatus.setName(name);
                systemStatus.setSpeed(speed);
                break;
            case Constants.TX_BYTES:
            case Constants.TX_CARRIER:
            case Constants.TX_COLLISIONS:
            case Constants.TX_DROPPED:
            case Constants.TX_ERRORS:
            case Constants.TX_OVERRUNS:
            case Constants.TX_PACKETS:
                networkAddress = metricsAgent.getIfstats().getAddress();
                name = metricsAgent.getIfstats().getName();
                String txCarrier = metricsAgent.getIfstats().getTxcarrier();
                String txCollision = metricsAgent.getIfstats().getTxcollisions();
                String txDropped = metricsAgent.getIfstats().getTxdropped();
                String txErrors = metricsAgent.getIfstats().getTxerrors();
                String txOverruns = metricsAgent.getIfstats().getTxoverruns();
                String txPackets = metricsAgent.getIfstats().getTxpackets();
                systemStatus.setAddress(networkAddress);
                systemStatus.setName(name);
                systemStatus.setTxcarrier(txCarrier);
                systemStatus.setTxcollisions(txCollision);
                systemStatus.setTxdropped(txDropped);
                systemStatus.setTxerrors(txErrors);
                systemStatus.setTxoverruns(txOverruns);
                systemStatus.setTxpackets(txPackets);
                break;

            case Constants.HEAP_USED :
                systemStatus.setHeapInitUsage(metricsAgent.getHeapInitMemoryUsage());
                systemStatus.setHeapCommitted(metricsAgent.getHeapCommittedMemory());
                systemStatus.setHeapUsed(metricsAgent.getUsedHeapMemory());
                systemStatus.setHeapMax(metricsAgent.getMaxHeapMemory());
                break;

            case Constants.NON_HEAP_USED:
                systemStatus.setNonHeapInitUsage(metricsAgent.getNonHeapInitMemory());
                systemStatus.setNonHeapCommitted(metricsAgent.getNonHeapCommittedMemory());
                systemStatus.setNonHeapUsed(metricsAgent.getNonUsedHeapMemory());
                systemStatus.setNonHeapMax(metricsAgent.getNonMaxHeapMemory());
                break;

        }

    }

    public void resetLowMetricsValues(SystemStatus systemStatus) {
        systemStatus.setCpuIdle(0);
        systemStatus.setCpuNice(0);
        systemStatus.setCpuUser(0);
        systemStatus.setCpuWait(0);
        systemStatus.setCommittedVirtualMemory(0);
        systemStatus.setTotalPhysicalMemory(0);
        systemStatus.setFreePhycialMemory(0);
        systemStatus.setRamUsage(0);
        systemStatus.setFreeSwapSize(0);
        systemStatus.setLoadAverage(0);
        systemStatus.setTotalSwapSize(0);
        systemStatus.setUsedSwapPercentage(0);
        systemStatus.setDiskName(" ");
        systemStatus.setNoOfReads(0);
        systemStatus.setDiskReadBytes(0);
        systemStatus.setNoOfWrites(0);
        systemStatus.setDiskWriteBytes(0);
        systemStatus.setTotalSpace(0);
        systemStatus.setUsedSpace(0);
        systemStatus.setFreeSpace(0);
        systemStatus.setFileCount(0);
        systemStatus.setTotalThreadCount(0);
        systemStatus.setPeakThreadCount(0);
        systemStatus.setDaemonThreadCount(0);
        systemStatus.setRunningThreadCount(0);
        systemStatus.setAddress(" ");
        systemStatus.setName(" ");
        systemStatus.setRxbytes(String.valueOf(0));
        systemStatus.setRxdropped(String.valueOf(0));
        systemStatus.setRxerrors(String.valueOf(0));
        systemStatus.setRxFrame(String.valueOf(0));
        systemStatus.setRxOverRuns(String.valueOf(0));
        systemStatus.setRxpackets(String.valueOf(0));
        systemStatus.setSpeed(String.valueOf(-1));
        systemStatus.setTxbytes(String.valueOf(0));
        systemStatus.setTxcarrier(String.valueOf(0));
        systemStatus.setTxcollisions(String.valueOf(0));
        systemStatus.setTxdropped(String.valueOf(0));
        systemStatus.setTxerrors(String.valueOf(0));
        systemStatus.setTxoverruns(String.valueOf(0));
        systemStatus.setTxpackets(String.valueOf(0));
        systemStatus.setHeapInitUsage(0);
        systemStatus.setHeapCommitted(0);
        systemStatus.setHeapUsed(0);
        systemStatus.setHeapMax(0);
        systemStatus.setNonHeapInitUsage(0);
        systemStatus.setNonHeapCommitted(0);
        systemStatus.setNonHeapUsed(0);
        systemStatus.setNonHeapMax(0);
        systemStatus.setCurrentlyLoadedClass(0);
        systemStatus.setTotalLoadedClass(0);
    }

    public void collectAllMetrics(SystemStatus systemStatus, Metrics metricsAgent)
            throws SigarException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException {
        double cpuIdle = metricsAgent.getCPUIdle();
        systemStatus.setCpuIdle(cpuIdle);

        double cpuNice = metricsAgent.getCPUNice();
        systemStatus.setCpuNice(cpuNice);

        double cpuUser = metricsAgent.getCPUUser();
        systemStatus.setCpuUser(cpuUser);
        double cpuWait = metricsAgent.getCPUWait();
        systemStatus.setCpuWait(cpuWait);
        double committedVM = metricsAgent.getCommittedVirtualMemorySize();
        systemStatus.setCommittedVirtualMemory(committedVM);
        double freePhysicalMemory = metricsAgent.getFreePhysicalMemory();
        double totalPhysicalMemory = metricsAgent.getTotalPhysicalMemory();
        systemStatus.setFreePhycialMemory(freePhysicalMemory);
        systemStatus.setTotalPhysicalMemory(totalPhysicalMemory);

        double ramUsage = metricsAgent.getRAMUsage();
        systemStatus.setRamUsage(ramUsage);

        double freeSwapUsage = metricsAgent.getFreeSwapSize();
        systemStatus.setFreeSwapSize(freeSwapUsage);

        double loadAverage = metricsAgent.getSystemLoadAverage();
        systemStatus.setLoadAverage(loadAverage);

        double totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
        systemStatus.setTotalSwapSize(totalSwapSize);

        double usedSwapSize = metricsAgent.getUsedSwapPercentage();
        systemStatus.setUsedSwapPercentage(usedSwapSize);

        for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
            for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
                String diskName = diskLevelMetrics.getDiskName();
                double noOfReads = diskLevelMetrics.getNoOfReads();
                double readBytes = diskLevelMetrics.getDiskReadBytes();
                systemStatus.setDiskName(diskName);
                systemStatus.setNoOfReads(noOfReads);
                systemStatus.setDiskReadBytes(readBytes);
                double noOfWrites = diskLevelMetrics.getNoOfWrites();
                double writeRequests = diskLevelMetrics.getDiskWriteBytes();
                systemStatus.setNoOfWrites(noOfWrites);
                systemStatus.setDiskWriteBytes(writeRequests);
                double totalDiskSpace = diskLevelMetrics.getTotalSpace();
                double usedDiskSpace = diskLevelMetrics.getUsedSpace();
                double freeDiskSpace = diskLevelMetrics.getFreeSpace();

                systemStatus.setTotalSpace(totalDiskSpace);
                systemStatus.setUsedSpace(usedDiskSpace);
                systemStatus.setFreeSpace(freeDiskSpace);
                double fileCount = diskLevelMetrics.getFileCount();
                systemStatus.setFileCount(fileCount);
            }
        }



        long threadCount = metricsAgent.getTotalStartedThreadCount();
        systemStatus.setTotalThreadCount(threadCount);

        int daemonThreadCount = metricsAgent.getDaemonThreadCount();
        systemStatus.setDaemonThreadCount(daemonThreadCount);

        int peakThreadCount = metricsAgent.getPeakThreadCount();
        systemStatus.setPeakThreadCount(peakThreadCount);

        int runningThreadCount = metricsAgent.getRunningThreadCount();
        systemStatus.setRunningThreadCount(runningThreadCount);

        String networkAddress = metricsAgent.getIfstats().getAddress();
        String name = metricsAgent.getIfstats().getName();
        String rxBytes = metricsAgent.getIfstats().getRxbytes();
        String rxDropped = metricsAgent.getIfstats().getRxdropped();
        String rxError = metricsAgent.getIfstats().getRxerrors();
        String rxFrame = metricsAgent.getIfstats().getRxFrame();
        String rxOverrun = metricsAgent.getIfstats().getRxOverRuns();
        String rxPackets = metricsAgent.getIfstats().getRxpackets();
        systemStatus.setAddress(networkAddress);
        systemStatus.setName(name);
        systemStatus.setRxbytes(rxBytes);
        systemStatus.setRxdropped(rxDropped);
        systemStatus.setRxerrors(rxError);
        systemStatus.setRxFrame(rxFrame);
        systemStatus.setRxOverRuns(rxOverrun);
        systemStatus.setRxpackets(rxPackets);


        String speed = metricsAgent.getIfstats().getSpeed();

        systemStatus.setSpeed(speed);


        String txBytes = metricsAgent.getIfstats().getTxbytes();
        String txCarrier = metricsAgent.getIfstats().getTxcarrier();
        String txCollision = metricsAgent.getIfstats().getTxcollisions();
        String txDropped = metricsAgent.getIfstats().getTxdropped();
        String txErrors = metricsAgent.getIfstats().getTxerrors();
        String txOverruns = metricsAgent.getIfstats().getTxoverruns();
        String txPackets = metricsAgent.getIfstats().getTxpackets();

        systemStatus.setTxbytes(txBytes);
        systemStatus.setTxcarrier(txCarrier);
        systemStatus.setTxcollisions(txCollision);
        systemStatus.setTxdropped(txDropped);
        systemStatus.setTxerrors(txErrors);
        systemStatus.setTxoverruns(txOverruns);
        systemStatus.setTxpackets(txPackets);

        systemStatus.setHeapInitUsage(metricsAgent.getHeapInitMemoryUsage());
        systemStatus.setHeapCommitted(metricsAgent.getHeapCommittedMemory());
        systemStatus.setHeapUsed(metricsAgent.getUsedHeapMemory());
        systemStatus.setHeapMax(metricsAgent.getMaxHeapMemory());

        systemStatus.setNonHeapInitUsage(metricsAgent.getNonHeapInitMemory());
        systemStatus.setNonHeapCommitted(metricsAgent.getNonHeapCommittedMemory());
        systemStatus.setNonHeapUsed(metricsAgent.getNonUsedHeapMemory());
        systemStatus.setNonHeapMax(metricsAgent.getNonMaxHeapMemory());

        systemStatus.setCurrentlyLoadedClass(metricsAgent.currentlyLoadedClassCount());
        systemStatus.setTotalLoadedClass(metricsAgent.totalLoadedClass());



    }

    public void memoryMetricsSelection(Metrics metricsAgent,
                                       SystemStatus systemStatus, Map<String, Map<String, Double>> coEfficentCalculator)
            throws MalformedObjectNameException, ReflectionException, SigarException, InstanceNotFoundException {

        for (Map.Entry<String, Map<String, Double>> err : coEfficentCalculator.entrySet()) {
            for (Map.Entry<String, Double> rer : err.getValue().entrySet()) {
                if (err.getKey().equalsIgnoreCase(Constants.MEMORY_USAGE)) {
                    changeMetrics(rer.getKey(), systemStatus, metricsAgent);
                }
            }
        }
    }

    public void cpuMetricsSelection(Metrics metricsAgent, SystemStatus systemStatus,
                                    Map<String, Map<String, Double>> coEfficentCalculator)
            throws MalformedObjectNameException, ReflectionException, SigarException, InstanceNotFoundException {
        for (Map.Entry<String, Map<String, Double>> err : coEfficentCalculator.entrySet()) {
            for (Map.Entry<String, Double> rer : err.getValue().entrySet()) {
                if (err.getKey().equalsIgnoreCase(Constants.CPU_USAGE)) {
                    changeMetrics(rer.getKey(), systemStatus, metricsAgent);
                }
            }
        }
    }

    public Map<String, SystemStatus> collectHighLevelMetrics(Metrics metricsAgent, SystemStatus systemStatus)
            throws SigarException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException {
        Map<String, SystemStatus> system = new HashMap<>();
        for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
            for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
                String diskName = diskLevelMetrics.getDiskName();
                double totalDiskSpace = diskLevelMetrics.getTotalSpace();
                double usedDiskSpace = diskLevelMetrics.getUsedSpace();
                double freeDiskSpace = diskLevelMetrics.getFreeSpace();
                systemStatus.setDiskName(diskName);
                systemStatus.setTotalSpace(totalDiskSpace);
                systemStatus.setUsedSpace(usedDiskSpace);
                systemStatus.setFreeSpace(freeDiskSpace);
                system.put(systemStatus.getDiskName(), systemStatus);
            }

        }

        double loadAverage = metricsAgent.getSystemLoadAverage();
        systemStatus.setLoadAverage(loadAverage);

        String networkAddress = metricsAgent.getIfstats().getAddress();
        String name = metricsAgent.getIfstats().getName();
        String rxBytes = metricsAgent.getIfstats().getRxbytes();
        systemStatus.setAddress(networkAddress);
        systemStatus.setName(name);
        systemStatus.setRxbytes(rxBytes);
        String txBytes = metricsAgent.getIfstats().getTxbytes();
        systemStatus.setTxbytes(txBytes);

        systemStatus.setCurrentlyLoadedClass(metricsAgent.currentlyLoadedClassCount());
        systemStatus.setTotalLoadedClass(metricsAgent.totalLoadedClass());
        double totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
        systemStatus.setTotalSwapSize(totalSwapSize);
        double totalPhysicalMemory = metricsAgent.getTotalPhysicalMemory();
        systemStatus.setTotalPhysicalMemory(totalPhysicalMemory);
        double committedVM = metricsAgent.getCommittedVirtualMemorySize();
        systemStatus.setCommittedVirtualMemory(committedVM);
        double ramUsage = metricsAgent.getRAMUsage();
        systemStatus.setRamUsage(ramUsage);


        return system;

    }



}
