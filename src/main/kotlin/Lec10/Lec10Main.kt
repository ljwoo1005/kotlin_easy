package Lec10

/*
 * 코틀린에서 상속을 다루는 방법
 *
 * 1. 추상 클래스
 * 2. 인터페이스
 * 3. 클래스를 상속할 때 주의할 점
 * 4. 상속 관련 지시어 정리
 */

/*
 * 1. 추상 클래스
 */
abstract class Animal(
    protected val species: String,
    protected open val legCount: Int
) {

    abstract fun move()

}

/*
 * 코틀린에서는 클래스 상속 시 extends 키워드가 아니라 : 을 사용한다.
 * Convention : 변수나 메서드 타입 선언에 사용하는 콜론은 이름 바로 뒤에, 클래스 상속에 사용하는 콜론은 primary constructor 우측에 한 칸 띄고 작성한다.
 * 그리고 상속받을 상위 클래스의 생성자를 바로 호출해야 한다.
 * 상위 클래스에 추상 메서드가 있다면 상속 시 해당 메서드를 "override" 키워드와 함께 구현해야 한다.
 */
class Cat(
    species: String
) : Animal(species, 4)  {

    // 코틀린에서 override는 애노테이션이 아니라 지시어이다.
    override fun move() {
        println("고양이가 사뿐 사뿐 걸어가~")
    }

}

class Penguin(
    species: String
) : Animal(species, 2) {

    private val wingCount: Int = 2

    override fun move() {
        println("펭귄이 움직입니다~ 꿱꿱")
    }

    // 상위 클래스의 getter 오버라이딩
    // 코틀린에서는 프로퍼티 상속 시 원본 클래스에서 "open" 키워드와 함께 선언되어야 한다.
    override val legCount: Int
        get() = super.legCount + this.wingCount

}

/*
 * 자바와 코틀린 모두 추상 클래스는 인스턴스화할 수 없다.
 */

/*
 * 2. 인터페이스
 */
interface Flyable {

    // 코틀린은 default 메서드를 default 키워드 없이 만들 수 있음
    fun act() {
        println("파닥 파닥")
    }

    fun fly()

}

interface Swimable {

    fun act() {
        println("어푸 어푸")
    }

}

class Penguin2(
    species: String
) : Animal(species, 2), Swimable, Flyable {

    private val wingCount: Int = 2

    override fun move() {
        println("펭귄이 움직인다~ 꿱꿱")
    }

    override val legCount: Int
        get() = super.legCount + this.wingCount

    /*
     * 여러 인터페이스의 동일한 이름의 default 메서드를 override할 때
     * default 메서드는 완성된 메서드이기에 컴파일러 입장에서 어느 인터페이스의 메서드를 호출해야할 지 판단할 수 없기에,
     * 구현체에서 호출 모호성을 해결해야 함
     * -> 구현체에서 default 메서드를 override함으로써 모호성 해결
     */
    override fun act() {
        super<Swimable>.act()
        super<Flyable>.act()
    }

    override fun fly() {
        TODO("Not yet implemented")
    }

}

/*
 * 자바와 코틀린 모두 인터페이스는 인스턴스화할 수 없다.
 * 그리고 코틀린에서는 backing field가 없는 프로퍼티를 인터페이스에 만들 수 있다.
 */
interface Swimable2 {

    /*
     * 이 프로퍼티는 Swimable2에 field가 있는 것이 아니라
     * 구현체에서 getter(val이니까 getter만, var의 경우 setter까지)를 구현해주기를 기대하는 것이다.
     * 사실 이 프로퍼티 앞에는 "abstract" 키워드가 존재하는데, 인터페이스 내부에서는 생략 가능하다.
     */
    val swimAbility: Int

    /*
     * 혹은 인터페이스에서 직접 구현해서 default 값을 정할 수도 있다.
     */
    val swimAbility2: Int
        get() = 3

    fun act() {
        println(swimAbility) // 이 프로퍼티는 인터페이스 내부에서 자유롭게 사용할 수 있다. 구현체에서 구현하면 어차피 값이 생길테니까
        println(swimAbility2)
        println("어푸 어푸")
    }

}

class Swim2 : Swimable2 {

