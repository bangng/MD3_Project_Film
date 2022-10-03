package rikkei.academy.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectMySQL {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/loginForm";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("thanh cong");


        } catch (ClassNotFoundException e) {
            System.out.println("that bai 1");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("that bai 2");
            throw new RuntimeException(e);
        }
        return connection;
    }
}
