package Lec15

/*
 * [학습 목표 & 복습 개념]
 * - 코틀린의 표준 불변 컬렉션(List, Map) 생성 문법과 중위 함수 `to`를 이용한 Pair 생성을 체화합니다.
 * - 대괄호(`[]`)를 사용한 직관적인 컬렉션 인덱싱 및 키 기반 요소 접근 방식을 학습합니다.
 * - 컬렉션이 비어있을 때의 예외 처리와 Map 조회 결과(nullable)에 대한 Elvis(`?:`) 연산자 활용법을 익힙니다.
 *
 * [문제 설명]
 * 컬렉션의 기초적인 생성과 접근을 다루는 3개의 함수를 완성하세요.
 *
 * 1. getFirstAndLast(numbers: List<Int>): Pair<Int, Int>
 *    - 주어진 정수 리스트의 첫 번째 원소와 마지막 원소를 Pair로 묶어 반환합니다.
 *    - 리스트가 비어있을 경우 IllegalArgumentException("리스트가 비어 있습니다.")을 발생시킵니다.
 *
 * 2. createDayMap(): Map<Int, String>
 *    - 1 -> "MONDAY", 2 -> "TUESDAY", 3 -> "WEDNESDAY"를 매핑하는 불변 Map을 생성하여 반환합니다.
 *
 * 3. getDayNameOrDefault(map: Map<Int, String>, day: Int): String
 *    - Map에서 해당 day(숫자)에 해당하는 요일 문자열을 조회하여 반환합니다.
 *    - 일치하는 요일이 없을 경우 기본값 "UNKNOWN"을 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - getFirstAndLast: 첫 번째와 마지막 요소 접근 시 대괄호 인덱스(`numbers[0]`, `numbers[numbers.lastIndex]`)를 사용하세요.
 * - getFirstAndLast: 리스트가 비어있다면 `if (numbers.isEmpty())` 등을 통해 IllegalArgumentException을 발생시키세요.
 * - createDayMap: 불변 맵 생성 함수 `mapOf`와 중위 함수 `to`를 반드시 사용하세요.
 * - getDayNameOrDefault: get() 메서드 대신 대괄호(`map[day]`) 접근과 Elvis(`?:`) 연산자를 사용하세요.
 *
 * [입출력 및 기대 결과]
 * - getFirstAndLast(listOf(10, 20, 30)) -> Pair(10, 30)
 * - getFirstAndLast(emptyList())         -> IllegalArgumentException 발생
 * - val dayMap = createDayMap()
 * - getDayNameOrDefault(dayMap, 1)       -> "MONDAY"
 * - getDayNameOrDefault(dayMap, 5)       -> "UNKNOWN"
 *
 * [💡 HINT]
 * - 코틀린에서 리스트의 마지막 인덱스는 `numbers.lastIndex` 프로퍼티를 통해 손쉽게 가져올 수 있습니다.
 * - `map[key]`의 반환 타입은 값이 존재하지 않을 수 있으므로 nullable 타입(`String?`)입니다.
 */

// 1. 첫 번째와 마지막 원소 추출 함수 뼈대
private fun getFirstAndLast(numbers: List<Int>): Pair<Int, Int> {
    TODO()
}

// 2. 요일 불변 맵 생성 함수 뼈대
private fun createDayMap(): Map<Int, String> {
    TODO()
}

// 3. 요일 이름 조회 및 기본값 반환 함수 뼈대
private fun getDayNameOrDefault(map: Map<Int, String>, day: Int): String {
    TODO()
}

fun main() {
    // 1. getFirstAndLast 검증
    val testList = listOf(10, 20, 30, 40)
    val firstAndLast = getFirstAndLast(testList)
    check(firstAndLast.first == 10) { "첫 번째 원소는 10이어야 합니다." }
    check(firstAndLast.second == 40) { "마지막 원소는 40이어야 합니다." }

    val emptyListThrown = runCatching { getFirstAndLast(emptyList()) }.isFailure
    check(emptyListThrown) { "빈 리스트를 전달하면 IllegalArgumentException이 발생해야 합니다." }

    // 2. createDayMap 검증
    val dayMap = createDayMap()
    check(dayMap.size == 3) { "요일 맵의 크기는 3이어야 합니다." }
    check(dayMap[1] == "MONDAY") { "1은 MONDAY와 매핑되어야 합니다." }
    check(dayMap[2] == "TUESDAY") { "2는 TUESDAY와 매핑되어야 합니다." }
    check(dayMap[3] == "WEDNESDAY") { "3은 WEDNESDAY와 매핑되어야 합니다." }

    // 3. getDayNameOrDefault 검증
    check(getDayNameOrDefault(dayMap, 1) == "MONDAY") { "1에 대한 요일은 MONDAY여야 합니다." }
    check(getDayNameOrDefault(dayMap, 2) == "TUESDAY") { "2에 대한 요일은 TUESDAY여야 합니다." }
    check(getDayNameOrDefault(dayMap, 99) == "UNKNOWN") { "존재하지 않는 키 99는 UNKNOWN을 반환해야 합니다." }

    println("✅ Quiz01 테스트 통과! (불변 컬렉션 생성 및 대괄호 접근)")
}
