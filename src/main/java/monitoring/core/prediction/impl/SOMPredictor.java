package monitoring.core.prediction.impl;

import com.google.gson.Gson;

//import monitoring.core.Entities.Metrics.TestRepository;
//import monitoring.core.Entities.Metrics.TestRepository;
import monitoring.core.DatabaseConnector;
//import monitoring.core.Entities.Metrics.TestRepository;
import monitoring.core.Entities.DBConfiguration.MetricInfoDao;
import monitoring.core.Entities.DBConfiguration.TestRepository;
import monitoring.core.bean.DiskLevelMetrics;
import monitoring.core.bean.HealthDeterminer;
import monitoring.core.bean.SystemStatus;
import monitoring.core.metrics.HeapDumper;
import monitoring.core.metrics.Metrics;
import monitoring.core.prediction.impl.utils.Constants;
import monitoring.core.som.Neuron;
import monitoring.core.som.SOM;
import monitoring.core.som.WeightVector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

/**
 * This class is for the anomaly prediction.
 */
@Service
public class SOMPredictor implements MonitoringAsService {
    @Autowired
    private SOM trainedSOM;
    private static final double INFINITE = Double.MAX_VALUE;
    @Autowired
    private static Gson gson = new Gson();
    @Autowired
    private static final Log logger = LogFactory.getLog(SOMPredictor.class);
    @Autowired
    SystemStatus systemStatus;
    @Autowired
    WeightVector testVector;
    @Autowired
    HeapDumper heapDumper;
//    @Autowired
//    private TestRepository dataCofigurationRepository;
    @Autowired
    DatabaseConnector databaseConnector;

    @Autowired
    TestRepository testRepository;

    private Map<String, Map<String, Double>> co_efficentCalculator;

    private int last = 0;
    private int secondLast = 0;
    private int current = 0;
    private int normal = 0;
    private int cpu = 0;
    private int mem = 0;

