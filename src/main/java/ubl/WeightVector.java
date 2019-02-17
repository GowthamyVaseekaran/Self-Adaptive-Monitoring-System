package ubl;

import java.io.Serializable;
import java.util.Random;

public class WeightVector implements Serializable {

    // number of metrics being collected
    private int size = 2;

    private double[] vector = new double[size];

    public WeightVector() {}

    public WeightVector(double[] vector) {
        this.vector = vector;
    }

    public double[] getVector() {
        return vector;
    }


    // function to initialize vector with random number from 0 to 100
    public void initialize() {
        Random generator = new Random();
        for(int i=0; i<size; i++) {
            vector[i] = generator.nextInt(100);
        }
    }

    // function to print vector
    public void print() {
        for(int i=0; i<size; i++)
            System.out.print(vector[i]);
        System.out.println();
    }

    // function to calculate Euclidean distance with given input vector
    public double getEuclideanDistance(WeightVector input) {
        double val =0;
        for(int i=0; i<size; i++) {
            val += (input.vector[i]-this.vector[i]) * (input.vector[i]-this.vector[i]);
        }
        return Math.sqrt(val);
    }

    // function to subtract given vector from this vector
    public WeightVector minus(WeightVector other) {
        WeightVector ans = new WeightVector();
        for(int i=0; i<size; i++)
            ans.vector[i] = this.vector[i] - other.vector[i];
        return ans;
    }

    // function to calculate mod of difference with given vector
    public int modDifference(WeightVector other) {
        WeightVector ans = new WeightVector();
        for(int i=0; i<size; i++)
            ans.vector[i] = Math.abs(this.vector[i] - other.vector[i]);
        double highest =0;
        int metric =0;
        for(int i=0; i<size; i++)
            if(ans.vector[i]>highest) {
                highest = ans.vector[i];
                metric = i;
            }

        return metric;
    }

    // function to add given vector with this vector
    public WeightVector add(WeightVector other) {
        WeightVector ans = new WeightVector();
        for(int i=0; i<size; i++)
            ans.vector[i] = this.vector[i] + other.vector[i];
        return ans;
    }

    // function to multiply this vector with given value
    public WeightVector multiply(float val) {
        WeightVector ans = new WeightVector();
        for(int i=0; i<size; i++)
            ans.vector[i] = this.vector[i] * val;
        return ans;
    }


    // calculate manhattan distance for given vector
    public double getManhattanDistance(WeightVector weightVector) {
        double mannDist = 0;
        WeightVector temp = this.minus(weightVector);
        for(int i=0; i<size; i++)
            mannDist += Math.abs(temp.vector[i]);
        return mannDist;
    }

    @Override
    public String toString() {
        return vector.toString();
    }
}

