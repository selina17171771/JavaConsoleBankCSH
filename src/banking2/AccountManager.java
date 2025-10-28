package banking2;

import java.util.Scanner;

public class AccountManager implements ICustomDefine {
    private static final int MAX = 50;
    private final Account[] accounts = new Account[MAX];
    private int count = 0;

    private final Scanner sc = new Scanner(System.in);

    /* ===== 메뉴 출력: 예시 포맷 그대로 ===== */
    public void showMenu() {
        System.out.println("-----Menu------");
        System.out.println("1.계좌개설");
        System.out.println("2.입\t금");
        System.out.println("3.출\t금");
        System.out.println("4.계좌정보출력");
        System.out.println("5.프로그램종료");
        System.out.print("선택:");
    }

    /* ===== 계좌개설 ===== */
    public void makeAccount() {
        if (count >= MAX) {
            System.out.println("더 이상 계좌를 추가할 수 없습니다.");
            return;
        }
        System.out.println("***신규계좌개설***");
        System.out.println("-----계좌선택------");
        System.out.println("1.보통계좌");
        System.out.println("2.신용신뢰계좌");
        System.out.print("선택:");
        int type = parseIntLine();

        System.out.print("계좌번호: ");
        String accNo = readNonEmpty();

        System.out.print("고객이름: ");
        String owner = readNonEmpty();

        System.out.print("잔고: ");
        int balance = parseIntLine();

        System.out.print("기본이자%(정수형태로입력): ");
        int baseRate = parseIntLine();

        Account acc;
        if (type == 1) {
            acc = new NormalAccount(accNo, owner, balance, baseRate);
        } else if (type == 2) {
            System.out.print("신용등급(A,B,C등급): ");
            String grade = readNonEmpty().toUpperCase();
            int extra = switch (grade) {
                case "A" -> A;
                case "B" -> B;
                case "C" -> C;
                default   -> {
                    System.out.println("등급이 올바르지 않아 C 등급으로 처리합니다.");
                    yield C;
                }
            };
            acc = new HighCreditAccount(accNo, owner, balance, baseRate, extra);
        } else {
            System.out.println("계좌 종류 선택이 올바르지 않습니다.");
            return;
        }

        accounts[count++] = acc; // 중복검사 없음(요구사항)
        System.out.println("계좌계설이 완료되었습니다.");
        System.out.println();
    }

    /* ===== 입금 ===== */
    public void depositMoney() {
        System.out.println("***입   금***");
        System.out.println("계좌번호와 입금할 금액을 입력하세요");
        System.out.print("계좌번호:");
        String accNo = readNonEmpty();

        int idx = findIndexByAccNo(accNo);
        if (idx < 0) {
            System.out.println("해당 계좌가 존재하지 않습니다.");
            System.out.println();
            return;
        }

        System.out.print("입금액:");
        int amount = parseIntLine();

        accounts[idx].deposit(amount); // 타입별(보통/신용) 이자 규칙 적용
        System.out.println("입금이 완료되었습니다.");
        System.out.println();
    }

    /* ===== 출금 ===== */
    public void withdrawMoney() {
        System.out.println("***출   금***");
        System.out.println("계좌번호와 출금할 금액을 입력하세요");
        System.out.print("계좌번호:");
        String accNo = readNonEmpty();

        int idx = findIndexByAccNo(accNo);
        if (idx < 0) {
            System.out.println("해당 계좌가 존재하지 않습니다.");
            System.out.println();
            return;
        }

        System.out.print("출금액:");
        int amount = parseIntLine();

        if (accounts[idx].withdraw(amount)) {
            System.out.println("출금이 완료되었습니다.");
        } else {
            System.out.println("잔액이 부족하여 출금에 실패했습니다.");
        }
        System.out.println();
    }

    /* ===== 계좌정보출력 ===== */
    public void showAccInfo() {
        System.out.println("***계좌정보출력***");
        if (count == 0) {
            System.out.println("등록된 계좌가 없습니다.");
            System.out.println("전체계좌정보 출력이 완료되었습니다.");
            System.out.println();
            return;
        }
        for (int i = 0; i < count; i++) {
            accounts[i].showInfoBlock(); // 예시 포맷 블록
        }
        System.out.println("전체계좌정보 출력이 완료되었습니다.");
        System.out.println();
    }

    /* ===== 내부 유틸 ===== */
    private int findIndexByAccNo(String accNo) {
        for (int i = 0; i < count; i++) {
            if (accounts[i].getAccountNo().equals(accNo)) return i;
        }
        return -1;
    }

    private String readNonEmpty() {
        while (true) {
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("다시 입력: ");
        }
    }

    private int parseIntLine() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("정수를 입력하세요: ");
            }
        }
    }
}
