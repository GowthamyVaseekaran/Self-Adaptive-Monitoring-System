package monitoring.core.configuration;

import monitoring.core.entities.dbConfiguration.MetricInfoDao;
import monitoring.core.entities.dbConfiguration.Metrics;
import monitoring.core.entities.dbConfiguration.TestRepository;
import monitoring.core.planner.som.WeightVector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains implementation to parse the values from the command
 * values are : cpu file, mem file and input datasize.
 */
public class FileParser {
    private Scanner cpuScanner;
    private Scanner memScanner;
    private static final Log logger = LogFactory.getLog(FileParser.class);

    @Autowired
    TestRepository testRepository;

    public FileParser(File cpuLog, File memLog) {
        try {
            this.cpuScanner = new Scanner(cpuLog, "UTF-8");
            if (memLog != null) {
                this.memScanner = new Scanner(memLog, "UTF-8");
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }

    }

    public FileParser() {

    }

    // function to read Metrics from the cpu and mem logs and create list of WeightVectors
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
            for (String s : strings) {
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
            for (String s : strings) {
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

    private MetricInfoDao transform(Metrics metircsInfoEntity) {
        MetricInfoDao metricInfoDao = new MetricInfoDao();
        metricInfoDao.setCpu(metircsInfoEntity.getCpu());
        metricInfoDao.setMemory(metircsInfoEntity.getMemory());
        return metricInfoDao;
    }

    public List<MetricInfoDao> findAllCpuInfo() {
        List<MetricInfoDao> returnList = new ArrayList<>();
        List<Metrics> allCpuInfo = testRepository.findAll();
        for (Metrics cpu : allCpuInfo) {
            returnList.add(transform(cpu));

        }

        for (MetricInfoDao test : returnList) {
            logger.info("cpu" + test.getCpu());
            logger.info("memory" + test.getMemory());
        }
        return returnList;
    }

    public ArrayList<WeightVector> createWeightVectors1() {

        ArrayList<WeightVector> vectors = new ArrayList<WeightVector>();
        String line = null;
        int i = 0;


        findAllCpuInfo();

        return vectors;
    }
}
