package ubl;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class ReadInput {
    ArrayList<Double> read(String type)
    {
        try
        {    
            if(type.equalsIgnoreCase("cpu")){
                return parseCpu();
            } else {
                return parseMem();
            }

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }


    private ArrayList<Double> parseMem() throws FileNotFoundException {

        ArrayList<Double> list = new ArrayList<Double>();
        int j;
        Scanner memScanner = new Scanner(new File("/home/thamy/Desktop/Self-Organizing-Map/logs/Anomolusmemory.txt"), "UTF-8");

        String line = null;
        while (memScanner.hasNextLine()) {
          //  j=0;
           // while(memScanner.hasNextLine()) {
                line = memScanner.nextLine();
             //   j++;
           // }
            String[] strings = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            for(String s: strings) {
                if(s!=null && s.length()>0) {
                    System.out.println(s);
                    data.add(s);
                }

            }
            double totMem = (Double.parseDouble(data.get(1))*100);

           double metric = Math.round(totMem);

           // double totMem = Double.parseDouble(data.get(1));
           // double metric = Math.round((Double.parseDouble(data.get(2))/(totMem)*100.0));

//            j=0;
//            while(j<2 && memScanner.hasNextLine()) {
//                line = memScanner.nextLine();
//                j++;
//            }
            list.add(metric);
        }
        memScanner.close();
        return list;
    }

    private ArrayList<Double> parseCpu() throws FileNotFoundException {
        Scanner cpuScanner = new Scanner(new File("/home/thamy/Desktop/Self-Organizing-Map/logs/Anomoluscpu.txt"), "UTF-8");

        ArrayList<Double> list = new ArrayList<Double>();
        while (cpuScanner.hasNextLine()) {
        //    int j=0;
            String line = null;
           // while(cpuScanner.hasNextLine()) {
                line = cpuScanner.nextLine();
               // j++;
           // }
            String[] strings = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            for(String s: strings) {
                if(s!=null && s.length()>0)
                    data.add(s);
            }

            double metric = Math.round(Double.parseDouble(data.get(6))*100);
            System.out.println(metric);

            list.add(metric);
        }
        cpuScanner.close();
        return list;
    }

    public static void main(String args[])
    {
        ReadInput ri=new ReadInput();
        ri.read("cpu");
    }
}
