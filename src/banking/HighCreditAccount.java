package banking;

/**
 * 고신용 예금계좌 (기본 이율 + 신용등급 가산) - 2단계에서 사용 예정
 */
public class HighCreditAccount extends NormalAccount {
    protected int creditRate; // ICustomDefine.A/B/C 중 하나

    public HighCreditAccount(String accountNo, String owner, int balance, int interestRate, int creditRate) {
        super(accountNo, owner, balance, interestRate);
        this.creditRate = creditRate;
    }
}
