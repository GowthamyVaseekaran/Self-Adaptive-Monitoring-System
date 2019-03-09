package monitoring.core.prediction.impl;

import org.hyperic.sigar.SigarException;

import java.io.IOException;
import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

/**
 * Implementation for monitoring service.
 */
public interface MonitoringAsService {
   String getTrainedBestMatchNeuron() throws IOException, ClassNotFoundException, InterruptedException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException, SigarException;
   String getSystemDetails();
}
