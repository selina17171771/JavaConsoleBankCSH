package banking.jdbc;

public class BankingVO {
	private int idx;
	private String accountNo;
	private String name;
	private double balance;
	private double interestRate;
	
	public BankingVO() {
		// TODO Auto-generated constructor stub
	}
	public BankingVO(int idx, String accountNo, String name, double balance, double interestRate) {
		this.idx = idx;
		this.accountNo = accountNo;
		this.name = name;
		this.balance = balance;
		this.interestRate = interestRate;
	}
	//getter and setter
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("[%d] %s | %s | 잔액: %.2f | 이자: %.2f%%",
				idx, accountNo, name, balance, interestRate);
	}
}
