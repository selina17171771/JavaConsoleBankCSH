package banking.jdbc;
/*
 connection "DB와의 세션을 대표하는 핸들"
  : 네트워크 연결(소켓), 사용자인증정보, 트랜잭션상태, 세션 설정등을 내부에 같고 있음
  - preparedStatement, CallableStatement, ResultSet 같은 것들을 만들고 다룸.
 */
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
