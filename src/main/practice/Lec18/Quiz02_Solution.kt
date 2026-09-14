package Lec18

/*
 * [핵심 해결 아이디어]
 * - Java에서 `Map<K, List<V>>`를 만들기 위해 `putIfAbsent()`나 `computeIfAbsent()`를 사용하던 번잡한 코드를 `groupBy` 단 한 줄로 대체할 수 있습니다.
 * - `associateBy`는 키와 값 추출 람다를 인자로 넘겨 `Map<K, V>`를 생성하며, 원본 객체에서 필요한 필드만 손쉽게 추출하여 매핑합니다.
 * - 중첩 컬렉션을 평탄화하는 `flatMap` 호출 시 내부에서 다시 람다를 중첩(`list.filter { fruit -> ... }`)하면 가독성이 떨어집니다. 이를 도메인 프로퍼티(`Quiz18Fruit.isSamePrice`)와 컬렉션 확장 프로퍼티(`List<Quiz18Fruit>.samePriceFilter`), 그리고 멤버 참조(`Quiz18Fruit::isSamePrice`)로 단계별로 캡슐화하면 비즈니스 의도가 명확한 선언형 코드가 완성됩니다.
 */

// 실습용 과일 도메인 데이터 클래스(Quiz18Fruit)는 Quiz01_Problem.kt에 선언된 모델을 공유합니다.

// 1. 이름별 그룹핑 모범 답안
private fun groupFruitsByName(fruits: List<Quiz18Fruit>): Map<String, List<Quiz18Fruit>> {
    // 과일의 name 프로퍼티를 키로 그룹화하여 Map<String, List<Quiz18Fruit>> 생성
    return fruits.groupBy { it.name }
}

// 2. ID별 출고가 매핑 모범 답안
private fun mapFruitIdToFactoryPrice(fruits: List<Quiz18Fruit>): Map<Long, Long> {
    // 첫 번째 람다는 키(id), 두 번째 람다는 값(factoryPrice)을 정의하여 Map<Long, Long> 생성
    return fruits.associateBy({ it.id }, { it.factoryPrice })
}

// 3-1. 출고가와 현재가가 동일한 과일만 필터링하는 확장 프로퍼티 모범 답안
private val List<Quiz18Fruit>.samePriceFilter: List<Quiz18Fruit>
    get() = this.filter(Quiz18Fruit::isSamePrice) // 멤버 참조를 활용하여 도메인 규칙을 만족하는 과일만 필터링

// 3-2. 중첩된 과일 상자 목록을 평탄화 및 필터링하는 모범 답안
private fun flattenSamePriceFruits(fruitBoxes: List<List<Quiz18Fruit>>): List<Quiz18Fruit> {
    // 확장 프로퍼티를 활용하여 2차원 리스트를 1차원 동일 가격 리스트로 평탄화 병합
    return fruitBoxes.flatMap { box -> box.samePriceFilter }
}

fun main() {
    val f1 = Quiz18Fruit(1L, "사과", 1_000L, 1_000L) // 동일 가격
    val f2 = Quiz18Fruit(2L, "사과", 1_200L, 1_500L) // 가격 다름
    val f3 = Quiz18Fruit(3L, "바나나", 3_000L, 3_000L) // 동일 가격
    val f4 = Quiz18Fruit(4L, "수박", 10_000L, 12_000L) // 가격 다름

    val allFruits = listOf(f1, f2, f3, f4)

    // 1. groupFruitsByName 검증
    val grouped = groupFruitsByName(allFruits)
    check(grouped["사과"] == listOf(f1, f2)) { "사과 그룹에는 f1, f2가 포함되어야 합니다." }
    check(grouped["바나나"] == listOf(f3)) { "바나나 그룹에는 f3이 포함되어야 합니다." }
    check(grouped["수박"] == listOf(f4)) { "수박 그룹에는 f4가 포함되어야 합니다." }

    // 2. mapFruitIdToFactoryPrice 검증
    val priceMap = mapFruitIdToFactoryPrice(allFruits)
    check(priceMap == mapOf(1L to 1_000L, 2L to 1_200L, 3L to 3_000L, 4L to 10_000L)) {
        "ID와 출고가가 올바르게 매핑되어야 합니다. 실제: $priceMap"
    }

    // 3. flattenSamePriceFruits 검증 (2차원 리스트 -> 1차원 동일 가격 리스트)
    val boxes = listOf(
        listOf(f1, f2),
        listOf(f3, f4)
    )
    val samePrices = flattenSamePriceFruits(boxes)
    check(samePrices == listOf(f1, f3)) {
        "동일 가격인 과일 f1(사과)과 f3(바나나)만 1차원으로 수집되어야 합니다. 실제: $samePrices"
    }

    println("✅ Quiz02 해답 검증 통과! (모든 테스트 케이스 성공)")
}

/*
 * [자바 대비 장점]
 * 1. 컬렉션 그룹핑의 단순화: Java에서는 `Collectors.groupingBy()`를 사용하거나 직접 루프를 돌며 Map에 리스트를 초기화하는 번거로움이 있었으나, Kotlin에서는 `groupBy` 확장 함수 하나로 직관적으로 해결됩니다.
 * 2. associateBy를 통한 깔끔한 Map 생성: Java Stream의 `Collectors.toMap(Fruit::getId, Fruit::getFactoryPrice)` 대신 Kotlin은 `associateBy`를 통해 키와 값을 자연스럽게 매핑하며 가독성을 높입니다.
 * 3. 확장 프로퍼티와 멤버 참조의 결합: Java에서는 특정 컬렉션 타입(`List<Fruit>`)에 외형적인 메서드나 프로퍼티를 추가할 수 없어 유틸 클래스를 별도로 만들어야 했으나, Kotlin은 확장 프로퍼티(`List<Quiz18Fruit>.samePriceFilter`)를 통해 도메인 특화 기능을 원래 리스트에 존재했던 멤버처럼 우아하게 호출할 수 있습니다.
 *
 * [자주 하는 실수 (Pitfall)]
 * 1. groupBy와 associateBy의 혼동: `groupBy`는 동일한 키를 가진 요소들을 리스트로 묶어 `Map<K, List<V>>`를 만들지만, `associateBy`는 1:1 매핑(`Map<K, V>`)을 수행합니다. 만약 `associateBy`에서 동일한 키를 가진 요소가 여러 개 나오면 마지막 요소가 이전 요소를 덮어쓰게 되므로 주의해야 합니다.
 * 2. 확장 프로퍼티에 Backing Field 선언 시도: 확장 프로퍼티는 실제 클래스에 메모리 필드를 추가할 수 없으므로 `val List<Fruit>.filtered = ...` 형태로 초기화할 수 없으며, 반드시 `get()` 게터 구문으로 정의해야 합니다.
 */
