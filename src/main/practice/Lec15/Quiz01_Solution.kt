package Lec15

/*
 * [핵심 해결 아이디어]
 * - 코틀린의 List는 get(index) 대신 대괄호 연산자 `list[index]`를 제공하여 배열처럼 직관적으로 접근할 수 있습니다.
 * - 컬렉션의 첫 번째 원소는 `numbers[0]`, 마지막 원소는 `numbers[numbers.lastIndex]`로 접근할 수 있으며, 빈 컬렉션인 경우 예외를 명시적으로 던져 방어합니다.
 * - 불변 Map은 `mapOf(k to v)`를 통해 생성하며, `to`는 내부적으로 Pair 객체를 생성하는 중위(infix) 함수입니다.
 * - Map의 값 조회 역시 `map[key]` 대괄호 문법을 사용하며, 키가 없을 경우 null을 반환하므로 Elvis 연산자(`?:`)를 사용하여 안전하게 기본값을 반환합니다.
 */

// 1. 첫 번째와 마지막 원소 추출 모범 답안 함수
private fun getFirstAndLast(numbers: List<Int>): Pair<Int, Int> {
    // 리스트가 비어있는 경우 요구사항에 따라 예외 발생
    if (numbers.isEmpty()) {
        throw IllegalArgumentException("리스트가 비어 있습니다.")
    }

    // 대괄호 연산자와 lastIndex 프로퍼티를 활용하여 양 끝 원소 추출
    val first = numbers[0]
    val last = numbers[numbers.lastIndex]

    return Pair(first, last)
}

// 2. 요일 불변 맵 생성 모범 답안 함수
private fun createDayMap(): Map<Int, String> {
    // mapOf와 중위 함수 to를 사용하여 불변 Map 생성
    return mapOf(
        1 to "MONDAY",
        2 to "TUESDAY",
        3 to "WEDNESDAY"
    )
}

// 3. 요일 이름 조회 및 기본값 반환 모범 답안 함수
private fun getDayNameOrDefault(map: Map<Int, String>, day: Int): String {
    // map[day] 대괄호 접근 및 키가 존재하지 않을 때 Elvis 연산자로 기본값 매핑
    return map[day] ?: "UNKNOWN"
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

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 직관적인 대괄호 인덱싱: 자바에서는 List.get(i), Map.get(k) 등 메서드를 반드시 호출해야 했으나, 코틀린은 배열과 동일하게 대괄호 `list[i]`, `map[k]` 문법을 일관되게 지원하여 코드 가독성이 매우 뛰어납니다.
 * 2. 간결한 불변 컬렉션 생성: 자바 9 이전에는 Map을 초기화하려면 new HashMap<>() 후 put()을 여러 번 호출해야 했지만, 코틀린은 `mapOf(1 to "MONDAY")`처럼 한 줄로 선언할 수 있습니다.
 * 3. 풍부한 표준 프로퍼티: `numbers.size - 1` 대신 `numbers.lastIndex` 같은 직관적인 확장 프로퍼티를 표준 라이브러리에서 기본 제공합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. Java 스타일의 get() 메서드 고집: 코틀린에서도 get() 메서드가 존재하지만, 코틀린다운(Idiomatic) 스타일은 대괄호 `[]` 연산자입니다.
 * 2. Map 조회 시 null 가능성 간과: `map[key]`는 해당 키가 없을 경우 null을 반환하므로, non-null 타입으로 받을 때는 반드시 Elvis 연산자(`?:`)나 적절한 null 처리를 병행해야 컴파일 오류를 방지할 수 있습니다.
 */
