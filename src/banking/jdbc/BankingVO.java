package banking.jdbc;

public class BankingVO {
    private int idx;
    private String accountNo;
    private String name;
    private double balance;
    private double interest; // % (예: 2.5)

    public BankingVO() {}

    public BankingVO(int idx, String accountNo, String name, double balance, double interest) {
        this.idx = idx;
        this.accountNo = accountNo;
        this.name = name;
        this.balance = balance;
        this.interest = interest;
    }

    public int getIdx() { return idx; }
    public void setIdx(int idx) { this.idx = idx; }

    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public double getInterest() { return interest; }
    public void setInterest(double interest) { this.interest = interest; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | 잔액: %,.2f | 이자율: %.2f%%",
                idx, accountNo, name, balance, interest);
    }
}
