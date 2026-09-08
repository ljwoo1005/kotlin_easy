package Lec15

/*
 * [핵심 해결 아이디어]
 * - 자바에서는 Map을 생성하기 위해 인스턴스를 만들고 put() 메서드를 반복 호출한 후 Collections.unmodifiableMap()으로 감싸야 했습니다.
 * - 반면 코틀린에서는 `mapOf(key to value)` 한 줄로 불변 Map을 직관적이고 안전하게 선언할 수 있습니다.
 * - 리스트의 인덱스와 요소를 함께 다룰 때, 자바의 인덱스 for 루프(`i = 0; i < size; i++`)는 인덱스 경계 오류(IndexOutOfBounds)의 위험이 있습니다.
 * - 코틀린의 `items.withIndex()`와 구조분해 선언(`(idx, item)`)을 활용하면 인덱스와 원소를 타입 안전하고 우아하게 순회할 수 있습니다.
 */

// 1. 가격 맵 생성 모범 답안 함수
private fun createPriceMap(): Map<String, Int> {
    // mapOf와 중위 함수 to를 사용하여 불변 Map을 선언적으로 생성
    return mapOf(
        "Apple" to 1000,
        "Banana" to 1500,
        "Cherry" to 2000
    )
}

// 2. 인덱스 포맷팅 리스트 생성 모범 답안 함수
private fun formatWithIndex(items: List<String>): List<String> {
    // 결과를 누적하기 위한 가변 리스트 생성 (기본 구현체: ArrayList)
    val result = mutableListOf<String>()

    // withIndex()를 통한 인덱스-값 동시 순회 및 구조분해
    for ((idx, item) in items.withIndex()) {
        // 문자열 템플릿을 사용하여 깔끔하게 포맷팅 후 추가
        result.add("${idx}번: ${item}")
    }

    // MutableList는 불변 List의 하위 타입이므로 추가 변환 없이 안전하게 반환
    return result
}

fun main() {
    // 1. createPriceMap 검증
    val priceMap = createPriceMap()
    check(priceMap.size == 3) { "가격 맵의 요소 개수는 3개여야 합니다." }
    check(priceMap["Apple"] == 1000) { "Apple의 가격은 1000이어야 합니다." }
    check(priceMap["Banana"] == 1500) { "Banana의 가격은 1500이어야 합니다." }
    check(priceMap["Cherry"] == 2000) { "Cherry의 가격은 2000이어야 합니다." }

    // 2. formatWithIndex 검증
    val fruits = listOf("사과", "바나나", "체리")
    val formattedList = formatWithIndex(fruits)
    check(formattedList.size == 3) { "결과 리스트 크기는 원본과 동일하게 3이어야 합니다." }
    check(formattedList[0] == "0번: 사과") { "0번째 요소 포맷이 올바르지 않습니다: ${formattedList[0]}" }
    check(formattedList[1] == "1번: 바나나") { "1번째 요소 포맷이 올바르지 않습니다: ${formattedList[1]}" }
    check(formattedList[2] == "2번: 체리") { "2번째 요소 포맷이 올바르지 않습니다: ${formattedList[2]}" }

    // 빈 리스트 전달 시 동작 검증
    val emptyFormatted = formatWithIndex(emptyList())
    check(emptyFormatted.isEmpty()) { "빈 리스트 전달 시 빈 결과 리스트를 반환해야 합니다." }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 선언적 컬렉션 초기화: 자바의 다소 장황한 `map.put()` 연속 호출과 `Collections.unmodifiableMap()` 래핑을 코틀린의 `mapOf("A" to 1)` 단일 표현식으로 대체하여 코드량을 줄이고 가독성을 극대화합니다.
 * 2. 안전한 인덱스 루프: 자바에서는 `list.get(i)` 호출 시 인덱스 오타나 경계 조건 실수로 인한 `IndexOutOfBoundsException` 위험이 상존하지만, 코틀린의 `withIndex()`는 인덱스와 값을 짝지어 안전하게 제공합니다.
 * 3. 가변/불변 타입 분리: 코틀린은 가변 컬렉션(`MutableList`)과 읽기 전용 컬렉션(`List`)을 컴파일 레벨에서 명확히 분리하여, 외부로 반환되는 컬렉션의 임의 수정을 효과적으로 방어합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 무분별한 가변 컬렉션 반환: 메서드 내부에서 `mutableListOf()`를 사용했더라도 반환 타입은 불변 인터페이스인 `List`로 선언하여 외부에 내부 컬렉션 변경 권한을 노출하지 않는 것이 좋은 설계 습관입니다.
 * 2. 중위 함수 `to`의 성능 고려: `1 to "A"`는 내부적으로 `Pair(1, "A")` 객체를 생성하므로, 대량의 데이터를 초기화할 때 성능이 극도로 민감한 영역이라면 직접 put을 사용하는 방식도 고려할 수 있지만, 일반적인 설정이나 소규모 컬렉션에서는 가독성이 훨씬 우수합니다.
 */