    /*
     * Class 'Swim2' is not abstract and does not implement abstract member:
     * val swimAbility: Int
     * 인터페이스에서 swimAbility 프로퍼티에 대해 구현체에서 구현해야 한다고 컴파일 에러가 발생한다.
     *
     * 인터페이스에서 직접 구현한 swimAbility2는 구현체에서 override하지 않는다 해서 에러가 발생하지 않는다.
     */
    override val swimAbility: Int
        get() = 3

}

/*
 * 3. 클래스를 상속받을 때 주의할 점
 *
 * 완성된 클래스를 상속받고자 할 때, class 키워드 앞에 "open" 키워드 작성해야 함(프로퍼티나 메서드도 동일)
 */
open class Base(
    open val number: Int = 100
) {
    init {
        println("Base Class")
        println(number)
    }
}

/*
 * Base 클래스를 상속받으며, Base클래스의 number 프로퍼티 또한 override하고 있다.
 * 이 때, 둘 다 init 블록이 있으며, Derived 클래스의 객체 생성 시 init 블록의 실행 순서는 어떻게 될까?
 */
class Derived(
    override val number: Int
) : Base(number) {
    init {
        println("Derived class")
    }
}

fun main() {

    Derived(300)
    /*
     * ====== 결과 ======
     * Base Class
     * 0
     * Derived Class
     * =================
     *
     * 실행 순서는 다음과 같다.
     * 1. 자식 클래스 주 생성자(Primary Constructor) 진입
     * 2. 부모 클래스 주 생성자 호출 (super(...))
     * 3. 부모 클래스의 프로퍼티 초기화 및 init 블록 실행 (코드에 선언된 순서대로)
     * 4. 자식 클래스의 프로퍼티 초기화 및 init 블록 실행 (코드에 선언된 순서대로)
     * 5. 부 생성자(Secondary Constructor) 본문 실행 (있을 경우)
     * ** 핵심 규칙: 부모 클래스의 초기화(init)가 완벽히 끝난 뒤에야 자식 클래스의 프로퍼티 초기화와 init 블록이 실행됩니다. **
     *
     * 그런데 분명 number에 300을 넣어줬고, Base에는 number 프로퍼티가 100의 default 값을 가지는데, 실제로 number는 0이 출력되었다.
     *
     * by gemini
     * 1. 다형성(가상 메서드 디스패치):
     *      코틀린의 프로퍼티 접근(number)은 내부적으로 getNumber()라는 Getter 메서드 호출로 변환됩니다.
     *      Derived가 number를 오버라이딩했기 때문에, 부모 Base의 init 블록에서 number를 부르면 자식 Derived의 getNumber()가 호출됩니다.
     * 2. 초기화 순서의 불일치:
     *      부모 생성자가 실행되는 시점에는 자식(Derived)의 필드 초기화가 아직 시작조차 되지 않은 상태입니다.
     *      따라서 자식의 number 필드는 JVM 기본값(Zero-value)인 **0**을 유지하고 있습니다.
     *
     * 상위 클래스에서 하위 클래스가 override 하고 있는 프로퍼티를
     * 생성자나 init 블록에 사용하게 되면 예상치 못한 값이 나올 수 있기 때문에
     * 상위 클래스를 설계할 때 생성자 또는 초기화 블록에 사용되는 프로퍼티에는 open을 피해야 한다.
     *
     * 코틀린 공식 가이드:
     * 상위 클래스의 생성자나 init 블록에서 open 프로퍼티나 open 메서드를 절대 호출하지 마십시오. (IDE 경고: Accessing non-final property in constructor)
     */

}

/*
 * 4. 상속 관련 지시어 정리
 *
 * 1. final : override할 수 없게 한다. 코틀린에서는 default로 보이지 않게 존재한다.
 * 2. open : 완성된 클래스, 프로퍼티, 메서드에 대해 override를 열어준다.
 * 3. abstract : 미완성을 나타내는 지시어. 프로퍼티, 메서드 앞에 붙으며 구현체나 자식 클래스가 반드시 override 해야 한다.
 * 4. override : 상위 타입을 오버라이드 하고 있다. (자바는 애노테이션이지만, 코틀린은 키워드로써 반드시 사용해줘야 한다.)
 */