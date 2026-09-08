package Lec01

/*
 * [학습 목표 & 복습 개념]
 * - 가변 변수(var)와 불변/읽기 전용 변수(val)의 선언과 차이점을 체화합니다.
 * - 코틀린 컴파일러의 타입 추론(Type Inference) 원리를 이해합니다.
 * - null을 허용하는 Nullable 타입(?)의 선언 및 null 할당 방식을 학습합니다.
 *
 * [문제 설명]
 * 초기 카운트 값(initialCount: Long)을 입력받아 변수를 선언하고 값을 조작하는 calculateCounters 함수를 완성하세요.
 * 1. 고정 가산 값 100L을 갖는 읽기 전용(불변) 변수를 선언합니다.
 * 2. 변경 가능한 카운터 변수를 선언하여 initialCount로 초기화한 후, 위 고정 가산 값을 더해 갱신합니다.
 * 3. null을 담을 수 있는 변수를 선언하여 initialCount 값을 넣었다가, 이후 명시적으로 null을 할당합니다.
 * 4. 최종적으로 (초기 카운트, 갱신된 카운터, null이 할당된 변수)를 Triple 객체로 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - 값이 변경되지 않는 고정 가산 값은 반드시 `val`로 선언하세요.
 * - 값이 갱신되는 카운터 변수는 반드시 `var`로 선언하세요.
 * - 타입 추론이 가능한 경우 불필요한 타입 명시를 생략하세요.
 * - null을 할당할 변수는 반드시 타입 뒤에 `?`를 붙여 Nullable 타입으로 선언하세요.
 *
 * [입출력 및 기대 결과]
 * - calculateCounters(10L) -> Triple(10L, 110L, null)
 * - calculateCounters(0L)  -> Triple(0L, 100L, null)
 *
 * [💡 HINT]
 * - 코틀린은 기본적으로 null을 허용하지 않습니다. null을 담으려면 타입 뒤에 물음표(?)를 붙여야 합니다. (예: `Long?`)
 * - '모든 변수는 우선 val로 만들고, 꼭 필요한 경우에만 var로 변경한다'는 원칙을 적용해보세요.
 */

// 학습자가 함수 본문을 완성할 수 있도록 뼈대를 제공합니다.
private fun calculateCounters(initialCount: Long): Triple<Long, Long, Long?> {
    TODO()
}

fun main() {
    val result1 = calculateCounters(10L)
    check(result1.first == 10L) { "첫 번째 값은 초기값 10L이어야 합니다. (현재: ${result1.first})" }
    check(result1.second == 110L) { "두 번째 값은 100L이 더해진 110L이어야 합니다. (현재: ${result1.second})" }
    check(result1.third == null) { "세 번째 값은 null이어야 합니다. (현재: ${result1.third})" }

    val result2 = calculateCounters(0L)
    check(result2.first == 0L) { "첫 번째 값은 초기값 0L이어야 합니다. (현재: ${result2.first})" }
    check(result2.second == 100L) { "두 번째 값은 100L이어야 합니다. (현재: ${result2.second})" }
    check(result2.third == null) { "세 번째 값은 null이어야 합니다. (현재: ${result2.third})" }

    println("✅ Quiz01 테스트 통과! (calculateCounters 정상 동작)")
}
