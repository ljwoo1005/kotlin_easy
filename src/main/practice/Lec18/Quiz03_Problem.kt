package Lec18

/*
 * [학습 목표 & 복습 개념]
 * - 복합 객체 구조(`List<Quiz18MarketBox>`)에서 중첩 리스트를 1차원으로 풀어내는 `flatMap` / `flatten`을 실무적으로 체화합니다.
 * - 컬렉션의 중복 요소를 특정 기준값으로 걸러내는 `distinctBy`의 사용법을 익힙니다.
 * - `groupBy`와 컬렉션 집계(`average()`, `sumOf()`)를 결합하여 카테고리별 통계 데이터를 산출하는 파이프라인을 작성합니다.
 * - 다중 조건 필터링(`filter`)과 동적 계산 기준 정렬(`sortedByDescending`)을 연결하는 함수형 체이닝을 체화합니다.
 *
 * [문제 설명]
 * 청과물 유통 센터의 선물 상자 배송 목록 및 상품 카탈로그를 분석하는 3가지 실무형 비즈니스 함수를 구현하세요.
 *
 * 1. getUniqueFruitNamesFromBoxes(boxes: List<Quiz18MarketBox>): List<String>
 *    - 여러 과일 상자(`boxes`)에 나뉘어 담긴 과일들을 1차원 리스트로 모읍니다.
 *    - 과일 이름(`name`)을 기준으로 중복을 제거한 뒤, 과일 이름만 담은 `List<String>`을 반환합니다.
 *
 * 2. calculateCategoryAveragePrices(fruits: List<Quiz18FruitItem>): Map<String, Double>
 *    - 과일 목록을 카테고리(`category`, 예: "국산과일", "열대과일", "감귤류")별로 그룹화합니다.
 *    - 각 카테고리에 속한 과일들의 현재가(`currentPrice`) 평균을 계산하여 `Map<String, Double>`로 반환합니다.
 *
 * 3. findRecommendedDiscountFruits(boxes: List<Quiz18MarketBox>, maxBudget: Long): List<Quiz18FruitItem>
 *    - 모든 상자에 든 과일들을 1차원으로 모읍니다.
 *    - 고객의 예산 한도(`currentPrice <= maxBudget`) 내에 있으면서,
 *      출고가 대비 할인 판매 중인 과일(`factoryPrice > currentPrice`)만 필터링합니다.
 *    - 필터링된 과일들을 할인 금액(`factoryPrice - currentPrice`)이 큰 순서대로 내림차순 정렬하여 반환합니다.
 *
 * [요구사항 & 제약조건]
 * - getUniqueFruitNamesFromBoxes: `flatMap`(또는 `flatten`)과 `distinctBy`, `map`을 체이닝하여 함수형으로 작성하세요.
 * - calculateCategoryAveragePrices: `groupBy`와 `mapValues` 또는 컬렉션의 `average()` 함수를 활용하여 구현하세요.
 * - findRecommendedDiscountFruits: `flatMap`, `filter`, `sortedByDescending`을 활용하여 작성하세요.
 *
 * [입출력 및 기대 결과]
 * val box1 = Quiz18MarketBox("B1", listOf(사과1000, 바나나3000))
 * val box2 = Quiz18MarketBox("B2", listOf(사과1200, 망고8000))
 * - getUniqueFruitNamesFromBoxes(listOf(box1, box2)) -> ["사과", "바나나", "망고"]
 * - calculateCategoryAveragePrices(...) -> {"국산과일"=1100.0, "열대과일"=5500.0}
 * - findRecommendedDiscountFruits(...) -> 할인액이 큰 순서대로 정렬된 리스트
 *
 * [💡 HINT]
 * - `boxes.flatMap { it.fruits }`를 호출하면 상자 객체들 속에 중첩된 과일 리스트들을 단일 리스트로 펼칠 수 있습니다.
 * - `distinctBy { it.name }`을 호출하면 지정한 키 셀렉터 기준으로 첫 번째 요소만 남기고 중복을 제거합니다.
 * - `mapValues { (_, list) -> list.map { it.currentPrice }.average() }`를 사용하면 Map의 값들을 평균값으로 간편하게 변환할 수 있습니다.
 */

