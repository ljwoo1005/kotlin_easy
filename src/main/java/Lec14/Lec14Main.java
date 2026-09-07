package Lec14;

public class Lec14Main {

    /*
     * 자바에서 Enum Class를 활용한 분기 처리
     *
     * Enum의 종류가 많아진다면 코드가 세로로 비례해서 증가하게 되어 가독성이 떨어지게 된다.
     * 그리고 메서드에서 Enum에 대한 정보만을 받는다 하더라도 else에 대한 경우를 처리해줘야 하는데,
     * else를 쓰자니 애매하고, 또 안쓰자니 애매하고, Exception을 던지는 것도 애매한 상황이 나올 수 있다.
     */
    private static void handleCountry(JavaCountry country) {

        if (country == JavaCountry.KOREA) {
            // 로직 처리
        }

        if (country == JavaCountry.AMERICA) {
            // 로직 처리
        }

    }

}
