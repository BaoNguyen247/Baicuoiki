package service;

import javax.swing.JOptionPane;

/**
 * Class để test kết nối MongoDB
 * Có thể chạy riêng class này để kiểm tra kết nối
 */
public class TestMongoConnection {
    public static void main(String[] args) {
        System.out.println("Đang kiểm tra kết nối MongoDB...");

        boolean isConnected = MongoDBConnection.testConnection();

        if (isConnected) {
            System.out.println("Kết nối MongoDB thành công!");
            JOptionPane.showMessageDialog(null,
                    "Kết nối MongoDB thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("Không thể kết nối đến MongoDB!");
            JOptionPane.showMessageDialog(null,
                    "Không thể kết nối đến MongoDB.\nVui lòng kiểm tra xem MongoDB đã được khởi động chưa.",
                    "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
