package monitoring.core.MetricsAdjustment;

import monitoring.core.MetricsAdjustment.utils.Constants;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.csvreader.CsvReader;

public class Co_EfficentCalculator {
    private static Instance cpuInstance = new SparseInstance(countNoOfLines());
    private static Instance memInstance = new SparseInstance(countNoOfLines());
    private static Instance committedVMInstnce = new SparseInstance(countNoOfLines());
    private static Instance freePhysicalMemInstance = new SparseInstance(countNoOfLines());
    private static Instance freeSwapMemInstance = new SparseInstance(countNoOfLines());
    private static Instance loadAvgInstance = new SparseInstance(countNoOfLines());
    private static Instance totalSwapInstance = new SparseInstance(countNoOfLines());
    private static Instance usedSwapInstance = new SparseInstance(countNoOfLines());
    private static Instance npOfReadsInstance = new SparseInstance(countNoOfLines());
    private static Instance noOfReadRequestInstance = new SparseInstance(countNoOfLines());
    private static Instance noOfWriteInstance = new SparseInstance(countNoOfLines());
    private static Instance noOfWriteRequestInstance = new SparseInstance(countNoOfLines());
    private static Instance totalThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance daemonThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance peakThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance runningThreadCountInstance = new SparseInstance(countNoOfLines());

    private static Map<String, Instance> totalInstances = new HashMap<>();


    private static void readCSV() throws IOException {
        CsvReader metrics = new CsvReader("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/data.csv");
        metrics.readHeaders();
        int lineNo = 0;
        while (metrics.readRecord()) {
            double cpu = Double.parseDouble(metrics.get(Constants.CPU_USAGE));
            double memory = Double.parseDouble(metrics.get(Constants.MEMORY_USAGE));
            double committedVM = Double.parseDouble(metrics.get(Constants.COMMITTED_VM));
            double freePhysicalMem = Double.parseDouble(metrics.get(Constants.FREE_PHYSICAL_MEMORY));
            double freeSwap = Double.parseDouble(metrics.get(Constants.FREE_SWAP_USAGE));
            double loadAvg = Double.parseDouble(metrics.get(Constants.LOAD_AVERAGE));
            double totalSwap = Double.parseDouble(metrics.get(Constants.TOTAL_SWAP_SIZE));
            double usedSwap = Double.parseDouble(metrics.get(Constants.USED_SWAP_SIZE));
            double noOfRead = Double.parseDouble(metrics.get(Constants.NO_OF_READS));
            double readRequest = Double.parseDouble(metrics.get(Constants.NO_OF_READ_REQUESTS));
            double noOfWrite = Double.parseDouble(metrics.get(Constants.NO_OF_WRITES));
            double writeRequest = Double.parseDouble(metrics.get(Constants.NO_OF_WRITE_REQUESTS));
            double totalThreadCount = Double.parseDouble(metrics.get(Constants.TOTAL_THREAD_COUNT));
            double daemonThreadCount = Double.parseDouble(metrics.get(Constants.DAEMON_THREAD_COUNT));
            double peakThreadCount = Double.parseDouble(metrics.get(Constants.PEAK_THREAD_COUNT));
            double runningThreadCount = Double.parseDouble(metrics.get(Constants.RUNNING_THREAD_COUNT));
            ParseValueToInstanceObj(lineNo, cpu, memory, committedVM, freePhysicalMem, freeSwap, loadAvg, totalSwap, usedSwap, cpuInstance, memInstance, committedVMInstnce, freePhysicalMemInstance, freeSwapMemInstance, loadAvgInstance, totalSwapInstance, usedSwapInstance);
            ParseValueToInstanceObj(lineNo, noOfRead, readRequest, noOfWrite, writeRequest, totalThreadCount, daemonThreadCount, peakThreadCount, runningThreadCount, npOfReadsInstance, noOfReadRequestInstance, noOfWriteInstance, noOfWriteRequestInstance, totalThreadCountInstance, daemonThreadCountInstance, peakThreadCountInstance, runningThreadCountInstance);
            // System.out.println(cpu);
            lineNo++;
        }
        totalInstances.put(Constants.CPU_USAGE, cpuInstance);
        totalInstances.put(Constants.MEMORY_USAGE, memInstance);
        totalInstances.put(Constants.COMMITTED_VM, committedVMInstnce);
        totalInstances.put(Constants.FREE_PHYSICAL_MEMORY, freePhysicalMemInstance);
        totalInstances.put(Constants.FREE_SWAP_USAGE, freeSwapMemInstance);
        totalInstances.put(Constants.LOAD_AVERAGE, loadAvgInstance);
        totalInstances.put(Constants.TOTAL_SWAP_SIZE, totalSwapInstance);
        totalInstances.put(Constants.USED_SWAP_SIZE, usedSwapInstance);
        totalInstances.put(Constants.NO_OF_READS, npOfReadsInstance);
        totalInstances.put(Constants.NO_OF_READ_REQUESTS, noOfReadRequestInstance);
        totalInstances.put(Constants.NO_OF_WRITES, noOfWriteInstance);
        totalInstances.put(Constants.NO_OF_WRITE_REQUESTS, noOfWriteRequestInstance);
        totalInstances.put(Constants.TOTAL_THREAD_COUNT, totalThreadCountInstance);
        totalInstances.put(Constants.DAEMON_THREAD_COUNT, daemonThreadCountInstance);
        totalInstances.put(Constants.PEAK_THREAD_COUNT, peakThreadCountInstance);
        totalInstances.put(Constants.RUNNING_THREAD_COUNT, runningThreadCountInstance);

    }

