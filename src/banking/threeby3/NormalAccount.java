package banking.threeby3;

public class NormalAccount extends Account {
    private static final long serialVersionUID = 1L;

    protected int interestRate; // % 정수

    public NormalAccount(String accountNo, String owner, int balance, int interestRate) {
        super(accountNo, owner, balance);
        this.interestRate = interestRate;
    }

    public int getInterestRate() { return interestRate; }

    @Override
    public void deposit(int amount) {
        int addBase = balance * interestRate / 100; // 소수점 버림
        balance = balance + addBase + amount;
    }

    @Override
    public String infoBlock() {
    	//콘솔 출력 대신 String 으로 내용을 반환
        StringBuilder sb = new StringBuilder();
        sb.append("-------------\n");
        sb.append(String.format("계좌번호>%s%n", accountNo));
        sb.append(String.format("고객이름>%s%n", owner));
        sb.append(String.format("잔고>%d%n", balance));
        sb.append(String.format("기본이자>%d%%%n", interestRate));
        sb.append("-------------\n");
        return sb.toString();
    }
}
