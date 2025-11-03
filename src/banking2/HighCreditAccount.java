package banking2;

/** 신용계좌: 잔고 + (잔고*기본이자%) + (잔고*추가이자%) + 입금액 */
public class HighCreditAccount extends NormalAccount {
    protected int creditRate; // ICustomDefine.A/B/C

    public HighCreditAccount(String accountNo, String owner, int balance, int interestRate, int creditRate) {
        super(accountNo, owner, balance, interestRate);
        this.creditRate = creditRate;
    }

    @Override
    public void deposit(int amount) {
        int addBase  = balance * interestRate / 100;
        int addExtra = balance * creditRate   / 100;
        balance = balance + addBase + addExtra + amount;
    }

    @Override
    public void showInfoBlock() {
        System.out.println("-------------");
        System.out.printf("계좌번호>%s%n", accountNo);
        System.out.printf("고객이름>%s%n", owner);
        System.out.printf("잔고>%d%n", balance);
        System.out.printf("기본이자>%d%%%n", interestRate);
        System.out.printf("신용등급>%s%n", gradeText());
        System.out.println("-------------");
    }

    private String gradeText() {
    	 if (creditRate == ICustomDefine.A) return "A";
    	 if (creditRate == ICustomDefine.B) return "B";
    	 return "C";
    }
}