    private static void ParseValueToInstanceObj(int lineNo, double cpu, double memory, double committedVM, double freePhysicalMem, double freeSwap, double loadAvg, double totalSwap, double usedSwap, Instance cpuInstance, Instance memInstance, Instance committedVMInstnce, Instance freePhysicalMemInstance, Instance freeSwapMemInstance, Instance loadAvgInstance, Instance totalSwapInstance, Instance usedSwapInstance) {
        cpuInstance.put(lineNo, cpu);
        memInstance.put(lineNo, memory);
        committedVMInstnce.put(lineNo, committedVM);
        freePhysicalMemInstance.put(lineNo, freePhysicalMem);
        freeSwapMemInstance.put(lineNo, freeSwap);
        loadAvgInstance.put(lineNo, loadAvg);
        totalSwapInstance.put(lineNo, totalSwap);
        usedSwapInstance.put(lineNo, usedSwap);
    }

    public static Map<String, Double> calculateCoEfficientCPU(Instance instance, Map<String, Instance> totalInstances) throws IOException {
        return calculateCoEfficient(instance, totalInstances, cpuInstance);
    }

    public static Map<String, Double> calculateCoEfficentMemory(Instance instance, Map<String, Instance> totalInstances) throws IOException {
        return calculateCoEfficient(instance, totalInstances, memInstance);
    }

    private static Map<String, Double> calculateCoEfficient(Instance instance, Map<String, Instance> totalInstances, Instance targetInstance) throws IOException {
        readCSV();
        Map<String, Double> list = new HashMap<>();

        PearsonCorrelationCoefficient pearsonCorrelationCoefficient = new PearsonCorrelationCoefficient();
        for (Map.Entry<String, Instance> e : totalInstances.entrySet()) {
            if (e.getValue() != targetInstance) {
                double coEff = pearsonCorrelationCoefficient.measure(instance, e.getValue());
                list.put(e.getKey(), coEff);
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
            e.printStackTrace();
        }
        String input;
        int count = 0;
        while(true)
        {
            try {
                assert bufferedReader != null;
                if ((input = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        return count-1;
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



    public static void main(String args[]) throws IOException {
        Map<String, Double> metricList = calculateCoEfficientCPU(cpuInstance, totalInstances);
        for (Map.Entry<String, Double> e : metricList.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue());
        }
    }


}
