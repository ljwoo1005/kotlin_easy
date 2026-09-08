package Lec02

/*
 * [학습 목표 & 복습 개념]
 * - Java의 명령형 if-null 검사 로직을 코틀린의 관용적(Idiomatic) Safe Call과 Elvis 연산자로 리팩토링합니다.
 * - Elvis 연산자를 활용하여 null인 경우 예외(throw)를 즉시 던지는 패턴(`?: throw`)을 학습합니다.
 * - Elvis 연산자를 활용하여 null인 경우 기본 불리언 값을 제공하는 패턴(`?: false`)을 체화합니다.
 *
 * [문제 설명]
 * 아래는 Java 스타일로 작성된 문자열 시작 검사 유틸리티 클래스입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaNullHandler {
 *     // 1. null이면 예외를 던지고, 아니면 "A"로 시작하는지 검사
 *     public boolean startsWithA1(String str) {
 *         if (str == null) {
 *             throw new IllegalArgumentException("문자열이 null입니다.");
 *         }
 *         return str.startsWith("A");
 *     }
 *
 *     // 2. null이면 null을 반환하고, 아니면 "A"로 시작하는지 검사
 *     public Boolean startsWithA2(String str) {
 *         if (str == null) {
 *             return null;
 *         }
 *         return str.startsWith("A");
 *     }
 *
 *     // 3. null이면 false를 반환하고, 아니면 "A"로 시작하는지 검사
 *     public boolean startsWithA3(String str) {
 *         if (str == null) {
 *             return false;
 *         }
 *         return str.startsWith("A");
 *     }
 * }
 * ----------------------------------------------------
 * 위 3가지 Java 메서드를 코틀린의 관용적 스타일(Safe Call과 Elvis)로 리팩토링한 함수 3개를 완성하세요.
 *
 * [요구사항 & 제약조건]
 * - `if (str == null)` 문을 사용하지 말고, 코틀린의 Safe Call(?.)과 Elvis(?:) 연산자를 사용하세요.
 * - startsWithAOrThrow: null일 경우 `throw IllegalArgumentException("문자열이 null입니다.")` 처리하세요.
 * - startsWithAOrNull: Safe Call(?.)을 사용하여 null일 때는 null을 그대로 반환하세요.
 * - startsWithAOrDefault: Elvis 연산자를 사용하여 null일 때는 기본값 `false`를 반환하세요.
 *
 * [입출력 및 기대 결과]
 * - startsWithAOrThrow("Apple")  -> true
 * - startsWithAOrThrow("Banana") -> false
 * - startsWithAOrThrow(null)     -> IllegalArgumentException 발생
 * - startsWithAOrNull("Apple")   -> true
 * - startsWithAOrNull(null)      -> null
 * - startsWithAOrDefault("Apple") -> true
 * - startsWithAOrDefault(null)   -> false
 *
 * [💡 HINT]
 * - 코틀린에서 `throw`는 Nothing 타입을 반환하는 표현식(Expression)이므로 `?: throw ...` 형태로 Elvis 연산자 우변에 바로 사용할 수 있습니다.
 * - Safe Call `str?.startsWith("A")`의 결과 타입은 `Boolean?`입니다.
 */

// 1. null이면 예외를 던지는 함수 뼈대
private fun startsWithAOrThrow(str: String?): Boolean {
    TODO()
}

// 2. null이면 null을 반환하는 함수 뼈대
private fun startsWithAOrNull(str: String?): Boolean? {
    TODO()
}

// 3. null이면 false를 반환하는 함수 뼈대
private fun startsWithAOrDefault(str: String?): Boolean {
    TODO()
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

    println("✅ Quiz02 테스트 통과! (Java to Idiomatic Kotlin 리팩토링 완료)")
}
