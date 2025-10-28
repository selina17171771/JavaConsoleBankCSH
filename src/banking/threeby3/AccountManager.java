package banking.threeby3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

//obj 시작시 자동로드 + 종료시 저장

public class AccountManager implements ICustomDefine {

    /* 저장소: 배열 -> HashSet */
	// 삽입순서를 유지하는 LinkedHashSet 사용
    private final Set<Account> accounts = new LinkedHashSet<>();
    private final Scanner sc = new Scanner(System.in);
    
    //파일명
    private static final String FILE_OBJ = "src/banking6/AccountInfo.obj";
    private static final String FILE_TEXT = "src/banking6/AutoSaveAccount.txt";
    
    private AutoSaver autoSaver;
    
    public AccountManager() {
    	loadFromFile();
    }

    /* ===== 메뉴 출력(스타일 유지, 삭제 메뉴 추가) ===== */
    public void showMenu() {
        System.out.println("-----Menu------");
        System.out.println("1.계좌개설");
        System.out.println("2.입\t금");
        System.out.println("3.출\t금");
        System.out.println("4.계좌정보출력");
        System.out.println("5.계좌정보삭제");
        System.out.println("6.저장옵션");
        System.out.println("7.프로그램종료");
        System.out.println("8. 3X3 게임 실행");
        System.out.print("선택:");
    }
    
    public void showSaveOptionMenu() {
    	System.out.println("-----저장옵션-----");
    	System.out.println("1. 자동저장On");
    	System.out.println("2. 자동저장Off");
    	System.out.println("선택: ");
    }
    
    public void startAutoSave() {
    	if (autoSaver != null && autoSaver.isAlive()) {
    		System.out.println("이미 자동저장이 실행중입니다.");
    		System.out.println();
    		return;
    	}
    	autoSaver = new AutoSaver(this);
    	autoSaver.start();
    	System.out.println("자동저장을 시작했습니다.");
    	System.out.println();
    }
    
    public void stopAutoSave() {
    	if (autoSaver == null && !autoSaver.isAlive()) {
    		System.out.println("자동저장이 실행중이 아닙니다.");
    		System.out.println();
    		return;
    	}

    	autoSaver.interrupt(); // 강제종료 X, 깨우는 신호
    	try {
    		autoSaver.join(1000);
    	}catch (InterruptedException ignored) {}
    	System.out.println("자동저장을 중지했습니다.");
    	System.out.println();
    }
    
    public void autoSaveToText() {
    	Account[] snapshot;
    	synchronized (accounts) {
			snapshot = accounts.toArray(new Account[0]);
		}
    	
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_TEXT, false))) {
    		for (Account a : snapshot) {
                bw.write(a.infoBlock()); // 요청한 출력 포맷 그대로
            }
            bw.flush();
        } catch (IOException e) {
            // 자동저장 실패 시 조용히 넘어가거나 로깅
        }
    	
    }

    /* ===== 파일 저장 ===== */
    public void saveToFile() {
    	/*
    	 저장(직렬화)
    	 객체 -> 바이트 -> 파일
    	 */
    	try (ObjectOutputStream oos = new ObjectOutputStream(
    			new BufferedOutputStream(new FileOutputStream(FILE_OBJ)))){
    		oos.writeObject(accounts);
    		oos.flush();
    		System.out.println("계좌 정보가 저장되었습니다. ");
    	}catch (IOException e) {
    		System.out.println("저장 중 오류 : " + e.getMessage());
    	}
    }
   
    /* ===== 파일 로드 ===== */
    public void loadFromFile() {
    	File f = new File(FILE_OBJ);
    	if (!f.exists()) {
    		System.out.println(FILE_OBJ + " 파일없음");
    		return;
    	}
    	/*
   	 	읽기 (역직렬화)
   	 	바이트 -> 객체복원
    	 */
    	 try (ObjectInputStream ois = new ObjectInputStream(
    	            new BufferedInputStream(new FileInputStream(f)))) {
    	        Object obj = ois.readObject(); //직렬화된 객체 읽어오기
    	        if (obj instanceof Set) { //obj가 Set인지 확인
    	            Set<Account> loaded = (Set<Account>) obj; //캐스팅하기

    	            // 핵심: 메모리 컬렉션은 항상 LinkedHashSet 유지
    	            synchronized (accounts) {
    	                accounts.clear(); //기존 메모리안에 계좌목록 삭제, 스레드 안정성을 위해 synchronized로 잠금처리
    	                // loaded가 HashSet이든 뭐든, 그 "현재 반복 순서" 그대로 옮겨 담음
    	                //새 Linkedhashset을 만들면서 loaded의 내용을 그대로 복사
    	                // 저장된 iteration order(반복순서) 유지
    	                accounts.addAll(new LinkedHashSet<>(loaded));
    	            }
    	            System.out.println("[안내] 저장된 계좌 정보를 불러왔습니다.");
    	        }
    	    } catch (IOException | ClassNotFoundException e) {
    	        System.out.println("[안내] 저장파일을 불러오지 못했습니다: " + e.getMessage());
    	    }
    }
    
    
    
    /* ===== 계좌개설: Set 중복 처리(equals/hashCode) ===== */
    /* ===== 저장옵션 ===== */
    public void saveOption() {
    	System.out.println("***자동저장을 시작합니다***");
    	System.out.println("저장 옵션을 선택하세요.");
    	System.out.println("1. 자동저장On, 2.자동저장Off");
        System.out.print("선택:");
        int option = parseIntLine();

    }
    public void makeAccount() {
        System.out.println("***신규계좌개설***");
        System.out.println("-----계좌선택------");
        System.out.println("1.보통계좌");
        System.out.println("2.신용신뢰계좌");
        System.out.println("3.특판계좌");
        System.out.print("선택:");
        int type = parseIntLine(); // 문자 입력시 재입력
        
        
        while(!(type == 1 || type == 2 || type == 3)) {
        	System.out.println("다시 입력하시오. ");
            type = parseIntLine(); // 문자 입력시 재입력
        }
        	
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
        } else if (type == 3) {
        	newAcc = new specialAccount(accNo, owner, balance, baseRate);
        } 
        else {
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
        Account[] snapshot;
        synchronized (accounts) {
            if (accounts.isEmpty()) {
                System.out.println("등록된 계좌가 없습니다.");
                System.out.println("전체계좌정보 출력이 완료되었습니다.");
                System.out.println();
                return;
            }
            snapshot = accounts.toArray(new Account[0]);
		}
        for (Account a : snapshot) a.showInfoBlock();
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
