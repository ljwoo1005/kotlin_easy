package Lec02

/*
 * [핵심 해결 아이디어]
 * - 코틀린의 'throw'는 구문(Statement)이 아닌 표현식(Expression)이므로, Elvis 연산자(?:) 우변에 직접 위치할 수 있습니다.
 *   이를 통해 별도의 if 문 없이 'str?.startsWith("A") ?: throw ...' 형태로 예외 처리를 압축할 수 있습니다.
 * - null을 그대로 전파하고자 할 때는 Safe Call(?.)만 단독으로 사용하면 좌변이 null일 때 자동으로 null이 반환됩니다.
 * - null일 때 대체 기본값이 필요한 경우 '?: 기본값'을 조합하여 non-null 타입으로 결과를 정제할 수 있습니다.
 */

// 1. null이면 예외를 던지는 모범 답안
private fun startsWithAOrThrow(str: String?): Boolean {
    // str?.startsWith("A")가 null이면 우변의 IllegalArgumentException을 던짐
    return str?.startsWith("A") ?: throw IllegalArgumentException("문자열이 null입니다.")
}

// 2. null이면 null을 반환하는 모범 답안 (Safe Call만으로 처리)
private fun startsWithAOrNull(str: String?): Boolean? {
    // str이 null이면 startsWith를 실행하지 않고 null을 반환
    return str?.startsWith("A")
}

// 3. null이면 false를 반환하는 모범 답안
private fun startsWithAOrDefault(str: String?): Boolean {
    // str이 null이어서 결과가 null이 되면 기본값 false를 채택
    return str?.startsWith("A") ?: false
}

fun main() {
    // 1. startsWithAOrThrow 검증
    check(startsWithAOrThrow("Apple")) { "startsWithAOrThrow('Apple')은 true여야 합니다." }
    check(!startsWithAOrThrow("Banana")) { "startsWithAOrThrow('Banana')은 false여야 합니다." }
    val exceptionThrown = runCatching { startsWithAOrThrow(null) }.isFailure
    check(exceptionThrown) { "startsWithAOrThrow(null)은 IllegalArgumentException을 던져야 합니다." }

    // 2. startsWithAOrNull 검증
    check(startsWithAOrNull("Apple") == true) { "startsWithAOrNull('Apple')은 true여야 합니다." }
    check(startsWithAOrNull("Banana") == false) { "startsWithAOrNull('Banana')은 false여야 합니다." }
    check(startsWithAOrNull(null) == null) { "startsWithAOrNull(null)은 null이어야 합니다." }

    // 3. startsWithAOrDefault 검증
    check(startsWithAOrDefault("Apple")) { "startsWithAOrDefault('Apple')은 true여야 합니다." }
    check(!startsWithAOrDefault("Banana")) { "startsWithAOrDefault('Banana')은 false여야 합니다." }
    check(!startsWithAOrDefault(null)) { "startsWithAOrDefault(null)은 false여야 합니다." }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 표현식으로서의 throw: 자바에서는 'throw'가 문장이어서 3항 연산자(?:)나 단일 표현식에 사용할 수 없었지만,
 *    코틀린에서는 표현식이므로 Elvis 연산자와 깔끔하게 합성되어 가독성이 극대화됩니다.
 * 2. 널 가능성의 명시화: 반환 타입 'Boolean'과 'Boolean?'을 통해 해당 함수가 null을 반환할 수 있는지 여부가
 *    시그니처 레벨에서 100% 투명하게 드러납니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 'str == null' 분기문의 무의식적 반복 사용: 자바에 익숙한 개발자는 코틀린에서도 습관적으로
 *    'if (str == null)'을 먼저 작성하곤 합니다. 코틀린에서는 Safe Call(?.)과 Elvis(?:)를 우선적으로 고려하는 것이 좋습니다.
 * 2. 불필요한 Elvis 연산자 중복: 'startsWithAOrNull'처럼 이미 Safe Call 결과가 null을 반환하는데
 *    '?: null'을 덧붙이는 불필요한 중복 코드를 작성하지 않도록 주의합니다.
 */
