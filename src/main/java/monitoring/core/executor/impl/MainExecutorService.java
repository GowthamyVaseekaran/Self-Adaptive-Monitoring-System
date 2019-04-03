package monitoring.core.executor.impl;

import com.google.gson.Gson;

import monitoring.core.DatabaseConnector;
import monitoring.core.email.EmailSender;
import monitoring.core.entities.dbConfiguration.MetricInfoDao;
import monitoring.core.entities.dbConfiguration.TestRepository;
import monitoring.core.executor.impl.utils.Constants;
import monitoring.core.bean.HealthDeterminer;
import monitoring.core.bean.SystemStatus;
import monitoring.core.monitor.HeapDumper;
import monitoring.core.monitor.Metrics;
import monitoring.core.planner.som.SOM;
import monitoring.core.planner.som.WeightVector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

/**
 * This class is for the anomaly prediction.
 */
@Service
public class MainExecutorService implements MonitoringAsService {
    @Autowired
    private SOM trainedSOM;
    private static final double INFINITE = Double.MAX_VALUE;
    @Autowired
    private static Gson gson = new Gson();
    @Autowired
    private static final Log logger = LogFactory.getLog(MainExecutorService.class);
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

    @Autowired
    MetricsChanger metricsChanger;

    @Autowired
    EmailSender emailSender;

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

        Yaml yaml = new Yaml();
        Reader yamlFile = new FileReader("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/src/main/resources/Configuration.yaml");

        Map<String , Object> yamlMaps = (Map<String, Object>) yaml.load(yamlFile);
        final List<Map<String, Object>> trainedObj = (List<Map<String, Object>>) yamlMaps.get("trainedObj");
        String trainedSOMFile = (String) trainedObj.get(0).get("SOMFile");
        String trainedMetricsFile = (String) trainedObj.get(1).get("MetricsFile");
        //System.out.println(trainedSOMFile);

        //System.out.println(yamlMaps.get("Browser"));



        trainedSOM = somDeSerialization(trainedSOMFile);

        co_efficentCalculator = metricsDeserilization(trainedMetricsFile);

        // TODO: 3/13/19 COMMENTED
        metricsChanger.resetLowMetricsValues(systemStatus);

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

        // TODO: 3/20/19 UNCOMMENT IT
        metricsChanger.collectHighLevelMetrics(metricsAgent, systemStatus);