// 실습용 과일 상품 데이터 클래스
data class Quiz18FruitItem(
    val id: Long,
    val name: String,
    val category: String,
    val factoryPrice: Long,
    val currentPrice: Long,
    val origin: String
)

// 실습용 과일 선물 상자 데이터 클래스
data class Quiz18MarketBox(
    val boxId: String,
    val fruits: List<Quiz18FruitItem>
)

// 1. 모든 상자에서 중복 없는 과일 이름 목록 추출 함수 뼈대
private fun getUniqueFruitNamesFromBoxes(boxes: List<Quiz18MarketBox>): List<String> {
    TODO()
}

// 2. 카테고리별 과일 현재가 평균 계산 함수 뼈대
private fun calculateCategoryAveragePrices(fruits: List<Quiz18FruitItem>): Map<String, Double> {
    TODO()
}

// 3. 예산 내 할인 과일 추천 및 할인액 순 정렬 함수 뼈대
private fun findRecommendedDiscountFruits(
    boxes: List<Quiz18MarketBox>,
    maxBudget: Long
): List<Quiz18FruitItem> {
    TODO()
}

fun main() {
    val f1 = Quiz18FruitItem(1L, "사과", "국산과일", 2_000L, 1_500L, "국내산") // 할인액: 500원
    val f2 = Quiz18FruitItem(2L, "사과", "국산과일", 2_500L, 2_000L, "국내산") // 할인액: 500원
    val f3 = Quiz18FruitItem(3L, "바나나", "열대과일", 4_000L, 3_000L, "수입산") // 할인액: 1,000원
    val f4 = Quiz18FruitItem(4L, "망고", "열대과일", 10_000L, 7_000L, "수입산") // 할인액: 3,000원
    val f5 = Quiz18FruitItem(5L, "샤인머스캣", "국산과일", 15_000L, 15_000L, "국내산") // 할인 없음(0원)

    val boxA = Quiz18MarketBox("BOX-01", listOf(f1, f3))
    val boxB = Quiz18MarketBox("BOX-02", listOf(f2, f4, f5))
    val allBoxes = listOf(boxA, boxB)

    // 1. getUniqueFruitNamesFromBoxes 검증
    val uniqueNames = getUniqueFruitNamesFromBoxes(allBoxes)
    check(uniqueNames == listOf("사과", "바나나", "망고", "샤인머스캣")) {
        "중복이 제거된 과일 이름 목록이어야 합니다. 실제: $uniqueNames"
    }

    // 2. calculateCategoryAveragePrices 검증
    val allFruits = listOf(f1, f2, f3, f4, f5)
    val avgPrices = calculateCategoryAveragePrices(allFruits)
    // 국산과일: (1500 + 2000 + 15000) / 3 = 18500 / 3 = 6166.666...
    // 열대과일: (3000 + 7000) / 2 = 5000.0
    check(avgPrices.containsKey("국산과일") && avgPrices.containsKey("열대과일")) {
        "모든 카테고리가 맵에 존재해야 합니다."
    }
    check(kotlin.math.abs((avgPrices["열대과일"] ?: 0.0) - 5000.0) < 0.001) {
        "열대과일의 평균가는 5000.0이어야 합니다. 실제: ${avgPrices["열대과일"]}"
    }

    // 3. findRecommendedDiscountFruits 검증 (예산 5,000원 이하, 할인 중인 과일, 할인액 내림차순)
    // 할인 중: f1(할인500, 가격1500), f2(할인500, 가격2000), f3(할인1000, 가격3000), f4(할인3000, 가격7000-예산초과), f5(할인없음)
    // 예산 5000 이하 후보: f1, f2, f3
    // 할인액 내림차순: f3(1000원) -> f1(500원) & f2(500원)
    val recommended = findRecommendedDiscountFruits(allBoxes, 5_000L)
    check(recommended.size == 3) { "조건에 맞는 과일은 f3, f1, f2 총 3개여야 합니다. 실제 크기: ${recommended.size}" }
    check(recommended.first().id == 3L) { "가장 할인액이 큰 f3(바나나, 1000원 할인)가 첫 번째여야 합니다. 실제: ${recommended.first().name}" }

    println("✅ Quiz03 테스트 통과! (실전 청과물 마켓 파이프라인 체화 완료)")
}
