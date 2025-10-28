package banking2;

import java.util.Scanner;

/**
 * 프로그램 시작점 (main 포함)
 * - 사용자가 종료(5)할 때까지 반복
 */
public class BankingSystemMain implements ICustomDefine {

    public static void main(String[] args) {
        AccountManager manager = new AccountManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                manager.showMenu();
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                int choice = Integer.parseInt(line);

                // 메뉴 유효성 체크 (1~5)
                if (choice < MAKE || choice > EXIT) {
                    throw new MenuSelectException("메뉴는 1~5 사이의 값만 가능합니다.");
                }

                switch (choice) {
                    case MAKE    -> manager.makeAccount();
                    case DEPOSIT -> manager.depositMoney();
                    case WITHDRAW-> manager.withdrawMoney();
                    case INQUIRE -> manager.showAccInfo();
                    case EXIT    -> {
                        System.out.println("프로그램을 종료합니다.");
                        sc.close();
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            } catch (MenuSelectException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                // 예기치 못한 오류도 종료되지 않게 보호
                System.out.println("오류가 발생했지만 프로그램은 계속됩니다: " + e.getMessage());
            }
        }
    }
}
