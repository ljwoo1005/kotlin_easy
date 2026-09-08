package Lec04

/*
 * [학습 목표 & 복습 개념]
 * - 코틀린의 동등성(Equality: `==`)과 동일성(Identity: `===`)의 차이를 이해하고 올바르게 적용합니다.
 * - 코틀린의 `==` 연산자가 내부적으로 Java의 `equals()`를 호출함을 체화합니다.
 * - `Comparable` 인터페이스를 구현한 객체에서 `compareTo()` 메서드를 직접 호출하지 않고 비교 연산자(`>`, `<` 등)를 사용하는 법을 학습합니다.
 *
 * [문제 설명]
 * 사용자 점수를 나타내는 데이터 클래스 `Quiz01Score`를 기반으로 두 점수를 비교하는 두 함수를 완성하세요.
 *
 * 1. checkIdentityAndEquality(a: Quiz01Score, b: Quiz01Score): Pair<Boolean, Boolean>
 *    - 두 객체의 동일성(참조 주소 일치 여부)과 동등성(값의 일치 여부)을 판정합니다.
 *    - 반환값의 첫 번째 요소(first)에는 동일성 여부, 두 번째 요소(second)에는 동등성 여부를 담아 Pair로 반환합니다.
 *
 * 2. isHigherScore(a: Quiz01Score, b: Quiz01Score): Boolean
 *    - 점수 객체 a가 점수 객체 b보다 높은 점수인지 판정합니다.
 *    - a의 점수가 b의 점수보다 크면 true, 그렇지 않으면 false를 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - 동일성(Identity) 비교에는 반드시 `===` 연산자를 사용하세요.
 * - 동등성(Equality) 비교에는 `equals()` 메서드를 직접 호출하지 말고 반드시 `==` 연산자를 사용하세요.
 * - 크기 비교에는 `compareTo()` 메서드를 직접 호출하지 말고 비교 연산자(`>`)를 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - val s1 = Quiz01Score(100)
 * - val s2 = s1
 * - val s3 = Quiz01Score(100)
 * - val s4 = Quiz01Score(80)
 * - checkIdentityAndEquality(s1, s2) -> Pair(true, true)
 * - checkIdentityAndEquality(s1, s3) -> Pair(false, true)
 * - isHigherScore(s1, s4)            -> true
 * - isHigherScore(s4, s1)            -> false
 *
 * [💡 HINT]
 * - 자바에서는 주소 비교에 `==`를 쓰고 값 비교에 `equals()`를 썼지만, 코틀린에서는 주소 비교에 `===`, 값 비교에 `==`를 씁니다.
 * - `Comparable`을 구현한 객체끼리는 `a > b`처럼 일반 숫자처럼 직관적인 부등호 기호를 사용할 수 있습니다.
 */

// 실습용 점수 데이터 클래스 (Comparable 구현)
data class Quiz01Score(
    val value: Int
) : Comparable<Quiz01Score> {
    override fun compareTo(other: Quiz01Score): Int = this.value.compareTo(other.value)
}

// 1. 동일성 및 동등성 판정 함수 뼈대
private fun checkIdentityAndEquality(a: Quiz01Score, b: Quiz01Score): Pair<Boolean, Boolean> {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

// 2. 점수 크기 비교 함수 뼈대
private fun isHigherScore(a: Quiz01Score, b: Quiz01Score): Boolean {
    // TODO: 여기에 코드를 작성하세요
    TODO()
}

fun main() {
    val s1 = Quiz01Score(100)
    val s2 = s1
    val s3 = Quiz01Score(100)
    val s4 = Quiz01Score(80)

    // 1. checkIdentityAndEquality 검증 (학습자 미구현 시 NotImplementedError 발생)
    val resultSameRef = checkIdentityAndEquality(s1, s2)
    check(resultSameRef.first) { "s1과 s2는 동일한 인스턴스이므로 동일성(===)이 true여야 합니다." }
    check(resultSameRef.second) { "s1과 s2는 값이 같으므로 동등성(==)이 true여야 합니다." }

    val resultDiffRef = checkIdentityAndEquality(s1, s3)
    check(!resultDiffRef.first) { "s1과 s3은 서로 다른 인스턴스이므로 동일성(===)이 false여야 합니다." }
    check(resultDiffRef.second) { "s1과 s3은 점수 값이 같으므로 동등성(==)이 true여야 합니다." }

    // 2. isHigherScore 검증
    check(isHigherScore(s1, s4)) { "100점(s1)은 80점(s4)보다 높아야 합니다." }
    check(!isHigherScore(s4, s1)) { "80점(s4)은 100점(s1)보다 높지 않아야 합니다." }
    check(!isHigherScore(s1, s3)) { "동일한 점수는 더 높지 않으므로 false여야 합니다." }

    println("✅ Quiz01 테스트 통과! (동일성/동등성 및 비교 연산자 기본 동작)")
}
