package banking.threeby3;
/*
짝수번째 입금에는 500원씩 축하금을 별도로 지급한다. 
절차는 다음과 같다.
계좌계설(입금으로 간주하지 않는다.)
입금1회차(홀수번째이므로 NormalAccount와 동일하다.)
잔고 + (잔고 * 기본이자) + 입금액 
입금2회차(짝수번째는 축하금이 지급된다.)
잔고 + (잔고 * 기본이자) + 입금액 + 500원 
따라서 계좌정보를 출력할때 현재 몇회차 입금이 되었는지가 디스플레이 되어야 한다. 
*/

public class specialAccount extends NormalAccount {
	//직렬화
	private static final long serialVersionUID = 1L;

	
	private int depositCount = 0;
	
	public specialAccount(String accountNo, String owner, int balance, int interestRate) {
		// TODO Auto-generated constructor stub
		super(accountNo, owner, balance, interestRate);
	}
	
	public int getDepositCount() {
		return depositCount;
	}
	
	public void deposit(int amount) {
		depositCount++;
		
		int addBase = balance * interestRate / 100;
		balance = balance + addBase + amount;
		
		//짝수번째 입금하면 +500 축하금
		if (depositCount % 2 == 0) {
			balance += 500;
			System.out.printf("%d 번째 입금 축하금 500원 지급", depositCount);
		}
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
        sb.append(String.format("입금회차>%d회%n", depositCount));
        sb.append("-------------\n");
        return sb.toString();
	}
}
