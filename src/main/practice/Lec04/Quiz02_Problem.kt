package Lec04

/*
 * [학습 목표 & 복습 개념]
 * - 자바의 장황한 비교 메서드(compareTo) 호출을 코틀린의 관용적인 비교 연산자(`>`, `<`)로 리팩토링합니다.
 * - 자바의 `equals()` 메서드 호출을 코틀린의 동등성 연산자(`==`)로 리팩토링합니다.
 * - 자바의 명시적 연산 메서드(plus) 호출을 코틀린의 연산자 오버로딩(`operator fun plus`) 기반 `+` 연산자로 리팩토링합니다.
 * - 단일 표현식 함수(Single-Expression Function, `=`)를 활용하여 간결하고 읽기 쉬운 코드를 완성합니다.
 *
 * [문제 설명]
 * 아래는 Java 스타일로 작성된 금액 비교 및 연산 유틸리티 클래스입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaMoneyManager {
 *     // 1. compareTo() 메서드를 직접 호출하여 금액 크기 비교
 *     public static boolean isGreaterThan(JavaMoney a, JavaMoney b) {
 *         return a.compareTo(b) > 0;
 *     }
 *
 *     // 2. equals() 메서드를 직접 호출하여 금액 일치 확인
 *     public static boolean isSameAmount(JavaMoney a, JavaMoney b) {
 *         return a.equals(b);
 *     }
 *
 *     // 3. plus() 메서드를 직접 호출하여 두 금액 합산
 *     public static JavaMoney sumMoney(JavaMoney a, JavaMoney b) {
 *         return a.plus(b);
 *     }
 * }
 * ----------------------------------------------------
 * 위 Java 메서드 3개를 코틀린의 관용적(Idiomatic) 연산자 문법으로 리팩토링하는 3개 함수를 완성하세요.
 *
 * 1. isGreaterThan(a: Quiz02Money, b: Quiz02Money): Boolean
 *    - compareTo()를 직접 호출하지 말고, 코틀린 비교 연산자(`>`)를 사용하여 단일 표현식으로 구현하세요.
 *
 * 2. isSameAmount(a: Quiz02Money, b: Quiz02Money): Boolean
 *    - equals()를 직접 호출하지 말고, 코틀린 동등성 연산자(`==`)를 사용하여 단일 표현식으로 구현하세요.
 *
 * 3. sumMoney(a: Quiz02Money, b: Quiz02Money): Quiz02Money
 *    - plus() 메서드를 직접 호출하지 말고, 연산자 오버로딩된 `+` 기호를 사용하여 단일 표현식으로 구현하세요.
 *
 * [요구사항 & 제약조건]
 * - `compareTo()`, `equals()`, `plus()` 메서드를 직접 호출하지 마세요.
 * - 코틀린의 관용 연산자(`>`, `==`, `+`)를 사용하세요.
 * - 가능한 경우 중괄호 블록과 return 문 대신 단일 표현식(`=`) 형태로 간결하게 작성하세요.
 *
 * [입출력 및 기대 결과]
 * - val m1 = Quiz02Money(2000L)
 * - val m2 = Quiz02Money(1000L)
 * - val m3 = Quiz02Money(2000L)
 * - isGreaterThan(m1, m2) -> true
 * - isGreaterThan(m2, m1) -> false
 * - isSameAmount(m1, m3)   -> true
 * - isSameAmount(m1, m2)   -> false
 * - sumMoney(m1, m2)       -> Quiz02Money(3000L)
 *
 * [💡 HINT]
 * - 코틀린에서 Comparable을 구현한 객체는 `a > b` 형태로 비교할 수 있습니다.
 * - `operator fun plus`가 정의된 객체는 `a + b` 연산자로 합산할 수 있습니다.
 * - 단일 표현식 함수는 `fun 함수명(...) = 식` 형태로 선언합니다.
 */

// 실습용 금액 데이터 클래스 (Comparable 및 연산자 오버로딩 구현)
data class Quiz02Money(
    val amount: Long
) : Comparable<Quiz02Money> {
    override fun compareTo(other: Quiz02Money): Int = this.amount.compareTo(other.amount)

    // '+' 연산자 오버로딩 구현
    operator fun plus(other: Quiz02Money): Quiz02Money = Quiz02Money(this.amount + other.amount)
}

// 1. 크기 비교 함수 뼈대
private fun isGreaterThan(a: Quiz02Money, b: Quiz02Money): Boolean {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 2. 동등성 비교 함수 뼈대
private fun isSameAmount(a: Quiz02Money, b: Quiz02Money): Boolean {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 3. 금액 합산 함수 뼈대
private fun sumMoney(a: Quiz02Money, b: Quiz02Money): Quiz02Money {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

fun main() {
    val m1 = Quiz02Money(2000L)
    val m2 = Quiz02Money(1000L)
    val m3 = Quiz02Money(2000L)

    // 1. isGreaterThan 검증 (학습자 미구현 시 NotImplementedError 발생)
    check(isGreaterThan(m1, m2)) { "2000L(m1)은 1000L(m2)보다 커야 합니다." }
    check(!isGreaterThan(m2, m1)) { "1000L(m2)은 2000L(m1)보다 크지 않아야 합니다." }
    check(!isGreaterThan(m1, m3)) { "동일한 금액끼리는 크지 않으므로 false여야 합니다." }

    // 2. isSameAmount 검증
    check(isSameAmount(m1, m3)) { "m1과 m3은 금액(2000L)이 동일하므로 true여야 합니다." }
    check(!isSameAmount(m1, m2)) { "m1(2000L)과 m2(1000L)는 금액이 다르므로 false여야 합니다." }

    // 3. sumMoney 검증
    val sum = sumMoney(m1, m2)
    check(sum == Quiz02Money(3000L)) { "2000L과 1000L의 합은 3000L이어야 합니다. (현재: $sum)" }

    println("✅ Quiz02 테스트 통과! (Java to Idiomatic Kotlin 리팩토링 완료)")
}
