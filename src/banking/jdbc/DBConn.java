package banking.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    // ojdbc11/ojdbc8 사용 시 드라이버 명은 동일
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    // XE 예시: @localhost:1521:XE  |  CDB/PDB 예시: @localhost:1521/ORCLPDB1
    private static final String URL  = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "hr";   // ← 본인 계정
    private static final String PASS = "1234";   // ← 본인 비밀번호

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle Driver load failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // 트랜잭션 제어는 호출부(App)에서 하도록 자동 커밋은 그대로 두거나 setAutoCommit 제어
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
