package banking.threeby3;

import java.io.Serializable;
import java.util.Objects;

//직렬화 인터페이스
/*
 객체를 파일로 저장하거나, 네트워크로 전송할 수 있도록 
 byte형태로 변환하는 과정. 
 ObjectOutputStream/ObjectInputStream 등을 사용해 
 객체를 저장/불러오기 하려면, 그 객체의 클래스가 '직렬화'가능한 클래스여야 한다.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L; //클래스 버전 식별자

    protected String accountNo; //계좌정보
    protected String owner; //계좌주
    protected int balance; //잔액

    public Account(String accountNo, String owner, int balance) {
        this.accountNo = accountNo;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountNo() { return accountNo; }
    public String getOwner()     { return owner; }
    public int getBalance()      { return balance; }

    // 입금(이자 계산)은 하위 클래스에서 구현
    public abstract void deposit(int amount);

    // 출금(이자 없음)
    public boolean withdraw(int amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    // 콘솔 출력 래퍼(wrapper)
    /*
     쓰레드 사용으로 인해 변경
     원 메서드를 콘솔 출력용으로 설계되었으나,
     파일에 문자열 형태로 저장해야해서, 문자열로 반환하는 버전이 필요.
     infoBlock을 활용해서 문자열 형태로 반환 저장
     */
    
    public void showInfoBlock() {
        System.out.print(infoBlock());
    }

    // 파일 저장 등에 재사용할 포맷 문자열
    // 각 자식클래스에서 정의내림
    public abstract String infoBlock();

    // HashSet 중복판정: 계좌번호만 기준
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account acc)) return false;
        return Objects.equals(this.accountNo, acc.accountNo);
    }
    @Override
    public int hashCode() {
        return Objects.hash(accountNo);
    }
}
