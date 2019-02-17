package ubl;


import java.io.File;
import java.io.IOException;

public class UBLController {
    int inputDataSet;
    LearningManager learningManager;
    SOM trainedSOM;
    int cpu_cap;    //CPU cap allocated to the VM

    public UBLController(int inputDataSet, File file1, File file) {
        this.inputDataSet = inputDataSet;
        learningManager = new LearningManager(inputDataSet, file1, file);
        cpu_cap = 1;
    }


    void startLearning() {
        trainedSOM = learningManager.train();
//        ModelGenerator modelGenerator = new ModelGenerator();
//        Instances dataset = modelGenerator.loadDataset("");
//        Instances traindataset = new Instances(dataset, 0, 1024);
//        MultilayerPerceptron multilayerPerceptron = (MultilayerPerceptron) modelGenerator.buildClassifier(traindataset);

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(args[2]);
        if (args.length != 3) {
            System.out.println("Usage: UBLController cpu-training_log mem-train_log input_datasize");
            System.exit(1);
        }
        UBLController ublController;

        ublController = new UBLController(Integer.parseInt(args[2]), new File(args[0]), new File(args[1]));
        ublController.startLearning();
        String line = null;

        Thread.sleep(3000);
//
//        int normal = 0;
//        int cpu = 0;
//        int mem = 0;
//        int last = 0;
//        int secondLast = 0;
//        int current = 0;
//
//        Scanner cpuScanner = new Scanner(new File("/home/thamy/Desktop/Self-Organizing-Map/logs/Anomoluscpu.txt"), "UTF-8");
//        Scanner memScanner = new Scanner(new File("/home/thamy/Desktop/Self-Organizing-Map/logs/Anomolusmemory.txt"), "UTF-8");
//
//        while (true) {
//            int j = 0;
//            while (!cpuScanner.hasNextLine()) ;
//
//                line = cpuScanner.nextLine();
//                //j++;
//
//            String[] strings = line.split(",");
//            ArrayList<String> data = new ArrayList<String>();
//            for (String s : strings) {
//                if (s != null && s.mapLength() > 0)
//                    data.add(s);
//            }
//            double[] metrics = new double[2];
//
//            // read cpu metric from file change to 6
//            metrics[0] = (Math.round((Double.parseDouble(data.get(6)) *100)));
//          //  j = 0;
//
//            while (!memScanner.hasNextLine()) ;
//           // while (j < 2) {
//                line = memScanner.nextLine();
//               // j++;
//          //  }
//            strings = line.split(",");
//            data = new ArrayList<String>();
//            for (String s : strings) {
//                if (s != null && s.mapLength() > 0)
//                    data.add(s);
//            }
//            double totMem = (Double.parseDouble(data.get(1))*100);
//
//            metrics[1] = Math.round(totMem);
//
//            // read mem metric from file and normalize
//           // metrics[1] = Math.round(((Double.parseDouble(data.get(1)) * 100.0) / totMem));
//           // j = 0;
////            while (!memScanner.hasNextLine()) ;
////            while (j < 2) {
////                line = memScanner.nextLine();
////                j++;
////            }
//            WeightVector testVector = new WeightVector(metrics);
//
//            secondLast = last;
//            last = current;
//
//            // get prediction for input vector
//            current = ublController.trainedSOM.testInput(testVector);
//            System.out.println("current status"+ current);
//            // if 3 consecutive predictions are anomaly then find cause
//            if(current ==1 && last==1 && secondLast ==1) {
//                System.out.println("Tring Tring Anomaly detected");
//            }
////            if (current == 1 && last == 1 && secondLast == 1) {
////
////                Neuron trainedNeuron = ublController.trainedSOM.getTrainedNeuron(testVector);
////                int prediction = ublController.trainedSOM.causeInference(trainedNeuron);
////
////                // if cause inference gives CPU as metric causing anomaly
////                if (prediction == 1) {
////                    cpu++;
////                    System.out.println("CPU " + cpu + " Anomaly detected cpu = " + metrics[0]);
////                    SignatureDriven signatureDriven = new SignatureDriven();
////                    int scalingFactor = signatureDriven.signature("cpu", metrics[0]);
////                    ublController.prepare(scalingFactor, ublController.cpu_cap, "cpu");
////                    System.out.println("anomaly detected -cpu");
////                }
////
////                // if cause inference gives Memory as metric causing anomaly
////                else if (prediction == 2) {
////                    mem++;
////                    System.out.println("Mem " + mem + " Anomaly detected" + "   mem  " + metrics[1]);
////                    SignatureDriven signatureDriven = new SignatureDriven();
////                    int scalingFactor = signatureDriven.signature("mem", metrics[1]);
////                    ublController.prepare(scalingFactor, (int) totMem, "mem");
////                }
////
////            } else {
////                normal++;
////                //System.out.println("Normal " + normal);
////            }
//            Thread.sleep(2000l);
//        }
    }

    void prepare(int scaling_factor, int current, String para) {
        try {
            if (para == "cpu") {
                if (cpu_cap == 10) {
                    System.out.println("Maximum CPU cap reached. Require VM migration");
                    return;
                } else if (scaling_factor >= 70)
                    cpu_cap++;
                else if (scaling_factor <= 30 && current > 1)
                    cpu_cap--;
//              else
//                {
//                    System.out.println("No scaling of CPU required");
//                    return;
//                }
//                if(newcpu>2)
//                {
//                    System.out.println("Migrating VM to a new host");
////                    Runtime.getRuntime().exec("sudo /usr/sbin/xm migrate 1 152.46.17.33 -l");
//                }
                System.out.println("Setting VCPU of VM to " + cpu_cap);
                Runtime.getRuntime().exec("sudo /usr/sbin/xm sched-credit -d 1 -c " + cpu_cap);
                Thread.sleep(100);
            } else {
                int newmem = scaling_factor * current / 100;
                if (newmem >= 1024) {
                    System.out.println("Migrating VM to another host");
                } else if (newmem > (current * 3) / 4) {
                    System.out.println("Resource scaled from " + current + " to " + Math.round(current));
                    Runtime.getRuntime().exec("sudo /usr/sbin/xm mem-set 1 " + Math.round(current));
                    Thread.sleep(100);
                } else {
                    System.out.println("Memory scaling not required as adequate resources are already given");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
