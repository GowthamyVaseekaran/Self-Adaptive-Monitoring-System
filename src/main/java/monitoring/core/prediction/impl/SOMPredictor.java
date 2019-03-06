package monitoring.core.prediction.impl;

import com.google.gson.Gson;

import monitoring.core.bean.HealthDeterminer;
import monitoring.core.bean.SystemStatus;
import monitoring.core.metrics.HeapDumper;
import monitoring.core.metrics.Metrics;
import monitoring.core.som.Neuron;
import monitoring.core.som.SOM;
import monitoring.core.som.WeightVector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    private String heapFile = "/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/heap.hprof";


    @Override
    public String getTrainedBestMatchNeuron() throws IOException, ClassNotFoundException,
            MalformedObjectNameException, InstanceNotFoundException, ReflectionException {

        trainedSOM = deSerialization("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/test.ser");


        Metrics metricsAgent = new Metrics("17014");

        double[] metrics = new double[2];

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
            systemStatus.setSystemStatus("CPU Anomaly");
            Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
            int prediction = trainedSOM.causeInference(trainedNeuron);

            // if cause inference gives CPU as metric causing anomaly
            if (prediction == 1) {
                cpu++;
                systemStatus.setSystemStatus("CPU Anomaly");
                logger.info("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
            } else if (prediction == 2) {
                // if cause inference gives Memory as metric causing anomaly
                mem++;
                systemStatus.setSystemStatus("Memory Anomaly");
                logger.info("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
            }

        } else if (current == 2 && last == 2 && secondLast == 2) {
            mem++;
            systemStatus.setSystemStatus("Memory Anomaly");
            logger.info("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
        } else {
            normal++;
            systemStatus.setSystemStatus("Normal");
            //Assigning mem and cpu anomaly count to zero, if we identify normal behaviour again after anomaly.
            // so when we identify again the anomaly the count will start from 0.
            mem = 0;
            cpu = 0;
        }

        //Heap dump will be performed when we identified more than five consecutive memory anomalies.
        if (mem > 5) {
            heapDumper.dumpHeap("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/heap-dump-file-"
                    + getCurrentDateAndTime() + ".hprof", true);
        }

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
