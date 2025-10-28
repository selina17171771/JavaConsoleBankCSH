package banking;

import java.util.Scanner;

public class AccountManager implements ICustomDefine{

	private static final int MAX = 50;
	private final Account[] accounts = new Account[MAX];
	private int count = 0;
	private final Scanner sc = new Scanner(System.in);
	
	public void showMenu() {   // 메뉴출력
		System.out.println("----ㅡMenu----");
		System.out.println("1. 계좌개설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체계좌정보출력");
		System.out.println("5. 프로그램종료");
		System.out.println("선택: ");
	}       // 메뉴출력
	
	public void makeAccount() {
		if (count <= MAX) {
			System.out.println("더 이상 계좌를 추가할 수 없습니다. ");
			return;
		}
		System.out.println("\n ***신규계좌개설***");
		System.out.print("계좌번호: ");
		String accNo = sc.nextLine();
		System.out.print("고객이름: ");
		String owner = sc.nextLine();
		System.out.println("잔고: ");
		int balance = sc.nextInt();
		
		accounts[count++] = new Account(accNo, owner, balance);
	}
	
	public void depositMoney() {
		/*
		 ***입   금***
		계좌번호와 입금할 금액을 입력하세요
		계좌번호:12345
		입금액:3000
		입금이 완료되었습니다.
		 */
		System.out.println("***입   금***");
		System.out.println("계좌번호와 입금할 금액을 입력하세요");
		System.out.print("계좌번호: ");
		String accNo = sc.nextLine();
		
		int idx = findIndexByAccNo(accNo);
		if (idx < 0) {
			System.out.println("해당 계좌는 존재하지 않습니다.");
			return;
		}
		System.out.print("입금액: ");
		int amount = sc.nextInt();
		
		accounts[idx].deposit(amount);
		System.out.println("입금이 완료되었습니다.");
	}
	public void withdrawMoney() {
        System.out.println("\n[출금]");
        System.out.print("계좌번호: ");
        String accNo = sc.nextLine().trim();

        int idx = findIndexByAccNo(accNo);
        if (idx < 0) {
            // 1단계 제약: 없는 경우 별도 처리 없이 종료
            System.out.println("해당 계좌가 존재하지 않습니다.");
            return;
        }
        System.out.print("출금액(0 이상): ");
        int amount = Integer.parseInt(sc.nextLine().trim());

        boolean ok = accounts[idx].withdraw(amount);
        if (ok) System.out.println("출금이 완료되었습니다.");
        else    System.out.println("잔액이 부족하여 출금에 실패했습니다.");
    }

    public void showAccInfo() {
        System.out.println("\n[전체계좌정보]");
        if (count == 0) {
            System.out.println("등록된 계좌가 없습니다.");
            return;
        }
        for (int i = 0; i < count; i++) {
            accounts[i].showInfo();
        }
    }

    // ====== 내부 유틸 ======
    private int findIndexByAccNo(String accNo) {
        for (int i = 0; i < count; i++) {
            if (accounts[i].getAccountNo().equals(accNo)) {
                return i;
            }
        }
        return -1;
    }
}