    @Override
    public String getTrainedBestMatchNeuron() throws IOException, ClassNotFoundException,
            MalformedObjectNameException, InstanceNotFoundException, ReflectionException, SigarException, InstantiationException, IllegalAccessException {

        trainedSOM = deSerialization("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/test.ser");

        co_efficentCalculator = metricsDeserilization("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/trainWithMetrics.ser");

        // TODO: 3/13/19 COMMENTED
        //resetLowMetricsValues(systemStatus);

        Metrics metricsAgent = new Metrics("17014");

        double[] metrics = new double[2];

        
        systemStatus.setCurrentTime(String.valueOf(java.time.LocalTime.now()));
        systemStatus.setSystemLevelMetrics(metricsAgent.getSystemLevelMetrics());
      
        metrics[0] = (Math.round((Double.parseDouble(
                String.valueOf(systemStatus.getSystemLevelMetrics().getCpuUsage())))));
        metrics[1] = Math.round(systemStatus.getSystemLevelMetrics().getUsedMemoryPercentage());

        HealthDeterminer healthDeterminer = new HealthDeterminer();
        //List<Double>cpuMetrics = new ArrayList<>();
        //cpuMetrics.add(Metrics[0]);
        healthDeterminer.setCpuUsage(metrics[0]);
        healthDeterminer.setMemoryUsage(metrics[1]);

        //metricsAgent.getDiskIO();


        testVector = new WeightVector(metrics);

        secondLast = last;
        last = current;

        // get prediction for input vector
        current = trainedSOM.testInput(testVector);
        logger.info("current status" + current);
        healthDeterminer.setSystemCurrentStatus(current);

        systemStatus.setHealthDeterminer(healthDeterminer);
        collectAllMetrics(systemStatus,metricsAgent);
        // TODO: 3/20/19 UNCOMMENT IT
       // collectHighLevelMetrics(metricsAgent);

        // TODO: 3/18/19 Commented for every metrics 
        if(current == 1) {
            // TODO: 3/20/19 UNCOMMENT IT
           // cpuMetricsSelection(metricsAgent,metrics);
        }  else if (current == 2){
            // TODO: 3/20/19 UNCOMMENT IT
           // memoryMetricsSelection(metricsAgent,metrics);
            //systemStatus.setSystemStatus(Constants.MEMORY_ANOMALY_TEXT);
        } else {
            normal++;
            // TODO: 3/20/19 UNCOMMENT IT
           // systemStatus.setSystemStatus(Constants.NORMAL_STATE_TEXT);
        }



        // TODO: 3/18/19 commented for 3 times 
//        if (current == 1 && last == 1 && secondLast == 1) {
//            logger.info("Tring Tring Anomaly detected");
//            systemStatus.setSystemStatus(Constants.CPU_ANOMALY_TEXT);
//            Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
//            int prediction = trainedSOM.causeInference(trainedNeuron);
//
//            // if cause inference gives CPU as metric causing anomaly
//            if (prediction == 1) {
//                cpuMetricsSelection(metricsAgent,metrics);
//
//                // trainWithMetrics.returnCoEfficient();
//            } else if (prediction == 2) {
//
//                memoryMetricsSelection(metricsAgent, metrics);
//
//            }
//
//        } else if (current == 2 && last == 2 && secondLast == 2) {
//            memoryMetricsSelection(metricsAgent, metrics);
//        } else {
//            normal++;
//            systemStatus.setSystemStatus(Constants.NORMAL_STATE_TEXT);
//            //Assigning mem and cpu anomaly count to zero, if we identify normal behaviour again after anomaly.
//            // so when we identify again the anomaly the count will start from 0.
////            mem = 0;
////            cpu = 0;
//        }


        // TODO: 3/12/19 DON'T DELETE - FOR TESTING
        //     collectAllMetrics(systemStatus,metricsAgent);

        try (FileWriter writer = new FileWriter("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/data.csv", true)) {

            //System.out.println("trainWithMetrics" + som.neurons.mapLength);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(metrics[0]);
            stringBuilder.append(",");
            stringBuilder.append(healthDeterminer.getMemoryUsage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getCpuIdle());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getCpuNice());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getCpuUser());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getCpuWait());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getCommittedVirtualMemory());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getFreePhycialMemory());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRamUsage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getLoadAverage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTotalSwapSize());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getFreeSwapSize());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getUsedSwapPercentage());
            stringBuilder.append(",");
            //for (List<DiskLevelMetrics> diskLevel: systemStatus.getDiskLevelMetrics()) {
             //   for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
                    stringBuilder.append(systemStatus.getNoOfReads());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getDiskReadBytes());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getNoOfWrites());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getDiskWriteBytes());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getTotalSpace());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getUsedSpace());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getFreeSpace());
                    stringBuilder.append(",");
                    stringBuilder.append(systemStatus.getFileCount());
                    stringBuilder.append(",");
