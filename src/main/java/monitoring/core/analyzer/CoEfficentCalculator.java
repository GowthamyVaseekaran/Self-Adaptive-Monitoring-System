package monitoring.core.analyzer;

import com.csvreader.CsvReader;
import monitoring.core.analyzer.utils.Constants;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is for calculating the co efficient between metrics.
 */
public class CoEfficentCalculator implements Serializable {

    private static final long serialVersionUID = 2000L;
    private static final Log logger = LogFactory.getLog(CoEfficentCalculator.class);
    private static Instance cpuInstance = new SparseInstance(countNoOfLines());
    private static Instance memInstance = new SparseInstance(countNoOfLines());
    private static Instance cpuIdleInstance = new SparseInstance(countNoOfLines());
    private static Instance cpuNiceInstance = new SparseInstance(countNoOfLines());
    private static Instance cpuUserInstance = new SparseInstance(countNoOfLines());
    private static Instance cpuWaitInstance = new SparseInstance(countNoOfLines());
    private static Instance committedVMInstnce = new SparseInstance(countNoOfLines());
    private static Instance freePhysicalMemInstance = new SparseInstance(countNoOfLines());
    private static Instance ramUsageInstance = new SparseInstance(countNoOfLines());
    private static Instance freeSwapMemInstance = new SparseInstance(countNoOfLines());
    private static Instance loadAvgInstance = new SparseInstance(countNoOfLines());
    private static Instance totalSwapInstance = new SparseInstance(countNoOfLines());
    private static Instance usedSwapInstance = new SparseInstance(countNoOfLines());
    private static Instance npOfReadsInstance = new SparseInstance(countNoOfLines());
    private static Instance noOfReadRequestInstance = new SparseInstance(countNoOfLines());
    private static Instance noOfWriteInstance = new SparseInstance(countNoOfLines());
    private static Instance noOfWriteRequestInstance = new SparseInstance(countNoOfLines());
    private static Instance totalDiskSpaceInstance = new SparseInstance(countNoOfLines());
    private static Instance usedDiskSpaceInstance = new SparseInstance(countNoOfLines());
    private static Instance freeDiskSpaceInstance = new SparseInstance(countNoOfLines());
    private static Instance fileCountInstance = new SparseInstance(countNoOfLines());
    private static Instance totalThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance daemonThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance peakThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance runningThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance rxBytesInstance = new SparseInstance(countNoOfLines());
    private static Instance rxDroppedInstance = new SparseInstance(countNoOfLines());
    private static Instance rxErrorInstance = new SparseInstance(countNoOfLines());
    private static Instance rxFramesInstance = new SparseInstance(countNoOfLines());
    private static Instance rxOverrunsInstance = new SparseInstance(countNoOfLines());
    private static Instance rxPacketsInstance = new SparseInstance(countNoOfLines());
    private static Instance speedInstance = new SparseInstance(countNoOfLines());
    private static Instance txBytesInstance = new SparseInstance(countNoOfLines());
    private static Instance txCarrierInstance = new SparseInstance(countNoOfLines());
    private static Instance txCollisionsInstance = new SparseInstance(countNoOfLines());
    private static Instance txDroppedInstance = new SparseInstance(countNoOfLines());
    private static Instance txErrorInstance = new SparseInstance(countNoOfLines());
    private static Instance txOverrunsInstance = new SparseInstance(countNoOfLines());
    private static Instance txPacketsInstance = new SparseInstance(countNoOfLines());
    private static Instance heapInitUsageInstance = new SparseInstance(countNoOfLines());
    private static Instance heapCommittedInstance = new SparseInstance(countNoOfLines());
    private static Instance heapUsedinstance = new SparseInstance(countNoOfLines());
    private static Instance heapMaxInstance = new SparseInstance(countNoOfLines());
    private static Instance nonHeapInitUsageInstance = new SparseInstance(countNoOfLines());
    private static Instance nonHeapCommittedInstance = new SparseInstance(countNoOfLines());
    private static Instance nonHeapUsedInstance = new SparseInstance(countNoOfLines());
    private static Instance nonHeapMaxInstance = new SparseInstance(countNoOfLines());
    private static Instance currentlyLoadedClassInstance = new SparseInstance(countNoOfLines());
    private static Instance totalLoadedClassInstance = new SparseInstance(countNoOfLines());


