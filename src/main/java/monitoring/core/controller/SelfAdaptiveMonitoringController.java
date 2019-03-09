package monitoring.core.controller;

import monitoring.core.prediction.impl.MonitoringAsService;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    @Autowired
    public SelfAdaptiveMonitoringController(MonitoringAsService service) {
        this.service = service;

    }

    @Scheduled(fixedRate = 5000)
    @GetMapping("/prediction")
    public String getPredictionWithMetrics() throws MalformedObjectNameException, InterruptedException,
            ReflectionException, IOException, InstanceNotFoundException, ClassNotFoundException, SigarException {

        return service.getTrainedBestMatchNeuron();
    }

    //todo:new API

    @GetMapping("/details")
    public String getSystemDetails()  {
        return service.getSystemDetails();
    }
}
