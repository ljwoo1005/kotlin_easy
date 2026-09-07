package Lec13

/*
 * 코틀린에서 중첩 클래스를 다루는 방법
 *
 * 1. 중첩 클래스의 종류(Lec13Main.java에서 설명)
 * 2. 코틀린의 중첩 클래스와 내부 클래스
 */

/*
 * 2. 코틀린의 중첩 클래스와 내부 클래스
 *
 * Effective Java에서는 "클래스 안에 클래스를 만들 때는 static 클래스를 사용하라"고 가이드하고 있다.
 * 코틀린에서는 위 가이드를 충실하게 따르고 있다.
 *
 * 코틀린에서는 기본적으로 중첩 클래스가 바깥 클래스를 참조하지 않는다.
 * 바깥 클래스를 참조하고 싶다면 inner 키워드를 추가해야 한다.
 */
class KotlinHouse(
    private val address: String,
    private val livingRoom: LivingRoom
) {

    /*
     * 자바에서 권장되는 중첩 클래스 (static class) 작성
     *
     * 코틀린에서 중첩 클래스는 기본적으로 바깥 클래스에 대한 연결이 없는 중첩 클래스가 만들어진다.
     */
    class LivingRoom(
        private val area: Double
    )

    /*
     * 자바에서 권장되지 않는 중첩 클래스 (inner class) 작성
     *
     * 코틀린에서 명시적으로 inner 라는 키워드를 작성해야 한다.
     * 그리고 inner class 내부에서 바깥 클래스에 대한 참조를 위해서는 "this@바깥클래스명" 을 사용한다.
     */
    inner class LivingRoom2(
        private val area: Double
    ) {

        val address: String
            get() = this@KotlinHouse.address

    }

}