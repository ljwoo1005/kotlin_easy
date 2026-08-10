package Lec01
class Person(var name: String) { }

fun main() {

    /*
     * 1. 변수 선언 키워드 - var과 val의 차이점
     *
     * 자바 코드
     * long number1 = 10L; // (1)
     * final long number2 = 10L; // (2)
     *
     * Java에서 long과 final long의 차이
     * -> 이 변수가 가변인가, 불변인가(read-only)
     *
     * Long number3 = 1_000L; // (3)
     * Lec01.Person person = new Lec01.Person("홍길동"); // (4)
     */

    // 코틀린에는 모든 변수에 수정 가능 여부를 표시해줘야 한다.
    var number1 = 10L // var : Variable의 약자, 변수 선언 키워드
    val number2 = 10L // val : Value의 약자, 상수 선언 키워드

    // 자바에서는 타입 명시가 필수이지만, 코틀린에서는 컴파일러가 타입을 자동으로 추론해주기 때문에 의무는 아니다.
    var number11: Long = 10L
    val number22: Long = 10L

    // 코틀린에서 변수 선언 후 초기화를 하지 않는다면?
    var number111 // 타입 선언하지 않았기 때문에 컴파일러가 타입을 추론하지 못해 에러
    var number1111: Long
    println(number1111) // 타입은 선언했지만 초기화하지 않아 값이 없기 때문에 에러
    number1111 = 10L
    println(number1111) // 정상

    val number222 // val도 마찬가지로 타입 선언이 없기에 컴파일러가 타입을 추론하지 못해 에러
    val number2222: Long
    println(number2222) // 위와 같은 이유
    number2222 = 10L
    println(number2222) // 자바와 마찬가지로 상수더라도 초기화하지 않은 변수에 대해 최초 1회 값 저장 가능

    // 간단한 Tip : 모든 변수는 우선 val로 만들고, 꼭 필요한 경우에만 var로 변경한다

    /*
     * 코틀린에서의 Primitive type
     * 숫자, 문자, 불리언과 같은 몇몇 타입은 내부적으로 특별한 표현을 갖는다.
     * 이 타입들은 실행 시 Primitive Value로 표현되지만,
     * 코드에서는 평범한 클래스처럼 보인다.
     *
     * 즉, 프로그래머가 boxing / unboxing을 고려하지 않아도 되도록 코틀린이 알아서 처리해준다.
     */

    /*
     * 코틀린에서의 nullable 변수
     * 코틀린에서 기본적으로 모든 변수에는 null이 들어갈 수 없게 설계되어있다.
     * 만약 변수에 null을 넣고 싶다면 타입 뒤에 "?"를 붙여야한다.
     * 이 때, "?"를 붙인 타입은 원본 타입과 아예 다른 타입으로 간주된다.
     */
    var number3: Long? = 10L
    number3 = null

    /*
     * 코틀린에서의 객체 인스턴스화
     * 코틀린에서 객체를 인스턴스화할 때에는 new 키워드를 사용하지 않는다.
     */
    var person = Person("홍길동")




}