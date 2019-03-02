package monitoring.core.utils.visulaizer;

import monitoring.core.som.SOM;

import javax.swing.*;
import java.awt.*;

/**
 *  This class contains the implementation to visualize self organizing map.
 */
public class Visualizer extends JFrame {
    private static final long SERIALVERSIONUID = 0L;

    public static final int FRAME_WIDTH = 900;
    public static final int FRAME_HEIGHT = 900;

    private SOM selfOrganizingMap = null;
    private int noOfNeuronCount = 0;


    public Visualizer(SOM som) {
        super("Self Organizing Map");
        this.selfOrganizingMap = som;
        noOfNeuronCount = som.length * som.breadth;
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createBufferStrategy(2);
    }

    public void draw() {
        Graphics graphics = getBufferStrategy().getDrawGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.RED);

        double[][] points = new double[noOfNeuronCount][2];
        double xmin = Double.MAX_VALUE, xmax = Double.MIN_VALUE;
        double ymin = Double.MAX_VALUE, ymax = Double.MIN_VALUE;

        //for (int x=0;x<noOfNeuronCount;x++) {
        int a = 0;
        for (int i = 0; i < selfOrganizingMap.length; i++) {
            for (int j = 0; j < selfOrganizingMap.breadth; j++) {
                points[a][0] = selfOrganizingMap.neurons[i][j].getWeightVector().getVector()[0];
                points[a][1] = selfOrganizingMap.neurons[i][j].getWeightVector().getVector()[1];
                xmin = Math.min(xmin, points[a][0]);
                ymin = Math.min(ymin, points[a][1]);
                xmax = Math.max(xmax, points[a][0]);
                ymax = Math.max(ymax, points[a][1]);
                a++;
            }

        }
        // }
//        for (int i = 0; i < noOfNeuronCount; i++) {
//            points[i][0] = som.getWeight(i, 0);
//            points[i][1] = som.getWeight(i, 1);
//            xmin = Math.min(xmin, points[i][0]);
//            ymin = Math.min(ymin, points[i][1]);
//            xmax = Math.max(xmax, points[i][0]);
//            ymax = Math.max(ymax, points[i][1]);
//        }

        // compute the scaling factor
        double dx = getWidth() / (xmax - xmin);
        double dy = getHeight() / (ymax - ymin);

        for (double[] pt : points) {
            // zero-base the coordinates and scale them to window size
            int x = (int) Math.round((pt[0] - xmin) * dx);
            int y = (int) Math.round((pt[1] - ymin) * dy);
            graphics.drawOval(x - 2, y - 2, 4, 4);
        }

        getBufferStrategy().show();
    }


}