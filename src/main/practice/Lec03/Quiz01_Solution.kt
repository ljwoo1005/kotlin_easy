package Lec03

/*
 * [핵심 해결 아이디어]
 * - 코틀린은 정수형 간 연산(Int / Int) 시 소수점을 자동으로 절삭하므로, 정확한 비율 계산을 위해 toDouble() 메서드를 명시적으로 호출합니다.
 * - Nullable 정수형(Int?)은 안전한 호출(?.)과 toLong()을 결합한 뒤, Elvis(?:) 연산자로 null일 경우 기본값 0L을 반환하도록 하여 NPE를 원천 방지합니다.
 * - 문자열 인덱싱 연산자([])를 사용하여 첫 글자([0])와 마지막 글자([length - 1])에 직관적으로 접근하고, 코틀린의 문자열 템플릿(${})으로 조합합니다.
 */

// 1. 비율 계산 모범 답안 함수
private fun calculateRatio(count: Int, total: Int): Double {
    // total이 0 이하일 경우 0.0 반환
    if (total <= 0) return 0.0
    // 정수 나눗셈 시 소수점 보존을 위해 명시적으로 Double 변환
    return count.toDouble() / total.toDouble()
}

// 2. 보너스 점수 합산 모범 답안 함수
private fun addBonusPoints(base: Long, bonus: Int?): Long {
    // Safe Call(?.)과 toLong()으로 변환 후, null이면 Elvis(?:)로 0L 대체
    val bonusLong = bonus?.toLong() ?: 0L
    return base + bonusLong
}

// 3. 이니셜 포맷팅 모범 답안 함수
private fun formatInitials(name: String): String {
    // 문자열 인덱싱([])과 템플릿(${})을 조합하여 포맷팅
    val first = name[0]
    val last = name[name.length - 1]
    return "[$first...$last]"
}

fun main() {
    // 1. calculateRatio 검증
    check(calculateRatio(1, 4) == 0.25) { "1 / 4의 비율은 0.25이어야 합니다. (현재: ${calculateRatio(1, 4)})" }
    check(calculateRatio(0, 10) == 0.0) { "0 / 10의 비율은 0.0이어야 합니다. (현재: ${calculateRatio(0, 10)})" }
    check(calculateRatio(5, 0) == 0.0) { "total이 0 이하이면 0.0을 반환해야 합니다. (현재: ${calculateRatio(5, 0)})" }

    // 2. addBonusPoints 검증
    check(addBonusPoints(1000L, 500) == 1500L) { "1000L + 500은 1500L이어야 합니다. (현재: ${addBonusPoints(1000L, 500)})" }
    check(addBonusPoints(1000L, null) == 1000L) { "bonus가 null이면 1000L이어야 합니다. (현재: ${addBonusPoints(1000L, null)})" }

    // 3. formatInitials 검증
    check(formatInitials("KOTLIN") == "[K...N]") { "KOTLIN의 이니셜은 [K...N]이어야 합니다. (현재: ${formatInitials("KOTLIN")})" }
    check(formatInitials("JAVA") == "[J...A]") { "JAVA의 이니셜은 [J...A]이어야 합니다. (현재: ${formatInitials("JAVA")})" }
    check(formatInitials("A") == "[A...A]") { "한 글자 'A'의 이니셜은 [A...A]이어야 합니다. (현재: ${formatInitials("A")})" }

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 명시적 변환을 통한 오류 예방: 자바에서는 int -> long, float -> double 등 암시적 프로모션이 발생하여 의도치 않은 타입 버그가 생길 수 있지만, 코틀린은 toLong(), toDouble() 등의 명시적 변환을 강제하여 타입 안전성을 극대화합니다.
 * 2. 안전한 Nullable 기본 타입 처리: 자바의 Integer 객체가 null일 때 unboxing 시 NPE가 발생하는 반면, 코틀린은 Int?에 대해 'bonus?.toLong() ?: 0L'과 같이 NPE 위험 없이 한 줄로 처리할 수 있습니다.
 * 3. 직관적인 문자열 인덱싱: 자바의 'name.charAt(0)' 대신 배열처럼 대괄호 인덱싱('name[0]')을 지원하여 가독성이 높습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 암시적 변환 기대: 'val l: Long = 10'처럼 Int 리터럴을 Long 타입 변수에 직접 대입하려 하면 컴파일 에러가 발생합니다. '10L' 접미사를 붙이거나 '10.toLong()'을 명시해야 합니다.
 * 2. 정수 나눗셈 후 toDouble() 호출: '(count / total).toDouble()'처럼 나눗셈을 먼저 수행하면 이미 소수점이 절삭된 0이 되므로, 반드시 나눗셈 전에 'count.toDouble() / total' 형태로 변환해야 합니다.
 */
