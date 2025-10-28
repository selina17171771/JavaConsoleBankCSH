package banking1;

import java.util.Scanner;

/**
 * 컨트롤러 클래스: 계좌 배열 관리 및 기능 구현
 * - 최대 50개까지만 저장 (차후 변경 가능)
 */
public class AccountManager implements ICustomDefine {

    private static final int MAX = 50;
    private final Account[] accounts = new Account[MAX];
    private int count = 0;
    private final Scanner sc = new Scanner(System.in);

    public void showMenu() {
        System.out.println("\n==== Bank Menu ====");
        System.out.println("1. 계좌개설");
        System.out.println("2. 입   금");
        System.out.println("3. 출   금");
        System.out.println("4. 전체계좌정보출력");
        System.out.println("5. 프로그램종료");
        System.out.print("선택: ");
    }

    public void makeAccount() {
        if (count >= MAX) {
            System.out.println("더 이상 계좌를 추가할 수 없습니다.");
            return;
        }
        System.out.println("\n[계좌개설]");
        System.out.print("계좌번호(숫자만): ");
        String accNo = sc.nextLine().trim();

        System.out.print("이름: ");
        String owner = sc.nextLine().trim();

        System.out.print("초기입금액(0 이상): ");
        int balance = Integer.parseInt(sc.nextLine().trim());

        // 1단계 제약: 중복 계좌번호 검사 생략
        accounts[count++] = new Account(accNo, owner, balance);
        System.out.println("계좌개설이 완료되었습니다.");
    }

    public void depositMoney() {
        System.out.println("\n[입금]");
        System.out.print("계좌번호: ");
        String accNo = sc.nextLine().trim();

        int idx = findIndexByAccNo(accNo);
        if (idx < 0) {
            // 1단계 제약: 없는 경우 별도 처리 없이 종료
            System.out.println("해당 계좌가 존재하지 않습니다.");
            return;
        }
        System.out.print("입금액(0 이상): ");
        int amount = Integer.parseInt(sc.nextLine().trim());

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
