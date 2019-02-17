package ubl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class FileParser {
    Scanner cpuScanner;
    Scanner memScanner;

    public FileParser(File cpu_log, File mem_log) {
        try {
            this.cpuScanner = new Scanner(cpu_log, "UTF-8");
            if(mem_log!=null)
                this.memScanner = new Scanner(mem_log, "UTF-8");
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
        while(i<length && cpuScanner.hasNextLine() && memScanner.hasNextLine()) {
           // int j=0;
           // while(j<6 && cpuScanner.hasNextLine()) {
                line = cpuScanner.nextLine();
            //    j++;
          //  }
            String[] strings = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            for(String s: strings) {
                if(s!=null && s.length()>0)
                    data.add(s);
            }

            double[] metrics = new double[2];
            // read CPU metric change to 6
            metrics[0] = (Math.round((Double.parseDouble(data.get(6))*100)));

           // j=0;
           // while(j<2 && memScanner.hasNextLine()) {
                line = memScanner.nextLine();
           //     j++;
           // }
            strings = line.split(",");
            data = new ArrayList<String>();
            for(String s: strings) {
                if(s!=null && s.length()>0)
                    data.add(s);
            }
            double totMem = (Double.parseDouble(data.get(1))*100);

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


    // function for creating WeightVectors from Daniels' logs
    public ArrayList<WeightVector> createWeightVectorsFromTestFile(int length, File file) {
        Scanner scanner = null;
        int i=0;
        try {
            scanner = new Scanner(file, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<WeightVector> vectors = new ArrayList<WeightVector>();

        while (i<=length && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] strings = line.split(" ");
            double[] metrics = new double[2];

            // normalize CPU and mem metrics
            metrics[0] = Math.round((Double.parseDouble(strings[4]))*100/Double.parseDouble(strings[2]));
            metrics[1] = Math.round((Double.parseDouble(strings[8]))*100/Double.parseDouble(strings[6]));

            vectors.add(new WeightVector(metrics));
            i++;
        }
        return vectors;
    }
}
