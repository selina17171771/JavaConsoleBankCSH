package banking5;

/**
 * 메뉴 선택 범위 이탈 시 던지는 사용자 정의 예외
 * 1단계에서는 간단히 메시지 출력용으로만 사용합니다.
 */
public class MenuSelectException extends RuntimeException {
    public MenuSelectException(String message) {
        super(message);
    }
}