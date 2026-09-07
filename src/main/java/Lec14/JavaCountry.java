package Lec14;

/*
 * 자바에서의 Enum Class
 *
 * - 추가적인 클래스를 상속받을 수 없다.
 * - 인터페이스는 구현할 수 있으며, 각 코드가 싱글톤이다.
 */
public enum JavaCountry {

    KOREA("KO"),
    AMERICA("US");

    private final String code;

    JavaCountry(String code) { this.code = code; }

    public String getCode() { return code;}

}
