package monitoring.core.planner.som;

import java.io.Serializable;

enum State { NORMAL, ANOMALOUS}

/**
 * This class contains the implementation to create neurons for som.
 */
public class Neuron implements Serializable {

    private WeightVector weightVector = new WeightVector();

    // state of the neuron: Normal, Anomalous
    private State state;

    // x-coordinate in som
    private int x;

    // y-coordinate in som
    private int y;

    // neighborhood area size of neuron
    double neighbourhoodAreaSize = 0;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public WeightVector getWeightVector() {
        return weightVector;
    }

    public void setWeightVector(WeightVector weightVector) {
        this.weightVector = weightVector;
    }

    public State getState() {
        return state;
    }

    public double getNeighbourhoodAreaSize() {
        return neighbourhoodAreaSize;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Neuron(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void initialize() {
        weightVector.initialize();
    }

    public void printVector() {
        weightVector.print();
    }

    public double getEuclideanDistance(WeightVector input) {
        return weightVector.getEuclideanDistance(input);
    }

    public void calculateManhattanDistance(Neuron neuron) {
        this.neighbourhoodAreaSize += (this.weightVector).getManhattanDistance(neuron.weightVector);
    }

}
