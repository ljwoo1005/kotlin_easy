package Lec15

/*
 * [학습 목표 & 복습 개념]
 * - Java의 명령형 컬렉션 초기화(`new HashMap<>()` + `put`)를 코틀린의 관용적인 불변 Map(`mapOf` + `to`)으로 리팩토링합니다.
 * - Java의 전통적인 인덱스 기반 for 루프(`for (int i=0; i<list.size(); i++)`)를 코틀린의 `withIndex()` 구조분해 순회로 전환합니다.
 * - 불변 컬렉션과 가변 컬렉션을 구분하고 적재적소에 사용하는 원칙을 체화합니다.
 *
 * [문제 설명]
 * 아래는 Java 스타일로 작성된 컬렉션 처리 클래스입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaCollectionHandler {
 *     // 1. 과일 이름과 가격을 매핑하는 불변 Map 생성
 *     public Map<String, Integer> createPriceMap() {
 *         Map<String, Integer> map = new HashMap<>();
 *         map.put("Apple", 1000);
 *         map.put("Banana", 1500);
 *         map.put("Cherry", 2000);
 *         return Collections.unmodifiableMap(map);
 *     }
 *
 *     // 2. 인덱스와 요소를 조합하여 "0번: Apple" 형태의 문자열 리스트 생성
 *     public List<String> formatWithIndex(List<String> items) {
 *         List<String> result = new ArrayList<>();
 *         for (int i = 0; i < items.size(); i++) {
 *             result.add(i + "번: " + items.get(i));
 *         }
 *         return result;
 *     }
 * }
 * ----------------------------------------------------
 * 위 Java 코드를 코틀린의 관용적(Idiomatic) 스타일로 리팩토링한 함수 2개를 완성하세요.
 *
 * [요구사항 & 제약조건]
 * - createPriceMap: Java의 `put()` 호출 방식 대신 `mapOf`와 중위 함수 `to`를 사용하여 불변 Map을 반환하세요.
 * - formatWithIndex: 전통적인 인덱스 루프 대신 `items.withIndex()`를 순회하며 인덱스와 값을 구조분해하여 문자열 템플릿(`"${idx}번: ${item}"`)으로 변환하세요.
 * - formatWithIndex: 결과 리스트를 누적할 때는 가변 리스트(`mutableListOf`)를 활용하되 반환 타입은 불변 `List<String>`으로 반환하세요.
 *
 * [입출력 및 기대 결과]
 * - val priceMap = createPriceMap()
 * - priceMap["Apple"]  -> 1000
 * - priceMap["Banana"] -> 1500
 * - priceMap["Cherry"] -> 2000
 * - formatWithIndex(listOf("Apple", "Banana")) -> listOf("0번: Apple", "1번: Banana")
 *
 * [💡 HINT]
 * - `for ((idx, item) in items.withIndex())` 구문을 사용하면 루프 내에서 인덱스와 요소를 한 번에 편리하게 다룰 수 있습니다.
 * - 가변 리스트(`mutableListOf`)는 코틀린에서 상위 불변 인터페이스인 `List`로 암시적 업캐스팅되어 안전하게 반환됩니다.
 */

// 1. 가격 맵 생성 함수 뼈대 (Java to Kotlin 리팩토링)
private fun createPriceMap(): Map<String, Int> {
    TODO()
}

// 2. 인덱스 포맷팅 리스트 생성 함수 뼈대 (withIndex 활용 리팩토링)
private fun formatWithIndex(items: List<String>): List<String> {
    TODO()
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

    println("✅ Quiz02 테스트 통과! (Java to Idiomatic Kotlin 리팩토링 완료)")
}
