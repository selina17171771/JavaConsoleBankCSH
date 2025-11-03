package banking.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BankingApp {
    public static void main(String[] args) {
        try (Connection con = DBConn.getConnection();
        	Scanner sc = new Scanner(System.in)) {
            con.setAutoCommit(false); // 트랜잭션 직접 제어
            BankingDAO dao = new BankingDAO(con);

            while (true) {
                System.out.println("\n=== JDBC 계좌관리 (banking.jdbc) ===");
                System.out.println("1. 계좌개설");
                System.out.println("2. 입금");
                System.out.println("3. 출금");
                System.out.println("4. 전체계좌정보출력");
                System.out.println("5. 지정계좌정보출력");
                System.out.println("6. 계좌삭제(프로시저)");
                System.out.println("0. 종료");
                System.out.print("선택: ");

                String sel = sc.nextLine().trim();
                try {
                    switch (sel) {
                        case "1" -> { openAccount(sc, dao); con.commit(); }
                        case "2" -> { deposit(sc, dao);     con.commit(); }
                        case "3" -> { withdraw(sc, dao);    con.commit(); }
                        case "4" -> listAll(dao);
                        case "5" -> showOne(sc, dao);
                        case "6" -> { deleteAccount(sc, dao); con.commit(); }
                        case "0" -> { System.out.println("프로그램 종료"); return; }
                        default -> System.out.println("메뉴를 다시 선택하세요.");
                    }
                } catch (Exception e) {
                    System.out.println("오류: " + e.getMessage());
                    try { con.rollback(); } catch (SQLException ignore) {}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1) 계좌개설
    private static void openAccount(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        System.out.print("이름: ");
        String name = sc.nextLine().trim();
        System.out.print("초기입금액: ");
        double init = Double.parseDouble(sc.nextLine().trim());
        System.out.print("이자율(%): ");
        double rate = Double.parseDouble(sc.nextLine().trim());

        int r = dao.openAccount(acc, name, init, rate);
        System.out.println(r == 1 ? "계좌 개설 완료" : "계좌 개설 실패 (계좌번호 중복 등)");
    }

    // 2) 입금 (기본이자 반영)
    private static void deposit(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        System.out.print("입금액: ");
        double amt = Double.parseDouble(sc.nextLine().trim());

        int r = dao.deposit(acc, amt);
        System.out.println(r == 1 ? "입금 완료 (기본이자 반영)" : "입금 실패: 계좌번호 확인");
    }

    // 3) 출금 (잔액 내)
    private static void withdraw(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        System.out.print("출금액: ");
        double amt = Double.parseDouble(sc.nextLine().trim());

        int r = dao.withdraw(acc, amt);
        System.out.println(r == 1 ? "출금 완료" : "출금 실패(잔액 부족 또는 계좌번호 오류)");
    }

    // 4) 전체계좌정보출력 (개설 순서)
    private static void listAll(BankingDAO dao) throws Exception {
        List<BankingVO> list = dao.findAll();
        if (list.isEmpty()) System.out.println("등록된 계좌가 없습니다.");
        else list.forEach(System.out::println);
    }

    // 5) 지정계좌정보출력
    private static void showOne(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        BankingVO vo = dao.findByAccountNo(acc);
        System.out.println(vo == null ? "해당 계좌가 없습니다." : vo);
    }

    // 6) 계좌삭제 (프로시저 호출)
    private static void deleteAccount(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("삭제할 계좌번호: ");
        String acc = sc.nextLine().trim();
        dao.deleteAccount(acc);
        System.out.println("삭제 요청 완료(프로시저 DeleteAccount 실행)");
    }
}
