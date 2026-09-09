package Lec16

import kotlin.math.abs

/*
 * [학습 목표 & 복습 개념]
 * - Java의 고전적인 static 유틸리티 클래스 패턴을 코틀린의 관용적인 확장 함수(Extension Function)로 전환합니다.
 * - 두 개의 피연산자 사이에 점과 괄호 없이 함수를 호출할 수 있는 중위 함수(`infix`) 선언 및 호출 문법을 체화합니다.
 * - 코틀린의 `IntRange` 범위 객체와 중위 함수를 조합하여 가독성 높은 관용적 코드를 작성합니다.
 *
 * [문제 설명]
 * 아래는 Java 스타일로 작성된 전형적인 정적 유틸리티 클래스입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaUtilityHelper {
 *     // 1. 문자열 n회 반복 결합
 *     public static String repeatWord(String str, int times) {
 *         if (times < 0) {
 *             throw new IllegalArgumentException("times는 0 이상이어야 합니다.");
 *         }
 *         StringBuilder sb = new StringBuilder();
 *         for (int i = 0; i < times; i++) {
 *             sb.append(str);
 *         }
 *         return sb.toString();
 *     }
 *
 *     // 2. 두 정수의 절댓값 차이(거리)
 *     public static int diff(int a, int b) {
 *         return Math.abs(a - b);
 *     }
 *
 *     // 3. 숫자를 주어진 최소/최대 범위(min, max) 내로 제한
 *     public static int clamp(int value, int min, int max) {
 *         if (value < min) return min;
 *         if (value > max) return max;
 *         return value;
 *     }
 * }
 * ----------------------------------------------------
 * 위 Java 코드를 코틀린의 관용적(Idiomatic) 스타일로 리팩토링한 함수 3개를 완성하세요.
 *
 * [요구사항 & 제약조건]
 * 1. repeatWord: String의 확장 함수(`fun String.repeatWord(times: Int): String`)로 작성하세요.
 *    - times가 0 미만이면 IllegalArgumentException("times는 0 이상이어야 합니다.")을 던집니다.
 *    - 코틀린 표준의 `repeat` 함수나 간결한 루프를 활용하세요.
 * 2. diff: Int의 중위 함수(`infix fun Int.diff(other: Int): Int`)로 작성하세요.
 *    - 두 정수 사이의 절댓값 차이를 반환하며, `10 diff 3`과 같이 중위 호출이 가능해야 합니다.
 * 3. clampTo: Int와 IntRange를 다루는 중위 함수(`infix fun Int.clampTo(range: IntRange): Int`)로 작성하세요.
 *    - 수신 객체(`this`)가 `range.first`보다 작으면 `range.first`, `range.last`보다 크면 `range.last`, 범위 안이면 `this`를 반환합니다.
 *    - `15 clampTo (1..10)` 형태로 중위 호출이 가능해야 합니다.
 *
 * [입출력 및 기대 결과]
 * - "Go".repeatWord(3)          -> "GoGoGo"
 * - "Kotlin".repeatWord(0)      -> ""
 * - "test".repeatWord(-1)       -> IllegalArgumentException 발생
 * - 10 diff 3                   -> 7
 * - 3 diff 10                   -> 7
 * - 15 clampTo (1..10)          -> 10
 * - (-5) clampTo (0..100)       -> 0
 * - 7 clampTo (1..10)           -> 7
 *
 * [💡 HINT]
 * - 중위 함수를 선언할 때는 함수 선언부 맨 앞에 `infix` 키워드를 붙이고, 매개변수는 반드시 정확히 1개만 받아야 합니다.
 * - `IntRange`의 시작 값은 `range.first`, 끝 값은 `range.last` 프로퍼티로 접근할 수 있습니다.
 * - 절댓값 계산에는 `kotlin.math.abs` 또는 `Math.abs`를 사용할 수 있습니다.
 */

// 1. 문자열 반복 결합 확장 함수 뼈대 (Java static to Kotlin 확장 함수)
private fun String.repeatWord(times: Int): String {
    TODO()
}

// 2. 두 정수 차이 계산 중위 함수 뼈대 (Java static to Kotlin infix 함수)
private infix fun Int.diff(other: Int): Int {
    TODO()
}

// 3. 범위 제한 중위 함수 뼈대 (IntRange 결합 Kotlin infix 함수)
private infix fun Int.clampTo(range: IntRange): Int {
    TODO()
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

    println("✅ Quiz02 테스트 통과! (Java to Idiomatic Kotlin 리팩토링 및 infix 체화 완료)")
}
