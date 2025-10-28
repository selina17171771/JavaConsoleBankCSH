package banking.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankingDAO {

	
	private final Connection con;
	
	//connect
	public BankingDAO(Connection con) {this.con = con;}
	
	//계좌개설
	//insert문으로 구현
	//preparedStatement 인터페이스 사용
	public int openAccount(String accountNo, String name, double initialBalance, double interestRate) throws SQLException {
		String sql = "insert into Banking (idx, accountNo, name, balance, interestRate) " +
					"values (seq_banking_idx.nextval, accountNo, name, initialBalance, interestRate";
		try (PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, accountNo);
			ps.setString(2, name);
			ps.setDouble(3, initialBalance);
			ps.setDouble(4, interestRate);
			return ps.executeUpdate();
			
		} 
	}	
	//입금처리
	public int deposit(String accountNo, double amount) throws SQLException {
		 String sql = "update banking " +
                 "   set balance = balance + (balance * (interestRate/100)) + ? " +
                 " where account_no = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setDouble(1, amount);
        ps.setString(2, accountNo);
        return ps.executeUpdate(); // 1: 성공, 0: 계좌 없음
    	}
	}
	//출금처리
	public int withdraw(String accountNo, double amount) throws SQLException {
		String sql = "update banking set balance = balance - ? " +
                " where account_no = ? and balance >= ?";
   try (PreparedStatement ps = con.prepareStatement(sql)) {
       ps.setDouble(1, amount);
       ps.setString(2, accountNo);
       ps.setDouble(3, amount);
       return ps.executeUpdate(); // 1: 성공, 0: 잔액 부족 또는 계좌 없음
   		}
	}
	//전체계좌출력
	public List<BankingVO> findAll() throws SQLException {
        String sql = "SELECT idx, account_no, name, balance, interestRate FROM banking ORDER BY idx";
        List<BankingVO> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) 
            	list.add(map(rs));
        	}
        return list;
	}
	
	//지정계좌정보출력(계좌정보일치)
	public BankingVO findByAccountNo(String accountNo) throws SQLException {
        String sql = "select idx, account_no, name, balance, interestRate" +
        		" from banking where account_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }
	
	//계좌삭제
	public void deleteAccount(String accountNo) throws SQLException {
		try(CallableStatement cs = con.prepareCall("{call deleteAccount(?)}")){
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
                rs.getDouble("interestRate")
        );
    }
	

}