        // TODO: 3/18/19 Commented for every monitor
        if (current == 1) {
            cpu++;
            systemStatus.setSystemStatus(Constants.CPU_ANOMALY_TEXT);
            logger.info("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
            // TODO: 3/20/19 UNCOMMENT IT

            metricsChanger.cpuMetricsSelection(metricsAgent, systemStatus, co_efficentCalculator);


            // cpuMetricsSelection(metricsAgent,monitor);

        } else if (current == 2) {
            mem++;
            systemStatus.setSystemStatus(Constants.MEMORY_ANOMALY_TEXT);
            logger.info("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
            // TODO: 3/20/19 UNCOMMENT IT
            metricsChanger.memoryMetricsSelection(metricsAgent, systemStatus, co_efficentCalculator);
            //systemStatus.setSystemStatus(Constants.MEMORY_ANOMALY_TEXT);
        } else {
            normal++;
            // TODO: 3/20/19 UNCOMMENT IT
            systemStatus.setSystemStatus(Constants.NORMAL_STATE_TEXT);
        }

        // TODO: 3/18/19 commented for 3 times
        if (current != 0 && last != 0 && secondLast != 0) {
            final List<Map<String, Object>> module_name = (List<Map<String, Object>>) yamlMaps.get("user");
            String toMailAdd = (String) module_name.get(0).get("email");
            //System.out.println();
            if (current == 1 && last == 1 && secondLast == 1) {
                ApplicationContext context =
                        new ClassPathXmlApplicationContext("Spring-Mail.xml");

                EmailSender mm = (EmailSender) context.getBean("emailSender");
                mm.sendMail("adaptivemonitoringsystem@gmail.com",
                        toMailAdd,
                        "[Alert] [important] Abnormal behaviour is detected in your system",
                        "Hi, \n\n Continuous Abnormal behaviors are identified in your system. The root cause can be excessive CPU utilization." +
                                "Detailed logs and graphs can be found in the dashboard. \n\n Happy Fixing. \n\n Thank you. \n\n Yours Sincerely, \n Monitoring Team.");
            } else if (current == 2 && last == 2 && secondLast == 2) {
                ApplicationContext context =
                        new ClassPathXmlApplicationContext("Spring-Mail.xml");

                EmailSender mm = (EmailSender) context.getBean("emailSender");
                mm.sendMail("adaptivemonitoringsystem@gmail.com",
                        toMailAdd,
                        "[Alert] [important] Abnormal behaviour is detected in your system",
                        "Hi, \n\n Continuous Abnormal behaviors are identified in your system. The root cause can be excessive Memory utilization." +
                                "Detailed logs and graphs can be found in the dashboard. \n\n Happy Fixing. \n\n Thank you. \n\n Yours Sincerely, \n Monitoring Team.");

            } else {
                ApplicationContext context =
                        new ClassPathXmlApplicationContext("Spring-Mail.xml");

                EmailSender mm = (EmailSender) context.getBean("emailSender");
                mm.sendMail("adaptivemonitoringsystem@gmail.com",
                        toMailAdd,
                        "[Alert] [important] Abnormal behaviour is detected in your system",
                        "Hi, \n\n Continuous Abnormal behaviors are identified in your system."
                                + "Detailed logs and graphs can be found in the dashboard. \n\n Happy Fixing. \n\n Thank you. \n\n Yours Sincerely, \n Monitoring Team.");
            }


        } else {

        }

        //Heap dump will be performed when we identified more than five consecutive memory anomalies.
        if (mem > 20) {
            heapDumper.dumpHeap("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/heap-dump-file-"
                    + getCurrentDateAndTime() + ".hprof", true);
            mem = 0;
        }


//        dataCofigurationRepository.findAll();

//        DatabaseConnector databaseConnector = new DatabaseConnector();
        // TODO: 3/14/19 UN COMMENT IT TO INSERT DATA INTO THE DATABASE
//       databaseConnector.insertDb(String.valueOf(monitor[0]),String.valueOf(monitor[1]),String.valueOf(systemStatus.getCpuIdle()),String.valueOf(systemStatus.getCpuNice()),String.valueOf(systemStatus.getCpuUser()),String.valueOf(systemStatus.getCpuWait()),String.valueOf(systemStatus.getCommittedVirtualMemory()),String.valueOf(systemStatus.getFreePhycialMemory()), String.valueOf(systemStatus.getRamUsage()), String.valueOf(systemStatus.getLoadAverage()), String.valueOf(systemStatus.getTotalSwapSize()),String.valueOf(systemStatus.getFreeSwapSize()),String.valueOf(systemStatus.getUsedSwapPercentage()),String.valueOf(systemStatus.getNoOfReads()),String.valueOf(systemStatus.getDiskReadBytes()), String.valueOf(systemStatus.getNoOfWrites()),String.valueOf(systemStatus.getDiskWriteBytes()),String.valueOf(systemStatus.getTotalSpace()),String.valueOf(systemStatus.getUsedSpace()),String.valueOf(systemStatus.getFreeSpace()),String.valueOf(systemStatus.getFileCount()),String.valueOf(systemStatus.getTotalThreadCount()),String.valueOf(systemStatus.getDaemonThreadCount()),String.valueOf(systemStatus.getPeakThreadCount()),String.valueOf(systemStatus.getRunningThreadCount()),systemStatus.getRxbytes(),systemStatus.getRxdropped(),systemStatus.getRxerrors(),systemStatus.getRxFrame(),systemStatus.getRxOverRuns(),systemStatus.getRxpackets(),systemStatus.getSpeed(),systemStatus.getTxbytes(),systemStatus.getTxcarrier(),systemStatus.getTxcollisions(),systemStatus.getTxdropped(),systemStatus.getTxerrors(),systemStatus.getTxoverruns(),systemStatus.getTxpackets(),systemStatus.getDiskName(),systemStatus.getAddress(),systemStatus.getName());

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
        List<monitoring.core.entities.dbConfiguration.Metrics> allCpuInfo = testRepository.findAll();
        for (monitoring.core.entities.dbConfiguration.Metrics cpu : allCpuInfo) {
            returnList.add(transform(cpu));

        }

        for (MetricInfoDao test : returnList) {
            logger.info("cpu" + test.getCpu());
            logger.info("memory" + test.getMemory());
        }
        return returnList;
    }

    private MetricInfoDao transform(monitoring.core.entities.dbConfiguration.Metrics metircsInfoEntity) {
        MetricInfoDao metricInfoDao = new MetricInfoDao();
        metricInfoDao.setCpu(metircsInfoEntity.getCpu());
        metricInfoDao.setMemory(metircsInfoEntity.getMemory());
        return metricInfoDao;
    }


    @Override
    public String getSystemDetails() {
        Metrics metrics = new Metrics("8000");
        return gson.toJson(metrics.getSystemStats());
    }

    private static SOM somDeSerialization(String file) throws IOException, ClassNotFoundException {
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

//    private Map<String, SystemStatus> collectHighLevelMetrics(Metrics metricsAgent) throws SigarException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException {
//        Map<String, SystemStatus> system=new HashMap<>();
//        for (List<DiskLevelMetrics> diskLevel: metricsAgent.getDiskIO()) {
//            for (DiskLevelMetrics diskLevelMetrics:diskLevel) {
//                String diskName = diskLevelMetrics.getDiskName();
//                double totalDiskSpace = diskLevelMetrics.getTotalSpace();
//                double usedDiskSpace = diskLevelMetrics.getUsedSpace();
//                double freeDiskSpace = diskLevelMetrics.getFreeSpace();
//                systemStatus.setDiskName(diskName);
//                systemStatus.setTotalSpace(totalDiskSpace);
//                systemStatus.setUsedSpace(usedDiskSpace);
//                systemStatus.setFreeSpace(freeDiskSpace);
//                system.put(systemStatus.getDiskName(),systemStatus);
//            }
//
//        }
//
//        double loadAverage=metricsAgent.getSystemLoadAverage();
//        systemStatus.setLoadAverage(loadAverage);
//
//        String networkAddress = metricsAgent.getIfstats().getAddress();
//        String name = metricsAgent.getIfstats().getName();
//        String rxBytes = metricsAgent.getIfstats().getRxbytes();
//        systemStatus.setAddress(networkAddress);
//        systemStatus.setName(name);
//        systemStatus.setRxbytes(rxBytes);
//        String txBytes = metricsAgent.getIfstats().getTxbytes();
//        systemStatus.setTxbytes(txBytes);
//
//        systemStatus.setCurrentlyLoadedClass(metricsAgent.currentlyLoadedClassCount());
//        systemStatus.setTotalLoadedClass(metricsAgent.totalLoadedClass());
//        double totalSwapSize = metricsAgent.getTotalSwapSpaceSize();
//        systemStatus.setTotalSwapSize(totalSwapSize);
//        double totalPhysicalMemory = metricsAgent.getTotalPhysicalMemory();
//        systemStatus.setTotalPhysicalMemory(totalPhysicalMemory);
//        double committedVM = metricsAgent.getCommittedVirtualMemorySize();
//        systemStatus.setCommittedVirtualMemory(committedVM);
//        double ramUsage = metricsAgent.getRAMUsage();
//        systemStatus.setRamUsage(ramUsage);
//
//
//        return system;
//
//    }

}
