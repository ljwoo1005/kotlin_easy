package Lec16

/*
 * 코틀린에서 다양한 함수를 다루는 방법
 *
 * 1. 확장함수
 * 2. infix 함수 (중위함수)
 * 3. inline 함수
 * 4. 지역함수
 */

/*
 * 1. 확장함수
 *
 * 확장함수의 등장 배경부터 알아보자.
 *
 * 코틀린은 자바랑 100% 호환하는 것을 목표로 하고 있다.
 * 그렇다 보니 기존 자바 코드 위에 자연스럽게 코틀린 코드를 추가할 수는 없을까? 하는 고민이 생기게 된다.
 * 자바로 만들어진 라이브러리를 유지보수 및 확장할 때 코틀린 코드를 덧붙이고 싶다는 니즈가 생긴다.
 *
 * 어떤 클래스 안에 있는 메서드처럼 호출할 수는 있지만, 함수는 밖에 만들 수 있게 하자!!는 개념이 나온다.
 * 함수의 코드 자체는 클래스 밖에 있는데 마치 클래스 안에 있는 멤버함수처럼 호출해서 쓰는 것이다.
 * 이런 함수를 확장함수라고 부른다.
 *
 * 예시로 String.kt의 확장함수를 하나 만들어보자.
 * 기능은 문자열의 가장 끝에 있는 문자를 가져오는 것이다.
 */
fun String.lastChar(): Char {
    return this[this.length-1]
}

/*
 * fun 확장하려는 클래스명.함수명(파라미터): 반환타입 {
 *      this를 이용해 실제 클래스 안의 값에 접근
 *      이 때, this를 "수신 객체"라고 부르며,
 *      확장하려는 클래스를 "수신 객체 타입"이라고 부른다.
 * }
 */

fun callLastChar() {
    val str = "ABC"
    println(str.lastChar()) // 마치 String 클래스에 있는 함수인 것처럼 호출하여 사용할 수 있다.
}

/*
 * 확장함수가 public이고, 확장함수에서 수신객체 클래스의 private 함수를 가져오면 캡슐화가 깨지는 것이 아닌가??
 * 그렇기에 확장함수는 클래스에 있는 private 또는 protected 멤버를 가져올 수 없다!!
 *
 * 두 번째로, 멤버함수와 확장함수의 시그니처(메서드 프로필)가 같다면?
 */
fun Lec16Main.nextYearAge(): Int {
    println("확장 함수")
    return this.age + 1
}

fun callNextYearAge() {
    val main = Lec16Main(10)
    println(main.nextYearAge()) // 멤버 함수가 호출된다.
}

/*
 * 멤버함수와 확장함수의 시그니처가 같다면 멤버함수가 우선적으로 호출된다.
 *
 * 확장함수를 먼저 만들었지만, 나중에 다른 기능의 똑같은 멤버함수가 생긴다면??
 * 의도하지 않은 기능이 호출되어 오류가 발생할 수 있다!!
 */

/*
 * 확장함수가 오버라이드된다면 어떻게 될까?
 */
open class Train(
    val name: String = "새마을기차",
    val price: Int = 5_000
)

fun Train.isExpensive(): Boolean {
    println("Train의 확장함수")
    return this.price >= 10_000
}

class Srt : Train("SRT", 40_000)

fun Srt.isExpensive(): Boolean {
    println("Srt의 확장함수")
    return this.price >= 10_0000
}

fun callExpensive() {

    val train: Train = Train()
    train.isExpensive() // Train의 확장함수

    val srt1: Train = Srt()
    srt1.isExpensive() // Train의 확장함수

    val srt2: Srt = Srt()
    srt2.isExpensive() // Srt의 확장함수

}

/*
 * 해당 변수의 "현재 타입"
 * 즉, 정적인 타입에 의해 어떤 확장함수가 호출될지 결정된다.
 */

/*
 * 확장함수 중간 정리
 * 1. 확장함수는 원본 클래스의 private, protected 멤버 접근이 안된다!
 * 2. 멤버함수, 확장함수 중 멤버함수에 우선권이 있다!
 * 3. 확장함수는 현재 타입을 기준으로 호출된다!
 */

