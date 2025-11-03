package banking.threeby3;

public class AutoSaver extends Thread {

    private final AccountManager manager;

    public AutoSaver(AccountManager manager) {
        this.manager = manager;
        setDaemon(true); // 프로그램 종료 시 함께 종료되는 데몬쓰레드
        setName("AutoSaver-Thread");
    }

    @Override
    public void run() {
        try {
            while (true) {
                manager.autoSaveToText(); // 5초마다 텍스트 파일로 저장
                System.out.println("계좌정보가 텍스트로 자동저장 되었습니다.");
                Thread.sleep(5000);
                
            }
        } catch (InterruptedException e) {
            // interrupt()가 호출되면 여기로 들어옴 → 정상 종료 루트
            // 필요시 정리 로직이 있으면 추가
        }
        //run() 메서드가 return 되면 스레드 수명 종료
    }
}
