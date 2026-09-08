package Lec03

/*
 * [학습 목표 & 복습 개념]
 * - Java의 'instanceof' 및 명시적 강제 형변환((Type) obj) 코드를 코틀린의 '스마트 캐스트(Smart Cast)'로 리팩토링합니다.
 * - 실패 가능성이 있는 객체 형변환을 코틀린의 안전한 캐스트 연산자('as?')와 Safe Call(?.)로 간결하게 처리합니다.
 * - Java의 문자열 연결(+) 또는 String.format()을 코틀린의 관용적인 '문자열 템플릿(${})'으로 개선합니다.
 *
 * [문제 설명]
 * 아래는 Java 스타일로 작성된 타입 확인 및 객체 포맷팅 유틸리티 클래스입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaPersonHandler {
 *     // 1. 객체 타입 확인 후 강제 캐스팅하여 포맷팅된 문자열 반환
 *     public static String formatPerson(Object obj) {
 *         if (obj instanceof Quiz02Person) {
 *             Quiz02Person person = (Quiz02Person) obj;
 *             return person.getName() + "(" + person.getAge() + "세)";
 *         }
 *         return "알 수 없는 대상";
 *     }
 *
 *     // 2. 객체 타입이 맞으면 나이를, 아니거나 null이면 null 반환
 *     public static Integer getAgeOrNull(Object obj) {
 *         if (obj instanceof Quiz02Person) {
 *             return ((Quiz02Person) obj).getAge();
 *         }
 *         return null;
 *     }
 * }
 * ----------------------------------------------------
 * 위 2가지 Java 메서드를 코틀린의 관용적 문법(스마트 캐스트, 안전한 캐스트 as?, 문자열 템플릿)으로 리팩토링하세요.
 *
 * 1. formatPerson(obj: Any?): String
 *    - obj가 Quiz02Person인 경우, 스마트 캐스트와 문자열 템플릿을 사용하여 "${obj.name}(${obj.age}세)"를 반환합니다.
 *    - obj가 Quiz02Person이 아니거나 null인 경우 "알 수 없는 대상"을 반환합니다.
 *
 * 2. getAgeOrNull(obj: Any?): Int?
 *    - if 문을 사용하지 않고, 코틀린의 안전한 캐스트 연산자(as?)와 Safe Call(?.)을 사용하여 단일 표현식으로 구현하세요.
 *    - obj가 Quiz02Person이면 해당 객체의 age를 반환하고, 아니거나 null이면 null을 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - formatPerson: 불필요한 명시적 캐스팅(as Quiz02Person)을 사용하지 말고, 'is' 검사를 통한 스마트 캐스트를 활용하세요.
 * - getAgeOrNull: if 문이나 'is' 검사를 사용하지 말고, 'as?'와 Safe Call(?.)을 결합하여 단 한 줄로 작성하세요.
 * - 문자열 연결 시 '+' 연산자나 String.format() 대신 문자열 템플릿(${})을 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - formatPerson(Quiz02Person("홍길동", 25)) -> "홍길동(25세)"
 * - formatPerson("텍스트")                   -> "알 수 없는 대상"
 * - formatPerson(null)                       -> "알 수 없는 대상"
 * - getAgeOrNull(Quiz02Person("이순신", 40)) -> 40
 * - getAgeOrNull("텍스트")                   -> null
 * - getAgeOrNull(null)                       -> null
 *
 * [💡 HINT]
 * - `is` 연산자로 타입을 확인하면 조건문 블록 안에서 대상 변수가 해당 타입으로 자동 스마트 캐스트됩니다.
 * - `as?` 연산자는 좌변이 우변의 타입이 아니거나 null인 경우 ClassCastException을 던지는 대신 null을 반환합니다.
 */

// 실습용 도메인 데이터 클래스
data class Quiz02Person(
    val name: String,
    val age: Int
)

// 1. 사람 정보 포맷팅 함수 뼈대 (스마트 캐스트 활용)
private fun formatPerson(obj: Any?): String {
    TODO()
}

// 2. 안전한 나이 조회 함수 뼈대 (as? 연산자 활용 단일 표현식)
private fun getAgeOrNull(obj: Any?): Int? {
    TODO()
}

fun main() {
    val person1 = Quiz02Person("홍길동", 25)
    val person2 = Quiz02Person("이순신", 40)

    // 1. formatPerson 검증
    check(formatPerson(person1) == "홍길동(25세)") { "정상 객체는 '홍길동(25세)'여야 합니다. (현재: ${formatPerson(person1)})" }
    check(formatPerson("임의의문자열") == "알 수 없는 대상") { "다른 타입은 '알 수 없는 대상'이어야 합니다. (현재: ${formatPerson("임의의문자열")})" }
    check(formatPerson(123) == "알 수 없는 대상") { "숫자 타입은 '알 수 없는 대상'이어야 합니다. (현재: ${formatPerson(123)})" }
    check(formatPerson(null) == "알 수 없는 대상") { "null은 '알 수 없는 대상'이어야 합니다. (현재: ${formatPerson(null)})" }

    // 2. getAgeOrNull 검증
    check(getAgeOrNull(person2) == 40) { "person2의 나이는 40이어야 합니다. (현재: ${getAgeOrNull(person2)})" }
    check(getAgeOrNull("문자열") == null) { "문자열 입력 시 null이어야 합니다. (현재: ${getAgeOrNull("문자열")})" }
    check(getAgeOrNull(null) == null) { "null 입력 시 null이어야 합니다. (현재: ${getAgeOrNull(null)})" }

    println("✅ Quiz02 테스트 통과! (스마트 캐스트 및 as? 안전 변환 정상 동작)")
}
