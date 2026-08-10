package Lec02
/*
 * 2. 코틀린에서 null을 다루는 방법
 *
 * - Kotlin에서 null 체크
 * - Safe Call과 Elvis 연산자
 * - null 아님 단언!!
 * - 플랫폼 타입
 *
 * Lec02.Lec02Main.java에서 작성한 Lec02.startsWithA() 메서드를 코틀린에서 안전하게 바꿔보자
 * (코틀린 함수 선언은 "fun" 키워드로 하며, 반환 타입 선언은 매개변수 괄호 끝에 선언)
 */
fun startsWithA1( str: String? ): Boolean {
    if( str == null ) {
        throw IllegalArgumentException("null이 들어왔습니다")
    }

    return str.startsWith("A")
}

fun startsWithA2( str: String? ): Boolean? {
    if( str == null ) {
        return null
    }

    return str.startsWith("A")
}

fun startsWithA3( str: String? ): Boolean {
    // err. str의 타입에 "?"를 붙였기 때문에 일반 String과는 아예 다른 타입.str이 null일지도 모르는 상황이라서
    // 코틀린에서는 "?" 타입에 대해 바로 메서드 호출을 할 수 없게끔 막아두었음
    str.startsWith("A")

    // "?"를 붙인 타입이더라도 사전에 null 검사를 진행하면 후에 메서드를 호출할 수 있다.
    if( str == null ) {
        return false
    }

    return str.startsWith("A")
}

/*
 * 해당 함수에서 str에 바로 메서드를 실행할 수 있는 이유는 타입 선언 시 "?"가 붙지 않았기 때문에
 * null이 아니라는 것이 보장되기 때문이다
 * 코틀린에서는 "?"이 붙은 타입을 원본 타입과 아예 다르게 취급한다
 */
fun startsWithA( str: String ): Boolean {
    return str.startsWith("A")
}


fun main() {

    /*
     * 코틀린에서 null이 가능한 타입만을 위한 기능
     * Safe call (?.) : null이 아니면 뒷부분을 실행하고, null이면 뒷부분을 실행하지 않는다 (그대로 null)
     */
    val str: String? = "ABC"
    str.length // 불가능
    str?.length // 가능!!

    /*
     * Elvis 연산자 (?:) : 앞의 연산 결과가 null이면 뒤의 값을 사용한다
     */
    val str2: String? = "ABC"
    str?.length ?: 0

}

/*
 * Safe call과 Elvis 연산자를 활용하여 위의 함수들을 코틀린스럽게 바꿔보자.
 */
fun startsWithA11( str: String? ): Boolean {
    return str?.startsWith("A") ?: throw IllegalArgumentException("null이 들어왔습니다")
}

fun startsWithA22( str: String? ): Boolean? {
    return str?.startsWith("A")
}

fun startsWithA33( str: String? ): Boolean {
    return str?.startsWith("A") ?: false
}

/*
 * Elvis 연산자는 early return에서도 사용할 수 있다.
 *
 * public long calculate(Long number) {
 *   if( number == null ) {
 *     return 0;
 *   }
 *   // 다음 로직
 * }
 *
 * fun calculate(number: Long?): Long {
 *   number ?: return 0
 *   // 다음 로직
 * }
 */

/*
 * null 아님 단언!!
 * nullable type이지만, 아무리 생각해도 null이 될 수 없는 경우가 있다. (. 앞에 !!)
 *
 * 만약 null 아님 단언한 부분에 null이 들어간다면 Runtime 환경에서 NPE가 발생한다.
 */
fun startsWithA4( str: String? ): Boolean {
    return str!!.startsWith("A") // !! : null 아님 단언!!
}

/*
 * 플랫폼 타입
 * 코틀린에서 자바 코드를 가져다 사용할 때 어떻게 처리될까?
 *
 * 자바 메서드에 @Nullable이 있다면, 코틀린에서 "?" 타입이 아니더라도 바로 호출할 수 없다.
 * 반면, 자바 메서드에 @NotNull이 있다면, 코틀린에서 "?" 타입이 아닐 때 바로 호출할 수 있다.
 *
 * 만약 자바쪽에 위와 같은 null과 관련된 애노테이션이 붙어있지 않다면 코틀린 입장에서는 해당 값의 null 여부를 판단할 수 없다.
 * 이것을 플랫폼 타입이라고 한다.
 *
 * 플랫폼 타입 : 코틀린이 null 관련 정보를 알 수 없는 타입
 * Runtime 시 Exception이 발생할 수 있다.
 *
 * 코틀린에서 자바 코드를 사용할 때 Tip
 * - 자바 코드를 읽으면 null 가능성 확인
 * - 자바 코드를 사용하는 부분을 Kotlin으로 Wrapping하여 자바 코드 호출 부분을 최소화
 */
fun startsWithA5( str: String ): Boolean {
    return str.startsWith("A")
}

val person = Person("홍길동") // 자바로 작성된 Person 클래스
startsWithA5(person.name) // 자바의 getName 메서드에 @Nullable이 있다면 호출 불가능, @NotNull이 있다면 호출 가능