//                    stringBuilder.append(systemStatus.getUsedPercentage());
//                    stringBuilder.append(",");
              //  }

           // }

            stringBuilder.append(systemStatus.getTotalThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getDaemonThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getPeakThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRunningThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxbytes());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxdropped());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxerrors());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxFrame());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxOverRuns());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxpackets());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSpeed());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTxbytes());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTxcarrier());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTxcollisions());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTxdropped());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTxerrors());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getRxOverRuns());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTxpackets());

            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getHeapInitUsage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getHeapCommitted());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getHeapUsed());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getHeapMax());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getNonHeapInitUsage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getNonHeapCommitted());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getNonHeapUsed());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getNonHeapMax());

            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getCurrentlyLoadedClass());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getTotalLoadedClass());


            // stringBuilder.append(som.neurons[i][j].getState());
            stringBuilder.append("\n");
            writer.write(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


        //Heap dump will be performed when we identified more than five consecutive memory anomalies.
        if (mem > 20) {
            heapDumper.dumpHeap("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/heap-dump-file-"
                    + getCurrentDateAndTime() + ".hprof", true);
            mem=0;
        }


//        dataCofigurationRepository.findAll();

//        DatabaseConnector databaseConnector = new DatabaseConnector();
        // TODO: 3/14/19 UN COMMENT IT TO INSERT DATA INTO THE DATABASE
//       databaseConnector.insertDb(String.valueOf(metrics[0]),String.valueOf(metrics[1]),String.valueOf(systemStatus.getCpuIdle()),String.valueOf(systemStatus.getCpuNice()),String.valueOf(systemStatus.getCpuUser()),String.valueOf(systemStatus.getCpuWait()),String.valueOf(systemStatus.getCommittedVirtualMemory()),String.valueOf(systemStatus.getFreePhycialMemory()), String.valueOf(systemStatus.getRamUsage()), String.valueOf(systemStatus.getLoadAverage()), String.valueOf(systemStatus.getTotalSwapSize()),String.valueOf(systemStatus.getFreeSwapSize()),String.valueOf(systemStatus.getUsedSwapPercentage()),String.valueOf(systemStatus.getNoOfReads()),String.valueOf(systemStatus.getDiskReadBytes()), String.valueOf(systemStatus.getNoOfWrites()),String.valueOf(systemStatus.getDiskWriteBytes()),String.valueOf(systemStatus.getTotalSpace()),String.valueOf(systemStatus.getUsedSpace()),String.valueOf(systemStatus.getFreeSpace()),String.valueOf(systemStatus.getFileCount()),String.valueOf(systemStatus.getTotalThreadCount()),String.valueOf(systemStatus.getDaemonThreadCount()),String.valueOf(systemStatus.getPeakThreadCount()),String.valueOf(systemStatus.getRunningThreadCount()),systemStatus.getRxbytes(),systemStatus.getRxdropped(),systemStatus.getRxerrors(),systemStatus.getRxFrame(),systemStatus.getRxOverRuns(),systemStatus.getRxpackets(),systemStatus.getSpeed(),systemStatus.getTxbytes(),systemStatus.getTxcarrier(),systemStatus.getTxcollisions(),systemStatus.getTxdropped(),systemStatus.getTxerrors(),systemStatus.getTxoverruns(),systemStatus.getTxpackets(),systemStatus.getDiskName(),systemStatus.getAddress(),systemStatus.getName());

        // TODO: 3/16/19 For testing
//        testRepository.findAllByCpuAndMemory();
        // TODO: 3/18/19 PLEASE COMMENT THIS LINE
        //collectAllMetrics(systemStatus,metricsAgent);
        //findAllCpuInfo();



        logger.info(gson.toJson(systemStatus));
        return gson.toJson(systemStatus);

    }

    public List<MetricInfoDao> findAllCpuInfo() {
        List<MetricInfoDao> returnList = new ArrayList<>();
        List<monitoring.core.Entities.DBConfiguration.Metrics> allCpuInfo = testRepository.findAll();
        for (monitoring.core.Entities.DBConfiguration.Metrics cpu : allCpuInfo)
        {
            returnList.add(transform(cpu));

        }

        for (MetricInfoDao test: returnList) {
            logger.info("cpu"+test.getCpu());
            logger.info("memory"+test.getMemory());
        }
        return returnList;
    }

    private MetricInfoDao transform(monitoring.core.Entities.DBConfiguration.Metrics metircsInfoEntity)
    {
        MetricInfoDao metricInfoDao = new MetricInfoDao();
        metricInfoDao.setCpu(metircsInfoEntity.getCpu());
        metricInfoDao.setMemory(metircsInfoEntity.getMemory());
        return metricInfoDao;
    }


    private void memoryMetricsSelection(Metrics metricsAgent, double[] metrics) throws MalformedObjectNameException, ReflectionException, SigarException, InstanceNotFoundException {
        mem++;
        systemStatus.setSystemStatus(Constants.MEMORY_ANOMALY_TEXT);
        logger.info("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
        for (Map.Entry<String, Map<String, Double>> err : co_efficentCalculator.entrySet()) {
            for (Map.Entry<String, Double> rer : err.getValue().entrySet()) {
                if(err.getKey().equalsIgnoreCase(monitoring.core.MetricsAdjustment.utils.Constants.MEMORY_USAGE) ) {
                    // TODO: 3/13/19 comment it 
                    changeMetrics(rer.getKey(),systemStatus,metricsAgent);

                    System.out.println(err.getKey() + " " + rer.getKey() + " -> " + rer.getValue());
                }
            }
        }
    }

    private void cpuMetricsSelection(Metrics metricsAgent, double[] metrics) throws MalformedObjectNameException, ReflectionException, SigarException, InstanceNotFoundException {
        cpu++;
        systemStatus.setSystemStatus(Constants.CPU_ANOMALY_TEXT);
        logger.info("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);

        for (Map.Entry<String, Map<String, Double>> err : co_efficentCalculator.entrySet()) {
            for (Map.Entry<String, Double> rer : err.getValue().entrySet()) {
                if(err.getKey().equalsIgnoreCase(monitoring.core.MetricsAdjustment.utils.Constants.CPU_USAGE) ) {
                    // TODO: 3/13/19 Comment it 
                    changeMetrics(rer.getKey(),systemStatus,metricsAgent);


                    System.out.println(err.getKey() + " " + rer.getKey() + " -> " + rer.getValue());
                }
            }
        }
    }


    @Override
    public String getSystemDetails() {
        Metrics metrics = new Metrics("8000");
        return gson.toJson(metrics.getSystemStats());
    }

    private static SOM deSerialization(String file) throws IOException, ClassNotFoundException {
        SOM trainedSOM = null;
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        trainedSOM = (SOM) objectInputStream.readObject();
        objectInputStream.close();
        return trainedSOM;
    }

    private static Map<String, Map<String, Double>> metricsDeserilization(String file) throws IOException, ClassNotFoundException {
        Map<String, Map<String, Double>> co_efficentCalculator = null;
        FileInputStream fileInputStream = new FileInputStream(file);
        // BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        co_efficentCalculator = (Map<String, Map<String, Double>>) objectInputStream.readObject();
        objectInputStream.close();
        return co_efficentCalculator;
    }

    private String getCurrentDateAndTime() {
        Date d1 = new Date();
        return String.valueOf(d1);
    }

    private Map<String, SystemStatus> collectHighLevelMetrics(Metrics metricsAgent) throws SigarException {
        Map<String, SystemStatus> system=new HashMap<>();
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
                system.put(systemStatus.getDiskName(),systemStatus);
            }

        }

        String networkAddress = metricsAgent.getIfstats().getAddress();
        String name = metricsAgent.getIfstats().getName();
        String rxBytes = metricsAgent.getIfstats().getRxbytes();
        systemStatus.setAddress(networkAddress);
        systemStatus.setName(name);
        systemStatus.setRxbytes(rxBytes);
        String txBytes = metricsAgent.getIfstats().getTxbytes();
        systemStatus.setTxbytes(txBytes);

        // TODO: 3/20/19 FOR TESTING
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


        return system;

    }

    private void changeMetrics(String metrics, SystemStatus systemStatus,Metrics metricsAgent) throws MalformedObjectNameException, ReflectionException, SigarException, InstanceNotFoundException {
        switch (metrics) {
            case Constants.CPU_IDLE:
                double cpuIdle = metricsAgent.getCPUIdle();
                systemStatus.setCpuIdle(cpuIdle);
                // systemStatus.getSystemLevelMetrics().setCpuIdle(metricsAgent.getSystemLevelMetrics().getCpuIdle());
                break;
            case Constants.CPU_NICE:
                double cpuNice = metricsAgent.getCPUNice();
                systemStatus.setCpuNice(cpuNice);
                // systemStatus.getSystemLevelMetrics().setCpuNice(metricsAgent.getSystemLevelMetrics().getCpuNice());
                break;
            case Constants.CPU_USER:
                double cpuUser = metricsAgent.getCPUUser();
                systemStatus.setCpuUser(cpuUser);
                // systemStatus.getSystemLevelMetrics().setCpuUser(metricsAgent.getSystemLevelMetrics().getCpuUser());
                break;
            case Constants.CPU_WAIT:
                double cpuWait = metricsAgent.getCPUWait();
                systemStatus.setCpuWait(cpuWait);
                // systemStatus.getSystemLevelMetrics().setCpuWait(metricsAgent.getSystemLevelMetrics().getCpuWait());
                break;
            case Constants.COMMITTED_VM:
                double committedVM = metricsAgent.getCommittedVirtualMemorySize();
                systemStatus.setCommittedVirtualMemory(committedVM);
                //systemStatus.getSystemLevelMetrics().setCommittedVirtualMemory(metricsAgent.getSystemLevelMetrics().getCommittedVirtualMemory());
                break;
            case Constants.FREE_PHYSICAL_MEMORY:
                double freePhysicalMemory = metricsAgent.getFreePhysicalMemory();
                double totalPhysicalMemory = metricsAgent.getTotalPhysicalMemory();
                systemStatus.setFreePhycialMemory(freePhysicalMemory);
                systemStatus.setTotalPhysicalMemory(totalPhysicalMemory);
                //systemStatus.getSystemLevelMetrics().setFreePhycialMemory(metricsAgent.getSystemLevelMetrics().getFreePhycialMemory());
                break;
            case Constants.RAM_USAGE:
                double ramUsage = metricsAgent.getRAMUsage();
                systemStatus.setRamUsage(ramUsage);
                // systemStatus.getSystemLevelMetrics().setRamUsage(metricsAgent.getSystemLevelMetrics().getRamUsage());
                break;
            case Constants.FREE_SWAP_USAGE:
                double freeSwapUsage = metricsAgent.getFreeSwapSize();
                systemStatus.setFreeSwapSize(freeSwapUsage);
                double totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
                systemStatus.setTotalSwapSize(totalSwapSize);
                //  systemStatus.getSystemLevelMetrics().setFreeSwapSize(metricsAgent.getSystemLevelMetrics().getFreeSwapSize());
                break;
            case Constants.LOAD_AVERAGE:
                double loadAverage=metricsAgent.getSystemLoadAverage();
                systemStatus.setLoadAverage(loadAverage);
                //systemStatus.getSystemLevelMetrics().setLoadAverage(metricsAgent.getSystemLevelMetrics().getLoadAverage());
                break;
            case Constants.TOTAL_SWAP_SIZE:
                totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
                systemStatus.setTotalSwapSize(totalSwapSize);
                freeSwapUsage = metricsAgent.getFreeSwapSize();
                systemStatus.setFreeSwapSize(freeSwapUsage);
                //systemStatus.getSystemLevelMetrics().setTotalSwapSize(metricsAgent.getSystemLevelMetrics().getTotalSwapSize());
                break;
            case Constants.USED_SWAP_SIZE:
                double usedSwapSize = metricsAgent.getUsedSwapPercentage();
                systemStatus.setUsedSwapPercentage(usedSwapSize);
                // systemStatus.getSystemLevelMetrics().setUsedSwapPercentage(metricsAgent.getSystemLevelMetrics().getUsedSwapPercentage());
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
//            case Constants.TOTAL_DISK_SPACE:
//            case Constants.USED_DISK_SPACE:
//            case Constants.FREE_DISK_SPACE:
//                for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
//                    for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
//                        String diskName = diskLevelMetrics.getDiskName();
//                        double totalDiskSpace = diskLevelMetrics.getTotalSpace();
//                        double usedDiskSpace = diskLevelMetrics.getUsedSpace();
//                        double freeDiskSpace = diskLevelMetrics.getFreeSpace();
//                        systemStatus.setDiskName(diskName);
//                        systemStatus.setTotalSpace(totalDiskSpace);
//                        systemStatus.setUsedSpace(usedDiskSpace);
//                        systemStatus.setFreeSpace(freeDiskSpace);
//                    }
//                }
//                break;
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
                //systemStatus.getThreadLevelMetrics().setTotalThreadCount(threadCount);
                break;
            case Constants.DAEMON_THREAD_COUNT:
                int daemonThreadCount = metricsAgent.getDaemonThreadCount();
                systemStatus.setDaemonThreadCount(daemonThreadCount);
                //systemStatus.getThreadLevelMetrics().setDaemonThreadCount(metricsAgent.getThreadLevelMetrics().getDaemonThreadCount());
                break;
            case Constants.PEAK_THREAD_COUNT:
                int peakThreadCount = metricsAgent.getPeakThreadCount();
                systemStatus.setPeakThreadCount(peakThreadCount);
                //systemStatus.getThreadLevelMetrics().setPeakThreadCount(metricsAgent.getThreadLevelMetrics().getPeakThreadCount());
                break;
            case Constants.RUNNING_THREAD_COUNT:
                int runningThreadCount = metricsAgent.getRunningThreadCount();
                systemStatus.setRunningThreadCount(runningThreadCount);
                // systemStatus.getThreadLevelMetrics().setRunningThreadCount(metricsAgent.getThreadLevelMetrics().getRunningThreadCount());
                break;
            case Constants.RX_BYTES:
            case Constants.RX_DROPPED:
            case Constants.RX_ERROR:
            case Constants.RX_FRAMES:
            case Constants.RX_OVERRUNS:
            case Constants.RX_PACKETS:
                String networkAddress = metricsAgent.getIfstats().getAddress();
                String name = metricsAgent.getIfstats().getName();
                //String rxBytes = metricsAgent.getIfstats().getRxbytes();
                String rxDropped = metricsAgent.getIfstats().getRxdropped();
                String rxError = metricsAgent.getIfstats().getRxerrors();
                String rxFrame = metricsAgent.getIfstats().getRxFrame();
                String rxOverrun = metricsAgent.getIfstats().getRxOverRuns();
                String rxPackets = metricsAgent.getIfstats().getRxpackets();
                systemStatus.setAddress(networkAddress);
                systemStatus.setName(name);
               // systemStatus.setRxbytes(rxBytes);
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
                //String txBytes = metricsAgent.getIfstats().getTxbytes();
                String txCarrier = metricsAgent.getIfstats().getTxcarrier();
                String txCollision = metricsAgent.getIfstats().getTxcollisions();
                String txDropped = metricsAgent.getIfstats().getTxdropped();
                String txErrors = metricsAgent.getIfstats().getTxerrors();
                String txOverruns = metricsAgent.getIfstats().getTxoverruns();
                String txPackets = metricsAgent.getIfstats().getTxpackets();
                systemStatus.setAddress(networkAddress);
                systemStatus.setName(name);
                //systemStatus.setTxbytes(txBytes);
                systemStatus.setTxcarrier(txCarrier);
                systemStatus.setTxcollisions(txCollision);
                systemStatus.setTxdropped(txDropped);
                systemStatus.setTxerrors(txErrors);
                systemStatus.setTxoverruns(txOverruns);
                systemStatus.setTxpackets(txPackets);
                break;

        }

    }

    private void resetLowMetricsValues(SystemStatus systemStatus) {
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
    }

    public void collectAllMetrics(SystemStatus systemStatus,Metrics metricsAgent) throws SigarException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException {
        double cpuIdle = metricsAgent.getCPUIdle();
        systemStatus.setCpuIdle(cpuIdle);

        double cpuNice = metricsAgent.getCPUNice();
        systemStatus.setCpuNice(cpuNice);

        double cpuUser = metricsAgent.getCPUUser();
        systemStatus.setCpuUser(cpuUser);
        // systemStatus.getSystemLevelMetrics().setCpuUser(metricsAgent.getSystemLevelMetrics().getCpuUser());

        double cpuWait = metricsAgent.getCPUWait();
        systemStatus.setCpuWait(cpuWait);
        // systemStatus.getSystemLevelMetrics().setCpuWait(metricsAgent.getSystemLevelMetrics().getCpuWait());

        double committedVM = metricsAgent.getCommittedVirtualMemorySize();
        systemStatus.setCommittedVirtualMemory(committedVM);
        //systemStatus.getSystemLevelMetrics().setCommittedVirtualMemory(metricsAgent.getSystemLevelMetrics().getCommittedVirtualMemory());

        double freePhysicalMemory = metricsAgent.getFreePhysicalMemory();
        double totalPhysicalMemory = metricsAgent.getTotalPhysicalMemory();
        systemStatus.setFreePhycialMemory(freePhysicalMemory);
        systemStatus.setTotalPhysicalMemory(totalPhysicalMemory);

        double ramUsage = metricsAgent.getRAMUsage();
        systemStatus.setRamUsage(ramUsage);
        // systemStatus.getSystemLevelMetrics().setRamUsage(metricsAgent.getSystemLevelMetrics().getRamUsage());

        double freeSwapUsage = metricsAgent.getFreeSwapSize();
        systemStatus.setFreeSwapSize(freeSwapUsage);
        //  systemStatus.getSystemLevelMetrics().setFreeSwapSize(metricsAgent.getSystemLevelMetrics().getFreeSwapSize());

        double loadAverage=metricsAgent.getSystemLoadAverage();
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
}
