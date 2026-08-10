package Lec02;

public class Lec02Main {

    /*
     * 이 메서드는 안전한가?
     * -> No : str 변수에 null이 올지 실제 문자열이 올지 확답할 수 없다.
     * null이 온다면 NPE가 발생할 것이다.
     */
    public boolean startsWithA( String str ) {
        return str.startsWith("A");
    }

    /*
     * startsWithA를 안전하게 변경하기
     */
    public boolean startsWithA1( String str ) {
        if( str == null ) {
            throw new IllegalArgumentException("null이 들어왔습니다");
        }

        return str.startsWith("A");
    }

    public Boolean startsWithA2 ( String str ) {
        if( str == null ) {
            return null;
        }

        return str.startsWith("A");
    }

    public boolean startsWithA3 ( String str ) {
        if( str == null ) {
            return false;
        }

        return str.startsWith("A");
    }

}


