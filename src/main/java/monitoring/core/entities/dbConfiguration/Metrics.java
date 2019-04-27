package monitoring.core.entities.dbConfiguration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity class.
 */
@Entity

public class Metrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cpu;

    private String memory;

    private String cpu_idle;

    private String cpu_nice;

    private String cpu_user;

    private String cpu_wait;

    private String committedVM;

    private String free_physical_memory;

    private String ram;

    private String load_avg;

    private String total_swap;

    private String free_swap;

    private String used_swap;

    private String no_of_read;

    private String read_byte;

    private String no_of_writes;

    private String write_bytes;

    private String total_disk;

    private String used_disk;

    private String free_disk;

    private String file_count;

    private String total_thread_count;

    private String daemon_thread_count;

    private String peak_thread_count;

    private String running_thread_count;

    private String rx_bytes;

    private String rx_dropped;

    private String rx_error;

    private String rx_frames;

    private String rx_overruns;

    private String rx_packet;

    private String speed;

    private String tx_bytes;

    private String tx_carrier;

    private String tx_collisions;

    private String tx_dropped;

    private String tx_errors;

    private String tx_overruns;

    private String tx_packets;

    private String disk_name;

    private String network_address;

    private String network_name;

    public Metrics() {
    }

    public Metrics(String cpu, String memory, String cpu_idle, String cpu_nice, String cpu_user,
                   String cpu_wait, String committedVM, String free_physical_memory, String ram, String load_avg,
                   String total_swap, String free_swap, String used_swap, String no_of_read, String read_byte,
                   String no_of_writes, String write_bytes, String total_disk, String used_disk, String free_disk,
                   String file_count, String total_thread_count, String daemon_thread_count, String peak_thread_count,
                   String running_thread_count, String rx_bytes, String rx_dropped, String rx_error, String rx_frames,
                   String rx_overruns, String rx_packet, String speed, String tx_bytes, String tx_carrier,
                   String tx_collisions, String tx_dropped, String tx_errors, String tx_overruns, String tx_packets,
                   String disk_name, String network_address, String network_name) {
        this.cpu = cpu;
        this.memory = memory;
        this.cpu_idle = cpu_idle;
        this.cpu_nice = cpu_nice;
        this.cpu_user = cpu_user;
        this.cpu_wait = cpu_wait;
        this.committedVM = committedVM;
        this.free_physical_memory = free_physical_memory;
        this.ram = ram;
        this.load_avg = load_avg;
        this.total_swap = total_swap;
        this.free_swap = free_swap;
        this.used_swap = used_swap;
        this.no_of_read = no_of_read;
        this.read_byte = read_byte;
        this.no_of_writes = no_of_writes;
        this.write_bytes = write_bytes;
        this.total_disk = total_disk;
        this.used_disk = used_disk;
        this.free_disk = free_disk;
        this.file_count = file_count;
        this.total_thread_count = total_thread_count;
        this.daemon_thread_count = daemon_thread_count;
        this.peak_thread_count = peak_thread_count;
        this.running_thread_count = running_thread_count;
        this.rx_bytes = rx_bytes;
        this.rx_dropped = rx_dropped;
        this.rx_error = rx_error;
        this.rx_frames = rx_frames;
        this.rx_overruns = rx_overruns;
        this.rx_packet = rx_packet;
        this.speed = speed;
        this.tx_bytes = tx_bytes;
        this.tx_carrier = tx_carrier;
        this.tx_collisions = tx_collisions;
        this.tx_dropped = tx_dropped;
        this.tx_errors = tx_errors;
        this.tx_overruns = tx_overruns;
        this.tx_packets = tx_packets;
        this.disk_name = disk_name;
        this.network_address = network_address;
        this.network_name = network_name;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getCpu_idle() {
        return cpu_idle;
    }

    public void setCpu_idle(String cpu_idle) {
        this.cpu_idle = cpu_idle;
    }

    public String getCpu_nice() {
        return cpu_nice;
    }

    public void setCpu_nice(String cpu_nice) {
        this.cpu_nice = cpu_nice;
    }

    public String getCpu_user() {
        return cpu_user;
    }

    public void setCpu_user(String cpu_user) {
        this.cpu_user = cpu_user;
    }

    public String getCpu_wait() {
        return cpu_wait;
    }

    public void setCpu_wait(String cpu_wait) {
        this.cpu_wait = cpu_wait;
    }

    public String getCommittedVM() {
        return committedVM;
    }

    public void setCommittedVM(String committedVM) {
        this.committedVM = committedVM;
    }

    public String getFree_physical_memory() {
        return free_physical_memory;
    }

    public void setFree_physical_memory(String free_physical_memory) {
        this.free_physical_memory = free_physical_memory;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getLoad_avg() {
        return load_avg;
    }

    public void setLoad_avg(String load_avg) {
        this.load_avg = load_avg;
    }

    public String getTotal_swap() {
        return total_swap;
    }

    public void setTotal_swap(String total_swap) {
        this.total_swap = total_swap;
    }

    public String getFree_swap() {
        return free_swap;
    }

    public void setFree_swap(String free_swap) {
        this.free_swap = free_swap;
    }

    public String getUsed_swap() {
        return used_swap;
    }

    public void setUsed_swap(String used_swap) {
        this.used_swap = used_swap;
    }

    public String getNo_of_read() {
        return no_of_read;
    }

    public void setNo_of_read(String no_of_read) {
        this.no_of_read = no_of_read;
    }

    public String getRead_byte() {
        return read_byte;
    }

    public void setRead_byte(String read_byte) {
        this.read_byte = read_byte;
    }

    public String getNo_of_writes() {
        return no_of_writes;
    }

    public void setNo_of_writes(String no_of_writes) {
        this.no_of_writes = no_of_writes;
    }

    public String getWrite_bytes() {
        return write_bytes;
    }

    public void setWrite_bytes(String write_bytes) {
        this.write_bytes = write_bytes;
    }

    public String getTotal_disk() {
        return total_disk;
    }

    public void setTotal_disk(String total_disk) {
        this.total_disk = total_disk;
    }

    public String getUsed_disk() {
        return used_disk;
    }

    public void setUsed_disk(String used_disk) {
        this.used_disk = used_disk;
    }

    public String getFree_disk() {
        return free_disk;
    }

    public void setFree_disk(String free_disk) {
        this.free_disk = free_disk;
    }

    public String getFile_count() {
        return file_count;
    }

    public void setFile_count(String file_count) {
        this.file_count = file_count;
    }

    public String getTotal_thread_count() {
        return total_thread_count;
    }

    public void setTotal_thread_count(String total_thread_count) {
        this.total_thread_count = total_thread_count;
    }

    public String getDaemon_thread_count() {
        return daemon_thread_count;
    }

    public void setDaemon_thread_count(String daemon_thread_count) {
        this.daemon_thread_count = daemon_thread_count;
    }

    public String getPeak_thread_count() {
        return peak_thread_count;
    }

    public void setPeak_thread_count(String peak_thread_count) {
        this.peak_thread_count = peak_thread_count;
    }

    public String getRunning_thread_count() {
        return running_thread_count;
    }

    public void setRunning_thread_count(String running_thread_count) {
        this.running_thread_count = running_thread_count;
    }

    public String getRx_bytes() {
        return rx_bytes;
    }

    public void setRx_bytes(String rx_bytes) {
        this.rx_bytes = rx_bytes;
    }

    public String getRx_dropped() {
        return rx_dropped;
    }

    public void setRx_dropped(String rx_dropped) {
        this.rx_dropped = rx_dropped;
    }

    public String getRx_error() {
        return rx_error;
    }

    public void setRx_error(String rx_error) {
        this.rx_error = rx_error;
    }

    public String getRx_frames() {
        return rx_frames;
    }

    public void setRx_frames(String rx_frames) {
        this.rx_frames = rx_frames;
    }

    public String getRx_overruns() {
        return rx_overruns;
    }

    public void setRx_overruns(String rx_overruns) {
        this.rx_overruns = rx_overruns;
    }

    public String getRx_packet() {
        return rx_packet;
    }

    public void setRx_packet(String rx_packet) {
        this.rx_packet = rx_packet;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTx_bytes() {
        return tx_bytes;
    }

    public void setTx_bytes(String tx_bytes) {
        this.tx_bytes = tx_bytes;
    }

    public String getTx_carrier() {
        return tx_carrier;
    }

    public void setTx_carrier(String tx_carrier) {
        this.tx_carrier = tx_carrier;
    }

    public String getTx_collisions() {
        return tx_collisions;
    }

    public void setTx_collisions(String tx_collisions) {
        this.tx_collisions = tx_collisions;
    }

    public String getTx_dropped() {
        return tx_dropped;
    }

    public void setTx_dropped(String tx_dropped) {
        this.tx_dropped = tx_dropped;
    }

    public String getTx_errors() {
        return tx_errors;
    }

    public void setTx_errors(String tx_errors) {
        this.tx_errors = tx_errors;
    }

    public String getTx_overruns() {
        return tx_overruns;
    }

    public void setTx_overruns(String tx_overruns) {
        this.tx_overruns = tx_overruns;
    }

    public String getTx_packets() {
        return tx_packets;
    }

    public void setTx_packets(String tx_packets) {
        this.tx_packets = tx_packets;
    }

    public String getDisk_name() {
        return disk_name;
    }

    public void setDisk_name(String disk_name) {
        this.disk_name = disk_name;
    }

    public String getNetwork_address() {
        return network_address;
    }

    public void setNetwork_address(String network_address) {
        this.network_address = network_address;
    }

    public String getNetwork_name() {
        return network_name;
    }

    public void setNetwork_name(String network_name) {
        this.network_name = network_name;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", cpu_idle='" + cpu_idle + '\'' +
                ", cpu_nice='" + cpu_nice + '\'' +
                ", cpu_user='" + cpu_user + '\'' +
                ", cpu_wait='" + cpu_wait + '\'' +
                ", committedVM='" + committedVM + '\'' +
                ", free_physical_memory='" + free_physical_memory + '\'' +
                ", ram='" + ram + '\'' +
                ", load_avg='" + load_avg + '\'' +
                ", total_swap='" + total_swap + '\'' +
                ", free_swap='" + free_swap + '\'' +
                ", used_swap='" + used_swap + '\'' +
                ", no_of_read='" + no_of_read + '\'' +
                ", read_byte='" + read_byte + '\'' +
                ", no_of_writes='" + no_of_writes + '\'' +
                ", write_bytes='" + write_bytes + '\'' +
                ", total_disk='" + total_disk + '\'' +
                ", used_disk='" + used_disk + '\'' +
                ", free_disk='" + free_disk + '\'' +
                ", file_count='" + file_count + '\'' +
                ", total_thread_count='" + total_thread_count + '\'' +
                ", daemon_thread_count='" + daemon_thread_count + '\'' +
                ", peak_thread_count='" + peak_thread_count + '\'' +
                ", running_thread_count='" + running_thread_count + '\'' +
                ", rx_bytes='" + rx_bytes + '\'' +
                ", rx_dropped='" + rx_dropped + '\'' +
                ", rx_error='" + rx_error + '\'' +
                ", rx_frames='" + rx_frames + '\'' +
                ", rx_overruns='" + rx_overruns + '\'' +
                ", rx_packet='" + rx_packet + '\'' +
                ", speed='" + speed + '\'' +
                ", tx_bytes='" + tx_bytes + '\'' +
                ", tx_carrier='" + tx_carrier + '\'' +
                ", tx_collisions='" + tx_collisions + '\'' +
                ", tx_dropped='" + tx_dropped + '\'' +
                ", tx_errors='" + tx_errors + '\'' +
                ", tx_overruns='" + tx_overruns + '\'' +
                ", tx_packets='" + tx_packets + '\'' +
                ", disk_name='" + disk_name + '\'' +
                ", network_address='" + network_address + '\'' +
                ", network_name='" + network_name + '\'' +
                '}';
    }
}
