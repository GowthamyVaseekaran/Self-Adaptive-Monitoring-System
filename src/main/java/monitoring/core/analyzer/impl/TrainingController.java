package monitoring.core.analyzer.impl;

import monitoring.core.analyzer.CoEfficentCalculator;
import monitoring.core.analyzer.LearningManager;
import monitoring.core.planner.som.SOM;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This class contains the implementation to train the som with the dataset.
 */

public class TrainingController {
    // TODO: 3/29/19 Changed to private 
    private int inputDataSet;
    private LearningManager learningManager;
    private CoEfficentCalculator coEfficentCalculator;
    private SOM trainedSOM;
    int cpuCap;    //CPU cap allocated to the VM
    Map<String, Map<String, Double>> bestMatchMetrics;
    private static final Log logger = LogFactory.getLog(TrainingController.class);

    public TrainingController(int inputDataSet, File file1, File file) {
        this.inputDataSet = inputDataSet;
        learningManager = new LearningManager(inputDataSet, file1, file);
        coEfficentCalculator = new CoEfficentCalculator();
        cpuCap = 1;
    }


    void startLearning() {
        Long startTime = System.currentTimeMillis();
        trainedSOM = learningManager.train();
        //Training to find out the best match Metrics.
        try {
            bestMatchMetrics = coEfficentCalculator.trainWithMetrics();
        } catch (IOException e) {
           logger.error(e.getMessage());
        }
        Long endTime = System.currentTimeMillis();
        Long trainningTime = endTime - startTime;
        logger.info("Training Time (Minutes) " + trainningTime / 1000);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info(args[2]);
        if (args.length != 3) {
            logger.error("Usage: TrainingController cpu-training_log mem-train_log input_datasize");
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
                    logger.info("Maximum CPU cap reached. Require VM migration");
                    return;
                } else if (scalingFactor >= 70) {
                    cpuCap++;
                } else if (scalingFactor <= 30 && current > 1) {
                    cpuCap--;
                }
                logger.info("Setting VCPU of VM to " + cpuCap);
                Runtime.getRuntime().exec("sudo /usr/sbin/xm sched-credit -d 1 -c " + cpuCap);
                Thread.sleep(100);
            } else {
                int newmem = scalingFactor * current / 100;
                if (newmem >= 1024) {
                    logger.info("Migrating VM to another host");
                } else if (newmem > (current * 3) / 4) {
                    logger.info("Resource scaled from " + current + " to " + Math.round(current));
                    Runtime.getRuntime().exec("sudo /usr/sbin/xm mem-set 1 " + Math.round(current));
                    Thread.sleep(100);
                } else {
                    logger.info("Memory scaling not required as adequate resources are already given");
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
