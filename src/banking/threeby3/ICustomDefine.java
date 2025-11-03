package banking.threeby3;

public interface ICustomDefine {
    // 메뉴
    int MAKE     = 1; // 계좌개설
    int DEPOSIT  = 2; // 입금
    int WITHDRAW = 3; // 출금
    int INQUIRE  = 4; // 계좌정보출력
    int DELETE   = 5; // 계좌정보삭제
    int SAVEOPT  = 6; // 저장옵션 (서브: 1.On / 2.Off)
    int EXIT     = 7; // 프로그램종료
    int GAME	 = 8; // 3x3 puzzle game 실행

    // 신용등급 추가 이자율(%)
    int A = 7;  // +7%
    int B = 4;  // +4%
    int C = 2;  // +2%
}
