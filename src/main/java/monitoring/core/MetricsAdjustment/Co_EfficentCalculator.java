package monitoring.core.MetricsAdjustment;

import monitoring.core.MetricsAdjustment.utils.Constants;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;

import java.io.*;
import java.util.*;

import com.csvreader.CsvReader;
import org.springframework.stereotype.Component;

@Component
public class Co_EfficentCalculator implements Serializable {

    private static final long serialVersionUID = 2000L;

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
    private static Instance totalDiskSpaceInstance = new SparseInstance(countNoOfLines());
    private static Instance usedDiskSpaceInstance = new SparseInstance(countNoOfLines());
    private static Instance freeDiskSpaceInstance = new SparseInstance(countNoOfLines());
    private static Instance fileCountInstance = new SparseInstance(countNoOfLines());
    private static Instance totalThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance daemonThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance peakThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance runningThreadCountInstance = new SparseInstance(countNoOfLines());
    private static Instance rx_bytesInstance=new SparseInstance(countNoOfLines());
    private static Instance rx_droppedInstance = new SparseInstance(countNoOfLines());
    private static Instance rx_errorInstance =new SparseInstance(countNoOfLines());
    private static Instance rx_framesInstance = new SparseInstance(countNoOfLines());
    private static Instance rx_overrunsnstance = new SparseInstance(countNoOfLines());
    private static Instance rx_packetsInstance =new SparseInstance(countNoOfLines());
    private static Instance speenInstance =new SparseInstance(countNoOfLines());
    private static Instance tx_bytesInstance =new SparseInstance(countNoOfLines());
    private static Instance tx_carrierInstance =new SparseInstance(countNoOfLines());
    private static Instance tx_collisionsInstance =new SparseInstance(countNoOfLines());
    private static Instance tx_droppedInstance = new SparseInstance(countNoOfLines());
    private static Instance tx_errorInstance = new SparseInstance(countNoOfLines());
    private static Instance tx_overrunsInstance =new SparseInstance(countNoOfLines());
    private static Instance tx_packetsInstance = new SparseInstance(countNoOfLines());

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
            double rx_bytes = Double.parseDouble(metrics.get(Constants.RX_BYTES));
            double rx_dropped = Double.parseDouble(metrics.get(Constants.RX_DROPPED));
            double rx_error = Double.parseDouble(metrics.get(Constants.RX_ERROR));
            double rx_frames = Double.parseDouble(metrics.get(Constants.RX_FRAMES));
            double rx_overruns = Double.parseDouble(metrics.get(Constants.RX_OVERRUNS));
            double rx_packets = Double.parseDouble(metrics.get(Constants.RX_PACKETS));
            double speed = Double.parseDouble(metrics.get(Constants.SPEED));
            double tx_bytes = Double.parseDouble(metrics.get(Constants.TX_BYTES));
            double tx_carrier = Double.parseDouble(metrics.get(Constants.TX_CARRIER));
            double tx_collision = Double.parseDouble(metrics.get(Constants.TX_COLLISIONS));
            double tx_dropped = Double.parseDouble(metrics.get(Constants.TX_DROPPED));
            double tx_errors = Double.parseDouble(metrics.get(Constants.TX_ERRORS));
            double tx_overruns = Double.parseDouble(metrics.get(Constants.TX_OVERRUNS));
            double tx_packets = Double.parseDouble(metrics.get(Constants.TX_PACKETS));

