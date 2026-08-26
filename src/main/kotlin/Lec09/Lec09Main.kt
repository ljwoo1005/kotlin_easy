package Lec09

/*
 * 코틀린에서 클래스를 다루는 방법
 *
 * 1. 클래스와 프로퍼티
 * 2. 생성자와 init
 * 3. 커스텀 getter, setter
 * 4. backing field
 */

/*
 * 1. 클래스와 프로퍼티
 *
 * 생성자가 클래스 이름 옆에 붙는다. constructor 지시어를 사용한다.
 * 멤버 필드 초기화 시 자동 형 추론 가능
 * 코틀린에서는 필드만 만들면 자동으로 getter, setter를 만들어준다.
 */
class Kotlin09Person(name: String, age: Int) { // 생성자가 클래스 이름 옆에 붙는다.

    val name = name
    var age = age

}

/*
 * 여기서 더 나아가서 생성자의 constructor 지시어는 생략 가능하다.
 * 그리고 생성자와 필드 선언을 동시에 해결할 수 있는데
 * 생성자 파라미터에 변수, 상수 키워드를 선언하면 된다.
 * 위와 아래 클래스의 내용물은 완전히 동일하다.
 */
class Kotlin09Person2(
    val name: String,
    var age: Int
)

/*
 * getter와 setter를 사용할 때 Java와 다르다.
 * 자바는 메서드 호출 형식을 사용하지만,
 * 코틀린은 자바스크립트처럼 .필드 형식으로 사용한다.
 *
 * 이는 자바 클래스를 코틀린에서 사용할 때도 마찬가지로 작성할 수 있다.
 */
fun callPerson() {

    val person = Kotlin09Person("LJW", 3)
    println(person.name) // 코틀린에서의 getter 사용

    person.age = 10 // 코틀린에서의 setter 사용
    println(person.age)

}

/*
 * 객체 생성자 호출 시점에서 파라미터의 유효성 검사를 진행하고자 할 때
 * 자바에서는 생성자 블록에서 진행했지만
 * 코틀린은 파라미터 선언부가 생성자 역할을 하기 때문에 어디서 진행할지 모르겠다.
 *
 * 이럴 때를 위해 코틀린 클래스는 init 블록을 사용할 수 있다.
 * init 블록 : 클래스의 생성자가 호출되는 시점에 1회 호출
 */
class Kotlin09Person3(
    val name: String,
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다")
        }
        println("초기화 블록")
    }

    /*
     * 코틀린에서 자바처럼 추가적인 생성자를 만들고 싶다면?
     * 클래스 본문에 constructor 키워드와 함께 만들 수 있다.
     */
    constructor(name: String): this(name, 1) { // this() : 위에 있는 본인의 생성자
        println("첫 번째 부생성자")
    }
    constructor(): this("LJW") {
        println("두 번째 부생성자")
    }

}

/*
 * 개념 정리
 * 주생성자(primary constructor) : 클래스 선언 시 클래스 이름 옆에 붙은 생성자. 반드시 존재해야 한다!
 * 단, 주생성자에 파라미터가 하나도 없다면 생략 가능!
 *
 *      class student { }
 *
 * 부생성자(secondary constructor) : 주생성자가 존재하는 상태에서 추가적인 생성자를 말한다.
 * 있을 수도 있고, 없을 수도 있다.
 * 부생성자는 "최종적으로" 주생성자를 this로 호출해야만 한다.
 * 중간 과정에서 다른 부생성자를 호출해도 상관 없다.
 * 그리고 부생성자는 body를 가질 수 있다.
 *
 * 이 때, 생성자들 간의 호출 순서는 역순으로 실행된다.
 * 위에서 두 번째 부생성자 호출 시 호출 순서는
 *  init body -> 첫 번째 부생성자 body -> 두 번째 부생성자 body
 * 가 된다.
 *
 * 그런데 코틀린에서는 부생성자보다는 default parameter를 권장한다.
 *
 * 객체의 내용을 다른 객체로 바꾸는(Ex. Alien -> Person) 경우에, 즉 Converting이 필요할 때 부생성자를 사용할 순 있지만,
 * 그보다는 정적 팩토리 메서드를 사용하는 것을 추천한다.
 */
class Kotlin09Person4(
    val name: String = "LJW",
    var age: Int = 2
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다")
        }
        println("초기화 블록")
    }
}

/*
 * 3. 커스텀 getter, setter
 *
 * custom getter, setter를 선택하는 기준
 * 객체의 속성을 나타내는 것이라면 custom getter, setter를 사용하고,
 * 아니라면 함수로 선언하는 것이 좋다.
 */
class Kotlin09Person5(
    val name: String,
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다")
        }
    }

    // 성인인지 확인하는 함수 : 자바 형식
    fun isAdult(): Boolean {
        return this.age >= 20
    }

    // custom getter : 프로퍼티처럼 보이게 만드는 방법
    val isAdult2: Boolean
        get() = this.age >= 20

}

/*
 * custom getter를 사용하면 자기 자신을 변형시킬 수 있다.
 */
class Kotlin09Person6(
    name: String, // name에 custom getter를 사용할 것이기 때문에 상수 선언 키워드를 제거
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다")
        }
    }

    // name의 custom getter 생성
//    val name = name
//        get() = name.uppercase()
    /*
     * 위처럼 작성했을 때 문제점 : name getter를 호출하면 재귀호출이 되어버린다.
     *
     * 1. 밖에서 Kotlin09Person06.name을 호출할 때 get()이 호출된다
     * 2. get() 호출 시 name이라는 field가 먼저 호출된다
     * 3. 안에서 name이라는 field 호출 시 name에 대한 get()이 호출된다.
     * 4. 2번이 반복된다.
     *
     * 결국 2 -> 3 -> 4 -> 2 -> 3 -> 4 -> 2 -> .... 무한 루프에 빠져버리게 된다.
     */


    val name = name // 우항의 name은 주생성자에서 받은 name으로, 불변 프로퍼티에 name에 바로 대입
        get() = field.uppercase() // name을 변환하기 위해 field라는 특수한 프로퍼티를 사용
    /*
     * field : 무한루프를 막기 위한 예약어로, 자기 자신을 가리킨다.
     * 위 상황에서는 field = name 이다.
     * 이러한 field를 보이지 않는 field라 하여 backing field라고 부른다.
     */

    /*
     * 근데 위같은 경우는 다른 방식으로도 처리할 수 있다.
     * 앞서 보았던 프로퍼티"처럼" 보이게 하는 custom getter로 처리할 수 있다.
     */
    val getUpperCaseName: String
        get() = this.name.uppercase()

}

/*
 * 다음은 custom setter를 만들어보자.
 */
class Kotlin09Person07(
    name: String,
    var age: Int
) {
    init {
        if (age <= 0) {
            throw IllegalArgumentException("나이는 ${age}일 수 없습니다")
        }
    }

    var name = name
        set(value) {
            field = value.uppercase()
        }

}