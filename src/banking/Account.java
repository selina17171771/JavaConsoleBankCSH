package banking;

public abstract class Account {
    protected String accountNo;
    protected String owner;
    protected int balance;

    public Account(String accountNo, String owner, int balance) {
        this.accountNo = accountNo;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountNo() { return accountNo; }
    public String getOwner() { return owner; }
    public int getBalance() { return balance; }

    /** 입금(이자 계산)은 하위 클래스에서 구현 */
    public abstract void deposit(int amount);

    /** 출금(이자 없음) */
    public boolean withdraw(int amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    /** 계좌정보출력(포맷 고정) - 하위에서 구체값 채움 */
    public abstract void showInfoBlock();
}
