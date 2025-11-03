package banking.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankingDAO {
    private final Connection con;
    public BankingDAO(Connection con) { this.con = con; }

    // 1) 계좌개설 /insert문으로 구현/ prepareStatement 인터페이스 사용
    public int openAccount(String accountNo, String name, double initBalance, double interest) throws SQLException {
        String sql = "INSERT INTO banking (idx, account_no, name, balance, interest) " +
                     "VALUES (seq_banking_idx.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNo);
            ps.setString(2, name);
            ps.setDouble(3, initBalance);
            ps.setDouble(4, interest);
            return ps.executeUpdate(); // 1 기대
        }
    }

    // 2) 입금 / update 문 구현 / 입금시에는 이자율 계산 (잔액+(잔액*기본이자)+입금액)
    public int deposit(String accountNo, double amount) throws SQLException {
        String sql = "UPDATE banking " +
                     "   SET balance = balance + (balance * (interest/100)) + ? " +
                     " WHERE account_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, accountNo);
            return ps.executeUpdate(); // 1: 성공, 0: 계좌 없음
        }
    }

    // 3) 출금 / 잔액내에서 가능 잔액보다 큰금액 불가
    public int withdraw(String accountNo, double amount) throws SQLException {
        String sql = "UPDATE banking SET balance = balance - ? " +
                     " WHERE account_no = ? AND balance >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, accountNo);
            ps.setDouble(3, amount);
            return ps.executeUpdate(); // 1: 성공, 0: 부족/계좌없음
        }
    }

    // 4) 전체계좌 / 개설된 순서대로 출력
    public List<BankingVO> findAll() throws SQLException {
        String sql = "SELECT idx, account_no, name, balance, interest FROM banking ORDER BY idx";
        List<BankingVO> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // 5) 지정계좌 / 계좌번호와 일치하는 계좌정보 출력
    public BankingVO findByAccountNo(String accountNo) throws SQLException {
        String sql = "SELECT idx, account_no, name, balance, interest FROM banking WHERE account_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    // 6) 계좌삭제 (프로시저 호출: DeleteAccount) / 프로시저로 작성후 호출하는 방식 
    public void deleteAccount(String accountNo) throws SQLException {
        try (CallableStatement cs = con.prepareCall("{call DeleteAccount(?)}")) {
            cs.setString(1, accountNo);
            cs.execute();
        }
    }

    private BankingVO map(ResultSet rs) throws SQLException {
        return new BankingVO(
                rs.getInt("idx"),
                rs.getString("account_no"),
                rs.getString("name"),
                rs.getDouble("balance"),
                rs.getDouble("interest")
        );
    }
}
