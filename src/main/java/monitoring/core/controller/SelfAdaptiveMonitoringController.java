package monitoring.core.controller;

import monitoring.core.entities.dbConfiguration.MetricInfoDao;
import monitoring.core.executor.impl.MonitoringAsService;
import monitoring.core.executor.impl.MainExecutorService;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;


/**
 * Controller class.
 */
@CrossOrigin(origins ="http://localhost:3000", maxAge = 4800, allowCredentials = "false")
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/system")
public class SelfAdaptiveMonitoringController {
    private final MonitoringAsService service;
    private final MainExecutorService mainExecutorService;
    @Autowired
    public SelfAdaptiveMonitoringController(MonitoringAsService service, MainExecutorService mainExecutorService) {
        this.service = service;

        this.mainExecutorService = mainExecutorService;
    }

    @Scheduled(fixedRate = 5000)
    @GetMapping("/prediction")
    public String getPredictionWithMetrics() throws MalformedObjectNameException, InterruptedException,
            ReflectionException, IOException, InstanceNotFoundException, ClassNotFoundException, SigarException, IllegalAccessException, InstantiationException {

        return service.getTrainedBestMatchNeuron();
    }

    @GetMapping("/details")
    public String getSystemDetails()  {
        return service.getSystemDetails();
    }

    @GetMapping("/baselines")
    public List<MetricInfoDao> listBaselines() {

        return mainExecutorService.findAllCpuInfo();
    }
}
