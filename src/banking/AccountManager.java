package banking;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AccountManager implements ICustomDefine {

    /* 저장소: 배열 -> HashSet */
    private final Set<Account> accounts = new HashSet<>();

    private final Scanner sc = new Scanner(System.in);

    /* ===== 메뉴 출력(스타일 유지, 삭제 메뉴 추가) ===== */
    public void showMenu() {
        System.out.println("-----Menu------");
        System.out.println("1.계좌개설");
        System.out.println("2.입\t금");
        System.out.println("3.출\t금");
        System.out.println("4.계좌정보출력");
        System.out.println("5.계좌정보삭제");
        System.out.println("6.프로그램종료");
        System.out.print("선택:");
    }

    /* ===== 계좌개설: Set 중복 처리(equals/hashCode) ===== */
    public void makeAccount() {
        System.out.println("***신규계좌개설***");
        System.out.println("-----계좌선택------");
        System.out.println("1.보통계좌");
        System.out.println("2.신용신뢰계좌");
        System.out.print("선택:");
        int type = parseIntLine(); // 문자 입력시 재입력

        System.out.print("계좌번호: ");
        String accNo = readNonEmpty();

        System.out.print("고객이름: ");
        String owner = readNonEmpty();

        System.out.print("잔고: ");
        int balance = parseIntLine();

        System.out.print("기본이자%(정수형태로입력): ");
        int baseRate = parseIntLine();

        Account newAcc;
        if (type == 1) {
            newAcc = new NormalAccount(accNo, owner, balance, baseRate);
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
            newAcc = new HighCreditAccount(accNo, owner, balance, baseRate, extra);
        } else {
            System.out.println("계좌 종류 선택이 올바르지 않습니다.");
            System.out.println();
            return;
        }

        // HashSet으로 중복 판정 (equals/hashCode: accountNo만 비교)
        if (!accounts.add(newAcc)) {
            // add 실패 == 중복 존재
            System.out.println("중복계좌발견됨. 덮어쓸까요?(y or n)");
            System.out.print("선택:");
            if (readYesNoYN()) {
                // 기존 삭제 후 신규 삽입 (dummy equals로 remove 가능)
                accounts.remove(newAcc);
                accounts.add(newAcc);
                System.out.println("계좌가 덮어쓰기 되었습니다.");
            } else {
                System.out.println("기존 계좌를 유지합니다. 신규 입력은 무시됩니다.");
            }
        } else {
            System.out.println("계좌계설이 완료되었습니다.");
        }
        System.out.println();
    }

    /* ===== 입금 (규칙 유지: 500원 단위, 음수 금지, 문자 방지) ===== */
    public void depositMoney() {
        System.out.println("***입   금***");
        System.out.println("계좌번호와 입금할 금액을 입력하세요");
        System.out.print("계좌번호:");
        String accNo = readNonEmpty();

        Account acc = findByAccNo(accNo);
        if (acc == null) {
            System.out.println("해당 계좌가 존재하지 않습니다.");
            System.out.println();
            return;
        }

        System.out.print("입금액:");
        int amount = parseIntLine();
        if (amount < 0) {
            System.out.println("음수를 입금할 수 없습니다.");
            System.out.println();
            return;
        }
        if (amount % 500 != 0) {
            System.out.println("입금액은 500원 단위로만 가능합니다.");
            System.out.println();
            return;
        }

        acc.deposit(amount); // 타입별 이자 규칙 적용(입금시에만)
        System.out.println("입금이 완료되었습니다.");
        System.out.println();
    }

    /* ===== 출금 (규칙 유지: 1000원 단위, 음수 금지, 잔고부족 Y/N) ===== */
    public void withdrawMoney() {
        System.out.println("***출   금***");
        System.out.println("계좌번호와 출금할 금액을 입력하세요");
        System.out.print("계좌번호:");
        String accNo = readNonEmpty();

        Account acc = findByAccNo(accNo);
        if (acc == null) {
            System.out.println("해당 계좌가 존재하지 않습니다.");
            System.out.println();
            return;
        }

        System.out.print("출금액:");
        int amount = parseIntLine();
        if (amount < 0) {
            System.out.println("음수를 출금할 수 없습니다.");
            System.out.println();
            return;
        }
        if (amount % 1000 != 0) {
            System.out.println("출금은 1000원 단위로만 가능합니다.");
            System.out.println();
            return;
        }

        int balance = acc.getBalance();
        if (amount > balance) {
            System.out.println("잔고가 부족합니다. 금액전체를 출금할까요?");
            System.out.print("YES/NO: ");
            if (readYesNoFULL()) {
                acc.withdraw(balance); // 전액 출금 (단위 예외)
                System.out.println("잔액 전액이 출금되었습니다.");
                System.out.println();
                return;
            } else {
                System.out.println("출금요청취소");
                System.out.println();
                return;
            }
        }

        if (acc.withdraw(amount)) {
            System.out.println("출금이 완료되었습니다.");
        } else {
            System.out.println("잔액이 부족하여 출금에 실패했습니다.");
        }
        System.out.println();
    }

    /* ===== 계좌정보출력 ===== */
    public void showAccInfo() {
        System.out.println("***계좌정보출력***");
        if (accounts.isEmpty()) {
            System.out.println("등록된 계좌가 없습니다.");
            System.out.println("전체계좌정보 출력이 완료되었습니다.");
            System.out.println();
            return;
        }
        for (Account a : accounts) {
            a.showInfoBlock();
        }
        System.out.println("전체계좌정보 출력이 완료되었습니다.");
        System.out.println();
    }

    /* ===== 계좌정보삭제 (4단계 추가) ===== */
    public void deleteAccount() {
        System.out.println("***계좌정보삭제***");
        System.out.print("삭제할 계좌번호:");
        String accNo = readNonEmpty();

        // equals/hashCode가 계좌번호만 비교하므로 임시 NormalAccount로 remove 가능
        Account dummy = new NormalAccount(accNo, "", 0, 0);
        if (accounts.remove(dummy)) {
            System.out.println("계좌가 삭제되었습니다.");
        } else {
            System.out.println("해당 계좌가 존재하지 않습니다.");
        }
        System.out.println();
    }

    /* ===== 내부 유틸: Set에서 계좌번호로 탐색(일반 기능은 반복 허용) ===== */
    private Account findByAccNo(String accNo) {
        // 일반 기능(입출금/조회)을 위한 탐색은 반복문 사용 가능
        for (Account a : accounts) {
            if (a.getAccountNo().equals(accNo)) return a;
        }
        return null;
    }

    private String readNonEmpty() {
        while (true) {
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("다시 입력: ");
        }
    }

    /** 정수만 허용. 문자 입력 시 재입력 유도 */
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

    /** 'y'/'n' 전용(덮어쓰기 확인) */
    private boolean readYesNoYN() {
        while (true) {
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y")) return true;
            if (s.equals("n")) return false;
            System.out.print("y 또는 n 을 입력하세요: ");
        }
    }

    /** YES/NO 전용(전액 출금 확인) */
    private boolean readYesNoFULL() {
        while (true) {
            String s = sc.nextLine().trim().toUpperCase();
            if (s.equals("YES") || s.equals("Y")) return true;
            if (s.equals("NO")  || s.equals("N")) return false;
            System.out.print("YES 또는 NO를 입력하세요: ");
        }
    }
}
