package monitoring.core.filewriting;

import monitoring.core.som.WeightVector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains implementation to parse the values from the command
 * values are : cpu file, mem file and input datasize.
 */
public class FileParser {
    Scanner cpuScanner;
    Scanner memScanner;

    public FileParser(File cpuLog, File memLog) {
        try {
            this.cpuScanner = new Scanner(cpuLog, "UTF-8");
            if (memLog != null) {
                this.memScanner = new Scanner(memLog, "UTF-8");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    // function to read metrics from the cpu and mem logs and create list of WeightVectors
    public ArrayList<WeightVector> createWeightVectors(int length) {

        ArrayList<WeightVector> vectors = new ArrayList<WeightVector>();
        String line = null;
        int i = 0;

        // while there are more entries in the logs
        while (i < length && cpuScanner.hasNextLine() && memScanner.hasNextLine()) {
           // int j=0;
           // while(j<6 && cpuScanner.hasNextLine()) {
                line = cpuScanner.nextLine();
            //    j++;
          //  }
            String[] strings = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            for (String s: strings) {
                if (s != null && s.length() > 0) {
                    data.add(s);
                }
            }

            double[] metrics = new double[2];
            // read CPU metric change to 6 *100
            metrics[0] = (Math.round((Double.parseDouble(data.get(6)) * 100)));

           // j=0;
           // while(j<2 && memScanner.hasNextLine()) {
                line = memScanner.nextLine();
           //     j++;
           // }
            strings = line.split(",");
            data = new ArrayList<String>();
            for (String s: strings) {
                if (s != null && s.length() > 0) {
                    data.add(s);
                }
            }
            //*100
            double totMem = (Double.parseDouble(data.get(1)) * 100);

            // normalize mem metric
            metrics[1] = Math.round(totMem);
//            j=0;
//            while(j<2 && memScanner.hasNextLine()) {
//                line = memScanner.nextLine();
//                j++;
//            }
            vectors.add(new WeightVector(metrics));
            i++;
        }
        return vectors;
    }
}
