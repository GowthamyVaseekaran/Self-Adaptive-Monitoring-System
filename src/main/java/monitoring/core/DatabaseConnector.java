package monitoring.core;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Component
public class DatabaseConnector {
    public Connection connect() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        String url = "jdbc:mysql://localhost:3306/monitoringsystem?useSSL=false&allowPublicKeyRetrieval=true";
        String userName = "root";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = null;
        try {

            conn = DriverManager.getConnection(url, userName, "root");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public DatabaseConnector() {


    }

    public void insertDb(String cpu , String memory,String cpu_idle, String cpu_nice, String cpu_user, String cpu_wait, String committedVM, String free_physical_memory, String ram, String load_avg, String total_swap, String free_swap, String used_swap, String no_of_read, String read_byte, String no_of_writes, String write_bytes, String total_disk, String used_disk, String free_disk, String file_count, String total_thread_count, String daemon_thread_count, String peak_thread_count, String running_thread_count, String rx_bytes, String rx_dropped, String rx_error, String rx_frames, String rx_overruns, String rx_packet, String speed, String tx_bytes, String tx_carrier, String tx_collisions, String tx_dropped, String tx_errors, String tx_overruns, String tx_packets, String disk_name, String network_address, String network_name) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        String sql = "INSERT INTO metrics(cpu ,memory,cpu_idle,cpu_nice,cpu_user,cpu_wait,committedVM,free_physical_memory,ram,load_avg,total_swap,free_swap,used_swap,no_of_read,read_byte,no_of_writes,write_bytes,total_disk,used_disk,free_disk,file_count,total_thread_count,daemon_thread_count,peak_thread_count,running_thread_count,rx_bytes,rx_dropped,rx_error,rx_frames,rx_overruns, rx_packet, speed,tx_bytes,tx_carrier,tx_collisions, tx_dropped,tx_errors,tx_overruns,tx_packets,disk_name,network_address,network_name) VALUES(? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpu);
            pstmt.setString(2, memory);
            pstmt.setString(3, cpu_idle);
            pstmt.setString(4, cpu_nice);
            pstmt.setString(5, cpu_user);
            pstmt.setString(6, cpu_wait);
            pstmt.setString(7, committedVM);
            pstmt.setString(8, free_physical_memory);
            pstmt.setString(9, ram);
            pstmt.setString(10, load_avg);
            pstmt.setString(11, total_swap);
            pstmt.setString(12, free_swap);
            pstmt.setString(13, used_swap);
            pstmt.setString(14, no_of_read);
            pstmt.setString(15, read_byte);
            pstmt.setString(16, no_of_writes);
            pstmt.setString(17, write_bytes);
            pstmt.setString(18, total_disk);
            pstmt.setString(19, used_disk);
            pstmt.setString(20, free_disk);
            pstmt.setString(21, file_count);
            pstmt.setString(22, total_thread_count);
            pstmt.setString(23, daemon_thread_count);
            pstmt.setString(24, peak_thread_count);
            pstmt.setString(25, running_thread_count);
            pstmt.setString(26, rx_bytes);
            pstmt.setString(27, rx_dropped);
            pstmt.setString(28, rx_error);
            pstmt.setString(29, rx_frames);
            pstmt.setString(30, rx_overruns);
            pstmt.setString(31, rx_packet);
            pstmt.setString(32, speed);
            pstmt.setString(33, tx_bytes);
            pstmt.setString(34, tx_carrier);
            pstmt.setString(35, tx_collisions);
            pstmt.setString(36, tx_dropped);
            pstmt.setString(37, tx_errors);
            pstmt.setString(38, tx_overruns);
            pstmt.setString(39, tx_packets);
            pstmt.setString(40, disk_name);
            pstmt.setString(41, network_address);
            pstmt.setString(42, network_name);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//public void insertDb(String cpu , String memory) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
//    String sql = "INSERT INTO Metrics(cpu ,memory) VALUES(?,?)";
//
//    try (Connection conn = this.connect();
//         PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//        pstmt.setString(1, cpu);
//        pstmt.setString(2, memory);
//
//
//
//        pstmt.executeUpdate();
//    } catch (SQLException e) {
//        System.out.println(e.getMessage());
//    }
//}

}