package banking3;

/**
 * 메뉴/이자율 상수 정의용 인터페이스
 * 1단계에서는 메뉴 상수만 사용합니다.
 */
public interface ICustomDefine {
    // 메뉴
    int MAKE    = 1; // 계좌개설
    int DEPOSIT = 2; // 입금
    int WITHDRAW= 3; // 출금
    int INQUIRE = 4; // 전체계좌정보출력
    int EXIT    = 5; // 프로그램종료

    // (2단계 확장용) 신용등급 이자율
    int A = 7;  // 7%
    int B = 4;  // 4%
    int C = 2;  // 2%
}
