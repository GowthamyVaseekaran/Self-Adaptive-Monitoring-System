package ubl;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class LearningManager {
    //training data size
    int inputDataSize;

    // SOM map mapLength
    int mapLength = 32;

    // SOM map mapBreadth
    int mapBreadth = 32;

    //weight vectors with which to train
    ArrayList<WeightVector> inputWeightVectors;

    // K for K-cross fold validation
    //change 100 to 1000
    final int K = 100;

    // used for our integrated code with two separate logs for CPU and memory
    public LearningManager(int inputDataSize, File cpu_log, File mem_log) {
        this.inputDataSize = inputDataSize;
        FileParser parser = new FileParser(cpu_log, mem_log);
        inputWeightVectors = parser.createWeightVectors(inputDataSize);
    }

    // used while training with Daniel's data set
    public LearningManager(int inputDataSize, File trainFile) {
        this.inputDataSize = inputDataSize;
        FileParser parser = new FileParser(trainFile, null);
        inputWeightVectors = parser.createWeightVectorsFromTestFile(inputDataSize, trainFile);
    }

    // function used for training
    public SOM train() {
        WeightVector[][] inputSets = createInputSets();
        SOM som = kFoldCrossValidation(inputSets);
        Visualizer visualizer = new Visualizer(som);
        //som.printNeurons();

        //saved model
        //serialize the object
        String fileName = "/home/thamy/Desktop/Self-Adaptive-Monitoring-System/HistoryData/test.ser";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            //method to serialize the object
            objectOutputStream.writeObject(som);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("serialization done");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        //System.out.println("Selected SOM");

        //JSON
//        for(int i=0;i<mapLength;i++){
//            for (int j=0;j<mapBreadth;j++) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("co-ordinates","("+i+","+j+")");
//                jsonObject.put()
//            }
//
//        }


        //Write the elements to the file
        try (PrintWriter writer = new PrintWriter(new File("/home/thamy/Desktop/Self-Adaptive-Monitoring-System/HistoryData/test.csv"))) {
            for (int i = 0; i < mapLength; i++) {
                for (int j = 0; j < mapBreadth; j++) {
                    //System.out.println("test" + som.neurons.mapLength);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("(" + i + " " + j + ")");
                    stringBuilder.append(",");
                    stringBuilder.append(som.neurons[i][j].getWeightVector().getVector()[0]);
                    stringBuilder.append(",");
                    stringBuilder.append(som.neurons[i][j].getWeightVector().getVector()[1]);
                    stringBuilder.append(",");
                    stringBuilder.append(som.neurons[i][j].getState());
                    stringBuilder.append("\n");
                    writer.write(stringBuilder.toString());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        visualizer.draw();


        //  System.out.println("SELF ORGANIZING MAP " + som.neurons);
        System.out.println("SOM is successfully trained ");
        return som;
    }

    // method to perform K fold cross validation
    private SOM kFoldCrossValidation(WeightVector[][] inputSets) {
        SOM[] soms = trainWithOtherSets(inputSets);
        for (int i = 0; i < K; i++)
            soms[i].classifyNeurons();
        for (int i = 0; i < K; i++)
            soms[i].calculateAccuracy(inputSets[i]);
        return getTheBestAccuracy(soms);
    }

    // get the SOM with the best accuracy
    private SOM getTheBestAccuracy(SOM[] trainedSoms) {
        double accuracy = 0;
        SOM bestAccuracySOM = null;
        for (int i = 0; i < trainedSoms.length; i++) {
            System.out.println("Som " + i + " accuracy: " + trainedSoms[i].getAccuracy());
            if (trainedSoms[i].getAccuracy() > accuracy) {
                accuracy = trainedSoms[i].getAccuracy();
                bestAccuracySOM = trainedSoms[i];
            }
        }
        return bestAccuracySOM;
    }

    // trains for K fold cross validation
    private SOM[] trainWithOtherSets(WeightVector[][] inputSets) {
        SOM[] soms = new SOM[K];
        for (int i = 0; i < K; i++)
            //creating K Self Organizing maps
            soms[i] = new SOM();
        int currentK = 0;

        while (currentK < K) {
            for (int i = 0; i < K; i++) {
                if (i != currentK) {
                    for (int k = 0; k < inputSets[i].length; k++) {
                        soms[currentK].trainWithInput(inputSets[i][k]);
                    }
                }
               // Visualizer visualizer = new Visualizer(soms[currentK]);
               // visualizer.draw();
            }
            // Visualizer visualizer = new Visualizer(soms[currentK]);
            // visualizer.draw();
            currentK++;
        }

        return soms;
    }

    // divides training data into K sets
    private WeightVector[][] createInputSets() {
        int setSize = inputDataSize / K;
        WeightVector inputSet[][] = new WeightVector[K][setSize];
        int i = 0;
        int m = 0;
        for (i = 0; i < K; i++) {
            for (int j = 0; j < setSize; j++) {
                inputSet[i][j] = inputWeightVectors.get(m);
                m++;
            }
        }
        return inputSet;
    }
}
