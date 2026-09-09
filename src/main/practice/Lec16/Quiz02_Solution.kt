package Lec16

import kotlin.math.abs

/*
 * [핵심 해결 아이디어]
 * - Java의 `public static` 유틸리티 메서드는 코틀린에서 확장 함수로 변환하면 인스턴스 메서드처럼 직관적으로 호출할 수 있습니다.
 * - `infix` 키워드를 함수 선언 앞에 붙이면 수신 객체와 단일 매개변수 사이에서 점(`.`)과 괄호(`()`)를 생략한 중위 호출(`a diff b`)이 가능해집니다.
 * - 코틀린의 `IntRange` 객체를 매개변수로 취함으로써 min과 max를 별도로 전달받던 Java 방식 대신 하나의 범위 객체로 우아하게 캡슐화합니다.
 */

// 1. 문자열 반복 결합 모범 답안 확장 함수
private fun String.repeatWord(times: Int): String {
    // 횟수가 음수인 경우 요구사항에 따라 예외 발생
    if (times < 0) {
        throw IllegalArgumentException("times는 0 이상이어야 합니다.")
    }
    // 코틀린 표준 라이브러리의 String.repeat 함수를 활용하여 간결하게 반환
    return this.repeat(times)
}

// 2. 두 정수 차이 계산 모범 답안 중위 함수
private infix fun Int.diff(other: Int): Int {
    // 수신 객체(this)와 인자(other)의 차이에 대한 절댓값 계산
    return abs(this - other)
}

// 3. 범위 제한 모범 답안 중위 함수
private infix fun Int.clampTo(range: IntRange): Int {
    // 범위를 벗어나는 경우 각각 최솟값(first)과 최댓값(last)으로 제한
    return when {
        this < range.first -> range.first
        this > range.last -> range.last
        else -> this
    }
}

fun main() {
    // 1. repeatWord 검증
    check("Hi".repeatWord(3) == "HiHiHi") { "Hi를 3번 반복하면 HiHiHi여야 합니다." }
    check("A".repeatWord(0) == "") { "0번 반복하면 빈 문자열이어야 합니다." }
    check("".repeatWord(5) == "") { "빈 문자열을 5번 반복해도 빈 문자열이어야 합니다." }

    val negativeTimesThrown = runCatching { "test".repeatWord(-1) }.isFailure
    check(negativeTimesThrown) { "times가 음수이면 IllegalArgumentException이 발생해야 합니다." }

    // 2. diff 검증 (일반 호출 및 중위 호출 모두 지원)
    check(10.diff(3) == 7) { "일반 호출: 10과 3의 차이는 7이어야 합니다." }
    check((10 diff 3) == 7) { "중위 호출: 10 diff 3은 7이어야 합니다." }
    check((3 diff 10) == 7) { "중위 호출: 3 diff 10은 7이어야 합니다." }
    check((5 diff 5) == 0) { "중위 호출: 5 diff 5는 0이어야 합니다." }

    // 3. clampTo 검증 (범위 초과, 범위 미달, 범위 내)
    check((15 clampTo (1..10)) == 10) { "15 clampTo (1..10)은 10이어야 합니다." }
    check(((-5) clampTo (0..100)) == 0) { "-5 clampTo (0..100)은 0이어야 합니다." }
    check((7 clampTo (1..10)) == 7) { "7 clampTo (1..10)은 7이어야 합니다." }
    check((1 clampTo (1..10)) == 1) { "경계값: 1 clampTo (1..10)은 1이어야 합니다." }
    check((10 clampTo (1..10)) == 10) { "경계값: 10 clampTo (1..10)은 10이어야 합니다." }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 도메인 특화 언어(DSL) 수준의 높은 가독성: `JavaUtilityHelper.diff(a, b)` 대신 `a diff b`, `clamp(v, 1, 10)` 대신 `v clampTo (1..10)`처럼 자연어에 가까운 중위 호출 문법을 제공하여 코드의 의도가 명확해집니다.
 * 2. 불필요한 인스턴스화/유틸리티 클래스 제거: 의미 없는 static 전용 헬퍼 클래스를 만들지 않고도 특정 도메인 객체나 원시 타입에 필요한 함수를 직접 결합할 수 있습니다.
 * 3. Range 객체와의 자연스러운 융합: 코틀린의 풍부한 범위(`IntRange`) 연산자와 결합하여 매개변수 개수를 줄이고 실수를 예방합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 중위 함수 선언 조건 위반: `infix` 함수는 반드시 멤버 함수이거나 확장 함수여야 하며, **매개변수는 정확히 1개**여야 합니다. (매개변수가 없거나 2개 이상이면 컴파일 에러)
 * 2. 연산자 우선순위 혼동: 중위 함수는 산술 연산자(`+`, `*`)보다 우선순위가 낮고, 논리/비교 연산자보다 우선순위가 다를 수 있으므로 복합 식에서는 괄호(`(10 diff 3)`)를 적절히 감싸주는 것이 안전합니다.
 * 3. 자바에서의 호출 방식: 코틀린의 `infix` 함수를 자바 코드에서 호출할 때는 중위 문법을 쓸 수 없으며, 일반 static 메서드(`Lec16MainKt.diff(10, 3)`) 형태로만 호출할 수 있습니다.
 */
