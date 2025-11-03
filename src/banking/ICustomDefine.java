package banking;

/*
 interface로 생성한다. 
메뉴선택과 이자율 지정을 위한 인터페이스형 상수를 정의한다. 
메뉴 : 계좌개설, 입금, 출금, 전체계좌정보출력, 종료를 1~5까지로 지정한다.
이자율 : 고객의 신용등급을 A, B, C로 나눠서 7%, 4%, 2%로 지정한다.
*/
public interface ICustomDefine {
    // 메뉴
    int MAKE    = 1; // 계좌개설
    int DEPOSIT = 2; // 입금
    int WITHDRAW= 3; // 출금
    int INQUIRE = 4; // 계좌정보출력
    int DELETE  = 5; // 계좌정보삭제 (4단계 추가)
    int EXIT    = 6; // 종료 (번호 한 칸 뒤로 이동)

    // 신용등급 추가 이자율(%)
    int A = 7;  // +7%
    int B = 4;  // +4%
    int C = 2;  // +2%
}
