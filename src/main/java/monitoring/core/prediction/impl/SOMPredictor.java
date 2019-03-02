package monitoring.core.prediction.impl;

import agent.performance.metrics.impl.Metrics;
import monitoring.core.bean.MerticObject;
import monitoring.core.som.Neuron;
import monitoring.core.som.SOM;
import monitoring.core.som.WeightVector;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is for the anomaly prediction.
 */
public class SOMPredictor {
    private static final double INFINITE = Double.MAX_VALUE;


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException {

        getTrainedBestMatchNeuron();
        // getTrainedBestMatchNeuronFileSample();

    }

    public static void getTrainedBestMatchNeuron() throws IOException, ClassNotFoundException, InterruptedException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException {

        SOM trainedSOM = deSerialization("/home/thamy/Desktop/Self-Adaptive-Monitoring-System/HistoryData/test.ser");

        int normal = 0;
        int cpu = 0;
        int mem = 0;
        int last = 0;
        int secondLast = 0;
        int current = 0;

        List<MerticObject> trainedData = new ArrayList<>();
        Metrics metricsAgent = new Metrics("17014");

        while (true) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();


            MerticObject merticObject = new MerticObject();
            double[] metrics = new double[2];
            double cpuMetrics = metricsAgent.getSystemCPU();
            merticObject.setCpuUsage(cpuMetrics);
            System.out.println(cpuMetrics);

            // read cpu metric from file change to 6
            metrics[0] = (Math.round((Double.parseDouble(String.valueOf(cpuMetrics)))));
            double memoryMetrics = metricsAgent.getMemoryUsage();
            merticObject.setMemoryUsage(memoryMetrics);
            System.out.println(memoryMetrics);
            double totMem = (Double.parseDouble(String.valueOf(memoryMetrics)));

            metrics[1] = Math.round(totMem);
            WeightVector testVector = new WeightVector(metrics);
            secondLast = last;
            last = current;

            // get prediction for input vector
            current = trainedSOM.testInput(testVector);
            System.out.println("current status" + current);

            if (current == 1 && last == 1 && secondLast == 1) {
                System.out.println("Tring Tring Anomaly detected");
                Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
                int prediction = trainedSOM.causeInference(trainedNeuron);

                // if cause inference gives CPU as metric causing anomaly
                if (prediction == 1) {
                    cpu++;
                    System.out.println("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
                }

                // if cause inference gives Memory as metric causing anomaly
                else if (prediction == 2) {
                    mem++;
                    System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
                }

            } else if (current == 2 && last == 2 && secondLast == 2) {
                mem++;
                System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
            } else {
                normal++;
                //System.out.println("Normal " + normal);
            }
            //Write the elements to the file
            try (FileWriter writer = new FileWriter("/home/thamy/Desktop/Self-Adaptive-Monitoring-System/HistoryData/nor.csv", true)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(now);
                stringBuilder.append(",");
                stringBuilder.append(merticObject.getCpuUsage());
                stringBuilder.append(",");
                stringBuilder.append(merticObject.getMemoryUsage());
                stringBuilder.append("\n");
                writer.write(stringBuilder.toString());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }

            Thread.sleep(2000L);
        }


    }

    public static void getTrainedBestMatchNeuronFileSample() throws IOException, ClassNotFoundException, InterruptedException {

        SOM trainedSOM = deSerialization("/home/thamy/Desktop/Self-Adaptive-Monitoring-System/HistoryData/test.ser");

        int normal = 0;
        int cpu = 0;
        int mem = 0;
        int last = 0;
        int secondLast = 0;
        int current = 0;
        String line = null;

        Scanner cpuScanner = new Scanner(new File("/home/thamy/Desktop/Self-Adaptive-Monitoring-System/logs/Anomoluscpu.txt"), "UTF-8");
        Scanner memScanner = new Scanner(new File("/home/thamy/Desktop/Self-Adaptive-Monitoring-System/logs/Anomolusmemory.txt"), "UTF-8");

        while (true) {
            int j = 0;
            while (!cpuScanner.hasNextLine()) ;

            line = cpuScanner.nextLine();
            //j++;

            String[] strings = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            for (String s : strings) {
                if (s != null && s.length() > 0) {
                    data.add(s);
                }
            }
            double[] metrics = new double[2];

            // read cpu metric from file change to 6
            metrics[0] = (Math.round((Double.parseDouble(data.get(6)) * 100)));

            while (!memScanner.hasNextLine()) ;

            line = memScanner.nextLine();

            strings = line.split(",");
            data = new ArrayList<String>();
            for (String s : strings) {
                if (s != null && s.length() > 0)
                    data.add(s);
            }
            double totMem = (Double.parseDouble(data.get(1)) * 100);

            metrics[1] = Math.round(totMem);

            WeightVector testVector = new WeightVector(metrics);

            secondLast = last;
            last = current;

            // get prediction for input vector
            current = trainedSOM.testInput(testVector);
            System.out.println("current status" + current);
            System.out.println("last" + last);
            System.out.println("second last" + secondLast);

            if (current == 1 && last == 1 && secondLast == 1) {
                System.out.println("Tring Tring Anomaly detected");
                Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
                int prediction = trainedSOM.causeInference(trainedNeuron);

                // if cause inference gives CPU as metric causing anomaly
                if (prediction == 1) {
                    cpu++;
                    System.out.println("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
                }

                // if cause inference gives Memory as metric causing anomaly
                else if (prediction == 2) {
                    mem++;
                    System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);

                }

            } else if (current == 2 && last == 2 && secondLast == 2) {
                mem++;
                System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
            } else {
                normal++;

            }
            Thread.sleep(2000L);
        }

    }


    private static SOM deSerialization(String file) throws IOException, ClassNotFoundException {
        SOM si = null;


        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
        si = (SOM) objectInputStream.readObject();
        objectInputStream.close();
        // System.out.println("iconic"+si.neurons.mapLength);

        return si;
    }


}
