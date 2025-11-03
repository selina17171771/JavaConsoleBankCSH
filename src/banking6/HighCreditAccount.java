package banking6;

public class HighCreditAccount extends NormalAccount {
    private static final long serialVersionUID = 1L;

    protected int creditRate; // ICustomDefine.A/B/C

    public HighCreditAccount(String accountNo, String owner, int balance, int interestRate, int creditRate) {
        super(accountNo, owner, balance, interestRate);
        this.creditRate = creditRate;
    }

    public int getCreditRate() { return creditRate; }

    private String gradeText() {
        if (creditRate == ICustomDefine.A) return "A";
        if (creditRate == ICustomDefine.B) return "B";
        return "C";
    }

    @Override
    public void deposit(int amount) {
        int addBase  = balance * interestRate / 100;
        int addExtra = balance * creditRate   / 100;
        balance = balance + addBase + addExtra + amount;
    }

    @Override
    public String infoBlock() {
    	//콘솔 출력 대신 String으로 내용을 반환
        StringBuilder sb = new StringBuilder(); //문자열변환
        sb.append("-------------\n");
        sb.append(String.format("계좌번호>%s%n", accountNo));
        sb.append(String.format("고객이름>%s%n", owner));
        sb.append(String.format("잔고>%d%n", balance));
        sb.append(String.format("기본이자>%d%%%n", interestRate));
        sb.append(String.format("신용등급>%s%n", gradeText()));
        sb.append("-------------\n");
        return sb.toString();
    }
}
