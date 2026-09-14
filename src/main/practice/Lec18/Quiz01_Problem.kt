package Lec18

/*
 * [학습 목표 & 복습 개념]
 * - 컬렉션의 핵심 필터링 및 변환 함수인 `filter`, `map`, `mapNotNull`의 동작 방식을 체화합니다.
 * - 컬렉션의 모든 요소가 조건을 만족하는지 검사하는 `all`, `none`, `any` 술어(Predicate) 함수의 차이를 이해합니다.
 * - `firstOrNull`, `lastOrNull`, `count`, `sortedByDescending`을 활용하여 조건에 맞는 데이터를 안전하게 집계하고 정렬하는 방법을 학습합니다.
 *
 * [문제 설명]
 * 청과물 데이터 목록(`List<Quiz18Fruit>`)을 다루는 3가지 기초 컬렉션 함수형 처리 함수를 완성하세요.
 *
 * 1. findInStockFruitNames(fruits: List<Quiz18Fruit>): List<String>
 *    - 현재가(`currentPrice`)가 존재하는(null이 아닌) 과일들만 골라내어 해당 과일의 이름을 리스트로 반환합니다.
 *    - `mapNotNull`을 사용하거나 `filter`와 `map`의 조합을 활용하세요.
 *
 * 2. checkFruitStockStatus(fruits: List<Quiz18Fruit>): Triple<Boolean, Boolean, Boolean>
 *    - 과일 목록의 상태를 3가지 술어 함수로 진단하여 Triple(first, second, third)로 반환합니다.
 *      - first: 목록의 모든 과일의 출고가(`factoryPrice`)가 0원 초과인가? (전부 만족 시 true)
 *      - second: 목록에 현재가(`currentPrice`)가 5,000원 이상인 과일이 하나라도 있는가? (하나라도 만족 시 true)
 *      - third: 목록에 과일 이름(`name`)이 빈 문자열("")인 과일이 전혀 없는가? (단 하나도 없으면 true)
 *
 * 3. getTopPricedFruitInfo(fruits: List<Quiz18Fruit>): Pair<Int, String?>
 *    - Pair(first, second) 형태로 반환합니다.
 *      - first: 현재가(`currentPrice`)가 정상 책정된(null이 아닌) 유효 과일의 총 개수(`count`)
 *      - second: 현재가(`currentPrice`)가 정상 책정된 과일 중 가장 가격이 비싼 과일의 이름(`name`).
 *                (과일이 전혀 없거나 가격이 책정된 과일이 없으면 null 반환)
 *
 * [요구사항 & 제약조건]
 * - findInStockFruitNames: 반환 타입은 `List<String>`이어야 합니다.
 * - checkFruitStockStatus: 반드시 `all`, `any`, `none` 함수를 각각 활용하여 Triple을 구성하세요.
 * - getTopPricedFruitInfo: `count`와 `sortedByDescending`, `firstOrNull`을 활용하여 안전하게 구현하세요.
 *
 * [입출력 및 기대 결과]
 * val sampleFruits = listOf(
 *     Quiz18Fruit(1L, "사과", 1000L, 1500L),
 *     Quiz18Fruit(2L, "바나나", 3000L, 4500L),
 *     Quiz18Fruit(3L, "수박", 10000L, 12000L),
 *     Quiz18Fruit(4L, "시가미정과일", 2000L, null)
 * )
 * - findInStockFruitNames(sampleFruits) -> ["사과", "바나나", "수박"]
 * - checkFruitStockStatus(sampleFruits) -> Triple(true, true, true)
 * - getTopPricedFruitInfo(sampleFruits) -> Pair(3, "수박")
 *
 * [💡 HINT]
 * - `mapNotNull`은 람다의 반환값이 null이 아닌 결과만 모아서 새로운 리스트로 만들어줍니다.
 * - `all`, `any`, `none`은 컬렉션의 요소를 순회하며 조건식을 평가해 Boolean 결과를 즉시 도출합니다.
 * - `sortedByDescending { it.currentPrice }`를 호출하면 높은 가격순으로 정렬할 수 있으며, `firstOrNull()`로 첫 요소를 안전하게 가져올 수 있습니다.
 */

// 실습용 과일 도메인 데이터 클래스
data class Quiz18Fruit(
    val id: Long,
    val name: String,
    val factoryPrice: Long,
    val currentPrice: Long?
) {
    // 출고가와 현재가가 동일한지 판별하는 커스텀 게터 프로퍼티
    val isSamePrice: Boolean
        get() = currentPrice != null && factoryPrice == currentPrice
}

// 1. 판매 중인 과일 이름 목록 추출 함수 뼈대
private fun findInStockFruitNames(fruits: List<Quiz18Fruit>): List<String> {
    TODO()
}

// 2. 과일 목록의 재고 상태 조건 진단 함수 뼈대
private fun checkFruitStockStatus(fruits: List<Quiz18Fruit>): Triple<Boolean, Boolean, Boolean> {
    TODO()
}

// 3. 유효 과일 수와 최고가 과일 이름 조회 함수 뼈대
private fun getTopPricedFruitInfo(fruits: List<Quiz18Fruit>): Pair<Int, String?> {
    TODO()
}

fun main() {
    val sampleFruits = listOf(
        Quiz18Fruit(1L, "사과", 1_000L, 1_500L),
        Quiz18Fruit(2L, "바나나", 3_000L, 4_500L),
        Quiz18Fruit(3L, "수박", 10_000L, 12_000L),
        Quiz18Fruit(4L, "시가미정과일", 2_000L, null)
    )

    // 1. findInStockFruitNames 검증
    val inStockNames = findInStockFruitNames(sampleFruits)
    check(inStockNames == listOf("사과", "바나나", "수박")) {
        "현재가가 null인 과일을 제외하고 ['사과', '바나나', '수박']이 반환되어야 합니다. 실제: $inStockNames"
    }

    // 2. checkFruitStockStatus 검증
    val status = checkFruitStockStatus(sampleFruits)
    check(status.first) { "모든 과일의 출고가는 0원 초과이므로 all 조건은 true여야 합니다." }
    check(status.second) { "수박(12,000원)이 존재하므로 5,000원 이상 any 조건은 true여야 합니다." }
    check(status.third) { "이름이 빈 문자열인 과일이 없으므로 none 조건은 true여야 합니다." }

    val invalidFruits = listOf(
        Quiz18Fruit(5L, "", 0L, 2_000L)
    )
    val invalidStatus = checkFruitStockStatus(invalidFruits)
    check(!invalidStatus.first) { "출고가가 0원이므로 all 조건은 false여야 합니다." }
    check(!invalidStatus.second) { "5,000원 이상인 과일이 없으므로 any 조건은 false여야 합니다." }
    check(!invalidStatus.third) { "이름이 빈 과일이 존재하므로 none 조건은 false여야 합니다." }

    // 3. getTopPricedFruitInfo 검증
    val topFruitInfo = getTopPricedFruitInfo(sampleFruits)
    check(topFruitInfo.first == 3) { "유효한 현재가를 가진 과일 개수는 3개여야 합니다. 실제: ${topFruitInfo.first}" }
    check(topFruitInfo.second == "수박") { "가장 가격이 비싼 과일은 수박이어야 합니다. 실제: ${topFruitInfo.second}" }

    val emptyInfo = getTopPricedFruitInfo(emptyList())
    check(emptyInfo.first == 0 && emptyInfo.second == null) {
        "빈 리스트 전달 시 Pair(0, null)이 반환되어야 합니다."
    }

    println("✅ Quiz01 테스트 통과! (기초 컬렉션 함수형 처리 완벽 체화)")
}
