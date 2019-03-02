package monitoring.core.training.impl;


import monitoring.core.som.SOM;
import monitoring.core.training.LearningManager;
import java.io.File;
import java.io.IOException;

/**
 * This class contains the implementation to train the SOM with the dataset.
 */
public class TrainingController {
    int inputDataSet;
    LearningManager learningManager;
    SOM trainedSOM;
    int cpuCap;    //CPU cap allocated to the VM

    public TrainingController(int inputDataSet, File file1, File file) {
        this.inputDataSet = inputDataSet;
        learningManager = new LearningManager(inputDataSet, file1, file);
        cpuCap = 1;
    }


    void startLearning() {
        Long startTime = System.currentTimeMillis();
        trainedSOM = learningManager.train();
        Long endTime = System.currentTimeMillis();
        Long trainningTime = endTime - startTime;
        System.out.println("Training Time (Minutes) " + trainningTime / 60000);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(args[2]);
        if (args.length != 3) {
            System.out.println("Usage: TrainingController cpu-training_log mem-train_log input_datasize");
            System.exit(1);
        }
        TrainingController trainingController;

        trainingController = new TrainingController(Integer.parseInt(args[2]), new File(args[0]), new File(args[1]));
        trainingController.startLearning();
        String line = null;

        Thread.sleep(3000);
    }

    void prepare(int scalingFactor, int current, String para) {
        try {
            if (para == "cpu") {
                if (cpuCap == 10) {
                    System.out.println("Maximum CPU cap reached. Require VM migration");
                    return;
                } else if (scalingFactor >= 70) {
                    cpuCap++;
                } else if (scalingFactor <= 30 && current > 1) {
                    cpuCap--;
                }
                System.out.println("Setting VCPU of VM to " + cpuCap);
                Runtime.getRuntime().exec("sudo /usr/sbin/xm sched-credit -d 1 -c " + cpuCap);
                Thread.sleep(100);
            } else {
                int newmem = scalingFactor * current / 100;
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