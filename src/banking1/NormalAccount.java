package banking1;

/**
 * 보통예금계좌 (이율 정보 포함) - 2단계에서 사용 예정
 */
public class NormalAccount extends Account {
    protected int interestRate; // % 단위 (예: 3)

    public NormalAccount(String accountNo, String owner, int balance, int interestRate) {
        super(accountNo, owner, balance);
        this.interestRate = interestRate;
    }
}
