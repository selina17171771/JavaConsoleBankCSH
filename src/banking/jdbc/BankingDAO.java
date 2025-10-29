package banking.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankingDAO {
    private final Connection con;
    public BankingDAO(Connection con) { this.con = con; }

    // 1) 계좌개설
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

    // 2) 입금 (기본이자 즉시 반영)
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

    // 3) 출금 (잔액 내에서만)
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

    // 4) 전체계좌 (개설 순서: idx ASC)
    public List<BankingVO> findAll() throws SQLException {
        String sql = "SELECT idx, account_no, name, balance, interest FROM banking ORDER BY idx";
        List<BankingVO> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // 5) 지정계좌
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

    // 6) 계좌삭제 (프로시저 호출: DeleteAccount)
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