    private static Map<String, Instance> totalInstances = new HashMap<>();

    private static void readCSV() throws IOException {
        CsvReader metrics = new CsvReader("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/data.csv");
        metrics.readHeaders();
        int lineNo = 0;
        while (metrics.readRecord()) {
            double cpu = Double.parseDouble(metrics.get(Constants.CPU_USAGE));
            double memory = Double.parseDouble(metrics.get(Constants.MEMORY_USAGE));
            double cpuIdle = Double.parseDouble(metrics.get(Constants.CPU_IDLE));
            double cpuNice = Double.parseDouble(metrics.get(Constants.CPU_NICE));
            double cpuUser = Double.parseDouble(metrics.get(Constants.CPU_USER));
            double cpuWait = Double.parseDouble(metrics.get(Constants.CPU_WAIT));
            double committedVM = Double.parseDouble(metrics.get(Constants.COMMITTED_VM));
            double freePhysicalMem = Double.parseDouble(metrics.get(Constants.FREE_PHYSICAL_MEMORY));
            double ramUsage = Double.parseDouble(metrics.get(Constants.RAM_USAGE));
            double freeSwap = Double.parseDouble(metrics.get(Constants.FREE_SWAP_USAGE));
            double loadAvg = Double.parseDouble(metrics.get(Constants.LOAD_AVERAGE));
            double totalSwap = Double.parseDouble(metrics.get(Constants.TOTAL_SWAP_SIZE));
            double usedSwap = Double.parseDouble(metrics.get(Constants.USED_SWAP_SIZE));
            double noOfRead = Double.parseDouble(metrics.get(Constants.NO_OF_READS));
            double readRequest = Double.parseDouble(metrics.get(Constants.READ_BYTES));
            double noOfWrite = Double.parseDouble(metrics.get(Constants.NO_OF_WRITES));
            double writeRequest = Double.parseDouble(metrics.get(Constants.WRITE_BYTES));
            double totalDiskSpace = Double.parseDouble(metrics.get(Constants.TOTAL_DISK_SPACE));
            double usedDiskSpace = Double.parseDouble(metrics.get(Constants.USED_DISK_SPACE));
            double freeDiskSpace = Double.parseDouble(metrics.get(Constants.FREE_DISK_SPACE));
            double fileCount = Double.parseDouble(metrics.get(Constants.FILE_COUNT));
            double totalThreadCount = Double.parseDouble(metrics.get(Constants.TOTAL_THREAD_COUNT));
            double daemonThreadCount = Double.parseDouble(metrics.get(Constants.DAEMON_THREAD_COUNT));
            double peakThreadCount = Double.parseDouble(metrics.get(Constants.PEAK_THREAD_COUNT));
            double runningThreadCount = Double.parseDouble(metrics.get(Constants.RUNNING_THREAD_COUNT));
            double rxBytes = Double.parseDouble(metrics.get(Constants.RX_BYTES));
            double rxDropped = Double.parseDouble(metrics.get(Constants.RX_DROPPED));
            double rxError = Double.parseDouble(metrics.get(Constants.RX_ERROR));
            double rxFrames = Double.parseDouble(metrics.get(Constants.RX_FRAMES));
            double rxOverruns = Double.parseDouble(metrics.get(Constants.RX_OVERRUNS));
            double rxPackets = Double.parseDouble(metrics.get(Constants.RX_PACKETS));
            double speed = Double.parseDouble(metrics.get(Constants.SPEED));
            double txBytes = Double.parseDouble(metrics.get(Constants.TX_BYTES));
            double txCarrier = Double.parseDouble(metrics.get(Constants.TX_CARRIER));
            double txCollision = Double.parseDouble(metrics.get(Constants.TX_COLLISIONS));
            double txDropped = Double.parseDouble(metrics.get(Constants.TX_DROPPED));
            double txErrors = Double.parseDouble(metrics.get(Constants.TX_ERRORS));
            double txOverruns = Double.parseDouble(metrics.get(Constants.TX_OVERRUNS));
            double txPackets = Double.parseDouble(metrics.get(Constants.TX_PACKETS));
            double heapInitUsage = Double.parseDouble(metrics.get(Constants.HEAP_INIT_USAGE));
            double heapCommitted = Double.parseDouble(metrics.get(Constants.HEAP_COMMITTED));
            double heapUsed = Double.parseDouble(metrics.get(Constants.HEAP_USED));
            double heapMax = Double.parseDouble(metrics.get(Constants.HEAP_MAX));
            double nonHeapInitUsage = Double.parseDouble(metrics.get(Constants.NON_HEAP_INIT));
            double nonHeapCommitted = Double.parseDouble(metrics.get(Constants.NON_HEAP_COMMITTED));
            double nonHeapUsed = Double.parseDouble(metrics.get(Constants.NON_HEAP_USED));
            double nonHeapMax = Double.parseDouble(metrics.get(Constants.NON_HEAP_MAX));
            double currentlyLoadedClass = Double.parseDouble(metrics.get(Constants.CURRENTLY_LOADED_CLASS));
            double totalLoadedClass = Double.parseDouble(metrics.get(Constants.TOTAL_LOADED_CLASS));

            cpuInstance.put(lineNo, cpu);
            memInstance.put(lineNo, memory);
            cpuIdleInstance.put(lineNo, cpuIdle);
            cpuNiceInstance.put(lineNo, cpuNice);
            cpuUserInstance.put(lineNo, cpuUser);
            cpuWaitInstance.put(lineNo, cpuWait);
            committedVMInstnce.put(lineNo, committedVM);
            freePhysicalMemInstance.put(lineNo, freePhysicalMem);
            ramUsageInstance.put(lineNo, ramUsage);
            freeSwapMemInstance.put(lineNo, freeSwap);
            loadAvgInstance.put(lineNo, loadAvg);
            totalSwapInstance.put(lineNo, totalSwap);
            usedSwapInstance.put(lineNo, usedSwap);
            npOfReadsInstance.put(lineNo, noOfRead);
            noOfWriteInstance.put(lineNo, noOfWrite);
            noOfWriteRequestInstance.put(lineNo, writeRequest);
            noOfReadRequestInstance.put(lineNo, readRequest);
            totalDiskSpaceInstance.put(lineNo, totalDiskSpace);
            usedDiskSpaceInstance.put(lineNo, usedDiskSpace);
            freeDiskSpaceInstance.put(lineNo, freeDiskSpace);
            fileCountInstance.put(lineNo, fileCount);
            totalThreadCountInstance.put(lineNo, totalThreadCount);
            daemonThreadCountInstance.put(lineNo, daemonThreadCount);
            peakThreadCountInstance.put(lineNo, peakThreadCount);
            runningThreadCountInstance.put(lineNo, runningThreadCount);
            rxBytesInstance.put(lineNo, rxBytes);
            rxDroppedInstance.put(lineNo, rxDropped);
            rxErrorInstance.put(lineNo, rxError);
            rxFramesInstance.put(lineNo, rxFrames);
            rxOverrunsInstance.put(lineNo, rxOverruns);
            rxPacketsInstance.put(lineNo, rxPackets);
            // txPacketsInstance.put(lineNo,txPackets);
            speedInstance.put(lineNo, speed);
            txBytesInstance.put(lineNo, txBytes);
            txCarrierInstance.put(lineNo, txCarrier);
            txCollisionsInstance.put(lineNo, txCollision);
            txDroppedInstance.put(lineNo, txDropped);
            txErrorInstance.put(lineNo, txErrors);
            txOverrunsInstance.put(lineNo, txOverruns);
            txPacketsInstance.put(lineNo, txPackets);
            heapInitUsageInstance.put(lineNo, heapInitUsage);
            heapCommittedInstance.put(lineNo, heapCommitted);
            heapUsedinstance.put(lineNo, heapUsed);
            heapMaxInstance.put(lineNo, heapMax);
            nonHeapInitUsageInstance.put(lineNo, nonHeapInitUsage);
            nonHeapCommittedInstance.put(lineNo, nonHeapCommitted);
            nonHeapUsedInstance.put(lineNo, nonHeapUsed);
            nonHeapMaxInstance.put(lineNo, nonHeapMax);
            currentlyLoadedClassInstance.put(lineNo, currentlyLoadedClass);
            totalLoadedClassInstance.put(lineNo, totalLoadedClass);


            lineNo++;
        }
        totalInstances.put(Constants.CPU_USAGE, cpuInstance);
        totalInstances.put(Constants.MEMORY_USAGE, memInstance);
        totalInstances.put(Constants.CPU_IDLE, cpuIdleInstance);
        totalInstances.put(Constants.CPU_NICE, cpuNiceInstance);
        totalInstances.put(Constants.CPU_USER, cpuUserInstance);
        totalInstances.put(Constants.CPU_WAIT, cpuWaitInstance);
        totalInstances.put(Constants.COMMITTED_VM, committedVMInstnce);
        totalInstances.put(Constants.FREE_PHYSICAL_MEMORY, freePhysicalMemInstance);
        totalInstances.put(Constants.RAM_USAGE, ramUsageInstance);
        totalInstances.put(Constants.FREE_SWAP_USAGE, freeSwapMemInstance);
        totalInstances.put(Constants.LOAD_AVERAGE, loadAvgInstance);
        totalInstances.put(Constants.TOTAL_SWAP_SIZE, totalSwapInstance);
        totalInstances.put(Constants.USED_SWAP_SIZE, usedSwapInstance);
        totalInstances.put(Constants.NO_OF_READS, npOfReadsInstance);
        totalInstances.put(Constants.READ_BYTES, noOfReadRequestInstance);
        totalInstances.put(Constants.NO_OF_WRITES, noOfWriteInstance);
        totalInstances.put(Constants.WRITE_BYTES, noOfWriteRequestInstance);
        totalInstances.put(Constants.TOTAL_DISK_SPACE, totalDiskSpaceInstance);
        totalInstances.put(Constants.USED_DISK_SPACE, usedDiskSpaceInstance);
        totalInstances.put(Constants.FREE_DISK_SPACE, freeDiskSpaceInstance);
        totalInstances.put(Constants.FILE_COUNT, fileCountInstance);
        totalInstances.put(Constants.TOTAL_THREAD_COUNT, totalThreadCountInstance);
        totalInstances.put(Constants.DAEMON_THREAD_COUNT, daemonThreadCountInstance);
        totalInstances.put(Constants.PEAK_THREAD_COUNT, peakThreadCountInstance);
        totalInstances.put(Constants.RUNNING_THREAD_COUNT, runningThreadCountInstance);
        totalInstances.put(Constants.RX_BYTES, rxBytesInstance);
        totalInstances.put(Constants.RX_DROPPED, rxDroppedInstance);
        totalInstances.put(Constants.RX_ERROR, rxErrorInstance);
        totalInstances.put(Constants.RX_FRAMES, rxFramesInstance);
        totalInstances.put(Constants.RX_OVERRUNS, rxOverrunsInstance);
        totalInstances.put(Constants.RX_PACKETS, rxPacketsInstance);
        totalInstances.put(Constants.SPEED, speedInstance);
        totalInstances.put(Constants.TX_BYTES, txBytesInstance);
        totalInstances.put(Constants.TX_CARRIER, txCarrierInstance);
        totalInstances.put(Constants.TX_COLLISIONS, txCollisionsInstance);
        totalInstances.put(Constants.TX_DROPPED, txDroppedInstance);
        totalInstances.put(Constants.TX_ERRORS, txErrorInstance);
        totalInstances.put(Constants.TX_OVERRUNS, txOverrunsInstance);
        totalInstances.put(Constants.TX_PACKETS, txPacketsInstance);
        totalInstances.put(Constants.HEAP_INIT_USAGE, heapInitUsageInstance);
        totalInstances.put(Constants.HEAP_COMMITTED, heapCommittedInstance);
        totalInstances.put(Constants.HEAP_USED, heapUsedinstance);
        totalInstances.put(Constants.HEAP_MAX, heapMaxInstance);
        totalInstances.put(Constants.NON_HEAP_INIT, nonHeapInitUsageInstance);
        totalInstances.put(Constants.NON_HEAP_COMMITTED, nonHeapCommittedInstance);
        totalInstances.put(Constants.NON_HEAP_USED, nonHeapUsedInstance);
        totalInstances.put(Constants.NON_HEAP_MAX, nonHeapMaxInstance);
        totalInstances.put(Constants.CURRENTLY_LOADED_CLASS, currentlyLoadedClassInstance);
        totalInstances.put(Constants.TOTAL_LOADED_CLASS, totalLoadedClassInstance);


    }


