package monitoring.core.MetricsAdjustment;

import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;

import java.io.IOException;
import java.util.*;

import com.csvreader.CsvReader;

public class Co_EfficentCalculator {
    private static Instance cpuInstance = new SparseInstance(8890);
    private static Instance memInstance = new SparseInstance(8890);
    private static Instance committedVMInstnce = new SparseInstance(8890);
    private static Instance freePhysicalMemInstance = new SparseInstance(8890);
    private static Instance freeSwapMemInstance = new SparseInstance(8890);
    private static Instance loadAvgInstance = new SparseInstance(8890);
    private static Instance totalSwapInstance = new SparseInstance(8890);
    private static Instance usedSwapInstance = new SparseInstance(8890);
    private static Instance npOfReadsInstance = new SparseInstance(8890);
    private static Instance noOfReadRequestInstance = new SparseInstance(8890);
    private static Instance noOfWriteInstance = new SparseInstance(8890);
    private static Instance noOfWriteRequestInstance = new SparseInstance(8890);
    private static Instance totalThreadCountInstance = new SparseInstance(8890);
    private static Instance daemonThreadCountInstance = new SparseInstance(8890);
    private static Instance peakThreadCountInstance = new SparseInstance(8890);
    private static Instance runningThreadCountInstance = new SparseInstance(8890);

    private static Map<String, Instance> totalInstances = new HashMap<>();


    private static void readCSV() throws IOException {
        CsvReader metrics = new CsvReader("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/data.csv");
        metrics.readHeaders();
        int lineNo = 0;
        while (metrics.readRecord()) {
            double cpu = Double.parseDouble(metrics.get("CPU"));
            double memory = Double.parseDouble(metrics.get("Memory"));
            double committedVM = Double.parseDouble(metrics.get("Committed VM"));
            double freePhysicalMem = Double.parseDouble(metrics.get("Free Physical Mem"));
            double freeSwap = Double.parseDouble(metrics.get("Free swap"));
            double loadAvg = Double.parseDouble(metrics.get("Load Avg"));
            double totalSwap = Double.parseDouble(metrics.get("Total Swap"));
            double usedSwap = Double.parseDouble(metrics.get("used swap"));
            double noOfRead = Double.parseDouble(metrics.get("No of Read"));
            double readRequest = Double.parseDouble(metrics.get("Read request"));
            double noOfWrite = Double.parseDouble(metrics.get("No Of Write"));
            double writeRequest = Double.parseDouble(metrics.get("Write request"));
            double totalThreadCount = Double.parseDouble(metrics.get("Total Thread Count"));
            double daemonThreadCount = Double.parseDouble(metrics.get("Daemon Thread Count"));
            double peakThreadCount = Double.parseDouble(metrics.get("Peak Thread Count"));
            double runningThreadCount = Double.parseDouble(metrics.get("Running Thread Count"));
            ParseValueToInstanceObj(lineNo, cpu, memory, committedVM, freePhysicalMem, freeSwap, loadAvg, totalSwap, usedSwap, cpuInstance, memInstance, committedVMInstnce, freePhysicalMemInstance, freeSwapMemInstance, loadAvgInstance, totalSwapInstance, usedSwapInstance);
            ParseValueToInstanceObj(lineNo, noOfRead, readRequest, noOfWrite, writeRequest, totalThreadCount, daemonThreadCount, peakThreadCount, runningThreadCount, npOfReadsInstance, noOfReadRequestInstance, noOfWriteInstance, noOfWriteRequestInstance, totalThreadCountInstance, daemonThreadCountInstance, peakThreadCountInstance, runningThreadCountInstance);
            // System.out.println(cpu);
            lineNo++;
        }
        totalInstances.put("cpu", cpuInstance);
        totalInstances.put("Memory", memInstance);
        totalInstances.put("committed VM", committedVMInstnce);
        totalInstances.put("Free physical memory", freePhysicalMemInstance);
        totalInstances.put("Free swap size", freeSwapMemInstance);
        totalInstances.put("Load Average", loadAvgInstance);
        totalInstances.put("Total swap size", totalSwapInstance);
        totalInstances.put("Used swap size", usedSwapInstance);
        totalInstances.put("No of reads", npOfReadsInstance);
        totalInstances.put("No of read requests", noOfReadRequestInstance);
        totalInstances.put("No of write", noOfWriteInstance);
        totalInstances.put("No of write requests", noOfWriteRequestInstance);
        totalInstances.put("total thread count", totalThreadCountInstance);
        totalInstances.put("Daemon thread count", daemonThreadCountInstance);
        totalInstances.put("Peak thread count", peakThreadCountInstance);
        totalInstances.put("Running thread count", runningThreadCountInstance);

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
        Map<String, Double> metricList = calculateCoEfficentMemory(memInstance, totalInstances);
        for (Map.Entry<String, Double> e : metricList.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue());
        }
    }


}
