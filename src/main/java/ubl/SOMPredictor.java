package ubl;


import agent.performance.metrics.impl.Metrics;
import org.json.simple.parser.ParseException;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

//import javax.ws.rs.core.Response;
//import lombok.Getter;

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


        Metrics metrics_agent = new Metrics("2971");
        while (true) {

            double[] metrics = new double[2];
            double cpu_metrics = metrics_agent.getSystemCPU();
            System.out.println(cpu_metrics);

            // read cpu metric from file change to 6
            metrics[0] = (Math.round((Double.parseDouble(String.valueOf(cpu_metrics)))));
            double memory_metrics = metrics_agent.getMemoryUsage();
            System.out.println(memory_metrics);
            //  j = 0;


            double totMem = (Double.parseDouble(String.valueOf(memory_metrics)));

            metrics[1] = Math.round(totMem);


            WeightVector testVector = new WeightVector(metrics);

            secondLast = last;
            last = current;

            // get prediction for input vector
            current = trainedSOM.testInput(testVector);
            System.out.println("current status" + current);

            // if 3 consecutive predictions are anomaly then find cause
//            if(current ==1 && last==1 && secondLast ==1) {
//                System.out.println("Tring Tring Anomaly detected");
//            }
//            if (current == 1 && last == 1 && secondLast == 1) {
//                System.out.println("Tring Tring Anomaly detected");
//                Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
//                int prediction = trainedSOM.causeInference(trainedNeuron);
//
//                // if cause inference gives CPU as metric causing anomaly
//                if (prediction == 1) {
//                    cpu++;
//                    System.out.println("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
//                    //  SignatureDriven signatureDriven = new SignatureDriven();
//                    // int scalingFactor = signatureDriven.signature("cpu", metrics[0]);
//                    //   ublController.prepare(scalingFactor, ublController.cpu_cap, "cpu");
//                    //    System.out.println("anomaly detected -cpu");
//                }
//
//                // if cause inference gives Memory as metric causing anomaly
//                else if (prediction == 2) {
//                    mem++;
//                    System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
////                    SignatureDriven signatureDriven = new SignatureDriven();
////                    int scalingFactor = signatureDriven.signature("mem", metrics[1]);
////                    ublController.prepare(scalingFactor, (int) totMem, "mem");
//                }
//
//            } else {
//                normal++;
//                //System.out.println("Normal " + normal);
//            }
            if (current == 1 && last == 1 && secondLast == 1) {
                System.out.println("Tring Tring Anomaly detected");
                Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
                int prediction = trainedSOM.causeInference(trainedNeuron);

                // if cause inference gives CPU as metric causing anomaly
                if (prediction == 1) {
                    cpu++;
                    System.out.println("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
                    //  SignatureDriven signatureDriven = new SignatureDriven();
                    // int scalingFactor = signatureDriven.signature("cpu", metrics[0]);
                    //   ublController.prepare(scalingFactor, ublController.cpu_cap, "cpu");
                    //    System.out.println("anomaly detected -cpu");
                }

                // if cause inference gives Memory as metric causing anomaly
                else if (prediction == 2) {
                    mem++;
                    System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
//                    SignatureDriven signatureDriven = new SignatureDriven();
//                    int scalingFactor = signatureDriven.signature("mem", metrics[1]);
//                    ublController.prepare(scalingFactor, (int) totMem, "mem");
                }

            } else if (current == 2 && last == 2 && secondLast == 2) {
                mem++;
                System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
            } else {
                normal++;
                //System.out.println("Normal " + normal);
            }
            Thread.sleep(2000l);
        }

    }

    public static void getTrainedBestMatchNeuronFileSample() throws IOException, ClassNotFoundException, InterruptedException {

        SOM trainedSOM = deSerialization("/home/thamy/Desktop/Self-Organizing-Map/HistoryData/test.ser");

        int normal = 0;
        int cpu = 0;
        int mem = 0;
        int last = 0;
        int secondLast = 0;
        int current = 0;
        String line = null;

        Scanner cpuScanner = new Scanner(new File("/home/thamy/Desktop/Self-Organizing-Map/logs/Anomoluscpu.txt"), "UTF-8");
        Scanner memScanner = new Scanner(new File("/home/thamy/Desktop/Self-Organizing-Map/logs/Anomolusmemory.txt"), "UTF-8");

        while (true) {
            int j = 0;
            while (!cpuScanner.hasNextLine()) ;

            line = cpuScanner.nextLine();
            //j++;

            String[] strings = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            for (String s : strings) {
                if (s != null && s.length() > 0)
                    data.add(s);
            }
            double[] metrics = new double[2];

            // read cpu metric from file change to 6
            metrics[0] = (Math.round((Double.parseDouble(data.get(6)) * 100)));
            //  j = 0;

            while (!memScanner.hasNextLine()) ;
            // while (j < 2) {
            line = memScanner.nextLine();
            // j++;
            //  }
            strings = line.split(",");
            data = new ArrayList<String>();
            for (String s : strings) {
                if (s != null && s.length() > 0)
                    data.add(s);
            }
            double totMem = (Double.parseDouble(data.get(1)) * 100);

            metrics[1] = Math.round(totMem);

            // read mem metric from file and normalize
            // metrics[1] = Math.round(((Double.parseDouble(data.get(1)) * 100.0) / totMem));
            // j = 0;
//            while (!memScanner.hasNextLine()) ;
//            while (j < 2) {
//                line = memScanner.nextLine();
//                j++;
//            }
            WeightVector testVector = new WeightVector(metrics);

            secondLast = last;
            last = current;

            // get prediction for input vector
            current = trainedSOM.testInput(testVector);
            System.out.println("current status" + current);
            System.out.println("last" + last);
            System.out.println("second last" + secondLast);
            // if 3 consecutive predictions are anomaly then find cause
//            if(current ==1 && last==1 && secondLast ==1) {
//
//            }
            if (current == 1 && last == 1 && secondLast == 1) {
                System.out.println("Tring Tring Anomaly detected");
                Neuron trainedNeuron = trainedSOM.getTrainedNeuron(testVector);
                int prediction = trainedSOM.causeInference(trainedNeuron);

                // if cause inference gives CPU as metric causing anomaly
                if (prediction == 1) {
                    cpu++;
                    System.out.println("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
                    //  SignatureDriven signatureDriven = new SignatureDriven();
                    // int scalingFactor = signatureDriven.signature("cpu", metrics[0]);
                    //   ublController.prepare(scalingFactor, ublController.cpu_cap, "cpu");
                    //    System.out.println("anomaly detected -cpu");
                }

                // if cause inference gives Memory as metric causing anomaly
                else if (prediction == 2) {
                    mem++;
                    System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
//                    SignatureDriven signatureDriven = new SignatureDriven();
//                    int scalingFactor = signatureDriven.signature("mem", metrics[1]);
//                    ublController.prepare(scalingFactor, (int) totMem, "mem");
                }

            } else if (current == 2 && last == 2 && secondLast == 2) {
                mem++;
                System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
            } else {
                normal++;
                //System.out.println("Normal " + normal);
            }
            Thread.sleep(2000l);
        }

    }


    public static SOM deSerialization(String file) throws IOException, ClassNotFoundException {
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
