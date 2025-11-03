package banking.threeby3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/*
 3x3 puzzle
a : 좌로
d : 우로
w : 위로
s : 아래로
계좌관리 프로그램에서 추가 실행
*/
public class puzzle3x3 {
	
	private static final int N = 3; //3x3퍼즐

	//테스트/제출 시 셔플 횟수 지정하기 위한 상수
	private static final int TEST_SHUFFLES = 3; //테스트 셔플
	private static final int PROD_SHUFFLES = 100; //최종 셔플
	
	private final int[][] board = new int[N][N]; //퍼즐 0~8 / 0은빈칸(X)
	
	//빈칸의 현재 좌표(zr, zc). 시작은 우하단(완성 상태에서 0이 마지막 칸)
	/*
	 완성 상태 
	 1 2 3
	 4 5 6
	 7 8 X
	 */
	private int zr = N -1, zc = N-1; //빈칸 위치
	
	//셔플 시 랜덤 선택용
	private final Random rnd = new Random();
	
	//계좌관리에서 연결 case 8 : Game start.
	public static void start(Scanner sc) {
		puzzle3x3 game = new puzzle3x3();
		game.run(sc);
	}
	
	//테스트용 main
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		start(sc);
	}
	
	//전체 게임 실행
	//셔플횟수입력 -> 보드초기화(완성상태) -> 셔플 -> 플레이 -> 재시작여부

	//게임실행
	private void run(Scanner sc) {
		System.err.println("\n===3x3 슬라이딩 퍼즐게임===");
		System.out.println("[이동] a:Left, d:Right, w:Up, s:Down");
		System.out.println("[종료] x: Exit");

		while(true) {
			int shuffleCount = askShuffleCount(sc);
			initSolved();
			shuffle(shuffleCount);
			gameLoop(sc);
			if(!askRestart(sc)) {
				System.out.println("메뉴로 돌아갑니다.");
				return;
			}
		}
	}

	//셔플 횟수 입력 (100+)
	private int askShuffleCount(Scanner sc) {
		while (true) {
			System.out.print("셔플횟수를 입력하세요: (100이상 입력)");
			int s = sc.nextInt();
			try {
				if ( s >= 100) return s;
			} catch (NumberFormatException ignore) {}
			System.out.println("숫자를 올바르게 입력하세요.");			
		}
	}
	//보드초기화(완성상태)
	private void initSolved() {
		int k = 1;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				board[r][c] = k;
				k++;
			}
		}
		//마지막 칸 빈칸 (X)
		board[N-1][N-1] = 0;
		// 빈칸 좌표 동기화
		zr = N-1;
		zc = N-1;
	}
	
	//셔플
	private void shuffle(int count) {
		char lastMove='\0'; //직전 이동키 없음
		for (int i = 0; i < count; i++) {
			List<Character> moves = Arrays.asList('a','d','w','s');
			Collections.shuffle(moves, rnd);
			
			boolean moved = false;
			for (char m : moves) {
				if (isOpposite(lastMove, m)) continue;
				if (tryMove(m)) {
					lastMove = m;
					moved = true;
					break;
				}
			}
			if (!moved) {
				for (char m : moves) {
					if (tryMove(m)) {lastMove = m; break;}
				}
			}
		}
	}
	// 두키가 서로 반대 방향인지 여부를 반환
	private boolean isOpposite(char a, char b) {
		return (a == 'a' && b == 'd') || (a == 'd' && b == 'a') ||
				(a == 'w' && b == 's') || (a == 's' && b == 'w');
		}
	
	
	//플레이
	private void gameLoop(Scanner sc) {
		int moveCount = 0;
		printBoard();
		while (true) {
			
			if (isSolved()) {
				System.out.println("퍼즐완성. 총 이동: " + moveCount +"회");
			return;
			}
			System.out.print("이동키 입력(a/d/w/s), 종료(q):");
			String in = sc.nextLine().trim().toLowerCase();
			if (in.isEmpty()) continue;
			
			char key = in.charAt(0);
			if (key == 'q') {
				System.out.println("게임을 종료합니다.");
				return;
			}
			
			if (key == 'a' ||key == 'd'||key == 'w'||key == 's') {
				if (tryMove(key)) {
					moveCount++;
					printBoard();

				} else {
					System.out.println("이동 불가 상태입니다. (가장자리)");
				}
			} else {
				System.out.println("알수 없는 키 입니다. ");
			}
		}
	}
	//키매핑
	private boolean tryMove(char key) {
		int nr = zr, nc = zc; //이동 후 빈칸 좌표 후보
		switch (key) {
		case 'a' : nc = zc + 1; break; // x -> 오른쪽
		case 'd' : nc = zc - 1; break; // x -> 왼쪽
		case 'w' : nr = zr + 1; break; // x -> 아래
		case 's' : nr = zr - 1; break; // x -> 위
		default: return false;
		}
		
		if (nr < 0 || nr >= N ||nc < 0 || nc >= N) return false; //보드범위밖, 이동불가

		//빈칸과 대상 칸을 교환(swap)
		swap(zr, zc, nr, nc);
		
		//빈칸 좌표 갱신
		zr = nr;
		zc = nc; 
		return true;
	}
	private void swap(int r1, int c1, int r2, int c2) {
		int t = board[r1][c1];
		board[r1][c1] = board[r2][c2];
		board [r2][c2] = t;
	}
	private void printBoard() {
		System.out.println();
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (board[r][c] == 0) {
					System.out.print(" X ");
				} else {
					System.out.printf("%3d", board[r][c]);
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("가능 이동 상태 안내: ");
		System.out.printf(" 위(w): %s | 아래(s): %s | 좌(a): %s | 우(d): %s",
				(zr < N-1 ? "가능" : "불가"),
				(zr > 0 ? "가능" : "불가"),
				(zc < N-1 ? "가능" : "불가"),
				(zc > 0 ? "가능" : "불가")
				);
	}
	private boolean isSolved() {
		int k = 1;
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (r== N-1 && c == N-1) {
					if (board[r][c] != 0) return false;
				}else {
					if (board[r][c] != k) return false;
					k++;
				}
			}
		}
		return true;
	}
	//재시작
	private boolean askRestart(Scanner sc) {
		while (true) {
		System.out.print("다시 시작할까요? (y/n): ");
		String s = sc.nextLine().trim().toLowerCase();
		if (s.equals("y")) return true;
		if (s.equals("n")) return false;
		System.out.println("y 또는 n 으로 입력하세요.");
		}
	}
}
