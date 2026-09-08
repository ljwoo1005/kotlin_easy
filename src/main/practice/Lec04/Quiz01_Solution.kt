package Lec04

/*
 * [핵심 해결 아이디어]
 * - 동일성(Identity)은 객체의 참조 주소가 일치하는지를 검사하므로 '===' 연산자를 사용합니다.
 * - 동등성(Equality)은 객체의 내부 상태(값)가 일치하는지를 검사하므로 '==' 연산자를 사용합니다. 코틀린의 '=='는 내부적으로 equals()를 호출합니다.
 * - 코틀린에서 Comparable을 구현한 객체는 compareTo() 메서드를 직접 호출할 필요 없이 '>', '<', '>=', '<='와 같은 표준 비교 연산자를 직접 사용할 수 있으며, 컴파일러가 이를 compareTo() 호출로 자동 변환합니다.
 */

// 실습용 점수 데이터 클래스(Quiz01Score)는 Quiz01_Problem.kt에 정의된 모델을 사용합니다.

// 1. 동일성 및 동등성 판정 모범 답안 함수
private fun checkIdentityAndEquality(a: Quiz01Score, b: Quiz01Score): Pair<Boolean, Boolean> {
    // 동일성(참조 주소 비교)은 '===' 연산자 사용
    val isIdentical = (a === b)

    // 동등성(값 비교, 내부적으로 equals 호출)은 '==' 연산자 사용
    val isEqual = (a == b)

    return Pair(isIdentical, isEqual)
}

// 2. 점수 크기 비교 모범 답안 함수
private fun isHigherScore(a: Quiz01Score, b: Quiz01Score): Boolean {
    // Comparable 구현 객체는 compareTo 대신 비교 연산자 '>'를 사용하여 직관적으로 비교
    return a > b
}

fun main() {
    val s1 = Quiz01Score(100)
    val s2 = s1
    val s3 = Quiz01Score(100)
    val s4 = Quiz01Score(80)

    // 1. checkIdentityAndEquality 검증
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

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 직관적인 연산자 문법: 자바에서는 주소 비교에 '==', 값 비교에 'equals()'를 사용해 초심자의 실수가 빈번했으나, 코틀린은 수학적 직관에 맞게 '=='를 동등성(값 비교)에 할당하고 동일성(주소 비교)을 '==='로 명확히 분리했습니다.
 * 2. 부등호 연산자 활용: 자바에서는 객체 크기 비교 시 'a.compareTo(b) > 0'과 같이 메서드를 직접 호출하고 0과 비교해야 했지만, 코틀린은 Comparable 객체에 대해 'a > b' 형태로 일반 원시 타입처럼 자연스럽게 작성할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 습관적인 equals() 직접 호출: 코틀린에서는 '=='가 내부적으로 널 검사를 포함하여 equals()를 안전하게 호출하므로, 'a.equals(b)' 대신 'a == b'를 사용하는 것이 코틀린다운(Idiomatic) 스타일입니다.
 * 2. '==='와 '=='의 혼동: 객체의 실제 주소(동일 인스턴스인지)를 확인해야 하는 특별한 경우가 아니라면 대부분의 비즈니스 로직에서는 값 비교인 '=='를 사용하는 것이 맞습니다.
 */
