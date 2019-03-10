package monitoring.core.training;

import monitoring.core.configuration.FileParser;
import monitoring.core.som.SOM;
import monitoring.core.som.WeightVector;
import monitoring.core.utils.visulaizer.Visualizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *  Methods used for training.
 */
public class LearningManager {
    private static final Log logger = LogFactory.getLog(LearningManager.class);
    //training data size
    int inputDataSize;

    // SOM map mapLength
    int mapLength = 32;

    // SOM map mapBreadth
    int mapBreadth = 32;

    //weight vectors with which to train
    ArrayList<WeightVector> inputWeightVectors;

    // kFactor for kFactor-cross fold validation
    //change 100 to 1000
    final int kFactor = 100;

    // used for our integrated code with two separate logs for CPU and memory
    public LearningManager(int inputDataSize, File cpuLog, File memLog) {
        this.inputDataSize = inputDataSize;
        FileParser parser = new FileParser(cpuLog, memLog);
        inputWeightVectors = parser.createWeightVectors(inputDataSize);
    }

    // function used for training
    public SOM train() {
        WeightVector[][] inputSets = createInputSets();
        SOM som = kFoldCrossValidation(inputSets);
        Visualizer visualizer = new Visualizer(som);
        //som.printNeurons();

        //saved model
        //serialize the object
        String fileName = "/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/test.ser";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            //method to serialize the object
            objectOutputStream.writeObject(som);
            objectOutputStream.close();
            fileOutputStream.close();
            logger.info("serialization done");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


        //Write the elements to the file
        try (PrintWriter writer = new PrintWriter(
                new File("/home/thamy/Pictures/Self-Adaptive-Monitoring-System/HistoryData/trainWithMetrics.csv"))) {
            for (int i = 0; i < mapLength; i++) {
                for (int j = 0; j < mapBreadth; j++) {
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
            logger.error(e.getMessage());
        }
        visualizer.draw();


        logger.info("SELF ORGANIZING MAP " + som.getAccuracy());
        logger.info("SOM is successfully trained ");
        return som;
    }

    // method to perform kFactor fold cross validation
    private SOM kFoldCrossValidation(WeightVector[][] inputSets) {
        SOM[] soms = trainWithOtherSets(inputSets);
        for (int i = 0; i < kFactor; i++) {
            soms[i].classifyNeurons();
        }
        for (int i = 0; i < kFactor; i++) {
            soms[i].calculateAccuracy(inputSets[i]);
        }
        return getTheBestAccuracy(soms);
    }

    // get the SOM with the best accuracy
    private SOM getTheBestAccuracy(SOM[] trainedSoms) {
        double accuracy = 0;
        SOM bestAccuracySOM = null;
        for (int i = 0; i < trainedSoms.length; i++) {
            logger.info("Som " + i + " accuracy: " + trainedSoms[i].getAccuracy() * 100);
            if (trainedSoms[i].getAccuracy() > accuracy) {
                accuracy = trainedSoms[i].getAccuracy();
                bestAccuracySOM = trainedSoms[i];
            }

        }
        return bestAccuracySOM;
    }

    // trains for kFactor fold cross validation
    private SOM[] trainWithOtherSets(WeightVector[][] inputSets) {
        SOM[] soms = new SOM[kFactor];
        for (int i = 0; i < kFactor; i++) {
            //creating kFactor Self Organizing maps
            soms[i] = new SOM();
        }
        int currentK = 0;

        while (currentK < kFactor) {
            for (int i = 0; i < kFactor; i++) {
                if (i != currentK) {
                    for (int k = 0; k < inputSets[i].length; k++) {
                        soms[currentK].trainWithInput(inputSets[i][k]);
                    }
                }

            }

            currentK++;
        }

        return soms;
    }

    // divides training data into kFactor sets
    private WeightVector[][] createInputSets() {
        int setSize = inputDataSize / kFactor;
        WeightVector inputSet[][] = new WeightVector[kFactor][setSize];
        int i = 0;
        int m = 0;
        for (i = 0; i < kFactor; i++) {
            for (int j = 0; j < setSize; j++) {
                inputSet[i][j] = inputWeightVectors.get(m);
                m++;
            }
        }
        return inputSet;
    }
}
