package monitoring.core.som;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the implementation for the scheleton for Self Organizing Map.
 */

public class SOM implements Serializable {
    // SOM map mapLength
    public int length = 32;

    // SOM map mapBreadth
    public int breadth = 32;

    // neuron sets in SOM
    public Neuron[][] neurons = new Neuron[length][breadth];

    private double accuracy;

    public double getAccuracy() {
        return accuracy;
    }

    private static final double INFINITE = Double.MAX_VALUE;

    // number of times each input should be trained default 10
    private static final int TIMES_TO_TRAIN_WITH_INPUT = 3;

    // threshold percentile default 80
    private static final int PERCENTILE = 85;

    public SOM() {
        initialize();
    }

    // initialize neurons with random values from 0 to 100
    private void initialize() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                neurons[i][j] = new Neuron(i, j);
                (neurons[i][j]).initialize();
            }
        }
    }

    // function for training with given input vector
    public void trainWithInput(WeightVector input) {
        for (int i = 0; i < TIMES_TO_TRAIN_WITH_INPUT; i++) {
            Neuron trainedNeuron = getTrainedNeuron(input);
            updateWeightsOfNeighbours(trainedNeuron, input);
        }
    }

    // function for getting the best mapped neuron with least Euclidean distance
    public Neuron getTrainedNeuron(WeightVector input) {
        Neuron trainedNeuron = null;
        double minDistance = INFINITE;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                double distance = neurons[i][j].getEuclideanDistance(input);
                if (minDistance > distance) {
                    minDistance = distance;
                    trainedNeuron = neurons[i][j];
                }
            }
        }
        return trainedNeuron;
    }

    // function for updating values of neighbours
    private void updateWeightsOfNeighbours(Neuron trained, WeightVector input) {
        int i = 0;
        int j = 0;
        int x = trained.getX();
        if (x > 0 && x < length) {
            i = x - 1;
        }
        int y = trained.getY();
        while (i < length && i <= x + 1) {
            if (y > 0 && y < length) {
                j = y - 1;
            }
            while (j < breadth && j <= y + 1) {
                updateWeight(neurons[i][j], input);
                j++;
            }
            i++;
        }
    }

    // function which sets weight vector according to formula
    // W(t+1) = W(t) + N(v,t)L(t)(D(t)-W(t))
    private void updateWeight(Neuron neuron, WeightVector input) {
        // value of learning coefficient
        int learningCoef = 1;

        // value of neighbourhood function
        float neighbourhoodFunction = 0.25f;

        WeightVector temp = input.minus(neuron.getWeightVector()).multiply(neighbourhoodFunction * learningCoef);
        WeightVector updated = (neuron.getWeightVector()).add(temp);
        neuron.setWeightVector(updated);
    }

    // calculate threshold values of neighborhood area size and classify neurons
    public void classifyNeurons() {
        calculateNeighbourhoodAreaFunction();
        double threshold = findThresholdValue();
        classify(threshold);
    }

    // function for classifying neurons as Normal or Anomalous
    // based on Neighborhood Area Size
    private void classify(double threshold) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                if (neurons[i][j].neighbourhoodAreaSize > threshold) {
                    neurons[i][j].setState(State.ANOMALOUS);
                } else {
                    neurons[i][j].setState(State.NORMAL);
                }
            }
        }
    }

    // function for finding threshold value based on percentile
    private double findThresholdValue() {
        List<Double> nas = new ArrayList<Double>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                nas.add(neurons[i][j].neighbourhoodAreaSize);
            }
        }
        Collections.sort(nas);
        int i = nas.size() * PERCENTILE / 100;
        return nas.get(i);
    }

    // function to calculate Neighborhood Area Size with neighbours
    private void calculateNeighbourhoodAreaFunction() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                if (i > 0) {
                    (neurons[i][j]).calculateManhattanDistance(neurons[i - 1][j]);
                }
                if (j < breadth - 1) {
                    (neurons[i][j]).calculateManhattanDistance(neurons[i][j + 1]);
                }
                if (i < length - 1) {
                    (neurons[i][j]).calculateManhattanDistance(neurons[i + 1][j]);
                }
                if (j > 0) {
                    (neurons[i][j]).calculateManhattanDistance(neurons[i][j - 1]);
                }
            }
        }
    }

    // function to display neurons
    public void printNeurons() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                neurons[i][j].printVector();
            }
        }
    }

    public void printSOM() {
        StringBuilder stringBuilder = new StringBuilder(330);
        stringBuilder.append(System.lineSeparator());
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                stringBuilder.append(neurons[i][j].getWeightVector().getVector());
            }
        }

        System.out.println(stringBuilder.toString());

    }

    // function to calculate accuracy of current SOM
    public void calculateAccuracy(WeightVector[] inputSet) {
        int normalCount = 0;
        //  System.out.println(inputSet.mapLength);
        for (int i = 0; i < inputSet.length; i++) {
            Neuron trainedNeuron = getTrainedNeuron(inputSet[i]);
            if (trainedNeuron.getState() == State.NORMAL) {
                normalCount++;
            }
        }
        accuracy = normalCount / (double) (inputSet.length);
    }

    // function to test if given input vector is normal or anomalous
    public int testInput(WeightVector input) {
        Neuron trainedNeuron = getTrainedNeuron(input);
        if (trainedNeuron.getState() == State.ANOMALOUS) {
            System.out.println("Am anomaly");
            return causeInference(trainedNeuron);
        } else {
            System.out.println("am normal");
            return 0;
        }

    }

    // function to determine cause for anomaly
    public int causeInference(Neuron trainedNeuron) {
        int x = trainedNeuron.getX();
        int y = trainedNeuron.getY();
        int m, n, o, p;
        if (x == 0) {
            m = 0;
        } else {
            m = x - 1;
        }
        if (x == length - 1) {
            n = length - 1;
        } else {
            n = x + 1;
        }
        if (y == 0) {
            o = 0;
        } else {
            o = y - 1;
        }
        if (y == breadth - 1) {
            p = breadth - 1;
        } else {
            p = y + 1;
        }
        List<Integer> majorityVoting = new ArrayList<Integer>();
        for (int i = m; i < n; i++) {
            for (int j = o; j < p; j++) {
                if (neurons[i][j].getState() == State.NORMAL) {
                    int metric = neurons[i][j].getWeightVector().modDifference(trainedNeuron.getWeightVector());
                    majorityVoting.add(metric);
                }
            }
        }
        int[] count = new int[3];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < majorityVoting.size(); j++) {
                if (majorityVoting.get(j) == i) {
                    count[i]++;
                }
            }
        }
        if (count[0] >= count[1]) {
            return 1;
        } else {
            return 2;
        }


    }


    // function to print neighborhood area sizes of all neurons
    public void printNAS() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                System.out.println(Math.round(neurons[i][j].neighbourhoodAreaSize));
            }
        }
    }
}
