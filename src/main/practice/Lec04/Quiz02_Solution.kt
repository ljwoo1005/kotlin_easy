package Lec04

/*
 * [핵심 해결 아이디어]
 * - Java의 'a.compareTo(b) > 0'은 코틀린에서 'a > b'로 직관적으로 대체됩니다.
 * - Java의 'a.equals(b)'는 코틀린의 동등성 연산자 'a == b'로 안전하고 간결하게 대체됩니다.
 * - Java의 'a.plus(b)' 메서드 호출은 코틀린의 'operator fun plus' 정의 덕분에 산술 연산자 'a + b'로 대체됩니다.
 * - 결과값이 단순 표현식인 경우 블록({ ... })과 return 대신 단일 표현식(=)을 사용하여 가독성을 극대화합니다.
 */

// 실습용 금액 데이터 클래스(Quiz02Money)는 Quiz02_Problem.kt에 정의된 모델을 사용합니다.

// 1. compareTo() 호출 대신 '>' 비교 연산자를 사용한 단일 표현식 모범 답안
private fun isGreaterThan(a: Quiz02Money, b: Quiz02Money): Boolean = a > b

// 2. equals() 호출 대신 '==' 동등성 연산자를 사용한 단일 표현식 모범 답안
private fun isSameAmount(a: Quiz02Money, b: Quiz02Money): Boolean = a == b

// 3. plus() 메서드 호출 대신 '+' 산술 연산자를 사용한 단일 표현식 모범 답안
private fun sumMoney(a: Quiz02Money, b: Quiz02Money): Quiz02Money = a + b

fun main() {
    val m1 = Quiz02Money(2000L)
    val m2 = Quiz02Money(1000L)
    val m3 = Quiz02Money(2000L)

    // 1. isGreaterThan 검증
    check(isGreaterThan(m1, m2)) { "2000L(m1)은 1000L(m2)보다 커야 합니다." }
    check(!isGreaterThan(m2, m1)) { "1000L(m2)은 2000L(m1)보다 크지 않아야 합니다." }
    check(!isGreaterThan(m1, m3)) { "동일한 금액끼리는 크지 않으므로 false여야 합니다." }

    // 2. isSameAmount 검증
    check(isSameAmount(m1, m3)) { "m1과 m3은 금액(2000L)이 동일하므로 true여야 합니다." }
    check(!isSameAmount(m1, m2)) { "m1(2000L)과 m2(1000L)는 금액이 다르므로 false여야 합니다." }

    // 3. sumMoney 검증
    val sum = sumMoney(m1, m2)
    check(sum == Quiz02Money(3000L)) { "2000L과 1000L의 합은 3000L이어야 합니다. (현재: $sum)" }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 코드 가독성과 비즈니스 직관성 향상: 자바에서는 'm1.compareTo(m2) > 0'이나 'm1.plus(m2)'와 같이 메서드명과 부가적인 비교 연산이 뒤섞여 비즈니스 의도가 한눈에 드러나지 않았습니다. 코틀린의 연산자 지원을 통해 'm1 > m2', 'm1 + m2'와 같이 도메인 수식 그대로 코드를 작성할 수 있습니다.
 * 2. 간결한 단일 표현식(Single Expression): 중괄호와 return 키워드를 걷어내고 '=' 기호로 결과를 선언적으로 표현함으로써 불필요한 보일러플레이트 코드를 완전히 제거했습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 연산자 오버로딩 시 'operator' 키워드 누락: 'operator' 키워드 없이 'fun plus(...)'로 선언하면 '+' 기호로 연산할 수 없으며 컴파일 에러가 발생합니다. 연산자 함수에는 반드시 'operator' 제어자를 명시해야 합니다.
 * 2. 불필요한 메서드 직접 호출: 연산자 오버로딩이 지원됨에도 습관적으로 'm1.plus(m2)'를 직접 호출하는 것은 코틀린의 관용적 장점을 살리지 못하는 안티패턴입니다.
 */
