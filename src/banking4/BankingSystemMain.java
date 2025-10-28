package banking4;

import java.util.Scanner;

public class BankingSystemMain implements ICustomDefine {
    public static void main(String[] args) {
        AccountManager manager = new AccountManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                manager.showMenu();
                String line = sc.nextLine().trim();     // 문자를 입력하면 아래 parseInt에서 예외 발생
                int choice = Integer.parseInt(line);     // try~catch로 문자 입력 방지

                if (choice < MAKE || choice > EXIT) {    // 개발자 정의 예외(지정 정수 이외)
                    throw new MenuSelectException("메뉴는 1~6 사이의 값만 가능합니다.");
                }

                switch (choice) {
                    case MAKE    -> manager.makeAccount();
                    case DEPOSIT -> manager.depositMoney();
                    case WITHDRAW-> manager.withdrawMoney();
                    case INQUIRE -> manager.showAccInfo();
                    case DELETE -> manager.deleteAccount();
                    case EXIT    -> {
                        System.out.println("프로그램종료");
                        sc.close();
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력할 수 있습니다.");
                System.out.println(); // 보기 좋게 줄바꿈
            } catch (MenuSelectException e) {
                System.out.println(e.getMessage());
                System.out.println();
            } catch (Exception e) {
                System.out.println("오류가 발생했지만 프로그램은 계속됩니다: " + e.getMessage());
                System.out.println();
            }
        }
    }
}
