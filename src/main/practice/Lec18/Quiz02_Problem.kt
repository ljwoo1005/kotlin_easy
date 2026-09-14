package Lec18

/*
 * [학습 목표 & 복습 개념]
 * - Java의 명령형 루프 및 조건문 기반 컬렉션 가공 코드를 코틀린의 관용적인 함수형 API로 리팩토링합니다.
 * - 컬렉션을 특정 키 기준으로 그룹핑(`Map<K, List<V>>`)하는 `groupBy`와, 1:1 키-값(`Map<K, V>`)으로 변환하는 `associateBy`의 차이를 체화합니다.
 * - 2차원 중첩 컬렉션을 평탄화하는 `flatMap`의 동작 원리를 이해하고, 클래스 커스텀 프로퍼티와 확장 프로퍼티를 결합하여 중첩 람다를 제거하는 리팩토링 기법을 학습합니다.
 *
 * [문제 설명]
 * 아래는 Java에서 과일 목록을 그룹화하고, 맵으로 변환하며, 2차원 리스트를 평탄화 필터링하기 위해 작성된 전형적인 명령형 코드입니다.
 * ----------------------------------------------------
 * // [참고: 리팩토링 대상 Java 코드]
 * public class JavaFruitCollectionHelper {
 *     // 1. 과일 이름별 그룹핑
 *     public static Map<String, List<Fruit>> groupByName(List<Fruit> fruits) {
 *         Map<String, List<Fruit>> map = new HashMap<>();
 *         for (Fruit fruit : fruits) {
 *             map.putIfAbsent(fruit.getName(), new ArrayList<>());
 *             map.get(fruit.getName()).add(fruit);
 *         }
 *         return map;
 *     }
 *
 *     // 2. ID -> 출고가(factoryPrice) 1:1 매핑
 *     public static Map<Long, Long> mapIdToFactoryPrice(List<Fruit> fruits) {
 *         Map<Long, Long> map = new HashMap<>();
 *         for (Fruit fruit : fruits) {
 *             map.put(fruit.getId(), fruit.getFactoryPrice());
 *         }
 *         return map;
 *     }
 *
 *     // 3. 중첩된 과일 상자 목록에서 출고가와 현재가가 동일한 과일만 1차원으로 수집
 *     public static List<Fruit> collectSamePriceFruits(List<List<Fruit>> fruitBoxes) {
 *         List<Fruit> result = new ArrayList<>();
 *         for (List<Fruit> box : fruitBoxes) {
 *             for (Fruit fruit : box) {
 *                 if (fruit.isSamePrice()) {
 *                     result.add(fruit);
 *                 }
 *             }
 *         }
 *         return result;
 *     }
 * }
 * ----------------------------------------------------
 * 위 Java 코드를 코틀린의 관용적인 고차 함수 및 확장 프로퍼티를 활용하여 리팩토링하세요.
 *
 * 1. groupFruitsByName(fruits: List<Quiz18Fruit>): Map<String, List<Quiz18Fruit>>
 *    - 과일의 이름(`name`)을 Key로, 해당 이름을 가진 과일들의 리스트(`List<Quiz18Fruit>`)를 Value로 하는 Map을 생성합니다.
 *    - 코틀린의 `groupBy` 함수를 사용하여 단 한 줄로 작성하세요.
 *
 * 2. mapFruitIdToFactoryPrice(fruits: List<Quiz18Fruit>): Map<Long, Long>
 *    - 과일의 `id`를 Key로, 과일의 출고가(`factoryPrice`)를 Value로 매핑하는 Map을 생성합니다.
 *    - 코틀린의 `associateBy` 함수(keySelector와 valueTransform 인자 2개 전달)를 사용하여 단 한 줄로 작성하세요.
 *
 * 3. 확장 프로퍼티 선언 및 flattenSamePriceFruits 구현
 *    - `List<Quiz18Fruit>`에 대한 확장 프로퍼티 `samePriceFilter`를 선언하세요.
 *      (게터 `get()`에서 `this.filter(Quiz18Fruit::isSamePrice)`를 호출하여 출고가와 현재가가 같은 과일만 필터링)
 *    - 함수 `flattenSamePriceFruits(fruitBoxes: List<List<Quiz18Fruit>>): List<Quiz18Fruit>`에서
 *      위 확장 프로퍼티와 `flatMap`을 조합하여 중첩 람다 없이 2차원 리스트를 1차원으로 평탄화하여 반환하세요.
 *
 * [요구사항 & 제약조건]
 * - groupFruitsByName: 반드시 `groupBy`를 사용하세요.
 * - mapFruitIdToFactoryPrice: 반드시 `associateBy`를 사용하고 두 개의 람다를 인자로 전달하세요.
 * - 확장 프로퍼티 `val List<Quiz18Fruit>.samePriceFilter: List<Quiz18Fruit>`를 정의하고,
 *   `flattenSamePriceFruits` 내부에서 이를 활용하여 `flatMap`을 호출하세요.
 *
 * [입출력 및 기대 결과]
 * val f1 = Quiz18Fruit(1L, "사과", 1000L, 1000L) // 동일 가격
 * val f2 = Quiz18Fruit(2L, "사과", 1000L, 1500L)
 * val f3 = Quiz18Fruit(3L, "바나나", 3000L, 3000L) // 동일 가격
 * - groupFruitsByName(listOf(f1, f2, f3)) -> {"사과": [f1, f2], "바나나": [f3]}
 * - mapFruitIdToFactoryPrice(listOf(f1, f2)) -> {1L: 1000L, 2L: 1000L}
 * - flattenSamePriceFruits(listOf(listOf(f1, f2), listOf(f3))) -> [f1, f3]
 *
 * [💡 HINT]
 * - `collection.groupBy { it.key }`는 `Map<K, List<T>>` 구조를 만듭니다.
 * - `collection.associateBy({ it.key }, { it.value })`는 1:1 `Map<K, V>` 구조를 만듭니다.
 * - 확장 프로퍼티는 `val List<타입>.프로퍼티명: 반환타입 get() = this.연산()` 형식으로 선언할 수 있습니다.
 * - `Quiz18Fruit::isSamePrice` 멤버 참조를 `filter`에 직접 전달할 수 있습니다.
 */

// 실습용 과일 도메인 데이터 클래스(Quiz18Fruit)는 Quiz01_Problem.kt에 선언된 모델을 공유합니다.

// 1. 이름별 그룹핑 함수 뼈대
private fun groupFruitsByName(fruits: List<Quiz18Fruit>): Map<String, List<Quiz18Fruit>> {
    TODO()
}

// 2. ID별 출고가 매핑 함수 뼈대
private fun mapFruitIdToFactoryPrice(fruits: List<Quiz18Fruit>): Map<Long, Long> {
    TODO()
}

// 3-1. 출고가와 현재가가 동일한 과일만 필터링하는 확장 프로퍼티 뼈대
private val List<Quiz18Fruit>.samePriceFilter: List<Quiz18Fruit>
    get() = TODO()

// 3-2. 중첩된 과일 상자 목록을 평탄화 및 필터링하는 함수 뼈대
private fun flattenSamePriceFruits(fruitBoxes: List<List<Quiz18Fruit>>): List<Quiz18Fruit> {
    TODO()
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

    println("✅ Quiz02 테스트 통과! (Java 명령형 코드를 코틀린 함수형으로 완벽 리팩토링)")
}
