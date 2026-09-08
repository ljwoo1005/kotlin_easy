package Lec01

/*
 * [핵심 해결 아이디어]
 * - 수정이 필요 없는 고정 가산 값은 'val'로 선언하여 불변성을 보장하고 의도치 않은 값 변경을 방지합니다.
 * - 값이 누적되는 카운터 변수는 'var'로 선언하여 자유롭게 값을 갱신합니다.
 * - 코틀린의 기본 타입은 null을 담을 수 없으므로, null을 대입해야 하는 변수는 반드시 타입 명시와 함께 '?'(Nullable)를 선언합니다.
 * - 초기 리터럴 값(100L)으로부터 컴파일러가 타입을 추론하므로 불필요한 타입 표기(': Long')를 생략하여 코틀린다운 간결함을 유지합니다.
 */

// 모범 답안 함수
private fun calculateCounters(initialCount: Long): Triple<Long, Long, Long?> {
    // 1. 불변(val) 가산 값 선언 (타입 추론 활용: Long)
    val step = 100L

    // 2. 가변(var) 카운터 변수 선언 후 가산 값을 더해 갱신
    var currentCount = initialCount
    currentCount += step

    // 3. null을 허용하는 Nullable 변수 선언 (타입 명시 필수: Long?)
    var nullableNumber: Long? = initialCount
    nullableNumber = null

    // 4. 연산 결과를 Triple 객체로 반환
    return Triple(initialCount, currentCount, nullableNumber)
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

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 간결한 변수 선언: 자바의 'final long step = 100L;' 대신 'val step = 100L'로 훨씬 간결하게 불변 변수를 정의할 수 있습니다.
 * 2. 타입 추론(Type Inference): 명시적으로 모든 타입을 적지 않아도 컴파일러가 우변의 대입식을 바탕으로 타입을 정확히 추론합니다.
 * 3. 컴파일 타임 널 안전성(Null Safety): 자바에서는 참조 타입 변수에 언제든 의도치 않게 null이 들어갈 수 있어 NPE 발생 위험이 높지만, 코틀린은 타입 시스템 레벨에서 Non-null(Long)과 Nullable(Long?)을 엄격히 구분합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 습관적인 'var' 남용: 모든 변수를 var로 선언하는 것은 가독성을 떨어뜨리고 휴먼 에러를 유발합니다. "모든 변수는 우선 val로 만들고, 꼭 필요한 경우에만 var로 변경한다"는 원칙을 지켜야 합니다.
 * 2. 'var num = null' 과 같은 무타입 null 초기화: 타입 명시 없이 null을 대입하면 컴파일러가 Nothing? 타입으로 추론하므로 이후 다른 값을 대입할 수 없습니다. null을 담으려면 'var num: Long? = null'처럼 명시적 Nullable 타입을 지정해야 합니다.
 */
