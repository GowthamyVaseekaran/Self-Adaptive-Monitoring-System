package monitoring.core.Entities.DBConfiguration;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TestRepository extends CrudRepository<Metrics,Integer>{
//   Iterable<Metrics> Metrics(String cpu, String memory, String cpu_idle, String cpu_nice, String cpu_user, String cpu_wait, String committedVM, String free_physical_memory, String ram, String load_avg, String total_swap, String free_swap, String used_swap, String no_of_read, String read_byte, String no_of_writes, String write_bytes, String total_disk, String used_disk, String free_disk, String file_count, String total_thread_count, String daemon_thread_count, String peak_thread_count, String running_thread_count, String rx_bytes, String rx_dropped, String rx_error, String rx_frames, String rx_overruns, String rx_packet, String speed, String tx_bytes, String tx_carrier, String tx_collisions, String tx_dropped, String tx_errors, String tx_overruns, String tx_packets, String disk_name, String network_address, String network_name);
//    List<Metrics> findAllByCpu(String cpu);
    List<Metrics> findAll();
}
