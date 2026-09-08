package Lec02

/*
 * [학습 목표 & 복습 개념]
 * - Safe Call(?.) 연산자의 동작 원리와 Nullable 타입 안전 호출 방식을 체화합니다.
 * - Elvis(?:) 연산자의 기본 동작 원리와 대체 기본값 지정 방식을 학습합니다.
 * - 코틀린의 Safe Call과 Elvis 연산자를 결합하여 안전하고 간결한 코드를 작성합니다.
 *
 * [문제 설명]
 * 전달받은 Nullable 문자열(str: String?)을 안전하게 처리하는 두 함수를 완성하세요.
 *
 * 1. getSafeLength(str: String?): Int
 *    - 입력된 문자열의 길이를 반환합니다.
 *    - 단, str이 null인 경우 기본값 0을 반환합니다.
 *
 * 2. getUpperOrNull(str: String?): String?
 *    - 입력된 문자열을 대문자로 변환하여 반환합니다.
 *    - 단, str이 null인 경우 대문자 변환을 시도하지 않고 그대로 null을 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - `if (str == null)` 문을 사용하지 말고, 코틀린 고유의 Safe Call(?.) 및 Elvis(?:) 연산자를 사용하세요.
 * - getSafeLength 함수는 Safe Call과 Elvis 연산자를 함께 결합하여 구현하세요.
 * - getUpperOrNull 함수는 Safe Call 연산자만 사용하여 단일 표현식으로 구현하세요.
 *
 * [입출력 및 기대 결과]
 * - getSafeLength("kotlin") -> 6
 * - getSafeLength(null)     -> 0
 * - getUpperOrNull("hello") -> "HELLO"
 * - getUpperOrNull(null)    -> null
 *
 * [💡 HINT]
 * - `?.` 연산자는 참조 대상이 null이 아닐 때만 뒷부분 프로퍼티나 메서드를 실행하고, null이면 연산 결과 전체가 null이 됩니다.
 * - `?:` 연산자는 좌변의 결과가 null인 경우 우변의 값을 대신 채택합니다.
 */

// 1. 문자열 길이 조회 함수 뼈대 (null일 경우 0)
private fun getSafeLength(str: String?): Int {
    TODO()
}

// 2. 대문자 변환 함수 뼈대 (null일 경우 null)
private fun getUpperOrNull(str: String?): String? {
    TODO()
}

fun main() {
    // 1. getSafeLength 검증
    check(getSafeLength("kotlin") == 6) { "문자열 'kotlin'의 길이는 6이어야 합니다. (현재: ${getSafeLength("kotlin")})" }
    check(getSafeLength("") == 0) { "빈 문자열의 길이는 0이어야 합니다. (현재: ${getSafeLength("")})" }
    check(getSafeLength(null) == 0) { "null 입력 시 길이는 0이어야 합니다. (현재: ${getSafeLength(null)})" }

    // 2. getUpperOrNull 검증
    check(getUpperOrNull("hello") == "HELLO") { "'hello'의 대문자 결과는 'HELLO'이어야 합니다. (현재: ${getUpperOrNull("hello")})" }
    check(getUpperOrNull(null) == null) { "null 입력 시 대문자 변환 결과는 null이어야 합니다. (현재: ${getUpperOrNull(null)})" }

    println("✅ Quiz01 테스트 통과! (Safe Call 및 Elvis 기본 연산 정상 동작)")
}
