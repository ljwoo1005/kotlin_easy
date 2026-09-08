package Lec02

/*
 * [핵심 해결 아이디어]
 * - 'str?.length'는 str이 null이 아니면 길이를 반환하고, null이면 null을 반환합니다.
 *   여기에 Elvis 연산자(?:)를 붙여 '?: 0'으로 처리하면 null일 때 기본값 0을 안전하게 반환할 수 있습니다.
 * - 'str?.uppercase()'는 str이 null일 경우 uppercase()를 아예 호출하지 않고 null을 반환하므로,
 *   추가적인 null 체크 없이 간결하게 Nullable 문자열을 처리할 수 있습니다.
 */

// 1. 문자열 길이 조회 모범 답안 (Safe Call과 Elvis 결합)
private fun getSafeLength(str: String?): Int {
    // str이 null이면 str?.length는 null이 되고, Elvis 연산자에 의해 0이 반환됨
    return str?.length ?: 0
}

// 2. 대문자 변환 모범 답안 (단일 표현식 Safe Call)
private fun getUpperOrNull(str: String?): String? {
    // str이 null이면 uppercase()를 실행하지 않고 null을 즉시 반환함
    return str?.uppercase()
}

fun main() {
    // 1. getSafeLength 검증
    check(getSafeLength("kotlin") == 6) { "문자열 'kotlin'의 길이는 6이어야 합니다. (현재: ${getSafeLength("kotlin")})" }
    check(getSafeLength("") == 0) { "빈 문자열의 길이는 0이어야 합니다. (현재: ${getSafeLength("")})" }
    check(getSafeLength(null) == 0) { "null 입력 시 길이는 0이어야 합니다. (현재: ${getSafeLength(null)})" }

    // 2. getUpperOrNull 검증
    check(getUpperOrNull("hello") == "HELLO") { "'hello'의 대문자 결과는 'HELLO'이어야 합니다. (현재: ${getUpperOrNull("hello")})" }
    check(getUpperOrNull(null) == null) { "null 입력 시 대문자 변환 결과는 null이어야 합니다. (현재: ${getUpperOrNull(null)})" }

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 보일러플레이트 제거: 자바에서는 'if (str == null) return 0; return str.length();' 처럼
 *    매번 null 검사 조건문을 작성해야 하지만, 코틀린은 'str?.length ?: 0' 한 줄로 표현 가능합니다.
 * 2. 타입 시스템의 NPE 원천 차단: 코틀린은 Non-null 타입(String)과 Nullable 타입(String?)을 컴파일 타임에
 *    구분하므로, 실수로 null 체크 없이 메서드를 호출하는 위험을 컴파일 에러 단계에서 방지합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. Safe Call 누락 후 direct 호출: Nullable 변수에 'str.length'를 바로 호출하면 컴파일 에러가 발생합니다.
 *    반드시 물음표 점(?.)을 붙여 안전하게 호출해야 합니다.
 * 2. 불필요한 null 아님 단언(!!) 사용: 기본값을 줄 수 있거나 null을 허용할 수 있는 상황에서 'str!!.length'를
 *    남용하면 런타임에 NullPointerException이 발생할 수 있으므로, Safe Call과 Elvis 연산자를 우선해야 합니다.
 */