    public static Map<String, Double> returnCoEfficient(Instance instance, Map<String, Instance> totalInstances) throws IOException {
        return calculateCoEfficient(instance, totalInstances);
    }


    private static Map<String, Double> calculateCoEfficient(Instance instance, Map<String, Instance> totalInstances) throws IOException {
        //readCSV();
        Map<String, Double> list = new HashMap<>();

        PearsonCorrelationCoefficient pearsonCorrelationCoefficient = new PearsonCorrelationCoefficient();
        for (Map.Entry<String, Instance> e : totalInstances.entrySet()) {
            if (e.getValue() != instance) {
                double coEff = pearsonCorrelationCoefficient.measure(instance, e.getValue());
                if (!Double.isNaN(coEff)) {
                    list.put(e.getKey(), coEff);
                }

            }
        }
        Map<String, Double> sortedMap = sortByValue(list);
        return sortedMap;
    }

    private static int countNoOfLines() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/data.csv"));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        String input;
        int count = 0;
        while (true) {
            try {
                assert bufferedReader != null;
                if ((input = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            count++;
        }
        return count - 1;
    }


    private static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


    private static Map<String, List<String>> getBestMatchMetrics() throws IOException {
        Map<String, List<String>> peopleByForename = new HashMap<>();
        Map<String, Double> metricList = returnCoEfficient(cpuInstance, totalInstances);
        List<String> bestMetricsList = new ArrayList<>();
        for (Map.Entry<String, Double> best : metricList.entrySet()) {
            if (best.getValue() > 0) {
                bestMetricsList.add(best.getKey());
            }
        }
        peopleByForename.put(Constants.MEMORY_USAGE, bestMetricsList);
        return peopleByForename;
    }

    public static Map<String, Map<String, Double>> trainWithMetrics() throws IOException {
        readCSV();
        Map<String, Map<String, Double>> peopleByForename = new HashMap<>();
        for (Map.Entry<String, Instance> best : totalInstances.entrySet()) {
            Map<String, Double> metricList = returnCoEfficient(best.getValue(), totalInstances);
            peopleByForename.put(best.getKey(), metricList);
        }

        String fileName = "/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/metricsRelationShip.ser";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            //method to serialize the object
            objectOutputStream.writeObject(peopleByForename);
            objectOutputStream.close();
            fileOutputStream.close();
            logger.info("Metrics serialization done");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        //peopleByForename.put(me)
        logger.info("Successfully Trained");

        return peopleByForename;
    }


    public void retrieveMap(Map<String, List<String>> map) {
        for (Map.Entry<String, List<String>> e : map.entrySet()) {
            for (String name : e.getValue()) {
                logger.info(name);
            }

        }
    }


    public static void main(String args[]) throws IOException {
        //trainWithMetrics();
        for (Map.Entry<String, Map<String, Double>> err : trainWithMetrics().entrySet()) {
            for (Map.Entry<String, Double> rer : err.getValue().entrySet()) {

                logger.info(err.getKey() + " " + rer.getKey() + "->" + rer.getValue());
            }
        }

    }
}
