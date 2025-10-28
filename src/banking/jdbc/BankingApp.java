package banking.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BankingApp {
	public static void main(String[] args) {
		
		try (Connection con = DBConnect.getConnection(); Scanner sc = new Scanner(System.in)){
			con.setAutoCommit(false);
            BankingDAO dao = new BankingDAO(con);
            
            while(true) {
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
                        case "1": openAccount(sc, dao); con.commit(); break;
                        case "2": deposit(sc, dao);     con.commit(); break;
                        case "3": withdraw(sc, dao);    con.commit(); break;
                        case "4": listAll(dao);                       break;
                        case "5": showOne(sc, dao);                    break;
                        case "6": deleteAccount(sc, dao); con.commit(); break;
                        case "0": System.out.println("프로그램 종료"); return;
                        default:  System.out.println("메뉴를 다시 선택하세요.");
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
	// 계좌개설
    private static void openAccount(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        System.out.print("이름: ");
        String name = sc.nextLine().trim();
        System.out.print("기본이자율(%): ");
        double interest = Double.parseDouble(sc.nextLine().trim());
        System.out.print("초기입금액: ");
        double init = Double.parseDouble(sc.nextLine().trim());

        int r = dao.openAccount(acc, name, init, interest);
        System.out.println(r + "건 계좌 개설 완료");
    }

    // 입금
    private static void deposit(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        System.out.print("입금액: ");
        double amt = Double.parseDouble(sc.nextLine().trim());

        int r = dao.deposit(acc, amt);
        if (r == 0) System.out.println("입금 실패: 계좌번호를 확인하세요.");
        else System.out.println("입금 완료 (기본이자 반영)");
    }

    // 출금
    private static void withdraw(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        System.out.print("출금액: ");
        double amt = Double.parseDouble(sc.nextLine().trim());

        int r = dao.withdraw(acc, amt);
        if (r == 0) System.out.println("출금 실패: 잔액 부족 또는 계좌번호 오류");
        else System.out.println("출금 완료");
    }

    // 전체계좌출력
    private static void listAll(BankingDAO dao) throws Exception {
        List<BankingVO> list = dao.findAll();
        if (list.isEmpty()) { System.out.println("등록된 계좌가 없습니다."); return; }
        list.forEach(System.out::println);
    }

    // 선택계좌출력
    private static void showOne(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        BankingVO vo = dao.findByAccountNo(acc);
        if (vo == null) System.out.println("해당 계좌가 없습니다.");
        else System.out.println(vo);
    }

    // 계좌삭제
    private static void deleteAccount(Scanner sc, BankingDAO dao) throws Exception {
        System.out.print("계좌번호: ");
        String acc = sc.nextLine().trim();
        dao.deleteAccount(acc);
        System.out.println("삭제 요청 완료(프로시저 실행)");
    }
}
