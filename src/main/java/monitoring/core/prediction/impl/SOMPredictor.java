package monitoring.core.prediction.impl;

import com.google.gson.Gson;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;

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

    private int last = 0;
    private int secondLast = 0;
    private int current = 0;
    private int normal = 0;
    private int cpu = 0;
    private int mem = 0;

    @Override
    public String getTrainedBestMatchNeuron() throws IOException, ClassNotFoundException,
            MalformedObjectNameException, InstanceNotFoundException, ReflectionException {

        trainedSOM = deSerialization("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/test.ser");

        Metrics metricsAgent = new Metrics("17014");

        double[] metrics = new double[2];

       // Co_EfficentCalculator test = new Co_EfficentCalculator();

        systemStatus.setDiskLevelMetrics(metricsAgent.getDiskLevelMetrics());
        systemStatus.setThreadLevelMetrics(metricsAgent.getThreadLevelMetrics());
        systemStatus.setSystemLevelMetrics(metricsAgent.getSystemLevelMetrics());

        metrics[0] = (Math.round((Double.parseDouble(
                String.valueOf(systemStatus.getSystemLevelMetrics().getCpuUsage())))));
        metrics[1] = Math.round(systemStatus.getSystemLevelMetrics().getUsedMemoryPercentage());

        HealthDeterminer healthDeterminer = new HealthDeterminer();
        healthDeterminer.setCpuUsage(metrics[0]);
        healthDeterminer.setMemoryUsage(metrics[1]);

        testVector = new WeightVector(metrics);

        secondLast = last;
        last = current;

        // get prediction for input vector
        current = trainedSOM.testInput(testVector);
        logger.info("current status" + current);
        healthDeterminer.setSystemCurrentStatus(current);

        systemStatus.setHealthDeterminer(healthDeterminer);

        if (current == 1 && last == 1 && secondLast == 1) {
            logger.info("Tring Tring Anomaly detected");
            systemStatus.setSystemStatus(Constants.CPU_ANOMALY_TEXT);
            Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
            int prediction = trainedSOM.causeInference(trainedNeuron);

            // if cause inference gives CPU as metric causing anomaly
            if (prediction == 1) {
                cpu++;
                systemStatus.setSystemStatus(Constants.CPU_ANOMALY_TEXT);
                logger.info("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
               // test.calculateCoEfficientCPU();
            } else if (prediction == 2) {
                // if cause inference gives Memory as metric causing anomaly
                mem++;
                systemStatus.setSystemStatus(Constants.MEMORY_ANOMALY_TEXT);
                logger.info("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);

            }

        } else if (current == 2 && last == 2 && secondLast == 2) {
            mem++;
            systemStatus.setSystemStatus(Constants.MEMORY_ANOMALY_TEXT);
            logger.info("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
        } else {
            normal++;
            systemStatus.setSystemStatus(Constants.NORMAL_STATE_TEXT);
            //Assigning mem and cpu anomaly count to zero, if we identify normal behaviour again after anomaly.
            // so when we identify again the anomaly the count will start from 0.
//            mem = 0;
//            cpu = 0;
        }

        try (FileWriter writer = new FileWriter("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/data.csv",true)) {

            //System.out.println("test" + som.neurons.mapLength);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(healthDeterminer.getCpuUsage());
            stringBuilder.append(",");
            stringBuilder.append(healthDeterminer.getMemoryUsage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getCommittedVirtualMemory());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getFreePhycialMemory());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getFreeSwapSize());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getLoadAverage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getTotalSwapSize());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getFreeSwapSize());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getSystemLevelMetrics().getUsedSwapPercentage());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getDiskLevelMetrics().getNoOfReads());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getDiskLevelMetrics().getNoOfReadRequest());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getDiskLevelMetrics().getNoOfWrites());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getDiskLevelMetrics().getNoOfWriteRequests());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getThreadLevelMetrics().getTotalThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getThreadLevelMetrics().getDaemonThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getThreadLevelMetrics().getPeakThreadCount());
            stringBuilder.append(",");
            stringBuilder.append(systemStatus.getThreadLevelMetrics().getRunningThreadCount());

            // stringBuilder.append(som.neurons[i][j].getState());
            stringBuilder.append("\n");
            writer.write(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


        //Heap dump will be performed when we identified more than five consecutive memory anomalies.
//        if (mem > 20) {
//            heapDumper.dumpHeap("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/heap-dump-file-"
//                    + getCurrentDateAndTime() + ".hprof", true);
//            mem=0;
//        }

        logger.info(gson.toJson(systemStatus));
        return gson.toJson(systemStatus);
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

    private String getCurrentDateAndTime() {
        Date d1 = new Date();
        return String.valueOf(d1);
    }
}
