package banking5;

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
