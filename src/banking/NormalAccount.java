package banking;

/** 보통계좌: 잔고 + (잔고*기본이자%) + 입금액 */
public class NormalAccount extends Account {
    protected int interestRate; // % 정수

    public NormalAccount(String accountNo, String owner, int balance, int interestRate) {
        super(accountNo, owner, balance);
        this.interestRate = interestRate;
    }

    @Override
    public void deposit(int amount) {
        int addBase = balance * interestRate / 100; // 소수점 버림
        balance = balance + addBase + amount;
    }

    @Override
    public void showInfoBlock() {
        System.out.println("-------------");
        System.out.printf("계좌번호>%s%n", accountNo);
        System.out.printf("고객이름>%s%n", owner);
        System.out.printf("잔고>%d%n", balance);
        System.out.printf("기본이자>%d%%%n", interestRate);
        System.out.println("-------------");
    }
}