/*
 * 그럼 자바에서 코틀린의 확장함수를 가져다 사용할 수 있나??
 * 자바에서는 마치 static 메서드처럼 호출할 수 있다.
 *
 * public static void main(String[] args) {
 *      Lec16MainKt.lastChar("ABC") // C
 * }
 */

/*
 * 확장함수라는 개념은 "확장프로퍼티"와도 연결된다.
 * 확장프로퍼티의 원리는 확장함수 + custon getter와 동일하다!
 */

// 확장함수
fun String.lastChar2(): Char {
    return this[this.length - 1]
}

// 확장프로퍼티
val String.lastChar2: Char
    get() = this[this.length - 1]

fun callLastChar2() {
    println("ABC".lastChar2()) // C
    println("ABC".lastChar2) // C
}

/*
 * 2. infix 함수 (중위함수)
 *
 * 중위함수는 함수를 호출하는 새로운 방법이다!
 *
 * 이전에 보았던 downTo, step도 함수이다! (중위 호출 함수)
 *
 * 기존 함수 호출 방식 : 변수.함수이름(argument)
 * 중위함수 호출 방식  : 변수 함수이름 argument (변수, argument가 각각 하나씩만 있을 때)
 */

fun Int.add(other: Int): Int {
    return this + other
}

infix fun Int.add2(other: Int): Int {
    return this + other
}

fun callAdd() {

    3.add(4) // 확장함수 호출

    3.add2(4) // 확장함수 호출
    3 add2 4 // 확장함수 & 중위함수 호출

}

/*
 * 3. inline 함수
 *
 * 함수가 호출되는 대신, 함수를 호출한 지점에 함수 본문을 그대로 복붙하고 싶은 경우!
 */
inline fun Int.add3(other: Int): Int {
    return this + other
}

fun main() {
    3.add3(4)
}

/*
 * 위 main을 자바로 디컴파일하면 다음과 같이 변한다.
 *
   public static final void main() {
      byte $this$add3$iv = 3;
      int other$iv = 4;
      int $i$f$add3 = 0;
      int var10000 = $this$add3$iv + other$iv;
   }
 *
 * 함수 본문 자체를 전달하게 되면 함수를 파라미터로 전달할 때에 오버헤드를 줄일 수 있다.
 * 함수 호출 시 call stack이 쌓이게 되는데, depth가 깊은 함수 호출 시에 발생하는 call stack을 줄일 수 있는 것이다.
 *
 * 하지만 inline 함수의 사용은 성능 측정과 함께 신중하게 사용되어야 한다!
 * 코틀린 라이브러리에서는 최적화를 어느 정도 해뒀기 때문에 적절하게 inline 함수가 붙어있다.
 */

/*
 * 4. 지역함수
 *
 * 함수 안에 함수를 선언할 수 있는데, 이를 지역함수라고 부른다.
 */
fun createObject(firstName: String, lastName: String, age: Int): Lec16Main {
    if ( firstName.isEmpty() ) {
        throw IllegalArgumentException("firstName은 비어있을 수 없습니다! 현재 값 : $firstName")
    }

    if ( lastName.isEmpty() ) {
        throw IllegalArgumentException("lastName은 비어있을 수 없습니다! 현재 값 : $lastName")
    }

    return Lec16Main(firstName, lastName, 1)
}

/*
 * 위 함수에서는 중복되는 부분이 존재한다.
 * 이름의 공백을 검사하여 예외를 던지는 부분인데, 다음과 같이 바꿀 수 있다.
 */
fun createObject2(firstName: String, lastName: String, age: Int): Lec16Main {
    fun validateName(name: String, fieldName: String) {
        if ( name.isEmpty() ) {
            throw IllegalArgumentException("${fieldName}은 비어있을 수 없습니다! 현재 값 : $name")
        }
    }

    validateName(firstName, "firstName")
    validateName(lastName, "lastName")

    return Lec16Main(firstName, lastName, age)
}

/*
 * 지역함수는 함수로 추출하면 좋을 것 같은데, 이 함수를 지금 함수 내에서만 사용하고 싶을 때 사용하면 된다.
 * 그러나 depth가 깊어지기도 하고, 코드가 깔끔하게 나오지는 않는다..
 *
 * 위와 같은 상황에서는 차라리 Lec16Main쪽에서 validation을 하는 것이 더 좋아 보인다.
 */