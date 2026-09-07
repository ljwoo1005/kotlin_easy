package Lec12

/*
 * 코틀린에서 object 키워드를 다루는 방법
 *
 * 1. static 함수와 변수
 * 2. 싱글톤
 * 3. 익명 클래스
 */

/*
 * 1. static 함수와 변수
 *
 * 코틀린에는 static 키워드가 존재하지 않는다.
 * 대신 companion object 라는 키워드를 사용한다.
 *
 * static : 클래스가 인스턴스화 될 때 새로운 값이 복제되는 것이 아니라 정적으로 인스턴스끼리 값을 공유
 * companion object (동반 객체) : 클래스와 동행하는 유일한 오브젝트 -> 인스턴스가 여러 개 생기더라도 이 클래스라는 설계도와 동행하는 유일한 object다.
 * 한 클래스에 하나의 companion object만 사용 가능하다.
 *
 * const 키워드 : 컴파일 타입 상수로, 특정 인스턴스의 생성 여부와 무관하게 컴파일 시점에 값이 확정되어야 한다.
 * 진짜 상수에 붙이기 위한 용도이고, primitive type과 String에만 붙일 수 있다.
 * val과 함께 사용하고 다음 3가지의 위치에서만 사용할 수 있다.
 *  - 파일 최상단
 *  - companion object
 *  - 싱글톤 클래스 (named object)
 *
 * companion object 또한 하나의 객체로 간주된다.
 * 때문에 이름을 붙일 수도 있고, interface를 구현할 수도 있다.
 *
 * companion object에 유틸성 함수들을 넣어도 되지만, 그보다는 파일 최상단을 활용하는 것을 추천한다.
 *
 * 그리고 자바에서 코틀린에 있는 static field나 static 함수를 사용하고 싶을 때 @JvmStatic 애노테이션을 붙여야 한다.
 */
class Person private constructor (
    var name: String,
    var age: Int,
) {

    // 이름이 있는 companion object + 인터페이스 구현
    companion object Factory : Log {

//        private val MIN_AGE = 1 // MIN_AGE가 런타임 시에 할당된다.
        private const val MIN_AGE = 1 // MIN_AGE가 컴파일 시에 할당된다.
        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }

        @JvmStatic
        override fun log() {
            println("나는 Person 클래스의 동행객체 Factory에요")
        }

    }

}

class Person2 private constructor (
    var name: String,
    var age: Int
) {

    // 이름이 없는 companion object
    companion object {

        var a = 1
            get() {
                return field
            }
            @JvmStatic
            set(value) {
                field = value
            }


        const val A = 1
        private const val MIN_AGE = 1
        fun newBaby(name: String): Person2 {
            return Person2(name, MIN_AGE)
        }

    }

}

/*
 * 2. 싱글톤
 *
 * 클래스 생성 시 앞에 object 키워드를 붙이면 끝이다..
 */
object Singleton {
    var a: Int = 0
}

fun callSingleton() {
    var a = Singleton.a
    Singleton.a += 10

    println(Singleton.a)
}

/*
 * 3. 익명 클래스
 *
 * 특정 인터페이스나 클래스를 상속받은 구현체를 일회성으로 사용할 때 쓰는 클래스
 * [ object : 익명 클래스 명 ] 구조로 사용할 수 있다.
 *
 * 자바에서는 new 타입이름() { }
 * 코틀린에서는 object : 타입이름 { }
 */
private fun moveSomething(moveable: Moveable) {
    moveable.move()
    moveable.fly()
}

fun callMoveSomething() {

    moveSomething(object : Moveable {
        override fun fly() {
            println("난다~~")
        }

        override fun move() {
            println("움직인다~~")
        }
    })

}

/*
 * object 키워드를 사용하는 2가지 경우
 *
 * - named object (객체 선언) : 고유한 이름이 지정된 싱글톤 객체
 * - anonymous object (객체 표현식) : 런타임에 일회성 인스턴스로 생성되는 익명 객체
 */
