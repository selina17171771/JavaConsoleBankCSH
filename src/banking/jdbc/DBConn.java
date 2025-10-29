package banking.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL  = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "hr";   
    private static final String PASS = "1234";  

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle Driver load failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
