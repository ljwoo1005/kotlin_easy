package Lec03

/*
 * [학습 목표 & 복습 개념]
 * - 자바와 달리 암시적 타입 변환이 없는 코틀린의 기본 타입 간 명시적 변환(toDouble(), toLong())을 체화합니다.
 * - Nullable 기본 타입(Int?)에 대한 Safe Call(?.)과 Elvis(?:) 연산자를 결합한 안전한 타입 변환을 학습합니다.
 * - 문자열 인덱싱(str[index])과 문자열 템플릿(${})을 결합하여 문자열을 다루는 관용적 코드를 익힙니다.
 *
 * [문제 설명]
 * 데이터 집계 및 포맷팅을 담당하는 3개의 기초 유틸리티 함수를 완성하세요.
 *
 * 1. calculateRatio(count: Int, total: Int): Double
 *    - 전체(total) 대비 건수(count)의 비율(0.0 ~ 1.0)을 계산하여 Double 타입으로 반환합니다.
 *    - 코틀린에서는 정수끼리 나눗셈을 하면 소수점이 버려지므로, 명시적으로 Double 타입으로 변환 후 연산해야 합니다.
 *    - total이 0 이하인 경우 0.0을 반환합니다.
 *
 * 2. addBonusPoints(base: Long, bonus: Int?): Long
 *    - 기본 점수(base)에 추가 보너스 점수(bonus)를 더한 최종 점수를 계산합니다.
 *    - bonus가 null인 경우 보너스 점수를 0점으로 간주하여 기본 점수를 그대로 유지합니다.
 *    - Int? 타입인 bonus를 Safe Call(?.)과 Elvis(?:)를 활용해 안전하게 Long으로 변환하여 더해야 합니다.
 *
 * 3. formatInitials(name: String): String
 *    - 전달받은 이름(name)의 '첫 글자'와 '마지막 글자'를 추출하여 "[첫글자...마지막글자]" 형식의 문자열로 반환합니다.
 *    - 문자열 인덱싱(name[index])과 문자열 템플릿(${})을 활용하세요.
 *    - 예: "KOTLIN" -> "[K...N]", "A" -> "[A...A]"
 *
 * [요구사항 & 제약조건]
 * - calculateRatio: 정수 나눗셈 방지를 위해 .toDouble() 변환 메서드를 반드시 명시적으로 호출하세요.
 * - addBonusPoints: if-null 검사를 사용하지 말고, Safe Call(?.)과 Elvis(?:)를 결합하여 bonus를 Long으로 변환하세요.
 * - formatInitials: Java의 charAt() 대신 코틀린 인덱싱 연산자([])와 문자열 템플릿(${})을 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - calculateRatio(1, 4)       -> 0.25
 * - calculateRatio(0, 10)      -> 0.0
 * - calculateRatio(5, 0)       -> 0.0
 * - addBonusPoints(1000L, 500) -> 1500L
 * - addBonusPoints(1000L, null)-> 1000L
 * - formatInitials("KOTLIN")   -> "[K...N]"
 * - formatInitials("JAVA")     -> "[J...A]"
 *
 * [💡 HINT]
 * - 코틀린의 기본 타입 변환은 toLong(), toDouble(), toInt() 등의 명시적 메서드를 호출해야 합니다.
 * - `bonus?.toLong() ?: 0L` 패턴을 사용하면 null 처리와 타입 변환을 한 번에 해결할 수 있습니다.
 * - 문자열의 길이는 `name.length`이며, 마지막 인덱스는 `name.length - 1`입니다.
 */

// 1. 비율 계산 함수 뼈대
private fun calculateRatio(count: Int, total: Int): Double {
    TODO()
}

// 2. 보너스 점수 합산 함수 뼈대
private fun addBonusPoints(base: Long, bonus: Int?): Long {
    TODO()
}

// 3. 이니셜 포맷팅 함수 뼈대
private fun formatInitials(name: String): String {
    TODO()
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

    println("✅ Quiz01 테스트 통과! (기본 타입 명시적 변환 및 문자열 인덱싱 정상 동작)")
}
