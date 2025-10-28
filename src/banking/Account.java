package banking;

/**
 * 계좌의 공통 정보 (1단계 핵심 클래스)
 * - 계좌번호(String), 이름(String), 잔액(int)
 */
public class Account {
    protected String accountNo;
    protected String owner;
    protected int balance;

    public Account(String accountNo, String owner, int balance) {
        this.accountNo = accountNo;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountNo() { return accountNo; }

    public void deposit(int amount) {
        // 1단계 제약: 금액 오류 검증 생략(0 이상만 테스트)
        balance += amount;
    }

    public boolean withdraw(int amount) {
        // 1단계 제약: 음수/잔액검사 등 추가처리 생략(0 이상만 테스트)
        if (amount > balance) return false; // 간단히 잔액부족만 체크
        balance -= amount;
        return true;
    }

    public void showInfo() {
        System.out.printf("[계좌번호] %s  [이름] %s  [잔액] %,d원%n", accountNo, owner, balance);
    }
}