            ParseValueToInstanceObj(lineNo, cpu, memory, committedVM, freePhysicalMem, freeSwap, loadAvg, totalSwap, usedSwap, noOfRead, readRequest, noOfWrite, writeRequest, totalDiskSpace, usedDiskSpace, freeDiskSpace, fileCount, totalThreadCount, cpuInstance, memInstance, committedVMInstnce, freePhysicalMemInstance, freeSwapMemInstance, loadAvgInstance, totalSwapInstance, usedSwapInstance, npOfReadsInstance, noOfReadRequestInstance, noOfWriteInstance, noOfWriteRequestInstance, totalDiskSpaceInstance, usedDiskSpaceInstance, freeDiskSpaceInstance, fileCountInstance, totalThreadCountInstance);
            ParseValueToInstanceObj(lineNo, daemonThreadCount, peakThreadCount, runningThreadCount, rx_bytes, rx_dropped, rx_error, rx_frames, rx_overruns, rx_packets, speed, tx_bytes, tx_carrier, tx_collision, tx_dropped, tx_errors, tx_overruns, tx_packets, daemonThreadCountInstance, peakThreadCountInstance, runningThreadCountInstance, rx_bytesInstance, rx_droppedInstance, rx_errorInstance, rx_framesInstance, rx_overrunsnstance, rx_packetsInstance, speenInstance, tx_bytesInstance, tx_carrierInstance, tx_collisionsInstance, tx_droppedInstance, tx_errorInstance, tx_overrunsInstance, tx_packetsInstance);

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
        totalInstances.put(Constants.RX_BYTES, rx_bytesInstance);
        totalInstances.put(Constants.RX_DROPPED,rx_droppedInstance);
        totalInstances.put(Constants.RX_ERROR, rx_errorInstance);
        totalInstances.put(Constants.RX_FRAMES, rx_framesInstance);
        totalInstances.put(Constants.RX_OVERRUNS, rx_overrunsnstance);
        totalInstances.put(Constants.RX_PACKETS, rx_packetsInstance);
        totalInstances.put(Constants.SPEED, speenInstance);
        totalInstances.put(Constants.TX_BYTES, tx_bytesInstance);
        totalInstances.put(Constants.TX_CARRIER,tx_carrierInstance);
        totalInstances.put(Constants.TX_COLLISIONS,tx_collisionsInstance);
        totalInstances.put(Constants.TX_DROPPED, tx_droppedInstance);
        totalInstances.put(Constants.TX_ERRORS, tx_errorInstance);
        totalInstances.put(Constants.TX_OVERRUNS, tx_overrunsInstance);
        totalInstances.put(Constants.TX_PACKETS, tx_packetsInstance);

    }

    private static void ParseValueToInstanceObj(int lineNo, double daemonThreadCount, double peakThreadCount, double runningThreadCount, double rx_bytes, double rx_dropped, double rx_error, double rx_frames, double rx_overruns, double rx_packets, double speed, double tx_bytes, double tx_carrier, double tx_collision, double tx_dropped, double tx_errors, double tx_overruns, double tx_packets, Instance daemonThreadCountInstance, Instance peakThreadCountInstance, Instance runningThreadCountInstance, Instance rx_bytesInstance, Instance rx_droppedInstance, Instance rx_errorInstance, Instance rx_framesInstance, Instance rx_overrunsnstance, Instance rx_packetsInstance, Instance speenInstance, Instance tx_bytesInstance, Instance tx_carrierInstance, Instance tx_collisionsInstance, Instance tx_droppedInstance, Instance tx_errorInstance, Instance tx_overrunsInstance, Instance tx_packetsInstance) {
        addToInstance(lineNo, daemonThreadCount, peakThreadCount, runningThreadCount, rx_bytes, rx_dropped, rx_error, rx_frames, rx_overruns, daemonThreadCountInstance, peakThreadCountInstance, runningThreadCountInstance, rx_bytesInstance, rx_droppedInstance, rx_errorInstance, rx_framesInstance, rx_overrunsnstance);
        addToInstance(lineNo, rx_packets, speed, tx_bytes, tx_carrier, tx_collision, tx_dropped, tx_errors, tx_overruns, rx_packetsInstance, speenInstance, tx_bytesInstance, tx_carrierInstance, tx_collisionsInstance, tx_droppedInstance, tx_errorInstance, tx_overrunsInstance);
        tx_packetsInstance.put(lineNo,tx_packets);
    }

    private static void addToInstance(int lineNo, double daemonThreadCount, double peakThreadCount, double runningThreadCount, double rx_bytes, double rx_dropped, double rx_error, double rx_frames, double rx_overruns, Instance daemonThreadCountInstance, Instance peakThreadCountInstance, Instance runningThreadCountInstance, Instance rx_bytesInstance, Instance rx_droppedInstance, Instance rx_errorInstance, Instance rx_framesInstance, Instance rx_overrunsnstance) {
        daemonThreadCountInstance.put(lineNo,daemonThreadCount);
        peakThreadCountInstance.put(lineNo,peakThreadCount);
        runningThreadCountInstance.put(lineNo,runningThreadCount);
        rx_bytesInstance.put(lineNo,rx_bytes);
        rx_droppedInstance.put(lineNo,rx_dropped);
        rx_errorInstance.put(lineNo,rx_error);
        rx_framesInstance.put(lineNo,rx_frames);
        rx_overrunsnstance.put(lineNo,rx_overruns);
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
                //todo:newly added
                //Co-eff - Only gets values greater than 0
                if(coEff>0) {
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



    private static  Map<String, List<String>> getBestMatchMetrics() throws IOException {
        Map<String, List<String>> peopleByForename = new HashMap<>();
        Map<String, Double> metricList = returnCoEfficient(cpuInstance, totalInstances);
        List<String> bestMetricsList = new ArrayList<>();
        for (Map.Entry<String, Double> best: metricList.entrySet()) {
            if(best.getValue()>0) {
               bestMetricsList.add(best.getKey());
            }
        }
        peopleByForename.put(Constants.MEMORY_USAGE,bestMetricsList);
        return peopleByForename;
    }

    public  Map<String, Map<String,Double>> trainWithMetrics() throws IOException {
        readCSV();
        Map<String, Map<String,Double>> peopleByForename = new HashMap<>();
        for (Map.Entry<String, Instance> best: totalInstances.entrySet()) {
            Map<String, Double> metricList = returnCoEfficient(best.getValue(), totalInstances);
            peopleByForename.put(best.getKey(),metricList);
        }

        String fileName = "/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/trainWithMetrics.ser";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            //method to serialize the object
            objectOutputStream.writeObject(peopleByForename);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("metrics serialization done");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //peopleByForename.put(me)
        System.out.println("Successfully Trained");

        return peopleByForename;
    }


    public void retrieveMap(Map<String, List<String>> map) {
                for (Map.Entry<String, List<String>> e : map.entrySet()) {
            for (String name:e.getValue()) {
                System.out.println(name);
            }

        }
    }



    public static void main(String args[]) throws IOException {
        //trainWithMetrics();
//        for (Map.Entry<String, Map<String, Double>> err:trainWithMetrics().entrySet()) {
//            for(Map.Entry<String,Double>rer:err.getValue().entrySet()) {
//
//                System.out.println(err.getKey()+" "+rer.getKey());
//            }
//        }
//        Map<String, List<String>> metricList = getBestMatchMetrics();
//        for (Map.Entry<String, List<String>> e : metricList.entrySet()) {
//            for (String name:e.getValue()) {
//                System.out.println(name);
//            }
//
//        }
       // getBestMatchMetrics();
    }


}
