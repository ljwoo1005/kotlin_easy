package Lec14

/*
 * 코틀린에서 다양한 클래스를 다루는 방법
 *
 * 1. Data Class
 * 2. Enum Class
 * 3. Sealed Class, Sealed Interface
 */

/*
 * 1. Data Class
 *
 * 자바에서의 DTO를 코틀린에서 만들어보자.
 *
 * 코틀린에서 "data class"를 정의하면 내부에서 getter, equals, hashCode, toString 함수들을 자동으로 만들어준다.
 *
 * 자바에서도 JDK 16부터 코틀린의 data class와 유사한 클래스인 "record class"를 도입했다고 한다.
 */
data class PersonDto(
    val name: String,
    val age: Int
)

fun callPerson() {

    // 여기서 named argument를 활용하면 builder 패턴을 사용하는 효과까지 누릴 수 있다.
    val person = PersonDto(
        name = "LJW",
        age = 100
    )
    val person2 = PersonDto(
        name = "LJW",
        age = 200
    )

    println(person) // toString 호출
    println(person == person2) // false

}

/*
 * 2. Enum Class
 */
enum class Country (
    private val code: String
) {
    KOREA("KO"),
    AMERICA("US")
    ;
}

/*
 * 이전 5강에서 나온 팁 중 하나를 봐보자.
 * when은 Enum class, Sealed Class와 함께 사용할 경우, 더욱 더 진가를 발휘한다.
 */
fun handleCountry(country: Country) {

    /*
     * Enum Class는 컴파일 단계에서 어떤 타입이 존재하는지 전부 알게 된다.
     * when에 Enum Class를 인자로 넣게 되면 해당 Enum Class에 어떤 타입이 있는지 전부 파악하게 되고,
     * 별도로 else를 처리하지 않아도 된다.
     * 그리고 IDE의 설정에 따라 Enum Class에 변화가 생긴다면 when 부분에서 warning 혹은 error를 발생시킬 수 있다.
     */
    when (country) {
        Country.KOREA -> TODO()
        Country.AMERICA -> TODO()
    }

}

/*
 * 3. Sealed Class, Sealed Interface
 *
 * sealed의 사전적 정의 :
 *  1. 봉인을 한 *******
 *  2. <도로가> 포장된
 *  3. 해결[처리]된
 *
 * sealed class의 등장 배경
 * 상속이 가능하도록 추상 클래스를 만들고자 하는데...
 * 외부에서는 이 클래스를 상속받지 않게 하고 싶어!!
 *
 * 그렇다면 우리가 작성한 클래스만 하위 클래스가 되도록 "봉인"하자 라는 의미에서 탄생
 *
 * Sealed Class에 대한 추가적인 이론적 특징
 *  - 컴파일 타임 때 하위 클래스의 타입을 모두 기억한다. 즉, 런타임 때 클래스 타입이 추가될 수 없다.
 *  - 하위 클래스를 작성할 때에는 Sealed Class와 같은 패키지에 있어야 한다.
 * Enum Class와의 차이점
 *  - 클래스를 상속받을 수 있다.
 *  - 하위 클래스는 멀티 인스턴스가 가능하다.
 *
 * 추가로, JDK17에서도 Sealed Class가 추가되었다고 한다.
 */
sealed class HyundaiCar(
    val name: String,
    val price: Long
)

class Avante : HyundaiCar("아반떼", 1_000L)
class Sonata : HyundaiCar("소나타", 2_000L)
class Grandeur : HyundaiCar("그렌저", 3_000L)

fun handleCar(car: HyundaiCar) {

    /*
     * Sealed Class를 when과 함께 사용하게 될 때 Enum Class와 같은 이점을 얻을 수 있다.
     */
    when (car) {
        is Avante -> TODO()
        is Sonata -> TODO()
        is Grandeur -> TODO()
    }

}