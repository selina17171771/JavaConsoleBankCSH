package banking4;

import java.util.Objects;

/* 추상: 상속 전용, 직접 인스턴스화 불가 */
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

    /* 입금(이자 계산)은 하위 클래스에서 구현 */
    public abstract void deposit(int amount);

    /* 출금(이자 없음) */
    public boolean withdraw(int amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    /* 포맷 고정 출력 */
    public abstract void showInfoBlock();

    /* ---------------------------
       Set 중복판정: 계좌번호만 기준
       --------------------------- */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account acc)) return false;
        return Objects.equals(this.accountNo, acc.accountNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNo);
    }
}
