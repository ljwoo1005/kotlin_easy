package Lec18

/*
 * [핵심 해결 아이디어]
 * - 여러 상자에 분산된 상품 목록을 다룰 때, `boxes.flatMap { it.fruits }`를 활용하면 중첩 구조를 풀면서 동시에 필요한 필드를 직접 평탄화된 단일 스트림으로 추출할 수 있습니다.
 * - `distinctBy`를 사용하면 객체의 특정 프로퍼티(예: `name`)를 고유 식별자로 사용하여 최초 발견된 요소만 남기고 중복을 우아하게 제거합니다.
 * - `groupBy`와 `mapValues`의 체이닝은 복합 그룹 통계 연산(평균, 합계 등)을 구현할 때 명령형 for 루프나 누적 맵 변수 없이도 순수 함수형 파이프라인으로 안전하게 처리할 수 있도록 돕습니다.
 * - `sortedByDescending`에 `factoryPrice - currentPrice`와 같은 계산 표현식을 전달하여 별도의 임시 정렬 필드 없이 동적 기준으로 정렬을 수행합니다.
 */

// 실습용 도메인 데이터 클래스(Quiz18FruitItem, Quiz18MarketBox)는 Quiz03_Problem.kt에 선언된 모델을 공유합니다.

// 1. 모든 상자에서 중복 없는 과일 이름 목록 추출 모범 답안
private fun getUniqueFruitNamesFromBoxes(boxes: List<Quiz18MarketBox>): List<String> {
    return boxes
        // 1) 각 상자에서 과일 리스트를 꺼내 1차원 리스트로 평탄화
        .flatMap { it.fruits }
        // 2) 과일 이름(name)을 기준으로 첫 번째 등장 요소만 남기고 중복 제거
        .distinctBy { it.name }
        // 3) 과일 객체에서 이름 문자열만 추출
        .map { it.name }
}

// 2. 카테고리별 과일 현재가 평균 계산 모범 답안
private fun calculateCategoryAveragePrices(fruits: List<Quiz18FruitItem>): Map<String, Double> {
    return fruits
        // 1) category 프로퍼티를 키로 그룹화하여 Map<String, List<Quiz18FruitItem>> 생성
        .groupBy { it.category }
        // 2) 각 카테고리의 과일 리스트를 순회하며 현재가의 평균값(Double)으로 값 변환
        .mapValues { (_, categoryFruits) ->
            categoryFruits.map { it.currentPrice }.average()
        }
}

// 3. 예산 내 할인 과일 추천 및 할인액 순 정렬 모범 답안
private fun findRecommendedDiscountFruits(
    boxes: List<Quiz18MarketBox>,
    maxBudget: Long
): List<Quiz18FruitItem> {
    return boxes
        // 1) 상자 내부 과일 리스트들을 단일 1차원 리스트로 병합
        .flatMap { it.fruits }
        // 2) 예산 이내이면서 실제로 출고가보다 할인되어 판매 중인 과일만 필터링
        .filter { fruit ->
            fruit.currentPrice <= maxBudget && fruit.factoryPrice > fruit.currentPrice
        }
        // 3) 할인 금액(출고가 - 현재가)이 큰 순서대로 내림차순 정렬
        .sortedByDescending { fruit ->
            fruit.factoryPrice - fruit.currentPrice
        }
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
    check(avgPrices.containsKey("국산과일") && avgPrices.containsKey("열대과일")) {
        "모든 카테고리가 맵에 존재해야 합니다."
    }
    check(kotlin.math.abs((avgPrices["열대과일"] ?: 0.0) - 5000.0) < 0.001) {
        "열대과일의 평균가는 5000.0이어야 합니다. 실제: ${avgPrices["열대과일"]}"
    }

    // 3. findRecommendedDiscountFruits 검증 (예산 5,000원 이하, 할인 중인 과일, 할인액 내림차순)
    val recommended = findRecommendedDiscountFruits(allBoxes, 5_000L)
    check(recommended.size == 3) { "조건에 맞는 과일은 f3, f1, f2 총 3개여야 합니다. 실제 크기: ${recommended.size}" }
    check(recommended.first().id == 3L) { "가장 할인액이 큰 f3(바나나, 1000원 할인)가 첫 번째여야 합니다. 실제: ${recommended.first().name}" }

    println("✅ Quiz03 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. distinctBy를 통한 간결한 중복 제거: Java에서는 특정 필드 기준 중복 제거 시 `filter(distinctByKey(Fruit::getName))`와 같은 복잡한 ConcurrentHashMap 트릭을 쓰거나 커스텀 `TreeSet`을 만들어야 하지만, Kotlin은 표준 라이브러리의 `distinctBy` 하나로 직관적으로 해결합니다.
 * 2. 컬렉션 표준 확장 함수 average(): Java Stream에서는 `mapToDouble().average().orElse(0.0)`처럼 기본형 특화 스트림으로 변환하고 Optional을 다뤄야 하지만, Kotlin의 `Iterable<Number>.average()`는 곧바로 `Double` 평균값을 산출합니다.
 * 3. mapValues를 통한 불변 맵 변환: Java에서는 Map의 Value만 변환하려면 EntrySet을 돌며 새로운 Map에 옮겨 담거나 복잡한 Collector를 써야 하지만, Kotlin은 `mapValues`로 가독성 높게 값 컬렉션을 변환합니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 빈 컬렉션의 `average()` 반환값: 비어있는 리스트에서 `average()`를 호출하면 예외가 발생하는 대신 `Double.NaN`을 반환합니다. 만약 빈 리스트에 대한 방어가 필요하다면 `if (list.isEmpty()) 0.0 else list.average()` 또는 삼항식으로 안전 처리를 해주어야 합니다.
 * 2. flatMap vs map+flatten의 가독성 선택: `boxes.map { it.fruits }.flatten()`과 `boxes.flatMap { it.fruits }`는 기능적으로 동일합니다. 다만 추가적인 변환이 결합될 때는 `flatMap`을 사용하는 것이 중간 컬렉션 생성을 줄이고 코드가 간결해집니다.
 */
