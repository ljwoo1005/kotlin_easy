package Lec18

/*
 * [핵심 해결 아이디어]
 * - `mapNotNull`은 컬렉션의 요소를 변환하면서 null인 결과를 자동으로 필터링해 주므로, `filter { it != null }.map { ... }`의 연쇄 호출을 하나의 간결한 연산으로 압축할 수 있습니다.
 * - 컬렉션의 조건 판별에는 명령형 루프와 if 플래그 변수 대신, 코틀린 표준 라이브러리의 `all`(전체 일치), `any`(최소 하나 일치), `none`(전체 불일치) 술어 함수를 사용하여 의도를 명확하게 표현합니다.
 * - 정렬과 단일 조회의 조합(`sortedByDescending` + `firstOrNull`)을 사용하면 빈 컬렉션인 경우에도 `NoSuchElementException` 같은 예외 없이 안전하게 null을 반환받을 수 있습니다.
 */

// 실습용 과일 도메인 데이터 클래스(Quiz18Fruit)는 Quiz01_Problem.kt에 선언된 모델을 공유합니다.

// 1. 판매 중인 과일 이름 목록 추출 모범 답안
private fun findInStockFruitNames(fruits: List<Quiz18Fruit>): List<String> {
    // currentPrice가 null이 아닌 경우에만 과일 이름을 반환하고, null인 경우 건너뜀
    return fruits.mapNotNull { fruit ->
        if (fruit.currentPrice != null) fruit.name else null
    }
    // 대안: fruits.filter { it.currentPrice != null }.map { it.name }
}

// 2. 과일 목록의 재고 상태 조건 진단 모범 답안
private fun checkFruitStockStatus(fruits: List<Quiz18Fruit>): Triple<Boolean, Boolean, Boolean> {
    // 1) all: 모든 과일의 factoryPrice가 0보다 큰지 검사
    val isAllFactoryPricePositive = fruits.all { it.factoryPrice > 0 }

    // 2) any: 현재가가 5,000원 이상인 과일이 하나라도 있는지 검사 (null safe 처리 포함)
    val isAnyExpensive = fruits.any { (it.currentPrice ?: 0L) >= 5_000L }

    // 3) none: 이름이 빈 문자열인 과일이 하나도 없는지 검사
    val isNoneEmptyName = fruits.none { it.name.isBlank() }

    return Triple(isAllFactoryPricePositive, isAnyExpensive, isNoneEmptyName)
}

// 3. 유효 과일 수와 최고가 과일 이름 조회 모범 답안
private fun getTopPricedFruitInfo(fruits: List<Quiz18Fruit>): Pair<Int, String?> {
    // 현재가가 책정된(null이 아닌) 과일들만 우선 필터링
    val validFruits = fruits.filter { it.currentPrice != null }

    // 유효한 과일의 개수 집계
    val validCount = validFruits.count()

    // 현재가를 기준으로 내림차순 정렬한 뒤 첫 번째 과일의 이름을 안전하게 꺼냄 (없으면 null)
    val topFruitName = validFruits.sortedByDescending { it.currentPrice }
        .firstOrNull()
        ?.name

    return Pair(validCount, topFruitName)
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

    println("✅ Quiz01 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. mapNotNull의 간결성: Java의 Stream API에서는 `.filter(Objects::nonNull).map(...)` 또는 `flatMap(Optional::stream)` 등을 써야 하지만, Kotlin은 `mapNotNull` 하나로 null 필터링과 매핑을 동시에 깔끔하게 처리합니다.
 * 2. 풍부한 술어 함수 제공: `all`, `any`, `none`이 컬렉션 인터페이스에 인라인 확장 함수로 직접 제공되므로, Java의 `stream().allMatch(...)`처럼 매번 Stream을 열고 닫는 보일러플레이트가 전혀 필요 없습니다.
 * 3. firstOrNull을 통한 Null-Safety: Java Stream의 `findFirst()`는 `Optional`을 반환하여 추가적인 언래핑 처리가 필요하지만, Kotlin은 `firstOrNull()`을 통해 코틀린 고유의 널 안정성 연산자(`?.`, `?:`)와 자연스럽게 연계됩니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. 빈 컬렉션에서 `first()` 호출: 컬렉션이 비어있을 가능성이 있을 때 `first()`나 `last()`를 직접 호출하면 `NoSuchElementException`이 발생합니다. 항상 안전한 `firstOrNull()`, `lastOrNull()`을 사용하는 습관을 들이는 것이 좋습니다.
 * 2. `all` 함수의 공집합 참(Vacuous Truth) 특성: 컬렉션이 비어있는 상태(`emptyList()`)에서 `all { ... }`을 호출하면 조건식과 무관하게 항상 `true`를 반환합니다. 비어있지 않으면서 조건을 만족해야 하는 비즈니스 로직이라면 `isNotEmpty() && all { ... }` 형태로 방어해야 합니다.
 */
